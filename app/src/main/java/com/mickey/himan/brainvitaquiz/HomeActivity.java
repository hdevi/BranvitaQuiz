package com.mickey.himan.brainvitaquiz;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * This Activity is used Initialise the following
 * Question_set
 * Readfile
 * Initialise Spinner
 * Shows the Result of Previous Quiz taken
 * Storage Permissions
 * Created by Himanshu Devi on 27/10/17.
 */
public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static final String TAG = HomeActivity.class.getSimpleName();

    /**
     * References Declaration Section
     */
    private Spinner mSpinner;
    private TextView mFirst_edit;
    private TextView mSecond_edit;
    private TextView mthird_edit;
    Context context;
    MediaPlayer backmusic;

    /**
     * Variables initialised Section
     */
    private int CorrectAnswers = 0;
    private int Cheated = 0;
    private int Questions = 0;
    private int Stats = 0;
    static boolean isStorageReadable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /**
         * Assigning Id's to reference variables
         */
        mFirst_edit = (TextView) findViewById(R.id.first_edit);
        mSecond_edit = (TextView) findViewById(R.id.Second_edit);
        mthird_edit = (TextView) findViewById(R.id.third_edit);
        mSpinner = (Spinner) findViewById(R.id.spin);
        mSpinner.setOnItemSelectedListener(this);

        /**
         * Music start on running Home Activity
         */
        backmusic = MediaPlayer.create(HomeActivity.this, R.raw.kbcmain);
        backmusic.start();

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

        initialise();
    }

    /**
     * initialise function does following things
     * Generates File Permission
     * Initialises Question set for a Different Quiz Topics
     * Initialises Spinner
     * Shows Previous Quiz Results by accessing Shared Prefernces in Home Activity
     */
    private void initialise() {
        storagePermit();
        Log.d(TAG, "File Permitted");
        initialiseQuestionSet();
        Log.d(TAG, "QuestionSet Initialised");
        initSpinner();
        showPrevResults();
    }

    /**
     * It Displays Previous Quiz Results
     */
    private void showPrevResults() {
        Save_Data.Init(this);
        Save_Data.LoadusingPref();
        Questions = Save_Data.GetQuestions();
        CorrectAnswers = Save_Data.GetCorrectAnswers();
        Cheated = Save_Data.GetCheat();
        try {
            Stats = (CorrectAnswers * 100) / Questions;
        }catch(ArithmeticException e)
        {
            e.printStackTrace();
        }
        mFirst_edit.setText(String.format("%d Correct", CorrectAnswers));
        mSecond_edit.setText(String.format("%d  Cheated", Cheated));
        mthird_edit.setText(String.format("%d  Percentage", Stats));

    }


    /**
     * Checks File Permission
     */
    private void storagePermit() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
    }

    /**
     * Parses JSON file from External storage and initialises Question Set
     */
    private void initialiseQuestionSet() {
        Parser.InitialiseQuestionSet(this);
        Log.d(TAG, "Iniside Question set Initialised");
    }

    /**
     * Initialises Spinner with Quiz Topics in JSON File
     */
    private void initSpinner() {
        Log.d(TAG, "Iniside Spinner");
        List<String> type_name = getTopicNames();
        Log.d(TAG, "" + type_name);
        if (type_name != null && type_name.size() > 0) {
            Log.d(TAG, "" + type_name);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, type_name);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), "Error While Loading", Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * Topics are added into the Arraylist and displayed in spinner
     * @return  topics which has List as a return type
     */
    private List<String> getTopicNames() {
        List<Quiz_Type> types = QuizHandler.getInstance().getTypes();
        List<String> type_name = new ArrayList<>();
        if (types != null) {
            String defaultTextForSpinner = "Please Select Topic";
            type_name.add(defaultTextForSpinner);
            for (int i = 0; i < types.size(); i++) {
                Quiz_Type type = types.get(i);
                String name = type.getTypeName();
                type_name.add(name);
            }
        }
        return type_name;
    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Checks if File is Readable and is authorized
                    isStorageReadable = FilePermissions.isExternalStorageReadable();
                    if (!isStorageReadable) {
                        Toast.makeText(this, "sorry_text", Toast.LENGTH_SHORT).show();
                    } else {
                        FilePermissions.ReadJSONFile(context);
                    }

                } else {
                    Log.d(TAG,"Permission Not granted");
                }
                return;
            }
        }
    }


    /**
     *
     * @param parent The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "Position of selected item: " + position);
        if (position == 0) {
            Toast.makeText(this, "Please select topic", Toast.LENGTH_SHORT).show();
        } else {
            backmusic.stop();
            // We have set default item for spinner hence subtracting 1 from position
            int selectedTypeIndex = position - 1;
            QuizHandler.getInstance().setSelectedTypeIndex(selectedTypeIndex);
            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
