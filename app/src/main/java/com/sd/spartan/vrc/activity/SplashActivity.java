package com.sd.spartan.vrc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.spartan.vrc.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private boolean action = false;
    private int SPLASH_TIME = 3000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        ImageView mSnakeImage = findViewById(R.id.image_snake);
        TextView mSnakeText = findViewById(R.id.text_main_snake);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        mSnakeImage.setImageResource(R.drawable.logo_vrc);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_move);
        mSnakeImage.getLayoutParams().width = width - 100;
        mSnakeImage.getLayoutParams().height = ((height / 2) - 130);
        mSnakeImage.requestLayout();
        mSnakeImage.startAnimation(animation);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.up_move);
        mSnakeText.startAnimation(animation2);

        new Handler().postDelayed(() -> {
            if (!action) {
                goActivity();
            }
        }, SPLASH_TIME);

    }

    private void goActivity() {
        Intent mySuperIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(mySuperIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPLASH_TIME = 0;
        action = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        SPLASH_TIME = 0;
        action = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        SPLASH_TIME = 0;
        action = true;
        finish();
    }
}