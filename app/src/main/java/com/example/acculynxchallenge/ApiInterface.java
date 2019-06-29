package com.example.acculynxchallenge;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    String JSONURL = "https://api.stackexchange.com";

    @GET("/2.2/questions?pagesize=100&order=desc&sort=week&site=stackoverflow")
    Call<String> getJSONQuestions();

    @GET("/2.2/questions/{id}/answers?order=desc&sort=activity&site=stackoverflow")
    Call<String> getAllAnswers(@Path("id") int questionID);
}
