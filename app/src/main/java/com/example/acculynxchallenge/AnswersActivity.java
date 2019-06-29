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
import static com.example.acculynxchallenge.PointsModel.earned;
import static com.example.acculynxchallenge.PointsModel.mAnswered;
import static com.example.acculynxchallenge.PointsModel.points;
import static com.example.acculynxchallenge.PointsModel.tries;
import static com.example.acculynxchallenge.QuestionActivity.EXTRA_ID;
import static com.example.acculynxchallenge.QuestionActivity.EXTRA_QUESTION;

/**
 * Activity to read JSON of all the answers and feed them into AnswersAdapter to be read onto
 * screen
 */
public class AnswersActivity extends AppCompatActivity
        implements AnswersAdapter.OnItemClickListener {

    ArrayList<AnswerModel> mList = new ArrayList<>(); // Holds all different answers
    private RecyclerView mRecyclerView; // RecyclerView used to allow for scrolling through questions
    private AnswersAdapter mAdapter; // Adapter used to display answer and details
    private TextView mTitle; // Displays the titles of the question

    /**
     * Called when loading up the AnswersActivity layout. Initializes extra intents
     * @param savedInstanceState instance used to create the AnswersActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answers_recycler);

        Intent intent = getIntent();
        String question = intent.getStringExtra(EXTRA_QUESTION); // Retrieves title of question
        int question_id = intent.getIntExtra(EXTRA_ID, 0); // Retrieves id of question
        mTitle = (TextView) findViewById(R.id.questionTitle); // Load up the title TextView
        mTitle.setText(question); // Sets the title
        getRetrofit(question_id); // Call to Retrofit method

    }

    /**
     * Creates Retrofit object to trace through StackExchange API JSON, calls method to read
     * Calls method to read the parsed JSON and create model objects for each
     * @param quest_id question id whose answers will be parsed
     */
    private void getRetrofit(int quest_id) {
        // Retrofit object to build API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        // Creates interface link between ApiInterface and Retrofit
        ApiInterface api = retrofit.create(ApiInterface.class);

        // Uses appropriate interface @GET to find answers
        Call<String> call = api.getAllAnswers(quest_id);

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

    /**
     * Parses through JSON find the value that belongs to the tag
     * @param response response received from using API
     */
    private void readAnswers(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONArray array = obj.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                AnswerModel model = new AnswerModel();
                JSONObject jObj = array.getJSONObject(i);
                // Finds tags in jObj and sets it to appropriate variable in model
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

    /**
     * Overrides from AnswersAdapter, adds points to the user score if correct answer is
     * selected. Deducts points if wrong answer is selected
     * @param position location of the card that is clicked
     */
    @Override
    public void onItemClick(int position) {
        AnswerModel clicked_answer = mList.get(position);
        int ans_id = mList.get(position).getAnswer_id();
        int question_id = mList.get(position).getQuestion_id();
        int clicked_score = clicked_answer.getScore();

        if (checkList(question_id)) {
            // If statement checks if answer click is the accepted answer
            if (clicked_answer.getIs_accepted()) {
                tries++;
                earned += clicked_score;
                // Feeds true into alertCreate to create appropriate AlertDialog
                alertCreate(true);
                // Adds question to answered to prevent duplicate clicks
                mAnswered.add(question_id);
                deductions = 0;
                tries = 0;
            } // end if
            // Else runs if answer is not accepted
            else {
                if (checkList(ans_id)) {
                    // Adds answer to answered to prevent duplicate clicks
                    mAnswered.add(ans_id);
                    tries++;
                    deductions += clicked_score;
                    earned -= deductions;
                    // Feeds false into alertCreate to create appropriate AlertDialog
                    alertCreate(false);
                } else {
                    makeToast("You have selected this answer already");
                }
            }
        } else {
            makeToast("You have answered this question already!");
        }
    }

    /**
     * Pops up a toast if answer was select already or questions was already answered
     * @param message info to display corresponding to the question/answer selected
     */
    public void makeToast(String message) {
        Toast.makeText(AnswersActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Checker to see if answer was already selected
     * @param pID id of the answer
     * @return true or false if answer was selected already or not
     */
    public boolean checkList(int pID) {
        if (!mAnswered.contains(pID)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Creates the alert after clicking on one of the answers
     * @param correct boolean to hold if correct answer was selected or not
     */
    public void alertCreate(Boolean correct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AnswersActivity.this);
        builder.setCancelable(true);
        if (correct) {
            points += earned;
            builder.setTitle("Correct!");
            builder.setMessage("It took you " + tries
                    + " attempt(s)!\nYou received: " + earned + " points\nNew score: " +
                    points);
            alertExit(builder);
        } else {
            builder.setTitle("Incorrect!");
            builder.setMessage(deductions + " points deducted from  points\n");
            alertExit(builder);
        }
    }

    /**
     * Method to respond to the clicks in the alert dialog
     * @param aBuilder alert dialog present in the AnswersActivity
     */
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
