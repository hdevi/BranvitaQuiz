package com.mickey.himan.brainvitaquiz;


import java.util.List;

/**
 * Created by Himanshu Devi on 3/11/17.
 */

public class Quiz_Type {

    private String typeName;
    private List<Question_set> questions;
    private List<Question_set> options;
    private List<Question_set> answer;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<Question_set> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question_set> questions) {
        this.questions = questions;
    }

    public List<Question_set> getOptions() {
        return options;
    }

    public List<Question_set> getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "Quiz_Type{" +
                "typeName='" + typeName + '\'' +
                ", questions=" + questions +
                ", options=" + options +
                ", answer=" + answer +
                '}';
    }
}
