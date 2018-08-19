package com.example.slurp.myapplication;

import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

// all game logic goes inside the game class (this class)
public class Game extends Observable{
    private String[] playerDirectionsBuffer;
    private Snake playerSnake;
    private Board board;
    private volatile Apple currentApple;
    private Boolean isGameOver;
    private int currentPlayerScore;
    private Boolean bufferDirectionAtIndexZeroHasBeenUsed;
    private int framerate;

    private Timer timer;

    private SnakeGameListener snakeGameListener;

    public Game(int framerate){
        this.framerate = framerate;
        this.playerDirectionsBuffer = new String[2];
    }

    // init the game state
    public void initGame(){
        this.bufferDirectionAtIndexZeroHasBeenUsed = false;
        this.isGameOver = false;

        this.playerDirectionsBuffer[0] = null;
        this.playerDirectionsBuffer[1] = null;

        this.currentPlayerScore = 0;

        this.board = new Board(25, 25);
        this.playerSnake = new Snake(10,
                this.board.getxTiles() / 2, this.board.getyTiles() / 2);

        Point freePositionOnBoard = Util.getRandomPositionThatDoesntCollideWithSnakeBody(
                this.playerSnake.getHeadAndBody(), this.board.getxTiles(), this.board.getyTiles()
        );

        this.currentApple = new Apple(freePositionOnBoard.x, freePositionOnBoard.y);

        if(snakeGameListener != null)
            snakeGameListener.onAppleEaten(this.currentPlayerScore);
    }

    public void startGame(){
        // startTimer;
        this.timer = new Timer();
        final Game self = (Game) this;

        setChanged();
        notifyObservers(self);

        this.timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                // notify
                update();
                setChanged();
                notifyObservers(self);

            }
        },1000, 1000 / this.framerate);
    }

    private synchronized void shiftBuffer(){
        if(this.playerDirectionsBuffer[1] != null){
            // shift array left by 1
            this.playerDirectionsBuffer[0] = this.playerDirectionsBuffer[1];
            this.playerDirectionsBuffer[1] = null;
        }
    }

    public void update(){
        if(!this.isGameOver){

            // move
            playerSnake.move(this.playerDirectionsBuffer[0]);

            if(this.playerDirectionsBuffer[1] == null)
                this.bufferDirectionAtIndexZeroHasBeenUsed = true;

            // shift through buffer: unless there are no more qued directions in it but 1
            this.shiftBuffer();

            // check apple
            // .equals with some objects can mean the same reference of obj, not value equality
            if(currentApple != null)
                if(playerSnake.getHeadAndBody().get(0).x == this.currentApple.pos.x &&
                        playerSnake.getHeadAndBody().get(0).y == this.currentApple.pos.y){

                    this.currentPlayerScore++;

                    if(snakeGameListener != null)
                        snakeGameListener.onAppleEaten(this.currentPlayerScore);

                    playerSnake.grow();
                    Log.d("snake", "growing");

                    // android environment doesn't seem to like the while loop when finding a free
                    // random position on the game board for the apple, as it's unknown has long it will
                    // take to find a space: so wrapped the code to run inside a thread to prevent crash
                    new Thread(){
                        @Override
                        public void run(){
                            Log.d("apple", "generating position for apple");
                            currentApple = null;
                            Point freePositionOnBoard = Util.getRandomPositionThatDoesntCollideWithSnakeBody(
                                    playerSnake.getHeadAndBody(), board.getxTiles(), board.getyTiles()
                            );
                            currentApple = new Apple(freePositionOnBoard.x, freePositionOnBoard.y);
                            Log.d("apple", "new apple assigned");
                        }
                    }.start();
                }

            // check for out of bounds
            if(playerSnake.getHeadAndBody().get(0).x < 0 ||
                    playerSnake.getHeadAndBody().get(0).x > this.board.xTiles ||
                    playerSnake.getHeadAndBody().get(0).y < 0 ||
                    playerSnake.getHeadAndBody().get(0).y > this.board.yTiles){
                this.isGameOver = true;
                return;
            }

            // check for snake hitting own body
            for(int i = 1; i < playerSnake.getHeadAndBody().size(); i++){
                Log.d("snake", "hit");
                Point joint = playerSnake.getHeadAndBody().get(i);
                if((playerSnake.getHeadAndBody().get(0).x == joint.x &&
                        playerSnake.getHeadAndBody().get(0).y == joint.y)){
                    this.isGameOver = true;
                    return;
                }
            }



        }else{
            this.snakeGameListener.onGameOver();
            // stop and clear timer
            this.timer.cancel();
            this.timer = null;
            Log.d("timer", "canceling timer");
        }
    }

    @Nullable
    private String oppositeDirectionOfDir(String dir){
        String oppositeDir = null;
        if(dir.equals("n"))
            oppositeDir = "s";
        if(dir.equals("e"))
            oppositeDir = "w";
        if(dir.equals("s"))
            oppositeDir = "n";
        if(dir.equals("w"))
            oppositeDir = "e";
        return oppositeDir;
    }

    private synchronized Boolean isDirectionAloud(String currentPlayerDirection){
        if(this.playerDirectionsBuffer[1] != null)
            if(oppositeDirectionOfDir(currentPlayerDirection).equals(playerDirectionsBuffer[1]))
                return false;

        if(this.playerDirectionsBuffer[0] != null)
            if(oppositeDirectionOfDir(currentPlayerDirection).equals(playerDirectionsBuffer[0]))
                return false;

        return true;
    }

    public synchronized void setCurrentPlayerDirection(String currentPlayerDirection) {
//        this.currentPlayerDirection = currentPlayerDirection;
        // shift array to left

        if(!isDirectionAloud(currentPlayerDirection))
            return;

        if(this.bufferDirectionAtIndexZeroHasBeenUsed){
            this.playerDirectionsBuffer[0] = currentPlayerDirection;
            this.bufferDirectionAtIndexZeroHasBeenUsed = false;
            return;
        }


        for(int i = 0; i < this.playerDirectionsBuffer.length; i++){
            if(this.playerDirectionsBuffer[i] == null){
                this.playerDirectionsBuffer[i] = currentPlayerDirection;
                return;
            }
        }

        // shift array left by 1
        this.playerDirectionsBuffer[0] = this.playerDirectionsBuffer[1];
        this.playerDirectionsBuffer[1] = currentPlayerDirection;
    }


    public String getCurrentPlayerDirection() {
        return this.playerDirectionsBuffer[0];
    }

    public Snake getPlayerSnake() {
        return playerSnake;
    }

    public Board getBoard() {
        return board;
    }

    public Apple getCurrentApple() {
        return currentApple;
    }

    public Boolean getGameOver() {
        return isGameOver;
    }

    public int getCurrentPlayerScore() {
        return currentPlayerScore;
    }

    public void setSnakeGameListener(SnakeGameListener snakeGameListener) {
        this.snakeGameListener = snakeGameListener;
    }
}
