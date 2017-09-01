package org.wdd.app.android.lolvideo.ui.category.presenter;

import org.wdd.app.android.lolvideo.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.lolvideo.ui.base.BasePresenter;
import org.wdd.app.android.lolvideo.ui.category.activity.NewsListActivity;
import org.wdd.app.android.lolvideo.ui.category.data.NewsListDataGetter;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;

import java.util.List;

/**
 * Created by richard on 8/25/17.
 */

public class NewsListPresenter implements BasePresenter, NewsListDataGetter.DataCallback {

    private NewsListActivity mView;
    private NewsListDataGetter mGetter;

    public NewsListPresenter(NewsListActivity view) {
        this.mView = view;
        mGetter = new NewsListDataGetter(view, this);
    }

    @Override
    public void cancelRequest() {
        mGetter.cancelSession();
    }

    public void getNewsListData(String url, boolean isAppend, ActivityFragmentAvaliable host) {
        mGetter.requestNewsList(url, isAppend, host);
    }

    @Override
    public void onRequestOk(boolean isAppend, boolean isLastPage, List<HtmlHref> menus, List<Video> videos) {
        if (videos == null || videos.size() == 0) {
            mView.showNotDataViews(isAppend);
            return;
        }
        mView.showNewsListView(isAppend, isLastPage, menus, videos);
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
