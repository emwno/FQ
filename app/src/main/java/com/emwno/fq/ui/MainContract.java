package com.emwno.fq.ui;

import com.emwno.fq.model.FQ;
import com.emwno.fq.model.Fuck;
import com.emwno.fq.ui.MainPresenter.State;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created on 16 Jul 2018.
 */
public interface MainContract {

    interface View {
        void onFucksReceived(List<Fuck> fuckList);

        void onErrorFucks();

        void onFQReceived(FQ fq);

        void onErrorFQ();
    }

    interface Model {
        Observable<List<Fuck>> getFucksWeb();

        Observable<List<Fuck>> getFucksLocal();

        Observable<FQ> getFQ(String url);
    }

    interface Presenter {
        void getFucks();

        void getFQ(Fuck fuck);

        void getFQMessage(Fuck fuck);

        Fuck getCurrentFuck();

        void handleBlanks(List<String> fuckBlanks);

        void changeState();

        State getCurrentState();
    }
}
