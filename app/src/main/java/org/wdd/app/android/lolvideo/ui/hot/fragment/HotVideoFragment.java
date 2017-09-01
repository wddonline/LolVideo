package org.wdd.app.android.lolvideo.ui.hot.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.lolvideo.ui.base.BaseFragment;
import org.wdd.app.android.lolvideo.ui.category.activity.NewsListActivity;
import org.wdd.app.android.lolvideo.ui.category.activity.VideoListActivity;
import org.wdd.app.android.lolvideo.ui.detail.activity.VideoDetailActivity;
import org.wdd.app.android.lolvideo.ui.hot.adapter.HotVideoAdapter;
import org.wdd.app.android.lolvideo.ui.hot.model.HotCategory;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;
import org.wdd.app.android.lolvideo.ui.hot.presenter.HotVideoPresenter;
import org.wdd.app.android.lolvideo.utils.AppToaster;
import org.wdd.app.android.lolvideo.utils.AppUtils;
import org.wdd.app.android.lolvideo.views.LineDividerDecoration;
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
    private HotVideoAdapter mAdapter;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusHeight = AppUtils.getStatusHeight(getActivity());
            View statusBar = mRootView.findViewById(R.id.fragment_hot_video_statusbar);
            statusBar.setVisibility(View.VISIBLE);
            statusBar.getLayoutParams().height = statusHeight;
        }

        mLoadView = mRootView.findViewById(R.id.fragment_hot_video_loadview);
        mRecyclerView = mRootView.findViewById(R.id.fragment_hot_video_listview);
        mRefreshLayout = mRootView.findViewById(R.id.fragment_hot_video_refreshview);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        LineDividerDecoration decoration = new LineDividerDecoration(getContext());
        int offset = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        decoration.setLeftOffset(offset);
        decoration.setRightOffset(offset);
        mRecyclerView.addItemDecoration(decoration);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelRequest();
    }

    public void showHotVideoView(List<HotCategory> cates) {
        if (mAdapter == null) {
            mLoadView.setStatus(LoadView.LoadStatus.Normal);
            mRefreshLayout.setVisibility(View.VISIBLE);

            mAdapter = new HotVideoAdapter(getContext());
            mAdapter.refreshData(cates);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);
            mAdapter.setOnHotVideoClickedListener(new HotVideoAdapter.OnHotVideoClickedListener() {
                @Override
                public void onMoreVideoClicked(boolean isVideo, Video video) {
                    if (isVideo) {
                        VideoListActivity.show(getActivity(), video.url);
                    } else {
                        NewsListActivity.show(getActivity(), video.url);
                    }
                }

                @Override
                public void onHotVideoClicked(boolean isVideo, Video video) {
                    VideoDetailActivity.show(getActivity(), video.title, video.url);
                }

            });
        } else {
            mRefreshLayout.setRefreshing(false);
            mAdapter.refreshData(cates);
        }
    }

    public void showRequestErrorView(String error) {
        if (mAdapter == null) {
            mLoadView.setStatus(LoadView.LoadStatus.Request_Failure, error);
        } else {
            mRefreshLayout.setRefreshing(false);
            AppToaster.show(error);
        }
    }

    public void showNetworkErrorView() {
        if (mAdapter == null) {
            mLoadView.setStatus(LoadView.LoadStatus.Network_Error);
        } else {
            mRefreshLayout.setRefreshing(false);
            AppToaster.show(R.string.no_connection_error);
        }
    }

    public void showNotDataViews() {
        if (mAdapter == null) {
            mLoadView.setStatus(LoadView.LoadStatus.No_Data);
        } else {
            mRefreshLayout.setRefreshing(false);
            AppToaster.show(R.string.no_data_error);
        }
    }
}
