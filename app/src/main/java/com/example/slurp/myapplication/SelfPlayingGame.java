package com.example.slurp.myapplication;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SelfPlayingGame extends Game{

    private String currentLegalDirection;

    public SelfPlayingGame(int framerate, int snakeLength, int gridSize) {
        super(framerate, snakeLength, gridSize);
//        super.setCurrentApple(null);
//        super.getCurrentApple().
        // set current apple = null
    }

    @Override
    public void update(){
        this.currentLegalDirection = generateLegalDirection();
        Log.d("chosen dir", currentLegalDirection);

        super.getPlayerSnake().move(
                this.currentLegalDirection != null ? currentLegalDirection : "w");

    }

    private String generateLegalDirection(){
        // check if snake at edge
        // wrap into a private method

            List<String> foundDisallowedDirections = new ArrayList<>();

            Map<String, Point> dirs = new HashMap<>();
            dirs.put("n", new Point(0, -1));
            dirs.put("e", new Point(1, 0));
            dirs.put("s", new Point(0, 1));
            dirs.put("w", new Point(-1, 0));

            Iterator it = dirs.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String, Point> pair = (Map.Entry) it.next();

                Snake snakeDeepCopy = (Snake) super.getPlayerSnake().clone();
                snakeDeepCopy.move(pair.getKey());

                Point snakeHeadCopy = new Point(snakeDeepCopy.getHeadAndBody().get(0));

                // check if out of bounds
                if (snakeHeadCopy.x < 0 ||
                        snakeHeadCopy.x > super.getBoard().xTiles ||
                        snakeHeadCopy.y < 0 ||
                        snakeHeadCopy.y > super.getBoard().yTiles) {
                    foundDisallowedDirections.add(pair.getKey());
                }

                // check if snake hits self/body
                for(int j = 1; j < snakeDeepCopy.getHeadAndBody().size(); j++){
                    Point joint = snakeDeepCopy.getHeadAndBody().get(j);
                    if((snakeHeadCopy.x == joint.x &&
                            snakeHeadCopy.y == joint.y)){
                        if(foundDisallowedDirections.indexOf(pair.getKey()) == -1)
                            foundDisallowedDirections.add(pair.getKey());
                    }
                }
                // add to arrayList
            }
//                this.aiListener.onIs1MoveAwayFromEdge(foundDisallowedDirections);
//                    return;

        // wrap into a smaller testable function
        List<String> allowedDirections = new ArrayList<>();
        String[] dirsArray = {"n", "e", "s", "w"};

        for(String dirFromArray : dirsArray){
            boolean isLegalDir = true;
            for(String disallowedDir: foundDisallowedDirections){
                if(disallowedDir.equals(dirFromArray)){
                    isLegalDir = false;
                    break;
                }
            }
            if(isLegalDir == true){
                allowedDirections.add(dirFromArray);
            }
        }

//        Log.d("allowed dirs", allowedDirections.toString());

        return allowedDirections.get(
                (int) Math.floor(Math.random() * allowedDirections.size())
        );
    }
}
