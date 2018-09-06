package com.example.slurp.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private final String SHARED_PREF_HIGH_SCORE_FILE_KEY = "SHARED_PREF_HIGH_SCORE_FILE_KEY";

    private final String SHARED_PREF_HIGH_SCORE_KEY_ONE_THUMB = "SHARED_PREF_HIGH_SCORE_KEY_ONE_THUMB";
    private final String SHARED_PREF_HIGH_SCORE_KEY_TWO_THUMBS = "SHARED_PREF_HIGH_SCORE_KEY_TWO_THUMBS";

    private String highScoreKeyForCurrentGameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mHandler = new Handler();
        final ScoreTextView mScoreTextView = new ScoreTextView(this, "score",
                RelativeLayout.ALIGN_PARENT_LEFT, Color.CYAN);
        final ScoreTextView mHighScoreTextView = new ScoreTextView(this, "high score",
                RelativeLayout.ALIGN_PARENT_RIGHT, Color.GREEN);

        Bundle recievedIntentFromMenu = getIntent().getExtras();
        int difficultyMode = recievedIntentFromMenu.getInt(MenuActivity.DIFFICULTY_MODE);


        // wrap into method
//        Context context = getActivity();
        SharedPreferences sharedPref = this.getSharedPreferences(
                SHARED_PREF_HIGH_SCORE_FILE_KEY, Context.MODE_PRIVATE);

        if(difficultyMode == 6){
            highScoreKeyForCurrentGameMode = SHARED_PREF_HIGH_SCORE_KEY_ONE_THUMB;
        }

        if(difficultyMode == 12){
           highScoreKeyForCurrentGameMode = SHARED_PREF_HIGH_SCORE_KEY_TWO_THUMBS;
        }

        int highScore = sharedPref.getInt(this.highScoreKeyForCurrentGameMode, 0);
        mHighScoreTextView.updateScore(highScore);


        this.game = new Game(difficultyMode,
                4, 25);
        this.game.setSnakeGameListener(new SnakeGameListener() {
            @Override
            public void onAppleEaten(int score) {
                new PlaySoundThread(getApplicationContext(),  R.raw.power_up, 0.4f).run();
                mScoreTextView.updateScore(score);
            }

            @Override
            public void onGameOver() {
                new PlaySoundThread(getApplicationContext(),  R.raw.game_over, 0.4f).run();

                // if new high score is greater than update view in memory and save high score
                // to be read from sharePreferences file for next time the activity/game mode is open
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                        SHARED_PREF_HIGH_SCORE_FILE_KEY, Context.MODE_PRIVATE);

                if(sharedPref.getInt(highScoreKeyForCurrentGameMode, 0) <
                        game.getCurrentPlayerScore()){

                    int newHighScore = game.getCurrentPlayerScore();
                    mHighScoreTextView.updateScore(newHighScore);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(highScoreKeyForCurrentGameMode, newHighScore);
                    editor.apply(); // asyncrhonous write/update of sharedPrefs file
                }
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


//        game.addObserver(mScoreTextView);


        RelativeLayout scoresRelativeLayout = new RelativeLayout(this);
        scoresRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(935,
                ViewGroup.LayoutParams.WRAP_CONTENT));
//        scoresRelativeLayout.setOrientation(RelativeLayout.HORIZONTAL);
//        scoresRelativeLayout.setBackgroundColor(Color.GREEN);

        scoresRelativeLayout.addView(mScoreTextView);
        scoresRelativeLayout.addView(mHighScoreTextView);

        root.addView(scoresRelativeLayout);


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
        leftBtn.setText("◀");
        leftBtn.setTextSize(35f);
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
        upBtn.setText("▲");
        upBtn.setTextSize(35f);
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
        downBtn.setText("▼");
        downBtn.setTextSize(35f);
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
        rightBtn.setText("▶");
        rightBtn.setTextSize(35f); 
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
        vibrator = getApplicationContext().getSystemService(Vibrator.class);

        if (vibrator.hasVibrator()) {
            Log.d("has vibrator", Boolean.toString(vibrator.hasVibrator()));

            VibrationEffect effect;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                VibrationEffect.DEFAULT_AMPLITUDE
                effect = VibrationEffect.createOneShot(50, 200);
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
