package com.example.slurp.myapplication;

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


        this.game = new Game();
        this.game.setSnakeGameListener(new SnakeGameListener() {
            @Override
            public void onAppleEaten() {
                new PlaySoundThread(getApplicationContext(),  R.raw.power_up, 0.2f).run();
            }

            @Override
            public void onGameOver() {
                new PlaySoundThread(getApplicationContext(),  R.raw.game_over, 0.3f).run();
            }
        });

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
                new RelativeLayout.LayoutParams(700, 500);
//        buttonsContainerParams.addRule();
        buttonsContainer.setLayoutParams(buttonsContainerParams);
        buttonsContainer.setBackgroundColor(Color.DKGRAY);
//        buttonsContainer.setGravity(Gravity.BOTTOM);

        root.addView(buttonsContainer);


        final Button leftBtn = new Button(this);
        leftBtn.setText("left");
        // green rgb(0, 204, 102)
        // light green rgb(204, 255, 230)


        RelativeLayout.LayoutParams btnLeftRelativeParams = new RelativeLayout.LayoutParams(235, 235);
        btnLeftRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        btnLeftRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
        leftBtn.setLayoutParams(btnLeftRelativeParams);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flashButton(leftBtn);
                vibrate();


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
        // blue rgb(0, 102, 255)
        // light blue rgb(204, 224, 255)


        RelativeLayout.LayoutParams btnTopRelativeParams = new RelativeLayout.LayoutParams(235, 235);
        btnTopRelativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btnTopRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        upBtn.setLayoutParams(btnTopRelativeParams);

        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flashButton(upBtn);
                vibrate();


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
        Color yellow = new Color();
//        yellow.
//        downBtn.setBackgroundColor(Color.R);
        // yellow rgb(255, 255, 0)
        // light yellow rgb(255, 255, 204)

        RelativeLayout.LayoutParams btnDownRelativeParams = new RelativeLayout.LayoutParams(235, 235);
        btnDownRelativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        btnDownRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        downBtn.setLayoutParams(btnDownRelativeParams);

        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flashButton(downBtn);
                vibrate();
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
        //red 255, 51, 0
        // light red (255, 214, 204)


        RelativeLayout.LayoutParams btnRightRelativeParams = new RelativeLayout.LayoutParams(235, 235);
        btnRightRelativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        btnRightRelativeParams.addRule(RelativeLayout.CENTER_VERTICAL);
        rightBtn.setLayoutParams(btnRightRelativeParams);

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashButton(rightBtn);
                vibrate();

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
