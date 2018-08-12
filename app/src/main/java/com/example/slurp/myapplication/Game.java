package com.example.slurp.myapplication;

import android.graphics.Point;
import android.util.Log;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

// all game logic goes inside the game class (this class)
public class Game extends Observable{
    private String currentPlayerDirection;
    private Snake playerSnake;
    private Board board;
    private Apple currentApple;
    private Boolean isGameOver;
    private int currentPlayerScore;

    private Timer timer;

    public Game(){

    }

    // init the game state
    public void initGame(){
        this.isGameOver = false;

        this.currentPlayerScore = 0;

        this.board = new Board(30, 30);
        this.playerSnake = new Snake(4,
                this.board.getxTiles() / 2, this.board.getyTiles() / 2);

        Point freePositionOnBoard = Util.getRandomPositionThatDoesntCollideWithSnakeBody(
                this.playerSnake.getHeadAndBody(), this.board.getxTiles(), this.board.getyTiles()
        );

        this.currentApple = new Apple(freePositionOnBoard.x, freePositionOnBoard.y);
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
                setChanged();
                notifyObservers(self);
                update();
            }
        },1000, 1000 / 12);
    }

    public void update(){
        if(!this.isGameOver){

            // move
            playerSnake.move(this.currentPlayerDirection);

            // check apple
            // .equals with some objects can mean the same reference of obj, not value equality
            if(playerSnake.getHeadAndBody().get(0).x == this.currentApple.pos.x &&
                    playerSnake.getHeadAndBody().get(0).y == this.currentApple.pos.y){

                this.currentPlayerScore++;

                playerSnake.grow();
                Log.d("snake", "growing");

                Point freePositionOnBoard = Util.getRandomPositionThatDoesntCollideWithSnakeBody(
                        this.playerSnake.getHeadAndBody(), this.board.getxTiles(), this.board.getyTiles()
                );
                this.currentApple = new Apple(freePositionOnBoard.x, freePositionOnBoard.y);
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
            // stop and clear timer
            this.timer.cancel();
            this.timer = null;
        }
    }

    public void setCurrentPlayerDirection(String currentPlayerDirection) {
        this.currentPlayerDirection = currentPlayerDirection;
    }

    public String getCurrentPlayerDirection() {
        return currentPlayerDirection;
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
}
