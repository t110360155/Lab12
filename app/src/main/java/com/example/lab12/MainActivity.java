package com.example.lab12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.media.tv.SectionResponse;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    class Data {
        Result result;
        class Result{
            Results[] results;
            class Results{
                String Station;
                String Destination;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_search).setOnClickListener(v -> {
            String URL = "https://tools-api.italkutalk.com/java/lab12";
            Request request = new Request.Builder().url(URL).build();
            OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.code() == 200) {
                        if(response.body() == null) return;
                        Data data = new Gson().fromJson(response.body().string(), Data.class);
                        final String[] items = new String[data.result.results.length];

                        for(int i = 0; i < items.length; i++) {
                            items[i] = "\n The train is about to enter: " + data.result.results[i].Station +
                                    "\n Train destination: " + data.result.results[i].Destination;
                        }
                        runOnUiThread(() ->{
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Taipei MRT arrival station")
                                    .setItems(items, null)
                                    .show();
                        });
                    } else if(!response.isSuccessful())
                        Log.e("Server errors", response.code() + " "+ response.message());
                    else
                        Log.e("Other erros", response.code() + " " + response.message());

                }
            });
        });
    }
}

