package com.example.slurp.myapplication;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class ScoreTextView extends android.support.v7.widget.AppCompatTextView implements Observer{

    private Game game;
    private Handler mHandler;

    public ScoreTextView(Context context) {
        super(context);
        this.mHandler = new Handler();

        this.setTextSize(20f);
        this.setText("score: 0");
    }


    @Override
    public void update(Observable observable, Object o) {
        this.game = (Game) observable;

    }

    public void updateScore(final int score){
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                setText("apples: " + Integer.toString(score));
                invalidate();
            }
        });
    }
}
