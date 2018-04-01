package com.mickey.himan.brainvitaquiz;


import java.util.List;

/**
 * This class Contains the elements in Object in an Array of Specific Topic
 * Created by Himanshu Devi on 27/10/17.
 */

public class Question_set {

    private int id;
    private String question;
    private List<String> options_Set;
    private String answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions_Set() {
        return options_Set;
    }

    public void setOptions_Set(List<String> options_Set) {
        this.options_Set = options_Set;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question_Set{"+
                "question ='" + question + '\'' +
                ", options_Set =" + options_Set +
                ", answer ='" + answer + '\'' +
                '}';
    }
}
