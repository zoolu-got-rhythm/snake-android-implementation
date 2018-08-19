package com.example.slurp.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    public final static String DIFFICULTY_MODE = "DIFFICULTY_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent myService = new Intent(MenuActivity.this, BackgroundServicePlayMusic.class);
        myService.setAction("");
        startService(myService);

        LinearLayout root = new LinearLayout(this);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);

        setContentView(root);

        TextView mTv1 = new TextView(this);
        mTv1.setText("select game mode");
        mTv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        root.addView(mTv1);

        Button easyBtn = new Button(this);
        easyBtn.setText("easy");
        root.addView(easyBtn);
        ViewGroup.LayoutParams easyBtnParams = easyBtn.getLayoutParams();
        easyBtnParams.width = 400;
        easyBtn.setLayoutParams(easyBtnParams);
        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(DIFFICULTY_MODE, 6);
                startActivity(intent);
            }
        });


        Button hardBtn = new Button(this);
        hardBtn.setText("hard");
        root.addView(hardBtn);

        ViewGroup.LayoutParams hardBtnParams = easyBtn.getLayoutParams();
        hardBtnParams.width = 400;
        hardBtn.setLayoutParams(hardBtnParams);
        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(DIFFICULTY_MODE, 12);
                startActivity(intent);
            }
        });




    }
}
