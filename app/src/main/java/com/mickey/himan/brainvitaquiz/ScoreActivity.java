package com.mickey.himan.brainvitaquiz;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * It Displays the Statistics of the current Quiz Taken
 * No of questions
 * No of Cheated questions
 * No of Coreect Questions Answered
 * Statistics
 * Created by Himanshu Devi on 3/11/17.
 */

public class ScoreActivity extends AppCompatActivity {

    //Constants
    private static final String TAG = ScoreActivity.class.getSimpleName();
    private static final String STATE_CHEAT = "cheat";
    private static final String STATE_QUESTIONS = "questions";
    private static final String STATE_ANSWERS = "answer";
    private static final String STATE_STATS = "stats";

    //References
    private TextView mQuestions;
    private TextView mCorrectAnswers;
    private TextView mCheat;
    private TextView mStats;
    private MediaPlayer fame;
    private Button mShowStats;

    //initialise variables
    private int Questions = 0;
    private int CorrectAnswers = 0;
    private int Cheat = 0;
    private int Stats = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Inside onCreate");
        setContentView(R.layout.activity_score);


        if (savedInstanceState != null) {
            Questions = savedInstanceState.getInt(STATE_QUESTIONS, 0);
            CorrectAnswers = savedInstanceState.getInt(STATE_ANSWERS, 0);
            Cheat = savedInstanceState.getInt(STATE_CHEAT, 0);
            Stats = savedInstanceState.getInt(STATE_STATS, 0);
            mQuestions.setText(String.format("%d", Questions));
            mCorrectAnswers.setText(String.format("%d", CorrectAnswers));
            mCheat.setText(String.format("%d", Cheat));
            mStats.setText(String.format("%d percent", Stats));
            Log.d(TAG, "Instance saved");
        }

        fame = MediaPlayer.create(ScoreActivity.this, R.raw.halloffame);
        fame.start();

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

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.item1:
                Toast.makeText(getApplicationContext(),"Select other Quiz",Toast.LENGTH_SHORT).show();
                homeActivity();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void homeActivity()
    {
        Intent intent = new Intent(ScoreActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(STATE_QUESTIONS, Questions);
        savedInstanceState.putInt(STATE_ANSWERS, CorrectAnswers);
        savedInstanceState.putInt(STATE_CHEAT, Cheat);
        savedInstanceState.putInt(STATE_STATS, Stats);
        Log.d(TAG, "Instance is being saved");
    }

    //Initialising Shared Preference
    private void init() {
        Save_Data.Init(this);

        mQuestions = (TextView) findViewById(R.id.stat_tot_ques_edit);
        mCorrectAnswers = (TextView) findViewById(R.id.stat_correct_edit);
        mCheat = (TextView) findViewById(R.id.stat_cheat_edit);
        mStats = (TextView) findViewById(R.id.stat_edit);

        showScores();
    }

    /**
     * This shows results of the quiz using shared preference
     */
    private void showScores() {
        mShowStats = (Button) findViewById(R.id.result_button);
        mShowStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Loading from SharedData class
                Save_Data.LoadusingPref();
                Questions = Save_Data.GetQuestions();
                Cheat = Save_Data.GetCheat();
                CorrectAnswers = Save_Data.GetCorrectAnswers();

                Stats = (CorrectAnswers * 100) / Questions;
                Log.d(TAG, "ResultActivity");

                mQuestions.setText(String.format("%d", Questions));
                mCorrectAnswers.setText(String.format("%d", CorrectAnswers));
                mCheat.setText(String.format("%d", Cheat));
                mStats.setText(String.format("%d percent", Stats));

                int messageResId = 0;
                messageResId = R.string.thanks_text;
                Toast.makeText(ScoreActivity.this, messageResId, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        fame.stop();
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }


}
