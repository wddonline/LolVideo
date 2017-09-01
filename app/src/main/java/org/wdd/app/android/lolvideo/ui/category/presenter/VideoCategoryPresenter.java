package org.wdd.app.android.lolvideo.ui.category.presenter;

import org.wdd.app.android.lolvideo.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.lolvideo.ui.base.BasePresenter;
import org.wdd.app.android.lolvideo.ui.category.fragment.VideoCategoryFragment;
import org.wdd.app.android.lolvideo.ui.category.data.VideoCategoryDataGetter;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;

import java.util.List;
import java.util.Map;

/**
 * Created by richard on 8/25/17.
 */

public class VideoCategoryPresenter implements BasePresenter, VideoCategoryDataGetter.DataCallback {

    private VideoCategoryFragment mView;
    private VideoCategoryDataGetter mGetter;

    public VideoCategoryPresenter(VideoCategoryFragment view) {
        this.mView = view;
        mGetter = new VideoCategoryDataGetter(view.getContext(), this);
    }

    @Override
    public void cancelRequest() {
        mGetter.cancelSession();
    }

    public void getLolVideoMenusData(ActivityFragmentAvaliable host) {
        mGetter.requestLolVideoMenus(host);
    }

    @Override
    public void onRequestOk(Map<String, List<HtmlHref>> menus) {
        if (menus == null || menus.size() == 0) {
            mView.showNoDataViews();
            return;
        }
        mView.showVideoMenuView(menus);
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