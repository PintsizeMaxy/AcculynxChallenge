package com.example.acculynxchallenge;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class QuestionsAdapter {

    class QuestionsHolder extends RecyclerView.ViewHolder {
        TextView questions;
        TextView num_of_answers;

        public QuestionsHolder(View view) {
            super(view);
            questions = (TextView) view.findViewById(R.id.questionDisplay);
            num_of_answers = (TextView) view.findViewById(R.id.numOfAnswers);
        }
    }
}
