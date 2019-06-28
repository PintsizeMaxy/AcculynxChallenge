package com.example.acculynxchallenge;

public class QuestionModel {
    private String title;
    private int num_of_answers;
    private int question_id;
    private Boolean answered;

    public Boolean getAnswered() {
        return answered;
    }

    public void setAnswered(Boolean answered) {
        this.answered = answered;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNum_of_answers() {
        return "Answers: " + num_of_answers;
    }

    public void setNum_of_answers(int num_of_answers) {
        this.num_of_answers = num_of_answers;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }
}
