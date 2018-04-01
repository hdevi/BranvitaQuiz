package com.mickey.himan.brainvitaquiz;

import java.util.ArrayList;
import java.util.List;

/**
 * It handles the Quiz types using the current instance of it.
 * Created by Himanshu Devi on 3/11/17.
 */

public class QuizHandler {

    private static final QuizHandler ourInstance = new QuizHandler();
    public static final int ERROR_INDEX = -99;
    private List<Quiz_Type> types;
    private int selectedTypeIndex = ERROR_INDEX;

    public static QuizHandler getInstance() {
        return ourInstance;
    }

    private QuizHandler() {
    }

    public List<Quiz_Type> getTypes() {
        return types;
    }

    public void setTypes(List<Quiz_Type> types) {
        this.types = types;
    }

    public int getSelectedTypeIndex() {
        return selectedTypeIndex;
    }

    public void setSelectedTypeIndex(int selectedTypeIndex) {
        this.selectedTypeIndex = selectedTypeIndex;
    }
}
