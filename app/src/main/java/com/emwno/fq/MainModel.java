package com.emwno.fq;

import android.util.Log;

import com.emwno.fq.network.FQ;
import com.emwno.fq.network.FQFactory;
import com.emwno.fq.network.FQService;
import com.emwno.fq.network.Fuck;

import java.util.List;

import io.reactivex.Observable;

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
        return mService.getOperations();
    }

    @Override
    public Observable<List<Fuck>> getFucksCache() {
        return null;
    }

    @Override
    public Observable<FQ> getFQ(String url) {
        Log.e("king", url);
        return mService.getFuck(url);
    }
}
