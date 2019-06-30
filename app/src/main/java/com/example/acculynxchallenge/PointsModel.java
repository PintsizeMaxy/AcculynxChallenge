package com.example.acculynxchallenge;

import android.app.Activity;

import java.util.ArrayList;

/**
 * This Model holds all the information related to amount of points earned and clicks during
 * selecting of accepted answer
 */
public class PointsModel extends Activity {

    protected static int points; // Total points earned through whole application
    protected static int deductions; // Points subtracted from earned from wrong guesses
    protected static int tries; // Amount of clicks it took to click the right answer
    protected static int earned; // Earned points from successfully answering
    protected static int total_answers;
    // Holds id's of questions already clicked
    protected static ArrayList<Integer> mAnswered = new ArrayList<>();
}
