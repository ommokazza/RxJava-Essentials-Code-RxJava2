package com.packtpub.apps.rxjava_essentials.chapter8.api.openweathermap;

import com.packtpub.apps.rxjava_essentials.chapter8.api.openweathermap.models.WeatherResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherMapApiManager {

    @Getter
    private static OpenWeatherMapApiManager instance = new OpenWeatherMapApiManager();

    private final OpenWeatherMapService mOpenWeatherMapService;

    private OpenWeatherMapApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mOpenWeatherMapService = retrofit.create(OpenWeatherMapService.class);
    }

    public Observable<WeatherResponse> getForecastByCity(String city) {
        return Observable.create((ObservableOnSubscribe<WeatherResponse>) e -> {
            e.onNext(mOpenWeatherMapService.getForecastByCity(city).execute().body());
            e.onComplete();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
