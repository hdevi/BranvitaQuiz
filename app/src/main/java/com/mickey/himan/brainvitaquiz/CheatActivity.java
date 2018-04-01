package com.mickey.himan.brainvitaquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Checks if the User Has cheated or not and Sends the Appropriate message if it does
 * It has button implemented which when clicked shows the Answer
 * Created by Himanshu Devi on 3/11/17.
 *
 */

public class CheatActivity extends AppCompatActivity {

    private static final String TAG = CheatActivity.class.getSimpleName();
    private static final String ANSWER_IS_TRUE = "com.mickey.himan.brainvitaquiz.answer_is_true";
    private static final String ANSWER_SHOWN = "com.mickey.himan.brainvitaquiz.answer_shown";
    private static final String SHOWN = "answer";
    private String mAnswerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private Boolean isAnswerShown = false;

    public static Intent newIntent(Context packageContext, String answerIsCorrect) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(ANSWER_IS_TRUE, answerIsCorrect);
        return intent;
    }


    public static boolean isCheated(Intent result) {
        return result.getBooleanExtra(ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            isAnswerShown = savedInstanceState.getBoolean(SHOWN,false);
            mAnswerTextView.setText(mAnswerIsTrue);
            Log.d(TAG, "Instance saved");
        }

        mAnswerIsTrue = getIntent().getStringExtra(ANSWER_IS_TRUE);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text);
        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (mAnswerIsTrue.equals(null)) {
                    mAnswerTextView.setText("");
                } else {
                    mAnswerTextView.setText(mAnswerIsTrue);
                }
                isAnswerShown = true;
                ShownResult(isAnswerShown);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(SHOWN, isAnswerShown);
        Log.d(TAG, "Instance is being saved");
    }

    private void ShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
