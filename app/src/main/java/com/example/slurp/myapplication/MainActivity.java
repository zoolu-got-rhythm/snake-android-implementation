package com.example.slurp.myapplication;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Game game;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mHandler = new Handler();
        this.game = new Game();
        View snakeGameView = new SnakeGameView(this, 900, 900);
        game.addObserver((Observer) snakeGameView);



        LinearLayout root = findViewById(R.id.root);
        root.setGravity(Gravity.CENTER);

        ScoreTextView mScoreTextView = new ScoreTextView(this);
        mScoreTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        game.addObserver(mScoreTextView);
        root.addView(mScoreTextView);


        root.addView(snakeGameView);


        RelativeLayout buttonsContainer = new RelativeLayout(this);

        RelativeLayout.LayoutParams buttonsContainerParams =
                new RelativeLayout.LayoutParams(650, 450);
//        buttonsContainerParams.addRule();
        buttonsContainer.setLayoutParams(buttonsContainerParams);
        buttonsContainer.setBackgroundColor(Color.DKGRAY);
//        buttonsContainer.setGravity(Gravity.BOTTOM);

        root.addView(buttonsContainer);


        final Button leftBtn = new Button(this);
        leftBtn.setText("left");
        RelativeLayout.LayoutParams btnLeftRelativeParams = new RelativeLayout.LayoutParams(220, 220);
        btnLeftRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        btnLeftRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
        leftBtn.setLayoutParams(btnLeftRelativeParams);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                flashButton(leftBtn);

                if(!game.getGameOver()){
                    game.setCurrentPlayerDirection("w");
                }else{
                    game.initGame();
                    game.setCurrentPlayerDirection("w");
                    game.startGame();
                }
            }
        });


        final Button upBtn = new Button(this);
        upBtn.setText("up");
        RelativeLayout.LayoutParams btnTopRelativeParams = new RelativeLayout.LayoutParams(220, 220);
        btnTopRelativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btnTopRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        upBtn.setLayoutParams(btnTopRelativeParams);

        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flashButton(upBtn);

                if(!game.getGameOver()){
                    game.setCurrentPlayerDirection("n");
                }else{
                    game.initGame();
                    game.setCurrentPlayerDirection("w");
                    game.startGame();
                }

            }
        });

        buttonsContainer.addView(upBtn);


        final Button downBtn = new Button(this);
        downBtn.setText("down");

        RelativeLayout.LayoutParams btnDownRelativeParams = new RelativeLayout.LayoutParams(220, 220);
        btnDownRelativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btnDownRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        downBtn.setLayoutParams(btnDownRelativeParams);

        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flashButton(downBtn);

                if(!game.getGameOver()){
                    game.setCurrentPlayerDirection("s");
                }else{
                    game.initGame();
                    game.setCurrentPlayerDirection("w");
                    game.startGame();
                }

            }
        });

        buttonsContainer.addView(downBtn);



        buttonsContainer.addView(leftBtn);

        final Button rightBtn = new Button(this);
        rightBtn.setText("right");
        RelativeLayout.LayoutParams btnRightRelativeParams = new RelativeLayout.LayoutParams(220, 220);
        btnRightRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnRightRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
        rightBtn.setLayoutParams(btnRightRelativeParams);

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashButton(rightBtn);

                if(!game.getGameOver()){
                    game.setCurrentPlayerDirection("e");
                }else{
                    game.initGame();
                    game.setCurrentPlayerDirection("w");
                    game.startGame();
                }
            }
        });

        buttonsContainer.addView(rightBtn);









//        root.addView(leftBtn);
//        root.addView(rightBtn);
//        root.addView(upBtn);
//        root.addView(downBtn);



    }

    private void flashButton(final Button button){


        button.setBackgroundColor(Color.GREEN);

        final Timer mTimer = new Timer();

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        button.setBackgroundColor(Color.LTGRAY);
                        mTimer.cancel();
                    }
                });
            }
        },  100);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.game.initGame();
        this.game.setCurrentPlayerDirection("w");
//        this.game.setCurrentPlayerDirection("n");
        this.game.startGame();
    }
}
