package org.wdd.app.android.lolvideo.ui.matches.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.lolvideo.ui.base.AbstractCommonAdapter.LoadStatus;
import org.wdd.app.android.lolvideo.ui.base.BaseFragment;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;
import org.wdd.app.android.lolvideo.ui.matches.adapter.MatchMenuAdapter;
import org.wdd.app.android.lolvideo.ui.matches.adapter.MatchVideoAdapter;
import org.wdd.app.android.lolvideo.ui.matches.presenter.MatchesPresenter;
import org.wdd.app.android.lolvideo.utils.AppToaster;
import org.wdd.app.android.lolvideo.utils.AppUtils;
import org.wdd.app.android.lolvideo.views.LineDividerDecoration;
import org.wdd.app.android.lolvideo.views.LoadView;

import java.util.List;

/**
 * Created by richard on 8/31/17.
 */

public class MatchesFragment extends BaseFragment {

    private View mRootView;
    private Toolbar mToolbar;
    private View mContentView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LoadView mLoadView;
    private View mMenuView;
    private RecyclerView mMenuListView;

    private MatchesPresenter mPresenter;
    private MatchVideoAdapter mVideoAdapter;
    private MatchMenuAdapter mMenuAdapter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_matches, container, false);
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
        mPresenter = new MatchesPresenter(this);
    }

    private void initViews() {
        setHasOptionsMenu(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusHeight = AppUtils.getStatusHeight(getActivity());
            View statusBar = mRootView.findViewById(R.id.fragment_matches_statusbar);
            statusBar.setVisibility(View.VISIBLE);
            statusBar.getLayoutParams().height = statusHeight;
        }

        mToolbar = mRootView.findViewById(R.id.fragment_matches_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                toggleMenu();
                return true;
            }
        });
        mToolbar.setTitle(R.string.hot_matches);

        mContentView = mRootView.findViewById(R.id.fragment_matches_content);
        mRefreshLayout = mRootView.findViewById(R.id.fragment_matches_refresh);
        mRecyclerView = mRootView.findViewById(R.id.fragment_matches_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LineDividerDecoration divider = new LineDividerDecoration(getContext());
        int offset = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        divider.setLeftOffset(offset);
        divider.setRightOffset(offset);
        mRecyclerView.addItemDecoration(divider);
        mLoadView = mRootView.findViewById(R.id.fragment_matches_load);
        mMenuView = mRootView.findViewById(R.id.fragment_matches_menu);
        mMenuListView = mRootView.findViewById(R.id.fragment_matches_menu_list);
        mMenuListView.setLayoutManager(new LinearLayoutManager(getContext()));

        mMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMenu();
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getMatchVideosData(false, host);
            }
        });
        mLoadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                mPresenter.getMatchVideosData(false, host);
            }
        });

    }

    private void toggleMenu() {
        Animation anim;
        if (mMenuView.getVisibility() == View.VISIBLE) {
            anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_out);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mMenuView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_in);
            mMenuView.setVisibility(View.VISIBLE);
        }
        anim.setDuration(500);
        anim.setInterpolator(new LinearInterpolator());
        mMenuListView.startAnimation(anim);
    }

    @Override
    protected void lazyLoad() {
        mPresenter.getMatchVideosData(false, host);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_filter, menu);
        if (mMenuAdapter != null) {
            menu.findItem(R.id.menu_filter).setVisible(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelRequest();
    }

    public void showMatchVideoView(boolean isAppend, boolean isLastPage, List<HtmlHref> menus, List<Video> videos) {
        if (mVideoAdapter == null) {
            mLoadView.setStatus(LoadView.LoadStatus.Normal);

            mVideoAdapter = new MatchVideoAdapter(getContext());
            mVideoAdapter.refreshData(videos);
            mRecyclerView.setAdapter(mVideoAdapter);
            mVideoAdapter.setOnLoadMoreListener(new AbstractCommonAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mPresenter.getMatchVideosData(true, host);
                }
            });

            mContentView.setVisibility(View.VISIBLE);
            mMenuAdapter = new MatchMenuAdapter(getContext(), "/bisai", getString(R.string.hot_matches), menus);
            mMenuAdapter.setOnMenuClickedListener(new MatchMenuAdapter.OnMenuClickedListener() {
                @Override
                public void onMenuClicked(HtmlHref href) {
                    mToolbar.setTitle(href.name);
                    mPresenter.getMatchVideosData(href.url, false, host);
                    mRecyclerView.scrollToPosition(0);
                    toggleMenu();
                }
            });
            mMenuListView.setAdapter(mMenuAdapter);
            mMenuAdapter.setLoadStatus(LoadStatus.NoMore);

            MenuItem menuItem = mToolbar.getMenu().findItem(R.id.menu_filter);
            if (menuItem != null) {
                menuItem.setVisible(true);
            }
        } else {
            if (isAppend) {
                mVideoAdapter.setLoadStatus(LoadStatus.Normal);
                mVideoAdapter.appendVideos(videos);
            } else {
                mRefreshLayout.setRefreshing(false);
                mVideoAdapter.refreshData(videos);
            }
        }
        mVideoAdapter.setLoadStatus(isLastPage ? LoadStatus.NoMore : LoadStatus.Normal);
    }

    public void showNotDataViews(boolean isAppend) {
        if (mVideoAdapter == null) {
            mLoadView.setStatus(LoadView.LoadStatus.No_Data);
        } else {
            if (isAppend) {
                mVideoAdapter.setLoadStatus(LoadStatus.Normal);
            } else {
                mRefreshLayout.setRefreshing(false);
            }
            AppToaster.show(R.string.no_data_error);
        }
    }

    public void showRequestErrorView(boolean isAppend, String error) {
        if (mVideoAdapter == null) {
            mLoadView.setStatus(LoadView.LoadStatus.Request_Failure, error);
        } else {
            if (isAppend) {
                mVideoAdapter.setLoadStatus(LoadStatus.Normal);
            } else {
                mRefreshLayout.setRefreshing(false);
            }
            AppToaster.show(error);
        }
    }

    public void showNetworkErrorView(boolean isAppend) {
        if (mVideoAdapter == null) {
            mLoadView.setStatus(LoadView.LoadStatus.Network_Error);
        } else {
            if (isAppend) {
                mVideoAdapter.setLoadStatus(LoadStatus.Normal);
            } else {
                mRefreshLayout.setRefreshing(false);
            }
            AppToaster.show(R.string.no_connection_error);
        }
    }
}
