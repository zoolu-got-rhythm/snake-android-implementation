package com.example.slurp.myapplication;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SelfPlayingGame extends Game{

    public SelfPlayingGame(int framerate) {
        super(framerate);
    }

    @Override
    public void update(){
        String legalDirection = generateLegalDirection();
        super.getPlayerSnake().move(legalDirection);
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
                    Point snakeHeadCopy = new Point(super.getPlayerSnake().getHeadAndBody().get(0));
                    Map.Entry<String, Point> pair = (Map.Entry) it.next();

                    snakeHeadCopy.offset(pair.getValue().x, pair.getValue().y);

                    // check if out of bounds
                    if (snakeHeadCopy.x < 0 ||
                            snakeHeadCopy.x > super.getBoard().xTiles ||
                            snakeHeadCopy.y < 0 ||
                            snakeHeadCopy.y > super.getBoard().yTiles) {
                        foundDisallowedDirections.add(pair.getKey());
                    }

                    // check if snake hits self/body
                    for(int j = 1; j < super.getPlayerSnake().getHeadAndBody().size(); j++){
                        Point joint = super.getPlayerSnake().getHeadAndBody().get(j);
                        if((snakeHeadCopy.x == joint.x &&
                                snakeHeadCopy.y == joint.y)){
                            if(foundDisallowedDirections.indexOf(pair.getKey()) == -1)
                                foundDisallowedDirections.add(pair.getKey());
                        }
                    }
                    // add to arrayList
                }

                Log.d("disallowed dirs", foundDisallowedDirections.toString());

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


        return allowedDirections.get(
                (int) Math.floor(Math.random() * allowedDirections.size())
        );
    }
}
