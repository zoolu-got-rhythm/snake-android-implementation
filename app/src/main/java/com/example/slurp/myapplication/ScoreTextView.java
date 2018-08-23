package com.example.slurp.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class ScoreTextView extends android.support.v7.widget.AppCompatTextView implements Observer{

    private Game game;
    private Handler mHandler;
    private String scorePrefix;

    public ScoreTextView(Context context, String scorePrefix, int relativeLayoutChildAlignment) {
        super(context);
        this.scorePrefix = scorePrefix;
        this.mHandler = new Handler();

        this.setTextSize(13f);
        this.setText(scorePrefix +" 0");
        this.setBackgroundColor(Color.GREEN);
        RelativeLayout.LayoutParams paramsrl1 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsrl1.addRule(relativeLayoutChildAlignment);
        this.setLayoutParams(paramsrl1);
        this.setPadding(13, 0, 13, 0);
    }


    @Override
    public void update(Observable observable, Object o) {
        this.game = (Game) observable;

    }

    public void updateScore(final int score){
        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                setText(scorePrefix + " " + Integer.toString(score));
                invalidate();
            }
        });
    }
}
