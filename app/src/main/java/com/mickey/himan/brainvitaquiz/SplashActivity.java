package com.mickey.himan.brainvitaquiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * This is Splash Activity
 * which activated when launching app
 * It has Animation and a MediaPlayer class
 * Created by Himanshu Devi on 27/10/17.
 */

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final long SPLASH_DELAY = 4000;

    /**
     * references declaration section
     */
    Handler mHandler;
    MediaPlayer welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "Splash Activity created");
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);


        mHandler = new Handler();

        /**
         * Starting music of app using MediaPlayer class
         */
        welcome = MediaPlayer.create(SplashActivity.this, R.raw.kbc);
        welcome.start();

        /**
         * Creating another thread for Animation (Rotating Globe )
         */
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.start();


        final ImageView imageView1 = (ImageView) findViewById(R.id.imageView2);

        /**
         * Below section is used to load the Animation with different transitions created using .XML file
         */
        final Animation animation_1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.antirotate);
        final Animation animation_4 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.blink);
        final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

        //Animation is started with animation 2 transition
        imageView1.startAnimation(animation_2);
        animation_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView1.startAnimation(animation_1);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        animation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageView1.startAnimation(animation_4);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation_4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView1.startAnimation(animation_3);

                /**
                 * Below section is used to create Specified Splash screen Delay
                 */
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handleSplashFinish();
                    }
                }, SPLASH_DELAY);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void handleSplashFinish() {
        //Music is stopped
        welcome.stop();

        /**
         * Moving to Home Activity using intent and destroying Splash Activity
         */
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        Log.d(TAG, "Finishing activity");
        finish();
    }

}
