package com.pmh.iotblog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.pmh.iotblog.Models.Clouds;
import com.pmh.iotblog.Models.Data;
import com.pmh.iotblog.Models.Main;
import com.pmh.iotblog.Models.TempIOT;
import com.pmh.iotblog.Models.Wind;
import com.pmh.iotblog.Service.APIService;
import com.pmh.iotblog.Service.DataService;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmartHomeActivity extends AppCompatActivity {

    MaterialToolbar toolbarSmart;
    TextView temp1, temp2, hum1, hum2, index_feels_like, index_wind, index_clouds;
    SwitchCompat switchCompat_hc, switchCompat_led;
    int id = 1583992;
    String AppKey = "9c50f7194f38a84e658ba6194598dd27";
    Main mainWeather;
    Data dataWeather;
    Wind wind;
    Clouds clouds;
    TempIOT tempIOTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_home);
        setContentView(R.layout.activity_smart_home);
        anhxa();
        GetDataOpenWeather(id, AppKey);
        GetDataTempInRoom();
        switchCompat_hc.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                DataService dataService = APIService.getServiceIOT();
                Call<List<TempIOT>> listCall = dataService.GetUpdateHC(1);
                listCall.enqueue(new Callback<List<TempIOT>>() {
                    @Override
                    public void onResponse(Call<List<TempIOT>> call, Response<List<TempIOT>> response) {
                        Toast.makeText(SmartHomeActivity.this, "Chế độ tự động ON", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<List<TempIOT>> call, Throwable t) {

                    }
                });
            } else {
                DataService dataService = APIService.getServiceIOT();
                Call<List<TempIOT>> listCall = dataService.GetUpdateHC(0);
                listCall.enqueue(new Callback<List<TempIOT>>() {
                    @Override
                    public void onResponse(Call<List<TempIOT>> call, Response<List<TempIOT>> response) {
                        Toast.makeText(SmartHomeActivity.this, "Chế độ tự động OFF", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<List<TempIOT>> call, Throwable t) {

                    }
                });
            }

        });
        switchCompat_led.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                DataService dataService = APIService.getServiceIOT();
                Call<List<TempIOT>> listCall = dataService.GetUpdateLED(1);
                listCall.enqueue(new Callback<List<TempIOT>>() {
                    @Override
                    public void onResponse(Call<List<TempIOT>> call, Response<List<TempIOT>> response) {
                        Toast.makeText(SmartHomeActivity.this, "LED 1 ON", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<List<TempIOT>> call, Throwable t) {

                    }
                });
            } else {
                DataService dataService = APIService.getServiceIOT();
                Call<List<TempIOT>> listCall = dataService.GetUpdateLED(0);
                listCall.enqueue(new Callback<List<TempIOT>>() {
                    @Override
                    public void onResponse(Call<List<TempIOT>> call, Response<List<TempIOT>> response) {
                        Toast.makeText(SmartHomeActivity.this, "LED 1 OFF", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<List<TempIOT>> call, Throwable t) {

                    }
                });
            }
        });

    }

    private void GetDataTempInRoom() {
        DataService dataService = APIService.getServiceIOT();
        Call<List<TempIOT>> listCall = dataService.GetDataIOT();
        listCall.enqueue(new Callback<List<TempIOT>>() {
            @Override
            public void onResponse(Call<List<TempIOT>> call, Response<List<TempIOT>> response) {
                tempIOTS = response.body().get(0);
                temp1.setText(tempIOTS.getTemp());
                hum1.setText(tempIOTS.getHum());
                if ((Integer.parseInt(tempIOTS.getHcSr501().toString())) == 1) {
                    switchCompat_hc.setChecked(true);
                } else {
                    switchCompat_hc.setChecked(false);
                }
                if ((Integer.parseInt(tempIOTS.getLed().toString())) == 1) {
                    switchCompat_led.setChecked(true);
                } else {
                    switchCompat_led.setChecked(false);
                }
            }

            @Override
            public void onFailure(Call<List<TempIOT>> call, Throwable t) {

            }
        });
    }

    private void GetDataOpenWeather(int id, String AppId) {
        DataService dataService = APIService.getService();
        Call<Data> dataCall = dataService.GetDataWeather(id, AppId);
        dataCall.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                dataWeather = response.body();
                wind = dataWeather.getWind();
                clouds = dataWeather.getClouds();
                mainWeather = dataWeather.getMain();

                DecimalFormat formatter = new DecimalFormat("#0.00");

                double temp = mainWeather.getTemp() - 273.15;
                temp2.setText(formatter.format(temp));

                int hum = mainWeather.getHumidity();
                hum2.setText(hum + "");

                double feels = mainWeather.getFeelsLike() - 273.15;
                index_feels_like.setText(formatter.format(feels));

                index_wind.setText(String.valueOf(wind.getSpeed()));
                index_clouds.setText(String.valueOf(clouds.getAll()));
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }

    private void anhxa() {
        temp1 = findViewById(R.id.temp1);
        temp2 = findViewById(R.id.temp2);
        hum1 = findViewById(R.id.hum1);
        hum2 = findViewById(R.id.hum2);
        switchCompat_hc = findViewById(R.id.switch_hc_sr501);
        switchCompat_led = findViewById(R.id.switch_led);
        index_feels_like = findViewById(R.id.index_feels_like);
        index_wind = findViewById(R.id.index_wind);
        index_clouds = findViewById(R.id.index_clouds);
        toolbarSmart = findViewById(R.id.toolbarSmart);
        setSupportActionBar(toolbarSmart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarSmart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}