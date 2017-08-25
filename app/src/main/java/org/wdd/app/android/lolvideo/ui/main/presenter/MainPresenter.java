package org.wdd.app.android.lolvideo.ui.main.presenter;

import org.wdd.app.android.lolvideo.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.lolvideo.ui.base.BasePresenter;
import org.wdd.app.android.lolvideo.ui.main.activity.MainActivity;
import org.wdd.app.android.lolvideo.ui.main.data.MainDataGetter;

/**
 * Created by richard on 8/25/17.
 */

public class MainPresenter implements BasePresenter, MainDataGetter.DataCallback {

    private MainActivity mView;
    private MainDataGetter mGetter;

    public MainPresenter(MainActivity view) {
        this.mView = view;
        mGetter = new MainDataGetter(view, this);
    }

    @Override
    public void cancelRequest() {
        mGetter.cancelSession();
    }

    public void getLolVideoMenusData(ActivityFragmentAvaliable host) {
        mGetter.requestLolVideoMenus(host);
    }

    @Override
    public void onRequestOk() {
        mView.showVideoMenuView();
    }

    @Override
    public void onRequestError(String error) {
        mView.showRequestErrorView(error);
    }

    @Override
    public void onNetworkError() {
        mView.showNetworkErrorView();
    }
}
