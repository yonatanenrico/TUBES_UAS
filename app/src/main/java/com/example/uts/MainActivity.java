package com.example.uts;

import static com.example.uts.MyApplication.CHANNEL_1_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.uts.SharedPreference.Preferences.UserPreferences;
import com.example.uts.api.ApiClient;
import com.example.uts.api.ApiInterface;
import com.example.uts.api.MenuResponse;
import com.example.uts.rv.DaftarMenu;
import com.example.uts.entity.Menu;
import com.example.uts.rv.RVMenuAdapter;
import com.example.uts.databinding.ActivityMainBinding;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NotificationManagerCompat notificationManager;
    private UserPreferences userPreferences;
//    private RVMenuAdapter adapter;
    private ApiInterface apiService;
    private LinearLayout layoutLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(R.string.menu);

        apiService = ApiClient.getClient().create(ApiInterface.class);
//        notificationManager = NotificationManagerCompat.from(this);
//
//        notification();

        layoutLoading = findViewById(R.id.layout_loading);

        userPreferences = new UserPreferences(this);

        RVMenuAdapter adapter = new RVMenuAdapter(new DaftarMenu().daftarMenu);
        binding.rvMenu.setAdapter(adapter);

//        getAllMenu();

        binding.btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
                finish();
            }
        });

        binding.btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AccountActivity.class));
                finish();
            }
        });
    }

//    private ArrayList<Menu> getListMenu() {
//        ArrayList<Menu> listMenu = new DaftarMenu().daftarMenu;
//        return listMenu;
//    }

    public void notification(){
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity( this,
                0,activityIntent,0);

        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.img);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Horeg Resto")
                .setContentText("Menu Favorit Minggu ini :")
                .setLargeIcon(picture)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Menu Favorit Minggu ini!\n" +
                                "Papeda + Ikan Buah Kuning\n" +
                                "Harga : Rp. 60.000,-\n\n" +
                                "Tunggu Apa Lagi, Ayo Pesan Sekarang!")
                        .setBigContentTitle("Horeg Resto"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.GREEN)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();

        notificationManager.notify(1, notification);
    }

//    private void getAllMenu() {
//        setLoading(true);
//        Call<MenuResponse> call = apiService.getAllMenu("Bearer " + userPreferences.getAccessToken());
//
//        call.enqueue(new Callback<MenuResponse>() {
//            @Override
//            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
//                if (response.isSuccessful()) {
//                    adapter.setListMenu(response.body().getListMenu());
//                    String size = String.valueOf(adapter.getItemCount());
//                    Toast.makeText(MainActivity.this, size + " Menu",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    try {
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                        Toast.makeText(MainActivity.this, jObjError.getString("message"),
//                                Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//                setLoading(false);
//            }
//
//            @Override
//            public void onFailure(Call<MenuResponse> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Network error",
//                        Toast.LENGTH_SHORT).show();
//                setLoading(false);
//            }
//        });
//    }

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
}