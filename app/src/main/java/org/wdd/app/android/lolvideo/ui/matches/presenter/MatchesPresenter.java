package org.wdd.app.android.lolvideo.ui.matches.presenter;

import org.wdd.app.android.lolvideo.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.lolvideo.ui.base.BasePresenter;
import org.wdd.app.android.lolvideo.ui.hot.data.HotVideoDataGetter;
import org.wdd.app.android.lolvideo.ui.hot.fragment.HotVideoFragment;
import org.wdd.app.android.lolvideo.ui.hot.model.HotCategory;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;
import org.wdd.app.android.lolvideo.ui.matches.data.MatchesDataGetter;
import org.wdd.app.android.lolvideo.ui.matches.fragment.MatchesFragment;

import java.util.List;

/**
 * Created by richard on 8/25/17.
 */

public class MatchesPresenter implements BasePresenter, MatchesDataGetter.DataCallback {

    private MatchesFragment mView;
    private MatchesDataGetter mGetter;

    public MatchesPresenter(MatchesFragment view) {
        this.mView = view;
        mGetter = new MatchesDataGetter(view.getContext(), this);
    }

    @Override
    public void cancelRequest() {
        mGetter.cancelSession();
    }

    public void getMatchVideosData(boolean isAppend, ActivityFragmentAvaliable host) {
        mGetter.requestMatchVideos(isAppend, host);
    }

    public void getMatchVideosData(String url, boolean isAppend, ActivityFragmentAvaliable host) {
        mGetter.requestMatchVideos(url, isAppend, host);
    }

    @Override
    public void onRequestOk(boolean isAppend, boolean isLastPage, List<HtmlHref> menus, List<Video> videos) {
        if (videos == null || videos.size() == 0) {
            mView.showNotDataViews(isAppend);
            return;
        }
        mView.showMatchVideoView(isAppend, isLastPage, menus, videos);
    }

    @Override
    public void onRequestError(boolean isAppend, String error) {
        mView.showRequestErrorView(isAppend, error);
    }

    @Override
    public void onNetworkError(boolean isAppend) {
        mView.showNetworkErrorView(isAppend);
    }
}
