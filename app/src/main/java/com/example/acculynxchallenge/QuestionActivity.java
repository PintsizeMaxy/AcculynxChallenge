package com.example.acculynxchallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Activity to read JSON of all the answers and feed them into QuestionsAdapter to be read onto
 * screen
 */
public class QuestionActivity extends AppCompatActivity
        implements QuestionsAdapter.OnItemClickListener{

    public static final String EXTRA_QUESTION = "question";
    public static final String EXTRA_ID = "questionID";

    ArrayList<QuestionModel> modelArrayList = new ArrayList<>();
    ArrayList<QuestionModel> filteredList;

    private RecyclerView mRecyclerView;
    private QuestionsAdapter mAdapter;

    /**
     * Called when loading up the AnswersActivity layout. Initializes extra intents
     * @param savedInstanceState instance used to create the AnswersActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_recycler);

        getRetrofit();
        createSearch();
    }

    private void createSearch(){
        EditText editText = findViewById(R.id.edittext);
        editText.setSelected(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String text){
        filteredList = new ArrayList<>();

        for (QuestionModel item : modelArrayList) {
            // if element satisfied if element contains substring
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                // Add Model to filtered list
                filteredList.add(item);
            }
        }

        mAdapter.filterList(filteredList);

    }

    /**
     * Creates Retrofit object to trace through StackExchange API JSON, calls method to read
     * Calls method to read the parsed JSON and create model objects for each
     *
     */
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
                Log.i(getString(R.string.respond), response.body());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i(getString(R.string.succeed), response.body());
                        String json_response = response.body();
                        readQuestions(json_response);
                    } else {
                        Log.i(getString(R.string.empty), getString(R.string.no_return));
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(getString(R.string.fail), getString(R.string.fail_data));
            }
        });
    }

    /**
     * Parses through JSON find the value that belongs to the tag
     *
     * @param response response received from using API
     */
    private void readQuestions(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray jsonArray = obj.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                QuestionModel model = new QuestionModel();
                JSONObject jObj = jsonArray.getJSONObject(i);
                if (jObj.has("accepted_answer_id")
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
            filteredList = modelArrayList;
            mAdapter.setOnItemClickListener(QuestionActivity.this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(QuestionActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overrides from QuestionsAdapter, determines which question card is clicked and loads
     * all answers that belong to the question
     *
     * @param position location of the card that is clicked
     */
    @Override
    public void onItemClick(int position){
        Intent question_detail = new Intent(this, AnswersActivity.class);
        QuestionModel clicked_item = filteredList.get(position);

        question_detail.putExtra(EXTRA_QUESTION, clicked_item.getTitle());
        question_detail.putExtra(EXTRA_ID, clicked_item.getQuestion_id());
        startActivity(question_detail);
    }

}
