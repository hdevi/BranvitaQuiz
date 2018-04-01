package com.mickey.himan.brainvitaquiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * It Saves the Data using Shared Preferences
 * It has Load Method which is used to get the Parameters which want to save
 * It has method which sets the parameters and show the result
 * Created by Himanshu Devi on 3/11/17.
 */

public class Save_Data {

    public static final String PREFERENCE = "prefer";
    public static int mQuestions = 0;
    public static int mCorrectAnswers = 0;
    public static int mCheat = 0;

    private static final String TAG = "SavedData";

    private static Context mContext;

    public static void Init(Context context)
    {
        mContext = context;
    }

    public static void LoadusingPref() {
        SharedPreferences settings = mContext.getSharedPreferences(PREFERENCE, 0);
        mQuestions = settings.getInt("ROOM1_FAN", 0);
        mCheat = settings.getInt("ROOM1_LIGHT", 0);
        mCorrectAnswers = settings.getInt("ROOM2_FAN", 0);
    }

    public static void StoreusingPref() {
        // get the existing preference file
        SharedPreferences settings = mContext.getSharedPreferences(PREFERENCE, 0);
        final SharedPreferences.Editor editor = settings.edit();

        editor.putInt("QUESTIONS", mQuestions);
        editor.putInt("CHEAT", mCheat);
        editor.putInt("CORRECT_ANSWERS", mCorrectAnswers);
        editor.commit();
    }

    public static void SetQuestions(int NO_OF_QUESTIONS)
    {
        mQuestions = NO_OF_QUESTIONS;
        Log.d(TAG, "SetQuestions");
    }
    public static void SetCheat(int NO_CHEAT)
    {
        mCheat = NO_CHEAT ;
        Log.d(TAG, "SetCheat");
    }
    public static void SetCorrectAnswers(int NO_CORRECT_ANSWERS)
    {
        mCorrectAnswers = NO_CORRECT_ANSWERS;
        Log.d(TAG, "SetCorrectAnswers");
    }

    public static int GetQuestions()
    {
        Log.d(TAG, "GetQuestions");
        return mQuestions ;
    }
    public static int GetCheat()
    {
        Log.d(TAG, "GetCheat");
        return mCheat ;
    }
    public static int GetCorrectAnswers()
    {
        Log.d(TAG, "GetCorrectAnswers");
        return mCorrectAnswers ;
    }
}
