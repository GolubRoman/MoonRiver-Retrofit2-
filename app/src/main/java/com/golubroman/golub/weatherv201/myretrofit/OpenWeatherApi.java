package com.golubroman.golub.weatherv201.myretrofit;

/**
 * Created by User on 20.02.2017.
 */

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApi{
        //http://api.openweathermap.org/data/2.5/forecast/daily";
        // ?id=cityName
        //&units=units
        //&cnt=16
       //&appid=20cfc892f26ead5608f881f1cadd2784
    //?q=kiev&units=metric&cnt=16&appid=28e909fe816297eafabdac9118bfa097
    @GET("/data/2.5/forecast/daily")
    Call<WeatherPOJO> getData(@Query("q") String cityName, @Query("units") String units, @Query("cnt") String cnt, @Query("appid") String appID);

}
