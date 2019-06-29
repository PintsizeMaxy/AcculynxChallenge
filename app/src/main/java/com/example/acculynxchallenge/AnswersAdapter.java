package com.example.acculynxchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnswersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    ArrayList<AnswerModel> mData = new ArrayList<>();
    AnswerModel mModel;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AnswersAdapter(Context context, ArrayList mData){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.answer_adapter, parent, false);
        AnswerHolder holder = new AnswerHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final AnswerHolder holder = (AnswerHolder) viewHolder;
        mModel = mData.get(position);
        holder.answer_id.setText(mModel.getAnswer_id());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class AnswerHolder extends RecyclerView.ViewHolder {
        TextView answer_id;

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
