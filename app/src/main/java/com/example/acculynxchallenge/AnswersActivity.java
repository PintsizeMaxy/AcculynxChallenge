package com.example.acculynxchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.example.acculynxchallenge.QuestionActivity.EXTRA_ID;
import static com.example.acculynxchallenge.QuestionActivity.EXTRA_QUESTION;

public class AnswersActivity extends AppCompatActivity
        implements AnswersAdapter.OnItemClickListener{

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

    private void readAnswers(String response){
        try{
            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("items");
            for (int i = 0; i < array.length(); i++){
                AnswerModel model = new AnswerModel();
                JSONObject jObj = array.getJSONObject(i);
                model.setAnswer_id(jObj.getInt("answer_id"));
                model.setIs_accepted(jObj.getBoolean("is_accepted"));
                model.setScore(jObj.getInt("score"));
                if(!model.getAnswered()) {
                    model.setAnswered(false);
                }
                mList.add(model);
            }

            mRecyclerView = (RecyclerView) findViewById(R.id.answerList);
            mAdapter = new AnswersAdapter(AnswersActivity.this, mList);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(AnswersActivity.this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(AnswersActivity.this));

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        AnswerModel clicked_answer = mList.get(position);
        if(clicked_answer.getIs_accepted()
                && !PointsModel.mAnswered.contains(mList.get(position).getAnswer_id())) {
            PointsModel.points++;
            mList.get(position).setAnswered(true);
            PointsModel.mAnswered.add(mList.get(position).getAnswer_id());
            Toast.makeText(AnswersActivity.this, Integer.toString(PointsModel.points), Toast.LENGTH_SHORT).show();
        }
    }
}
