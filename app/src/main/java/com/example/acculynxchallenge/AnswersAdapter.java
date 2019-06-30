package com.example.acculynxchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adapter used to display all JSON information related to Answers onto the screen
 */
public class AnswersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater; // Loads in the layout used for AnswersActivity
    ArrayList<AnswerModel> mData = new ArrayList<>(); // Holds the answers belonging to the question
    AnswerModel mModel; // Current model to parse through

    private OnItemClickListener mListener; // ClickListener for adapter and for activity to use

    /**
     * Interface used as a personal clickListener to be implemented by AnswersActivity
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
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * Constructor to create the AnswersAdapter view
     * @param context activity to be displayed in
     * @param mData ArrayList of answer details
     */
    public AnswersAdapter(Context context, ArrayList mData){
        mInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    /**
     * Creates ViewHolder for the RecyclerView in AnswersActivity
     * @param parent layout to attach view holder to
     * @param viewType determines if layout will be added or inflated
     * @return made holder from custom AnswerHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.answer_adapter, parent, false);
        AnswerHolder holder = new AnswerHolder(view);
        return holder;
    }

    /**
     * Overrides onBindViewHolder from RecyclerView to generate and show items
     * @param viewHolder RecyclerView to display items in
     * @param position position to grab the answer from in the ArrayList
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final AnswerHolder holder = (AnswerHolder) viewHolder;
        mModel = mData.get(position);
        String text = "Answer ID: " + mModel.getAnswer_id();
        holder.answer_id.setText(text);
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
     * Custom ViewHolder to hold onClickListener for items in recycler view as well as display
     * text in appropriate spot in the CardView
     */
    class AnswerHolder extends RecyclerView.ViewHolder {
        TextView answer_id; // TextView to display the id of the answer

        /**
         * Creates the cards and displays them
         * @param view where to show the items
         */
        public AnswerHolder(View view){
            super(view);
            answer_id = (TextView) view.findViewById(R.id.answerDisplay);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
