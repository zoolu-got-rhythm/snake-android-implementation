package com.example.slurp.myapplication;

import android.graphics.Point;

public class Apple {
    Point pos;

    public Apple(int x, int y){
        this.pos = new Point(x, y);
    }

    public Point getPos() {
        return pos;
    }
}
