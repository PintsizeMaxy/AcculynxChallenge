package com.example.acculynxchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
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

public class AnswersActivity extends AppCompatActivity {

    ArrayList<AnswerModel> mList = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);

        Intent intent = getIntent();

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
                mList.add(model);
            }

            mRecyclerView = (RecyclerView) findViewById(R.id.questionList);

        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
