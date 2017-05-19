package com.packtpub.apps.rxjava_essentials.chapter8.api.stackexchange;

import com.packtpub.apps.rxjava_essentials.chapter8.api.stackexchange.models.UsersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StackExchangeService {

    @GET("/2.2/users?order=desc&pagesize=10&sort=reputation&site=stackoverflow")
    Call<UsersResponse> getTenMostPopularSOusers();

    @GET("/2.2/users?order=desc&sort=reputation&site=stackoverflow")
    Call<UsersResponse> getMostPopularSOusers(@Query("pagesize") int howmany);

}


