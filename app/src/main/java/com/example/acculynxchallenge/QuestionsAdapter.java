package com.example.acculynxchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    ArrayList<QuestionModel> mData = new ArrayList<>();
    QuestionModel mCurrent;

    public QuestionsAdapter(Context context, ArrayList mData) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final QuestionsHolder qHolder = (QuestionsHolder) holder;
        mCurrent = mData.get(position);
        qHolder.questions.setText(mCurrent.getTitle());
        qHolder.num_of_answers.setText(mCurrent.getNum_of_answers());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

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
