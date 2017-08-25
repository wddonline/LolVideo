package org.wdd.app.android.lolvideo.ui.hot.presenter;

import android.util.SparseArray;

import org.wdd.app.android.lolvideo.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.lolvideo.ui.base.BasePresenter;
import org.wdd.app.android.lolvideo.ui.hot.data.HotVideoDataGetter;
import org.wdd.app.android.lolvideo.ui.hot.fragment.HotVideoFragment;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;
import org.wdd.app.android.lolvideo.ui.main.model.VideoItem;

import java.util.List;

/**
 * Created by richard on 8/25/17.
 */

public class HotVideoPresenter implements BasePresenter, HotVideoDataGetter.DataCallback {

    private HotVideoFragment mView;
    private HotVideoDataGetter mGetter;

    public HotVideoPresenter(HotVideoFragment view) {
        this.mView = view;
        mGetter = new HotVideoDataGetter(view.getContext(), this);
    }

    @Override
    public void cancelRequest() {
        mGetter.cancelSession();
    }

    public void getHotVideoData(ActivityFragmentAvaliable host) {
        mGetter.requestHotVideo(host);
    }

    @Override
    public void onRequestOk(List<String> titiles, List<HtmlHref> mores, SparseArray<List<VideoItem>> hots) {
        mView.showHotVideoView(titiles, mores, hots);
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
