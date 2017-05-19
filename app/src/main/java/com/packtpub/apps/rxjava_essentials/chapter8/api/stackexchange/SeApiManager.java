package com.packtpub.apps.rxjava_essentials.chapter8.api.stackexchange;

import com.packtpub.apps.rxjava_essentials.chapter8.api.stackexchange.models.User;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.experimental.Accessors;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Accessors(prefix = "m")
public class SeApiManager {

    private final StackExchangeService mStackExchangeService;

    public SeApiManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mStackExchangeService = retrofit.create(StackExchangeService.class);
    }

    public Observable<List<User>> getTenMostPopularSOusers() {
        return Observable.create((ObservableOnSubscribe<List<User>>) e -> {
            e.onNext(mStackExchangeService.getTenMostPopularSOusers().execute().body().getUsers());
            e.onComplete();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<User>> getMostPopularSOusers(int howmany) {
        return Observable.create((ObservableOnSubscribe<List<User>>) e -> {
            e.onNext(mStackExchangeService.getMostPopularSOusers(howmany).execute().body().getUsers());
            e.onComplete();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
