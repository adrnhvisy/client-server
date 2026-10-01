package com.adrian.studentapp.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    // Sesuaikan "token" ini dengan key JSON dari response Laravel kamu
    @SerializedName("token")
    private String token;

    // Jika Laravel juga mengirim pesan sukses, kamu bisa tambahkan:
    // @SerializedName("message")
    // private String message;

    public String getToken() {
        return token;
    }
}
