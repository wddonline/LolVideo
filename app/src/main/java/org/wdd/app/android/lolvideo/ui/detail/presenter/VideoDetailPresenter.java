package org.wdd.app.android.lolvideo.ui.detail.presenter;

import org.wdd.app.android.lolvideo.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.lolvideo.ui.base.BasePresenter;
import org.wdd.app.android.lolvideo.ui.category.data.VideoCategoryDataGetter;
import org.wdd.app.android.lolvideo.ui.category.fragment.VideoCategoryFragment;
import org.wdd.app.android.lolvideo.ui.detail.activity.VideoDetailActivity;
import org.wdd.app.android.lolvideo.ui.detail.data.VideoDeatilDataGetter;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;

import java.util.List;
import java.util.Map;

/**
 * Created by richard on 8/25/17.
 */

public class VideoDetailPresenter implements BasePresenter, VideoDeatilDataGetter.DataCallback {

    private VideoDetailActivity mView;
    private VideoDeatilDataGetter mGetter;

    public VideoDetailPresenter(VideoDetailActivity view) {
        this.mView = view;
        mGetter = new VideoDeatilDataGetter(view, this);
    }

    @Override
    public void cancelRequest() {
        mGetter.cancelSession();
    }

    public void getVideoDetailData(String url, ActivityFragmentAvaliable host) {
        mGetter.requestVideoDetail(url, host);
    }

    @Override
    public void onRequestOk(Video video, List<Video> videos) {
        mView.showVideoDetailViews(video, videos);
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