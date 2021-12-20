package com.example.uts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uts.api.ApiClient;
import com.example.uts.api.ApiInterface;
import com.example.uts.api.UserResponse;
import com.example.uts.entity.User;
import com.example.uts.SharedPreference.Preferences.UserPreferences;
import com.example.uts.databinding.ActivityAccountBinding;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {
    public static final int LAUNCH_EDIT_ACTIVITY = 123;

    private ActivityAccountBinding binding;
    private User user;
    private UserPreferences userPreferences;
    private ApiInterface apiService;
    private LinearLayout layoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(R.string.akun);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        userPreferences = new UserPreferences(this);
        user = userPreferences.getUserLogin();

        layoutLoading = findViewById(R.id.layout_loading);

        binding.tvNamaAkun.setText(user.getUsername());
        binding.tvEmail.setText("Email : " + user.getEmail());
        binding.tvPassword.setText("Password : " + user.getPassword());
        Glide.with(AccountActivity.this)
                .load(user.getImage())
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.undraw_teacher_35j2)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivGambar);

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountActivity.this, EditActivity.class);
                startActivityForResult(i, LAUNCH_EDIT_ACTIVITY);
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
//                userPreferences.logout();
//                checkLogin();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_EDIT_ACTIVITY && resultCode == Activity.RESULT_OK)
        {
            user = userPreferences.getUserLogin();
            binding.tvNamaAkun.setText(user.getUsername());
            binding.tvEmail.setText("Email : " + user.getEmail());
            binding.tvPassword.setText("Password : " + user.getPassword());
            Glide.with(AccountActivity.this)
                    .load(user.getImage())
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.undraw_teacher_35j2)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.ivGambar);
        }
    }

    private void logout() {
        setLoading(true);
        Call<UserResponse> call = apiService.logout("Bearer "+ userPreferences.getAccessToken());

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call,
                                   Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AccountActivity.this, response.body().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    userPreferences.logout();
                    checkLogin();
                } else {
                    try {
                        JSONObject jObjError = new
                                JSONObject(response.errorBody().string());
                        Toast.makeText(AccountActivity.this,
                                jObjError.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(AccountActivity.this,
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                setLoading(false);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(AccountActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
                setLoading(false);
            }
        });
    }

    private void checkLogin(){
        if(!userPreferences.checkLogin()) {
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
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
            layoutLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AccountActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}