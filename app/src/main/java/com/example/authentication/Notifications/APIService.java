package com.example.authentication.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authoriztion:key="
    })

    @POST("fom/send")
    Call<Response> sendNotification(@Body Sender body);
}
