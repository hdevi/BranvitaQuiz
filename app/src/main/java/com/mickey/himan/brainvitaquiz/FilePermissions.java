package com.mickey.himan.brainvitaquiz;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Himanshu Devi on 3/11/17.
 * This Class checks if External Storage on phone is Readable or Not
 * Also it Reads JSON File from the storage
 */

public class FilePermissions {
    private static final String TAG = FilePermissions.class.getSimpleName();

    //Checks if External storage is Readable or Not
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d(TAG, "External Storage Readable");
            return true;
        }

        return false;
    }

    /**
     * Reads the File stored in external storage directory
     * @param context It is current context of application
     * @return string from file in external storage
     */
    public static String ReadJSONFile(Context context)
    {
        StringBuffer buffer = new StringBuffer();
        context.getApplicationContext();
        try {

            File externalStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File QuizFile = new File(externalStorageDir, "quiz/Quiz.json");
            FileInputStream stream = new FileInputStream(QuizFile);
            //InputStream is = context.getAssets().open("questions.json");
            BufferedReader reader = null;
            try {
                Log.d(TAG, "Read data from file");
                //the file channel associated with this file input stream
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                reader = new BufferedReader(new InputStreamReader(stream));
                String mLine;
                while ((mLine = reader.readLine()) != null) {
                    buffer.append(mLine);
                }
            } catch (IOException e) {
                Log.e(TAG, "Exception while reading file: " + e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}

