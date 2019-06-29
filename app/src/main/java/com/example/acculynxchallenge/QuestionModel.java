package com.example.acculynxchallenge;

/**
 * Model to hold all details about the question
 */
public class QuestionModel {
    private String title; // Title of the question
    private int num_of_answers; // Number of answers to a question
    private int question_id; // ID of the question

    /**
     * Getter to retrieve question's title
     * @return String question title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for question_id
     * @return integer id of the question
     */
    public int getQuestion_id() {
        return question_id;
    }

    /**
     * Getter for num_of_answers
     * @return String number of answers to the question
     */
    public String getNum_of_answers() {
        return "Answers: " + num_of_answers;
    }

    /**
     * Setter for title
     * @param title question's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Setter for num_of_answers
     * @param num_of_answers number of answers to questions
     */
    public void setNum_of_answers(int num_of_answers) {
        this.num_of_answers = num_of_answers;
    }

    /**
     * Setter for question_id
     * @param question_id id of the questions
     */
    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }
}
