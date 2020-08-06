package com.pmh.iotblog.Service;

import com.pmh.iotblog.Models.Data;
import com.pmh.iotblog.Models.TempIOT;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataService {

    @GET("weather")
    Call<Data> GetDataWeather(@Query("id") int id, @Query("appid") String appid);

    @GET("index.php")
    Call<List<TempIOT>> GetDataIOT();

    @GET("index.php")
    Call<List<TempIOT>> GetUpdateHC(@Query("hc") int hc);

    @GET("index.php")
    Call<List<TempIOT>> GetUpdateLED(@Query("led") int led);
}
