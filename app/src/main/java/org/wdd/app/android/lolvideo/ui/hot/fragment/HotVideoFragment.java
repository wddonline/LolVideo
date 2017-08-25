package org.wdd.app.android.lolvideo.ui.hot.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.ui.base.BaseFragment;
import org.wdd.app.android.lolvideo.ui.hot.presenter.HotVideoPresenter;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;
import org.wdd.app.android.lolvideo.ui.main.model.VideoItem;
import org.wdd.app.android.lolvideo.views.LoadView;

import java.util.List;

/**
 * Created by richard on 8/25/17.
 */

public class HotVideoFragment extends BaseFragment {

    private View mRootView;
    private LoadView mLoadView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private HotVideoPresenter mPresenter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_hot_video, container, false);
            initData();
            initViews();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void initData() {
        mPresenter = new HotVideoPresenter(this);
    }

    private void initViews() {
        mLoadView = mRootView.findViewById(R.id.fragment_hot_video_loadview);
        mRecyclerView = mRootView.findViewById(R.id.fragment_hot_video_listview);
        mRefreshLayout = mRootView.findViewById(R.id.fragment_hot_video_refreshview);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getHotVideoData(host);
            }
        });

    }

    @Override
    protected void lazyLoad() {
        mPresenter.getHotVideoData(host);
    }

    public void showHotVideoView(List<String> titiles, List<HtmlHref> mores, SparseArray<List<VideoItem>> hots) {
        mLoadView.setStatus(LoadView.LoadStatus.Normal);
        mRefreshLayout.setVisibility(View.VISIBLE);
    }

    public void showRequestErrorView(String error) {
        mLoadView.setStatus(LoadView.LoadStatus.Request_Failure, error);
    }

    public void showNetworkErrorView() {
        mLoadView.setStatus(LoadView.LoadStatus.Network_Error);
    }
}
