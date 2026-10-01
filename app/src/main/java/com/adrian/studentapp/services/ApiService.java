package com.adrian.studentapp.services;

import com.adrian.studentapp.model.LoginRequest;
import com.adrian.studentapp.model.LoginResponse;
import com.adrian.studentapp.model.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

//ini adalah route | kalau di php route nya web.php
public interface ApiService {

    @GET("students")
    Call<List<Student>> getAllStudents();

    @GET("students/{id}")
    Call<Student> getStudent(@Path("id") int id);

    @POST("students")
    Call<Student> addStudent(@Body Student student);

    @PUT("students/{id}")
    Call<Student> updateStudent(@Path("id") int id, @Body Student student);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @Headers("Accept: application/json")
    @DELETE("students/{id}")
    Call<Void> deleteStudent(@Header("Authorization") String token, @Path("id") int id);

    @Headers("Accept: application/json")
    @POST("logout")
    Call<Void> logout(@Header("Authorization") String token);

}
