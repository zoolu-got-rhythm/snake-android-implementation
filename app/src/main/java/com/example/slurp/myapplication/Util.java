package com.example.slurp.myapplication;

import android.graphics.Point;

import java.util.List;

public class Util {
    public static Point getRandomPositionThatDoesntCollideWithSnakeBody(List<Point> snakeBody, int maxX, int maxY){

        int x = (int) Math.floor(Math.random() * maxX);
        int y = (int) Math.floor(Math.random() * maxY);

        Boolean foundSpaceOnGridThatDoesntCollideWithSnakeBody = false;
        while(!foundSpaceOnGridThatDoesntCollideWithSnakeBody){
            foundSpaceOnGridThatDoesntCollideWithSnakeBody = true;
            for(Point p : snakeBody){
                if(x == p.x && y == p.y){
                    x = (int) Math.floor(Math.random() * maxX);
                    y = (int) Math.floor(Math.random() * maxY);
                    foundSpaceOnGridThatDoesntCollideWithSnakeBody = false;
                    break;
                }
            }
        }

        return new Point(x, y);
    }
}
