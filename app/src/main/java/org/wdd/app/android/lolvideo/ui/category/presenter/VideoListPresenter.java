package org.wdd.app.android.lolvideo.ui.category.presenter;

import org.wdd.app.android.lolvideo.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.lolvideo.ui.base.BasePresenter;
import org.wdd.app.android.lolvideo.ui.category.activity.VideoListActivity;
import org.wdd.app.android.lolvideo.ui.category.data.VideoListDataGetter;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;

import java.util.List;

/**
 * Created by richard on 8/25/17.
 */

public class VideoListPresenter implements BasePresenter, VideoListDataGetter.DataCallback {

    private VideoListActivity mView;
    private VideoListDataGetter mGetter;

    public VideoListPresenter(VideoListActivity view, String url) {
        this.mView = view;
        mGetter = new VideoListDataGetter(view, url, this);
    }

    @Override
    public void cancelRequest() {
        mGetter.cancelSession();
    }

    public void getVideoListData(boolean isAppend, ActivityFragmentAvaliable host) {
        mGetter.requestVideoList(isAppend, host);
    }

    public void getVideoListData(String url, boolean isAppend, ActivityFragmentAvaliable host) {
        mGetter.requestVideoList(url, isAppend, host);
    }

    @Override
    public void onRequestOk(boolean isAppend, boolean isLastPage, List<HtmlHref> menus, List<Video> videos) {
        if (videos == null || videos.size() == 0) {
            mView.showNotDataViews(isAppend);
            return;
        }
        mView.showVideoListView(isAppend, isLastPage, menus, videos);
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
