package com.example.acculynxchallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class QuestionActivity extends AppCompatActivity {

    ArrayList<QuestionModel> modelArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private QuestionsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);

        getRetrofit();
    }

    private void getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ApiInterface api = retrofit.create(ApiInterface.class);

        Call<String> call = api.getJSONQuestions();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("onResponse", response.body());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("isSuccessful()", response.body());
                        String json_response = response.body();
                        readQuestions(json_response);
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

    private void readQuestions(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray jsonArray = obj.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                QuestionModel model = new QuestionModel();
                JSONObject jObj = jsonArray.getJSONObject(i);
                if (jObj.optBoolean("is_answered")
                        && jObj.optInt("answer_count") > 1) {
                    model.setNum_of_answers(jObj.getInt("answer_count"));
                    model.setTitle(jObj.getString("title"));
                    model.setQuestion_id(jObj.getInt("question_id"));
                    model.setNum_of_answers(jObj.getInt("answer_count"));
                    modelArrayList.add(model);
                }
            }
            mRecyclerView = (RecyclerView) findViewById(R.id.questionList);
            mAdapter = new QuestionsAdapter(QuestionActivity.this, modelArrayList);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(QuestionActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
