package com.packtpub.apps.rxjava_essentials.chapter4;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.packtpub.apps.rxjava_essentials.R;
import com.packtpub.apps.rxjava_essentials.Utils;
import com.packtpub.apps.rxjava_essentials.apps.AppInfo;
import com.packtpub.apps.rxjava_essentials.apps.ApplicationAdapter;
import com.packtpub.apps.rxjava_essentials.apps.ApplicationsList;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class DistinctExampleFragment extends Fragment {

    @InjectView(R.id.fragment_first_example_list)
    RecyclerView mRecyclerView;

    @InjectView(R.id.fragment_first_example_swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ApplicationAdapter mAdapter;

    private ArrayList<AppInfo> mAddedApps = new ArrayList<>();

    public DistinctExampleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_example, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mAdapter = new ApplicationAdapter(new ArrayList<>(), R.layout.applications_list_item);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.myPrimaryColor));
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        // Progress
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setVisibility(View.GONE);

        List<AppInfo> apps = ApplicationsList.getInstance().getList();

        loadList(apps);

        testAdditionals(apps);
    }


    private void loadList(List<AppInfo> apps) {
        mRecyclerView.setVisibility(View.VISIBLE);

        Observable<AppInfo> fullOfDuplicates = Observable.fromIterable(apps)
                .take(3)
                .repeat(3);

        fullOfDuplicates.subscribe(appInfo -> Utils.logMessage("take(3)/repeat(3) test : onNext() : " + appInfo.getName()));

        fullOfDuplicates.distinct()
                .subscribe(new Observer<AppInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Something went south!", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(AppInfo appInfo) {
                        mAddedApps.add(appInfo);
                        mAdapter.addApplication(mAddedApps.size() - 1, appInfo);
                    }
                });
    }


    private void testAdditionals(List<AppInfo> apps) {
        Observable<AppInfo> fullOfDuplicates = Observable.fromIterable(apps)
                .take(3)
                .repeat(3);

        Integer[] temperatures = {21, 21, 21, 22, 22, 22, 23, 23, 22, 21, 21, 21};
        Observable<Integer> sensor = Observable.fromArray(temperatures);
        sensor.distinctUntilChanged()
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Utils.logMessage("distinctUntilChanged : onSubscribe()");
                    }

                    @Override
                    public void onNext(Integer value) {
                        Utils.logMessage("distinctUntilChanged : onNext() = " + value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Utils.logMessage("distinctUntilChanged : onComplete()");
                    }
                });

        sensor.firstElement().subscribe(new MaybeObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Utils.logMessage("firstElement() - onSubscribe()");
            }

            @Override
            public void onSuccess(Integer value) {
                Utils.logMessage("firstElement() - onSuccess() = " + value);
            }

            @Override
            public void onError(Throwable e) {
                Utils.logMessage("firstElement() - onError()");
            }

            @Override
            public void onComplete() {
                Utils.logMessage("firstElement() - onComplete()");
            }
        });

        sensor.filter(v -> v < 0)
                .last(17)
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Utils.logMessage("last() - onSubscribe()");
                    }

                    @Override
                    public void onSuccess(Integer value) {
                        Utils.logMessage("last() - onSuccess() = " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.logMessage("last() - onError()");
                    }
                });

        //TODO: Add proper test code for sample()
//        sensor.sample(2, TimeUnit.SECONDS)
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Utils.logMessage("sample() - onSubscribe()");
//                    }
//
//                    @Override
//                    public void onNext(Integer value) {
//                        Utils.logMessage("sample() - onNext() = " + value);
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Utils.logMessage("sample() - onError()");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Utils.logMessage("sample() - onComplete()");
//                    }
//                });

        //TODO: Add proper test code for timeout()
//        sensor.timeout(2, TimeUnit.SECONDS)
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Utils.logMessage("timeout() - onSubscribe()");
//                    }
//
//                    @Override
//                    public void onNext(Integer value) {
//                        Utils.logMessage("timeout() - onNext() = " + value);
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Utils.logMessage("timeout() - onError()");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Utils.logMessage("timeout() - onComplete()");
//                    }
//                });
    }
}
