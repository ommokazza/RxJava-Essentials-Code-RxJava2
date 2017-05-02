package com.packtpub.apps.rxjava_essentials.example3;


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
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ThirdExampleFragment extends Fragment {

    @InjectView(R.id.fragment_first_example_list)
    RecyclerView mRecyclerView;

    @InjectView(R.id.fragment_first_example_swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private ApplicationAdapter mAdapter;

    private ArrayList<AppInfo> mAddedApps = new ArrayList<>();

    private Disposable mTimeDisposable;

    public ThirdExampleFragment() {
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

        AppInfo appOne = apps.get(0);

        AppInfo appTwo = apps.get(1);

        AppInfo appThree = apps.get(2);

        loadApps(appOne, appTwo, appThree);

    }

    private void loadApps(AppInfo appOne, AppInfo appTwo, AppInfo appThree) {
        mRecyclerView.setVisibility(View.VISIBLE);

        Observable<AppInfo> threeOfThem = Observable.just(appOne, appTwo, appThree);

        threeOfThem.subscribe(new Observer<AppInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                Utils.logMessage("onSubscribe() in loadList()");
            }

            @Override
            public void onComplete() {
                Utils.logMessage("onComplete() in loadList()");
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Here is the list!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
                Utils.logMessage("onError() in loadList()");
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(AppInfo appInfo) {
                Utils.logMessage("onNext() in loadList() - " + appInfo.getName());
                mAddedApps.add(appInfo);
                mAdapter.addApplication(mAddedApps.size() - 1, appInfo);
            }
        });

        Observable.interval(3, 3, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Utils.logMessage("onSubscribe() of interval : " + d.toString());
                        mTimeDisposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        Utils.logMessage("onNext() of interval : " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.logMessage("onError() of interval");
                    }

                    @Override
                    public void onComplete() {
                        Utils.logMessage("onComplete() of interval");
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.logMessage("mTimeDisposable.isDisposed() ? " + mTimeDisposable.isDisposed());
        if (!mTimeDisposable.isDisposed()) {
            mTimeDisposable.dispose();
        }
    }
}
