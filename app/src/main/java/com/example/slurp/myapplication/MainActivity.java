package com.example.slurp.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
        final ScoreTextView mScoreTextView = new ScoreTextView(this);

        Bundle recievedIntentFromMenu = getIntent().getExtras();


        this.game = new Game(recievedIntentFromMenu.getInt(MenuActivity.DIFFICULTY_MODE),
                10, 25);
        this.game.setSnakeGameListener(new SnakeGameListener() {
            @Override
            public void onAppleEaten(int score) {
                new PlaySoundThread(getApplicationContext(),  R.raw.power_up, 0.2f).run();
                mScoreTextView.updateScore(score);

            }

            @Override
            public void onGameOver() {
                new PlaySoundThread(getApplicationContext(),  R.raw.game_over, 0.3f).run();
            }
        });

        View snakeGameView = new SnakeGameView(this, 900, 900);
        game.addObserver((Observer) snakeGameView);

        snakeGameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(game.getGameOver()) {
                    game.initGame();
                    game.setCurrentPlayerDirection("w");
                    game.startGame();
                }
            }
        });

        LinearLayout root = findViewById(R.id.root);
        root.setGravity(Gravity.CENTER);

        mScoreTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        game.addObserver(mScoreTextView);
        root.addView(mScoreTextView);

        root.addView(snakeGameView);

        RelativeLayout buttonsContainer = new RelativeLayout(this);

        RelativeLayout.LayoutParams buttonsContainerParams =
                new RelativeLayout.LayoutParams(710, 500);
//        buttonsContainerParams.addRule();
        buttonsContainer.setLayoutParams(buttonsContainerParams);
        buttonsContainer.setBackgroundColor(Color.DKGRAY);
//        buttonsContainer.setGravity(Gravity.BOTTOM);

        root.addView(buttonsContainer);


        final Button leftBtn = new Button(this);
        leftBtn.setText("left");
        leftBtn.setTextColor(Color.WHITE);

        final int nesGreen = getResources().getColor(R.color.nesGreen);
        leftBtn.setBackgroundColor(nesGreen);
        // green rgb(0, 204, 102)
        // light green rgb(204, 255, 230)


        RelativeLayout.LayoutParams btnLeftRelativeParams = new RelativeLayout.LayoutParams(235, 250);
        btnLeftRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        btnLeftRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
        leftBtn.setLayoutParams(btnLeftRelativeParams);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flashButton(leftBtn, Color.CYAN,
                        nesGreen);
                vibrate();

                if(!game.getGameOver())
                    game.setCurrentPlayerDirection("w");
            }
        });


        final Button upBtn = new Button(this);
        upBtn.setText("up");
        upBtn.setTextColor(Color.WHITE);

        // blue rgb(0, 102, 255)
        // light blue rgb(204, 224, 255)
        final int nesBlue = getResources().getColor(R.color.nesBlue);
        upBtn.setBackgroundColor(nesBlue);


        RelativeLayout.LayoutParams btnTopRelativeParams = new RelativeLayout.LayoutParams(235, 250);
        btnTopRelativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btnTopRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        upBtn.setLayoutParams(btnTopRelativeParams);

        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flashButton(upBtn, Color.CYAN,
                        nesBlue);
                vibrate();

                if(!game.getGameOver())
                    game.setCurrentPlayerDirection("n");
            }
        });

        buttonsContainer.addView(upBtn);


        final Button downBtn = new Button(this);
        downBtn.setText("down");
        downBtn.setTextColor(Color.WHITE);
        final int yellowNesColour = getResources().getColor(R.color.nesYellow);
        downBtn.setBackgroundColor(yellowNesColour);
//        yellow.
//        downBtn.setBackgroundColor(Color.R);
        // yellow rgb(255, 255, 0)
        // light yellow rgb(255, 255, 204)

        RelativeLayout.LayoutParams btnDownRelativeParams = new RelativeLayout.LayoutParams(235, 250);
        btnDownRelativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btnDownRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        downBtn.setLayoutParams(btnDownRelativeParams);

        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flashButton(downBtn, Color.CYAN,
                        yellowNesColour);
                vibrate();
                if(!game.getGameOver())
                    game.setCurrentPlayerDirection("s");
            }
        });

        buttonsContainer.addView(downBtn);



        buttonsContainer.addView(leftBtn);

        final Button rightBtn = new Button(this);
        rightBtn.setText("right");
        rightBtn.setTextColor(Color.WHITE);

        //red 255, 51, 0
        final int redNesColour = getResources().getColor(R.color.nesRed);
        rightBtn.setBackgroundColor(redNesColour);
        // light red (255, 214, 204)


        RelativeLayout.LayoutParams btnRightRelativeParams = new RelativeLayout.LayoutParams(235, 250);
        btnRightRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnRightRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
        rightBtn.setLayoutParams(btnRightRelativeParams);

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashButton(rightBtn,
                        Color.CYAN, redNesColour);
                vibrate();

                if(!game.getGameOver())
                    game.setCurrentPlayerDirection("e");

            }
        });

        buttonsContainer.addView(rightBtn);









//        root.addView(leftBtn);
//        root.addView(rightBtn);
//        root.addView(upBtn);
//        root.addView(downBtn);



    }

    private void flashButton(final Button button, int flashColour, final int origialColour){

        button.setBackgroundColor(flashColour);

        final Timer mTimer = new Timer();

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        button.setBackgroundColor(origialColour);
                        mTimer.cancel();
                    }
                });
            }
        },  100);
    }

    private void vibrate(){
        Vibrator vibrator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vibrator = getApplicationContext().getSystemService(Vibrator.class);
            Log.d("has vibrator", Boolean.toString(vibrator.hasVibrator()));

            VibrationEffect effect;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                effect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(effect);
            }else{
                vibrator.vibrate(50);
            }
        }
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
