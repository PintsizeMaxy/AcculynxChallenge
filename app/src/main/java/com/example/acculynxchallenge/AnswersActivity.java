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
import static com.example.acculynxchallenge.PointsModel.total_answers;
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
     *
     * @param savedInstanceState instance used to create the AnswersActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answers_recycler); // Loads RecyclerView

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
     *
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
                        readAnswers(json_response); // Calls method to read in data, if successful
                    } else {
                        Log.i("isEmpty", "Got nothing back");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i("oFailure", "Unable to get JSON data");
            }
        });
    }

    /**
     * Parses through JSON find the value that belongs to the tag
     *
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
                total_answers++;
                mList.add(model);
            }

            mRecyclerView = (RecyclerView) findViewById(R.id.answerList);
            // Create adapter to be used to RecyclerView and display data
            mAdapter = new AnswersAdapter(AnswersActivity.this, mList);
            // Pairs the RecyclerView with the data to allow displaying
            mRecyclerView.setAdapter(mAdapter);
            // Implement Click Listener from Adapter to respond to clicks on cards
            mAdapter.setOnItemClickListener(AnswersActivity.this);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(AnswersActivity.this));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overrides from AnswersAdapter, adds points to the user score if correct answer is
     * selected. Deducts points if wrong answer is selected
     *
     * @param position location of the card that is clicked
     */
    @Override
    public void onItemClick(int position) {
        AnswerModel clicked_answer = mList.get(position); // details of answer selected
        int ans_id = clicked_answer.getAnswer_id(); // ID of answer selected
        int question_id = clicked_answer.getQuestion_id(); // ID of question being answered
        int clicked_score = clicked_answer.getScore(); // Score to add or subtract from points earned

        if (checkList(question_id)) {
            // If statement checks if answer click is the accepted answer
            if (clicked_answer.getIs_accepted()) {
                tries++; // Increase amount of clicks taken
                earned += clicked_score; // Add the points of the accepted answer to your points
                endAnswering(question_id, "correct");
            } // end if
            // Else runs if answer is not accepted
            else {
                if (tries == total_answers) {
                    earned += deductions; // Re-apply points initially subtracted from score
                    endAnswering(question_id, "uh oh"); // Feeds uh-oh parameters
                } else if (checkList(ans_id)) {
                    tries++;
                    // Adds answer to answered to prevent duplicate clicks
                    mAnswered.add(ans_id); // Add answer to list of answered to prevent multi-click
                    deductions += clicked_score; // Increases total amount of points lost
                    earned -= clicked_score; // Subtract score from total amount of points
                    // Feeds false into alertCreate to create appropriate AlertDialog
                    alertCreate("incorrect");
                } else {
                    makeToast("You selected this answer already");
                }
            }
        } else {
            makeToast("You answered this question already!");
        }
    }

    /**
     * Called if correct answer, or and uh oh is encountered
     * @param question_id question to be added to the list of answered questions
     * @param result determines if correct or uh oh
     */
    public void endAnswering(int question_id, String result) {
        alertCreate(result); // Feeds true into alertCreate to create appropriate AlertDialog
        mAnswered.add(question_id); // Adds question to answered to prevent duplicate clicks
        deductions = 0; // Resets deductions back to start
        earned = 0; // Resets earned back to start
        tries = 0; // Resets tries back to start
        total_answers = 0; // Resets total_answers back to 0
    }

    /**
     * Pops up a toast if answer was selected already or question was already answered
     *
     * @param message info to display corresponding to the question/answer selected
     */
    public void makeToast(String message) {
        Toast.makeText(AnswersActivity.this,
                message,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Checker to see if answer was already selected
     *
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
     *
     * @param correct string to hold if correct answer was selected or not
     */
    public void alertCreate(String correct) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AnswersActivity.this);
        builder.setCancelable(true);
        // If statement checks if the correct answer was found
        if (correct.equals("correct")) {
            points += earned; // Add earned points to user's point stockpile
            alertExit(builder, "Correct!", "It took you " + tries
                    + " attempt(s)!\nYou received: " + earned + " points\nNew score: " +
                    points); //
        } else if (correct.equals("incorrect")) {
            alertExit(builder, "Incorrect",
                    deductions + " points deducted from score");
        } else {
            alertExit(builder, "Uh Oh!",
                    "Looks like the question has an accepted answer, " +
                            "but none of them are the right answer!\n" + deductions +
                            " points given back. \n Score: " + points);
        }
    }

    /**
     * Method to respond to the clicks in the alert dialog
     *
     * @param aBuilder alert dialog present in the AnswersActivity
     */
    public void alertExit(AlertDialog.Builder aBuilder, String pTitle, String pMessage) {
        aBuilder.setTitle(pTitle); // Title of AlertDialog
        aBuilder.setMessage(pMessage); // Message displayed below title of AlertDialog
        aBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        aBuilder.show(); // Pop-up the AlertDialog
    }
}
