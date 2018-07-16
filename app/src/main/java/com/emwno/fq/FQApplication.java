package com.emwno.fq;

import android.app.Application;

import io.realm.Realm;

/**
 * Created on 16 Jul 2018.
 */
public class FQApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
