package com.example.acculynxchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adapter used to display all JSON information related to Questions onto the screen
 */
public class QuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater inflater; // Loads in the layout used for QuestionsActivity
    ArrayList<QuestionModel> mData; // Holds the answers belonging to the question
    QuestionModel mCurrent; // Current model to parse through
    private OnItemClickListener mListener; // ClickListener for adapter and for activity to use

    /**
     * Interface used as a personal clickListener to be implemented QuestionsActivity
     */
    public interface OnItemClickListener{
        /**
         * Determines what answer was selected
         * @param position position of card selected
         */
        void onItemClick(int position);
    }

    /**
     * Sets the onClickListener to provide responses for clicks in Activity
     * @param listener Activity to be linked with listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    /**
     * Constructor to create the QuestionsAdapter view
     * @param context activity to be displayed in
     * @param mData ArrayList of answer details
     */
    public QuestionsAdapter(Context context, ArrayList mData) {
        inflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    /**
     * Creates ViewHolder for the RecyclerView in AnswersActivity
     * @param viewGroup layout to attach view holder to
     * @param viewType determines if layout will be added or inflated
     * @return made holder from custom AnswerHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.questions_adapter, viewGroup, false);
        QuestionsHolder holder = new QuestionsHolder(view);
        return holder;
    }

    /**
     * Overrides onBindViewHolder from RecyclerView to generate and show items
     * @param viewHolder RecyclerView to display items in
     * @param position position to grab the answer from in the ArrayList
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final QuestionsHolder qHolder = (QuestionsHolder) viewHolder;
        mCurrent = mData.get(position);
        String id = "Question ID: " + mCurrent.getQuestion_id();
        qHolder.questions.setText(mCurrent.getTitle()); // Sets the title TextView
        qHolder.num_of_answers.setText(mCurrent.getNum_of_answers()); // Sets number of answers
        qHolder.question_id.setText(id);
    }

    /**
     * Gets size of ArrayList
     * @return size of ArrayList
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Displays new information
     * @param filteredList questions to be displayed to user
     */
    public void filterList(ArrayList<QuestionModel> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    /**
     * Custom ViewHolder to hold onClickListener for items in recycler view as well as display
     * text in appropriate spot in the CardView
     */
    class QuestionsHolder extends RecyclerView.ViewHolder {
        TextView questions; // Question title to display
        TextView num_of_answers; // Number of answers to display
        TextView question_id; // Question id to display in TextView
        /**
         * Creates the cards and displays them
         * @param view where to show the items
         */
        public QuestionsHolder(View view) {
            super(view);
            questions = (TextView) view.findViewById(R.id.questionDisplay);
            num_of_answers = (TextView) view.findViewById(R.id.numOfAnswers);
            question_id = (TextView) view.findViewById(R.id.questionID);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position); // Reacts if item is clicked
                        }
                    }
                }
            });
        }
    }
}
