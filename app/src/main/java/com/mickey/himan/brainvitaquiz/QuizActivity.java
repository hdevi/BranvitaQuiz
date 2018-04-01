package com.mickey.himan.brainvitaquiz;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 *
 * This Activity class does following things
 * Initialises selected topics from JSON file
 * It updates the User Interface of Quiz activity when Next question Arrives
 * It also Sets the parameters to save the content using Shared Preferences
 * Checks if the selected Answer is True and Displays the toast And goes to next question
 * Created by Himanshu Devi on 3/11/17.
 *
 */

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Constants
     */
    private static final String TAG = QuizActivity.class.getSimpleName();
    private static final String IS_CHEATED = "cheater";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;


    /**
     * References
     */
    private RadioGroup mOptionTextView;
    private TextView mQuestionTextView;
    private MediaPlayer music;
    private boolean mIsCheater;
    private Quiz_Type mSelectedType = null;
    private int mCurrentQuestionIndex = 0;
    private List<String> strArr = null;


    /**
     * Initialisation of variables
     */
    static int NO_OF_QUESTIONS = 0;
    static int NO_CORRECT_ANSWERS = 0;
    static int NO_CHEAT = 0;
    int noCheat = 0;
    int noCorrect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "Activity created");
        setContentView(R.layout.activity_quiz);

        music = MediaPlayer.create(QuizActivity.this, R.raw.got);
        music.start();

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

        //SharedPreferences need to be initialized in all the classes where they are used
        Save_Data.Init(this);

        mQuestionTextView = (TextView) findViewById(R.id.question_text);
        mOptionTextView = (RadioGroup) findViewById(R.id.option_set);

        findViewById(R.id.question_text).setOnClickListener(this);
        findViewById(R.id.cheat).setOnClickListener(this);

        if (savedInstanceState != null) {
            mCurrentQuestionIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(IS_CHEATED, false);
            Log.d(TAG, "Instance saved");
        }
        mOptionTextView.clearCheck();
        initUI();
    }

    //Saving the state of the activity on rotation
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentQuestionIndex);
        Log.d(TAG, "Instance is being saved");
    }

    //Initializing the GUI
    private void initUI() {
        initSelectedType();
        startQuiz();
    }


    //Initializing the selected topics taken from JSON file
    private void initSelectedType() {
        int selectedTypeIndex = QuizHandler.getInstance().getSelectedTypeIndex();
        if (selectedTypeIndex != QuizHandler.ERROR_INDEX) {
            List<Quiz_Type> types = QuizHandler.getInstance().getTypes();
            mSelectedType = getSelectedType(selectedTypeIndex, types);
        } else {
            Log.e(TAG, "Quiz topic is not selected, finishing activity");
            finish();
        }
    }


    private Quiz_Type getSelectedType(int selectedTypeIndex, List<Quiz_Type> types) {
        Quiz_Type type = null;
        Log.d(TAG, "Selected index: " +selectedTypeIndex);
        if (types != null && types.size() > 0) {
            try {
                type = types.get(selectedTypeIndex);
            } catch (Exception e) {
                Log.e(TAG, "This should not happen, selectedTopicIndex set incorrectly somewhere");
            }
        }
        return type;
    }

    private void startQuiz() {
        updateUI();
    }


    //This method gives a new question with Relevant options Displayed
    private void updateUI() {
        List<Question_set> questions = mSelectedType.getQuestions();
        Log.d(TAG,"List of Questions for Selected Type:" + mSelectedType+ " "+questions);
        String question = questions.get(mCurrentQuestionIndex).getQuestion();
        mQuestionTextView.setText(question);
        Log.d(TAG, "Current question: " + question);
        strArr = questions.get(mCurrentQuestionIndex).getOptions_Set();
        for (int i = 0; i < mOptionTextView.getChildCount(); i++) {
            ((RadioButton) mOptionTextView.getChildAt(i)).setText(strArr.get(i));
        }
        mIsCheater = false;
        mOptionTextView.clearCheck();
    }

    //Increments the current index of question
    public void selectNextQuestion() {
        if (!quizOver()) {
            mCurrentQuestionIndex++;
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Button clicked: " + v);
        switch (v.getId()) {
            case R.id.question_text:
                ontextClicked();
                break;

            case R.id.cheat:
                Log.d(TAG, "Cheat Button Clicked");
                music.pause();
                onCheatButtonClicked();
                music.start();
                break;
        }
    }

    private void onCheatButtonClicked() {
        //Start Cheat Activity
        List<Question_set> questions = mSelectedType.getQuestions();
        String answerIsCorrect = questions.get(mCurrentQuestionIndex).getAnswer();
        Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsCorrect);
        startActivityForResult(intent, REQUEST_CODE_CHEAT);
    }


    // This method checks the Answer when the Next Button is clicked
    private void ontextClicked() {
        if (quizOver()) {
            showScores();
        } else {
            if (mOptionTextView.getCheckedRadioButtonId() != -1) {
                checkAnswer(((RadioButton) findViewById(mOptionTextView.getCheckedRadioButtonId())).getText().toString());
                selectNextQuestion();
                updateUI();
            } else {
                Toast.makeText(this, "Please select any one option", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //This method is responsible for storing the quiz statistics in the SharedPreferences and moving over to ResultActivity
    private void showScores() {
        Log.d(TAG, "Quiz over show results");
        checkAnswer(((RadioButton) findViewById(mOptionTextView.getCheckedRadioButtonId())).getText().toString());

        NO_OF_QUESTIONS = mCurrentQuestionIndex + 1;
        NO_CHEAT = noCheat;
        NO_CORRECT_ANSWERS = noCorrect;

        Save_Data.SetQuestions(NO_OF_QUESTIONS);
        Save_Data.SetCheat(NO_CHEAT);
        Save_Data.SetCorrectAnswers(NO_CORRECT_ANSWERS);
        Save_Data.StoreusingPref();

        music.stop();
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
        finish();
    }

    //Checks if Quiz is over
    private boolean quizOver() {
        int totalQuestions = mSelectedType.getQuestions().size();
        return !(mCurrentQuestionIndex < totalQuestions - 1);
    }

    // This method checks the answer and records whether the user has cheated or not. Also the number of Correct Answers
    private void checkAnswer(String userPressedTrue) {
        List<Question_set> questions = mSelectedType.getQuestions();
        String answerIsCorrect = questions.get(mCurrentQuestionIndex).getAnswer();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgement_toast;
            Toast.makeText(this, R.string.judgement_toast, Toast.LENGTH_SHORT).show();
            noCheat++;
            Log.d(TAG, "Cheated!");
        } else {
            if (userPressedTrue.equals(answerIsCorrect)) {
                messageResId = R.string.correct_toast;
                noCorrect++;
                Log.d(TAG, "Correct Answer!");
            } else {
                messageResId = R.string.incorrect_toast;
                Log.d(TAG, "Incorrect Answer!");
            }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }
    }

    // Collects the result of the CheatActivity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.isCheated(data);
        }
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
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
