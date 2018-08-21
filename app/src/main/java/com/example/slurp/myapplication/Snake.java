package com.example.slurp.myapplication;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


// there's many different ways and coupling to design snake.
// could do tight couples or can do the game class controls the other classes without them
// knowing about each other abit, abit like inversion of control

public class Snake implements Cloneable{
    private List<Point> headAndBody;
    private int snakeJointsIncludingHead;
    private int stepsUntilCanGrow;

    public Snake(int snakeJointsIncludingHead, int startX, int startY){
        this.snakeJointsIncludingHead = snakeJointsIncludingHead;
        this.headAndBody = new CopyOnWriteArrayList<>();
        this.stepsUntilCanGrow = 0;

        for(int i = 0; i < this.snakeJointsIncludingHead; i++){
            this.headAndBody.add(new Point(startX + i, startY));
        }
    }

    public void grow(){
//        Point tail = this.headAndBody.get(this.headAndBody.size() - 1);
//        this.headAndBody.add(new Point(tail.x, tail.y));
        snakeJointsIncludingHead += 4;
        this.stepsUntilCanGrow = snakeJointsIncludingHead - this.headAndBody.size();
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

        // the order of the control flow from here and below in this method is important
        Point tailToAddIfNeedsToGrow = null;

        for(int i = headAndBody.size() - 1; i > 0; i--){

            if(i == headAndBody.size() - 1){
                tailToAddIfNeedsToGrow = new Point(headAndBody.get(i));
            }

            Point newPosForJoint = headAndBody.get(i - 1);

            if(i < headAndBody.size()){
                headAndBody.set(i, new Point(newPosForJoint.x, newPosForJoint.y));
            }
        }

        // append tail if snake is in process of growing by n squares
        if(stepsUntilCanGrow > 0){
            headAndBody.add(tailToAddIfNeedsToGrow);
        }

        head.set(head.x + directionX, head.y + directionY);

        if(this.stepsUntilCanGrow > 0){
            this.stepsUntilCanGrow--;
        }
    }

    @Override
    public Object clone() {
        Snake obj = null;
        try {
            obj = (Snake) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public List<Point> getHeadAndBody() {
        return headAndBody;
    }
}
