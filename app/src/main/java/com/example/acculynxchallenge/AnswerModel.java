package com.example.acculynxchallenge;

public class AnswerModel {

    private int answer_id;
    private Boolean is_accepted;
    private int score;

    public String getAnswer_id() {
        return "Answer ID: " + answer_id;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    public Boolean getIs_accepted() {
        return is_accepted;
    }

    public void setIs_accepted(Boolean is_accepted) {
        this.is_accepted = is_accepted;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
