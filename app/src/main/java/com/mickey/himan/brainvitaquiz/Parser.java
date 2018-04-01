package com.mickey.himan.brainvitaquiz;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class Reads a file from External storage and puts it in a String Variable.
 * The parsing logic is done recursively. First data is parsed using root object then using JSONArray
 * all objects in that Array are parsed by Key name and put that in the Arraylist
 *
 * Created by Himanshu Devi on 27/10/17.
 */

public class Parser {
    private static final String TAG = Parser.class.getSimpleName();

    /**
     * @param context It is a Current Context of Application
     */
    public static void InitialiseQuestionSet(Context context) {
        String data = FilePermissions.ReadJSONFile(context);
        Log.d(TAG, "Jason file read");
        List<Quiz_Type> types = dataParser(data);
        Log.d(TAG, "ArrayList of all the topic Wise Qustion and Answer Set" + types);
        QuizHandler.getInstance().setTypes(types);
    }

    /**
     * @param data It string which is stored in a file
     * @return The Arraylist of all values related to a specific Key according to sequence return in a file
     */
    private static List<Quiz_Type> dataParser(String data) {
        Log.d(TAG, "(In dataparser)DATA: " + data);
        List<Quiz_Type> quiz = null;
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject questions = new JSONObject(data);
                Log.d(TAG, "(In dataparser)questions: " + questions);
                JSONArray topicsarray = questions.getJSONArray(Constants.TYPES);
                Log.d(TAG, "(In dataparser)Topic's Array: " + topicsarray);
                quiz = ArrayParser(topicsarray);
                Log.d(TAG, "(In dataparser)Finished: " + quiz);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Returning from dataparser");
        return quiz;
    }

    /**
     * @param topicsarray It is Array where "types" is the Key
     * @return It is ArrayList where all types of Quizes are contain in this List
     */
    private static List<Quiz_Type> ArrayParser(JSONArray topicsarray) {
        Log.d(TAG, "(In ArrayParser)topic array: " + topicsarray);
        List<Quiz_Type> types = null;

        if (topicsarray != null) {
            types = new ArrayList<>();
            Log.d(TAG, "Inside Array Parser " + topicsarray.length());
            for (int i = 0; i < topicsarray.length(); i++) {
                JSONObject topicJson = topicsarray.optJSONObject(i);
                Log.d(TAG, "OBJECTS: " + topicsarray.optJSONObject(i));
                Quiz_Type type = TypeParser(topicJson);
                Log.d(TAG, "(In ArrayParser)type: " + type);
                types.add(type);

            }
            Log.d(TAG, "(In ArrayParser)type: " + types);
        }
        return types;
    }

    /**
     * @param topicJson It is a JSONobject which is type_name in this case SO, Quiz Type containing specific questions is sorted out
     * @return type object which of type Quiz_Type
     */
    private static Quiz_Type TypeParser(JSONObject topicJson) {
        Log.d(TAG, "(In TypeParser)topic Json: " + topicJson);
        Quiz_Type type = null;
        if (topicJson != null) {
            type = new Quiz_Type();
            Log.d(TAG, "Inside Topic Parser ");
            String typeName = topicJson.optString(Constants.TYPE_NAME);
            type.setTypeName(typeName);
            Log.d(TAG, "Topic Name: " + type.getTypeName());
            JSONArray questionsArray = topicJson.optJSONArray(Constants.QUESTION_SET);
            Log.d(TAG, "(In TypeParser)QUESTION ARRAY: " + questionsArray);
            List<Question_set> questions = QuestionsArrayParser(questionsArray);
            Log.d(TAG, "(In TypeParser) questions: " + questions);
            type.setQuestions(questions);
            Log.d(TAG, "(In TypeParser)Topic Questions: " + type);
        }

        return type;
    }

    /**
     * @param questionsArray It is array of "question_set" key passed to parse the whole set for a specific topic
     * @return Array of Question set with one object
     */
    private static List<Question_set> QuestionsArrayParser(JSONArray questionsArray) {
        List<Question_set> questions = null;
        if (questionsArray != null) {
            questions = new ArrayList<>();
            Log.d(TAG, "(Question array: ) " + questionsArray.length());
            for (int i = 0; i < questionsArray.length(); i++) {
                Log.d(TAG, "Inside QuestionArray Parser ");
                JSONObject questionJson = questionsArray.optJSONObject(i);
                Question_set question = Questionparser(questionJson);
                Log.d(TAG, "question " + question);
                questions.add(question);
            }
        }
        Log.d(TAG, "RETURNING QuestionArray Parser: " + questions);
        return questions;
    }

    /**
     *
     * @param questionJson contains single JSONObject(question_set) in an Array
     * @return object containing one Question_set in the array
     */
    private static Question_set Questionparser(JSONObject questionJson) {
        Question_set question = null;
        if (questionJson != null) {
            question = new Question_set();
            //Log.d(TAG,"Inside Question Parser ");
            question.setQuestion(questionJson.optString(Constants.QUESTION));
            question.setAnswer(questionJson.optString(Constants.ANSWER));
            JSONArray optionsetJson = questionJson.optJSONArray(Constants.OPTIONS_SET);
            List<String> optionset = OptionSetParser(optionsetJson);
            Log.d(TAG, "Inside Question parser value of optionset: " + optionset);
            question.setOptions_Set(optionset);
        }
        return question;
    }

    /**
     *
     * @param optionsetJson Array of options set
     * @return List containing options
     */
    private static List<String> OptionSetParser(JSONArray optionsetJson) {
        List<String> optionset = null;
        if (optionsetJson != null) {
            optionset = new ArrayList<>();

            for (int i = 0; i < optionsetJson.length(); i++) {
                Log.d(TAG, "Inside Optionset Parser ");
                String option = optionsetJson.optString(i);
                Log.d(TAG, "Inside Optionset Parser " + option);
                optionset.add(option);
            }
        }
        Log.d(TAG, "returning from option parser " + optionset);
        return optionset;
    }
}
