package com.example.uts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.uts.api.ApiClient;
import com.example.uts.api.ApiInterface;
import com.example.uts.api.UserResponse;
import com.example.uts.entity.User;
import com.example.uts.SharedPreference.Preferences.UserPreferences;
import com.example.uts.SharedPreference.Room.DatabaseClient;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etPassword, etEmail, etNamaAkun;
    private ImageView ivRegister;
    private MaterialButton btnClear, btnRegister, btnLogin;
    private UserPreferences userPreferences;
    private ApiInterface apiService;
    private LinearLayout layoutLoading;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(R.string.register);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        layoutLoading = findViewById(R.id.layout_loading);

        userPreferences = new UserPreferences(RegisterActivity.this);

        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etNamaAkun = findViewById(R.id.etNamaAkun);
        ivRegister = findViewById(R.id.ivRegister);

        btnClear = findViewById(R.id.btnClear);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearField();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    register();
//                    register(etPassword.getText().toString().trim(), etUsername.getText().toString().trim(),
//                            etNamaAkun.getText().toString().trim());
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private String bitmapToBase64(Bitmap bitmap) {
        if(bitmap == null)
            return null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    private void register() {
        setLoading(true);
        bitmap = ((BitmapDrawable) ivRegister.getDrawable()).getBitmap();

        User user = new User(
                etEmail.getText().toString(),
                etPassword.getText().toString(),
                etNamaAkun.getText().toString(),
                bitmapToBase64(bitmap));

        Call<UserResponse> call = apiService.register(user);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call,
                                   Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this,
                            response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    clearField();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    try {
                        JSONObject jObjError = new
                                JSONObject(response.errorBody().string());
                        Toast.makeText(RegisterActivity.this,
                                jObjError.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
                setLoading(false);
            }
        });
    }

//    private void register(String password, String username, String nama_akun){
//
//        class RegisterUser extends AsyncTask<Void, Void, Void> {
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//                User user = new User();
//                user.setPassword(password);
//                user.setUsername(username);
//                user.setNama_akun(nama_akun);
//
//                DatabaseClient.getInstance(RegisterActivity.this)
//                        .getDatabase()
//                        .userDao()
//                        .registerUser(user);
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void unused) {
//                super.onPostExecute(unused);
//                Toast.makeText(RegisterActivity.this, "Selamat! Anda Berhasil Mendaftar", Toast.LENGTH_SHORT).show();
//                clearField();
//                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//                finish();
//            }
//
//        }
//
//        RegisterUser registerUser = new RegisterUser();
//        registerUser.execute();
//    }

    private void clearField(){
        etPassword.setText("");
        etEmail.setText("");
        etNamaAkun.setText("");
    }

    private boolean validateForm(){
        /* Check data is empty or not */
        if(etPassword.getText().toString().trim().isEmpty() || etEmail.getText().toString().trim().isEmpty()
                || etNamaAkun.getText().toString().trim().isEmpty()){
            Toast.makeText(RegisterActivity.this,"Tolong isi field yang kosong",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.VISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.INVISIBLE);
        }
    }
}