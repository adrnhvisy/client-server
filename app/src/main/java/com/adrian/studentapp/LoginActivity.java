package com.adrian.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.adrian.studentapp.model.LoginRequest;
import com.adrian.studentapp.model.LoginResponse;
import com.adrian.studentapp.services.ApiClient;
import com.adrian.studentapp.services.ApiService;
import com.adrian.studentapp.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // 1. Deklarasi variabel komponen UI
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnLogin;

    // Deklarasi variabel untuk Network & Shared Preferences
    private ApiService apiService;
    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Memuat layout activity_login.xml

        // 2. Inisialisasi: Menghubungkan variabel dengan ID di layout
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Inisialisasi API Service dan SharedPrefManager
        apiService = ApiClient.getService();
        prefManager = new SharedPrefManager(this);

        // Pengecekan sesi: Jika sudah login, langsung ke MainActivity
        if (prefManager.getToken() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return; // Hentikan eksekusi kode di bawahnya
        }

        // 3. Menambahkan Listener pada Tombol Login
        btnLogin.setOnClickListener(v -> {
            // Ambil teks dari input dan hapus spasi berlebih
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validasi input: pastikan tidak ada yang kosong
            if (email.isEmpty()) {
                etEmail.setError("Email tidak boleh kosong");
                etEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Password tidak boleh kosong");
                etPassword.requestFocus();
                return;
            }

            // Jika validasi lulus, jalankan fungsi login
            performLogin(email, password);
        });
    }

    // 4. Fungsi untuk mengeksekusi request Login ke Laravel (Seperti di Fase 3)
    private void performLogin(String email, String password) {
        // Tampilkan feedback ke pengguna
        Toast.makeText(this, "Sedang masuk...", Toast.LENGTH_SHORT).show();
        btnLogin.setEnabled(false); // Nonaktifkan tombol saat proses

        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnLogin.setEnabled(true); // Aktifkan tombol kembali

                if (response.isSuccessful() && response.body() != null) {
                    // Simpan token ke SharedPreferences
                    prefManager.saveToken(response.body().getToken());

                    Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                    // Pindah ke MainActivity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Gagal. Periksa kembali email dan password.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Koneksi Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
