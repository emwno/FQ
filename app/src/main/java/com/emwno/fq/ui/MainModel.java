package com.emwno.fq.ui;

import android.util.Log;

import com.emwno.fq.model.FQ;
import com.emwno.fq.model.Fuck;
import com.emwno.fq.network.FQFactory;
import com.emwno.fq.network.FQService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.realm.Realm;

/**
 * Created on 16 Jul 2018.
 */
public class MainModel implements MainContract.Model {

    private FQService mService;

    public MainModel() {
        mService = FQFactory.getRetrofitInstance().create(FQService.class);
    }

    @Override
    public Observable<List<Fuck>> getFucksWeb() {
        return mService.getOperations().doOnNext(this::saveFucksToCache);
    }

    @Override
    public Observable<List<Fuck>> getFucksLocal() {
        List<Fuck> results = Realm.getDefaultInstance().where(Fuck.class).findAll();
        return Observable.just(results).delay(1, TimeUnit.SECONDS);
    }

    @Override
    public Observable<FQ> getFQ(String url) {
        Log.e("king", url);
        return mService.getFuck(url);
    }

    private void saveFucksToCache(List<Fuck> fuckList) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(fuckList);
        realm.commitTransaction();
        realm.close();
    }
}
