package com.example.uts.api;

import com.example.uts.entity.Login;
import com.example.uts.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiInterface {
    @Headers({"Accept: application/json"})
    @POST("api/register")
    Call<UserResponse> register(@Body User user);

    @Headers({"Accept: application/json"})
    @POST("api/login")
    Call<UserResponse> login(@Body Login login);

    @Headers({"Accept: application/json"})
    @PUT("api/user")
    Call<UserResponse> editUser(@Header("Authorization") String token,
                                @Body User user);

    @Headers({"Accept: application/json"})
    @GET("api/logout")
    Call<UserResponse> logout(@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("api/menu")
    Call<MenuResponse> getAllMenu(@Header("Authorization") String token);
}
