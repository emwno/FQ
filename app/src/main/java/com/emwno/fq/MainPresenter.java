package com.emwno.fq;

import com.emwno.fq.network.Field;
import com.emwno.fq.network.Fuck;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 16 Jul 2018.
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private MainModel mModel;

    private Fuck mFuck;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private State mCurrentState = State.EXPANDED;

    public MainPresenter(MainContract.View view) {
        mView = view;
        mModel = new MainModel();
    }

    @Override
    public void getFucks() {
        Disposable disposable = Observable.concat(mModel.getFucksLocal(), mModel.getFucksWeb())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                        fuckList -> mView.onFucksReceived(fuckList),
                        throwable -> mView.onErrorFucks());

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getFQ(Fuck fuck) {
        String url = fuck.getUrl();
        for (Field field : fuck.getFields())
            url = url.replace(":" + field.getField(), field.getData());

        mFuck = fuck;

        Disposable disposable = mModel.getFQ(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                        fq -> mView.onFQReceived(fq),
                        throwable -> mView.onErrorFQ());

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getFQMessage(Fuck fuck) {
        for (int i = 0; i < fuck.getFields().size(); i++) {
            fuck.getFields().get(i).setData("~|");
        }
        getFQ(fuck);
    }

    public Fuck getCurrentFuck() {
        return mFuck;
    }

    @Override
    public void handleBlanks(List<String> fuckBlanks) {
        boolean allBlank = true;
        for (int i = 0; i < getCurrentFuck().getFields().size(); i++) {
            String blank = fuckBlanks.get(i);

            if (blank.contains("/")) {
                mView.onErrorFQ();
                return;
            }

            if (!blank.isEmpty()) {
                allBlank = false;
                getCurrentFuck().getFields().get(i).setData(fuckBlanks.get(i));
            } else {
                getCurrentFuck().getFields().get(i).setData("~|");
            }
        }

        if (!allBlank) getFQ(getCurrentFuck());
    }

    @Override
    public void changeState() {
        if (mCurrentState == State.EXPANDED) {
            mCurrentState = State.COMPRESSED;
        } else {
            mCurrentState = State.EXPANDED;
        }
    }

    @Override
    public State getCurrentState() {
        return mCurrentState;
    }

    enum State {
        EXPANDED,
        COMPRESSED
    }
}
