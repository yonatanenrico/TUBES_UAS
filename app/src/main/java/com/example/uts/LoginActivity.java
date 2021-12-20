package com.example.uts;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uts.api.ApiClient;
import com.example.uts.api.ApiInterface;
import com.example.uts.api.UserResponse;
import com.example.uts.entity.Login;
import com.example.uts.entity.User;
import com.example.uts.SharedPreference.Preferences.UserPreferences;
import com.example.uts.SharedPreference.Room.DatabaseClient;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private MaterialButton btnClear, btnLogin, btnRegister;
    private UserPreferences userPreferences;
    private User user;
    private ApiInterface apiService;
    private LinearLayout layoutLoading;
    private String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.login);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        userPreferences = new UserPreferences(LoginActivity.this);

        user = new User();

        layoutLoading = findViewById(R.id.layout_loading);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnClear = findViewById(R.id.btnClear);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        /* Apps will check the login first from shared preferences */
        checkLogin();

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etEmail.setText("");
                etPassword.setText("");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm()){
                    login();
//                    attemptLogin(etUsername.getText().toString().trim(), etPassword.getText().toString().trim());
                }
            }
        });
    }

    private void login() {
        setLoading(true);
        Login login = new Login(etEmail.getText().toString(), etPassword.getText().toString());
        Call<UserResponse> call = apiService.login(login);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call,
                                   Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,
                            response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    access_token = response.body().getAccessToken();
                    user = response.body().getUser();

                    userPreferences.setUser(user.getId(), user.getEmail(), etPassword.getText().toString(),
                            user.getUsername(), user.getImage(), access_token);
                    checkLogin();
                } else {
                    try {
                        JSONObject jObjError = new
                                JSONObject(response.errorBody().string());
                        Toast.makeText(LoginActivity.this,
                                jObjError.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
                setLoading(false);
            }
        });
    }
//    private void attemptLogin(String username, String password){
//
//        class AttemptLogin extends AsyncTask<Void, Void, User> {
//            @Override
//            protected User doInBackground(Void... voids) {
//
//                User user = DatabaseClient.getInstance(LoginActivity.this)
//                        .getDatabase()
//                        .userDao()
//                        .attemptLogin(username,password);
//
//                return user;
//            }
//
//            @Override
//            protected void onPostExecute(User user) {
//                super.onPostExecute(user);
//                if(user == null){
//                    Toast.makeText(LoginActivity.this, "Username atau password salah", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(LoginActivity.this, "Berhasil login", Toast.LENGTH_SHORT).show();
//                    userPreferences.setUser(user.getId(), user.getPassword(), user.getUsername(),
//                            user.getNama_akun());
//                }
//                checkLogin();
//            }
//
//        }
//
//        AttemptLogin attemptLogin = new AttemptLogin();
//        attemptLogin.execute();
//    }

    private boolean validateForm(){
        /* Check username & password is empty or not */
        if(etEmail.getText().toString().trim().isEmpty() || etPassword.getText().toString().trim().isEmpty()){
            Toast.makeText(LoginActivity.this,"Username Atau Password Kosong",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void checkLogin(){
        if(userPreferences.checkLogin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
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