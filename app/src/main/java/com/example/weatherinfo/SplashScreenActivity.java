package com.example.weatherinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView weatherLogoImageView = findViewById(R.id.weather_logo);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate); // Load the rotation animation

        weatherLogoImageView.startAnimation(rotateAnimation);

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation ended, start the main activity or navigate to another screen
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the splash screen activity
            }

            @Override
            public void onAnimationStart(Animation animation) {
                // Animation started
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeated (if needed)
            }
        });
    }
}