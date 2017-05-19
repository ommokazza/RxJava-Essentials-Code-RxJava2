package com.packtpub.apps.rxjava_essentials.chapter8.api.openweathermap;

import com.packtpub.apps.rxjava_essentials.chapter8.api.openweathermap.models.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {

    @GET("/data/2.5/weather")
    Call<WeatherResponse> getForecastByCity(@Query("q") String city);
}
