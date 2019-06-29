package com.example.acculynxchallenge;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    String JSONURL = "https://api.stackexchange.com";

    @GET("/2.2/questions?pagesize=100&order=desc&sort=week&site=stackoverflow")
    Call<String> getJSONQuestions();

    @GET("/2.2/questions/{id}/answers?order=desc&sort=activity&site=stackoverflow")
    Call<String> getAllAnswers(@Path("id") int questionID);

    @GET("/2.2/search?order=desc&sort=activity&intitle={id}&site=stackoverflow")
    Call<String> getSearchResults(@Path("id") int searchQuery);
}
