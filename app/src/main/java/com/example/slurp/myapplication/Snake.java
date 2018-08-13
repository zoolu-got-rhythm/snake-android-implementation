package com.example.slurp.myapplication;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;


// there's many different ways and coupling to design snake.
// could do tight couples or can do the game class controls the other classes without them
// knowing about each other abit, abit like inversion of control

public class Snake {
    private List<Point> headAndBody;
    private int snakeJointsIncludingHead;

    public Snake(int snakeJointsIncludingHead, int startX, int startY){
        this.snakeJointsIncludingHead = snakeJointsIncludingHead;
        this.headAndBody = new ArrayList<>();

        for(int i = 0; i < this.snakeJointsIncludingHead; i++){
            this.headAndBody.add(new Point(startX + i, startY));
        }
    }

    public void grow(){
//        Point tail = this.headAndBody.get(this.headAndBody.size() - 1);
//        this.headAndBody.add(new Point(tail.x, tail.y));
        snakeJointsIncludingHead += 1;
    }

    public void move(String dir){

        Point head = this.headAndBody.get(0);

        int directionX = 0, directionY = 0;
        // mutate head: depending on dir
        if(dir.equals("n"))
            directionY = -1;
        if(dir.equals("e"))
            directionX = +1;
        if(dir.equals("s"))
            directionY = +1;
        if(dir.equals("w"))
            directionX = -1;


        for(int i = snakeJointsIncludingHead - 1; i > 0; i--){
            Point newPosForJoint = headAndBody.get(i - 1);
            if(i < headAndBody.size()){
                headAndBody.set(i, new Point(newPosForJoint.x, newPosForJoint.y));
            }else{
                headAndBody.add(i, new Point(newPosForJoint.x, newPosForJoint.y));
            }
        }

        head.set(head.x + directionX, head.y + directionY);
    }

    public List<Point> getHeadAndBody() {
        return headAndBody;
    }
}