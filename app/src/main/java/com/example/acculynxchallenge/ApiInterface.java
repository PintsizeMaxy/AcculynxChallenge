package com.example.acculynxchallenge;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    String JSONURL = "https://api.stackexchange.com";

    @GET("/2.2/questions?order=desc&sort=creation&site=stackoverflow")
    Call<String> getJSONQuestions();
}
