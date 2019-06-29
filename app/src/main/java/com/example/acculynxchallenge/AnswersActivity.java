package com.example.acculynxchallenge;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.acculynxchallenge.PointsModel.deductions;
import static com.example.acculynxchallenge.PointsModel.mAnswered;
import static com.example.acculynxchallenge.PointsModel.points;
import static com.example.acculynxchallenge.PointsModel.tries;
import static com.example.acculynxchallenge.QuestionActivity.EXTRA_ID;
import static com.example.acculynxchallenge.QuestionActivity.EXTRA_QUESTION;

public class AnswersActivity extends AppCompatActivity
        implements AnswersAdapter.OnItemClickListener {

    ArrayList<AnswerModel> mList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private AnswersAdapter mAdapter;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answers_recycler);

        Intent intent = getIntent();
        String question = intent.getStringExtra(EXTRA_QUESTION);
        int question_id = intent.getIntExtra(EXTRA_ID, 0);
        mTitle = (TextView) findViewById(R.id.questionTitle);
        mTitle.setText(question);
        getRetrofit(question_id);

    }

    private void getRetrofit(int ans_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<String> call = api.getAllAnswers(ans_id);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("onResponse", response.body());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("isSuccessful()", response.body());
                        String json_response = response.body();
                        readAnswers(json_response);
                    } else {
                        Log.i("isEmpty", "Got nothing back");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void readAnswers(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                AnswerModel model = new AnswerModel();
                JSONObject jObj = array.getJSONObject(i);
                model.setAnswer_id(jObj.getInt("answer_id"));
                model.setIs_accepted(jObj.getBoolean("is_accepted"));
                model.setScore(jObj.getInt("score"));
                model.setQuestion_id(jObj.getInt("question_id"));
                mList.add(model);
            }

            mRecyclerView = (RecyclerView) findViewById(R.id.answerList);
            mAdapter = new AnswersAdapter(AnswersActivity.this, mList);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(AnswersActivity.this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(AnswersActivity.this));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        AnswerModel clicked_answer = mList.get(position);
        String ans_id = mList.get(position).getAnswer_id();

        if (clicked_answer.getIs_accepted()
                && checkList(ans_id)) {
            tries++;
            points += clicked_answer.getScore();
            points -= deductions;
            alertCreate(true);
            mAnswered.add(ans_id);
            deductions = 0;
        }
        if (!clicked_answer.getIs_accepted()) {
            if (checkList(ans_id)) {
                mAnswered.add(ans_id);
                tries++;
                deductions += clicked_answer.getScore();
                alertCreate(false);
            } else {
                Toast.makeText(AnswersActivity.this,
                        "You have selected this answer already",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean checkList(String pID){
        if(!mAnswered.contains(pID)){
            return true;
        } else{
            return false;
        }
    }

    public void alertCreate(Boolean correct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AnswersActivity.this);
        builder.setCancelable(true);
        if (correct) {
            builder.setTitle("Correct!");
            builder.setMessage("It took you " + tries
                    + " attempt(s)!\nYou received: " + points + " points");
            alertExit(builder);
        } else {
            builder.setTitle("Incorrect!");
            builder.setMessage(deductions + " points deducted from  points");
            alertExit(builder);
        }
    }

    public void alertExit(AlertDialog.Builder aBuilder) {
        aBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        aBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        aBuilder.show();
    }
}
