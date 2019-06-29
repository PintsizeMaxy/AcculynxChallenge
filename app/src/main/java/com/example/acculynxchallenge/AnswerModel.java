package com.example.acculynxchallenge;

/**
 * Holds the information of the answer's details
 */
public class AnswerModel {

    private int answer_id; // Holds the id of the answer
    private int question_id; // Holds the id of the question that the answer belongs to
    private Boolean is_accepted; // Holds if the answer is the one that is accepted
    private int score; // Score the answer received

    /**
     * Getter for answer_id
     * @return answer's id
     */
    public int getAnswer_id() {
        return answer_id;
    }

    /**
     * Getter for question_id
     * @return question's id
     */
    public int getQuestion_id() {
        return question_id;
    }

    /**
     * Getter for score
     * @return score of answer
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter for is_accepted
     * @return true or false depending if question is accepted
     */
    public Boolean getIs_accepted() {
        return is_accepted;
    }

    /**
     * Setter for question_id
     * @param question_id id of the question to be set to question_id
     */
    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    /**
     * Setter for answer_id
     * @param answer_id id of the answer to be set to answer_id
     */
    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    /**
     * Setter for is_accepted
     * @param is_accepted true or false to be saved into is_accepted
     */
    public void setIs_accepted(Boolean is_accepted) {
        this.is_accepted = is_accepted;
    }

    /**
     * Setter for score
     * @param score integer to be saved into score, score of the answer
     */
    public void setScore(int score) {
        this.score = score;
    }
}
