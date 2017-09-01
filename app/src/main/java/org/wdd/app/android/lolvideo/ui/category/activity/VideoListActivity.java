package org.wdd.app.android.lolvideo.ui.category.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.lolvideo.ui.base.BaseActivity;
import org.wdd.app.android.lolvideo.ui.category.presenter.VideoListPresenter;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;
import org.wdd.app.android.lolvideo.ui.matches.adapter.MatchMenuAdapter;
import org.wdd.app.android.lolvideo.ui.matches.adapter.MatchVideoAdapter;
import org.wdd.app.android.lolvideo.utils.AppToaster;
import org.wdd.app.android.lolvideo.views.LineDividerDecoration;
import org.wdd.app.android.lolvideo.views.LoadView;

import java.util.List;

public class VideoListActivity extends BaseActivity {

    public static void show(Activity activity, String url) {
        Intent intent = new Intent(activity, VideoListActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    private Toolbar mToolbar;
    private View mContentView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LoadView mLoadView;
    private View mMenuView;
    private RecyclerView mMenuListView;

    private VideoListPresenter mPresenter;
    private MatchVideoAdapter mVideoAdapter;
    private MatchMenuAdapter mMenuAdapter;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        initData();
        initTitles();
        initViews();
    }

    private void initTitles() {
        mToolbar = (Toolbar) findViewById(R.id.activity_video_list_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                toggleMenu();
                return true;
            }
        });
        mToolbar.setTitle(R.string.hot_videos);
    }

    private void initData() {
        mUrl = getIntent().getStringExtra("url");
        mPresenter = new VideoListPresenter(this, mUrl);
    }

    private void initViews() {
        mContentView = findViewById(R.id.activity_video_list_content);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_video_list_refresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_video_list_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LineDividerDecoration divider = new LineDividerDecoration(this);
        int offset = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        divider.setLeftOffset(offset);
        divider.setRightOffset(offset);
        mRecyclerView.addItemDecoration(divider);
        mLoadView = (LoadView) findViewById(R.id.activity_video_list_loadview);
        mMenuView = findViewById(R.id.activity_video_list_menu_container);
        mMenuListView = (RecyclerView) findViewById(R.id.activity_video_list_menu);
        mMenuListView.setLayoutManager(new LinearLayoutManager(this));

        mMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMenu();
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getVideoListData(false, host);
            }
        });
        mLoadView.setReloadClickedListener(new LoadView.OnReloadClickedListener() {
            @Override
            public void onReloadClicked() {
                mPresenter.getVideoListData(false, host);
            }
        });

        mPresenter.getVideoListData(false, host);
    }

    private void toggleMenu() {
        Animation anim;
        if (mMenuView.getVisibility() == View.VISIBLE) {
            anim = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
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
            anim = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
            mMenuView.setVisibility(View.VISIBLE);
        }
        anim.setDuration(500);
        anim.setInterpolator(new LinearInterpolator());
        mMenuListView.startAnimation(anim);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        if (mMenuAdapter != null) {
            menu.findItem(R.id.menu_filter).setVisible(true);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancelRequest();
    }

    public void showVideoListView(boolean isAppend, boolean isLastPage, List<HtmlHref> menus, List<Video> videos) {
        if (mVideoAdapter == null) {
            mLoadView.setStatus(LoadView.LoadStatus.Normal);

            mVideoAdapter = new MatchVideoAdapter(this);
            mVideoAdapter.refreshData(videos);
            mRecyclerView.setAdapter(mVideoAdapter);
            mVideoAdapter.setOnLoadMoreListener(new AbstractCommonAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    mPresenter.getVideoListData(true, host);
                }
            });
            mContentView.setVisibility(View.VISIBLE);
            if (menus != null && menus.size() > 0) {
                mMenuAdapter = new MatchMenuAdapter(this, mUrl, getString(R.string.hot_videos), menus);
                mMenuAdapter.setOnMenuClickedListener(new MatchMenuAdapter.OnMenuClickedListener() {
                    @Override
                    public void onMenuClicked(HtmlHref href) {
                        mToolbar.setTitle(href.name);
                        mPresenter.getVideoListData(href.url, false, host);
                        mRecyclerView.scrollToPosition(0);
                        toggleMenu();
                    }
                });
                mMenuListView.setAdapter(mMenuAdapter);
                mMenuAdapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.NoMore);

                MenuItem menuItem = mToolbar.getMenu().findItem(R.id.menu_filter);
                if (menuItem != null) {
                    menuItem.setVisible(true);
                }
            }
        } else {
            if (isAppend) {
                mVideoAdapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
                mVideoAdapter.appendVideos(videos);
            } else {
                mRefreshLayout.setRefreshing(false);
                mVideoAdapter.refreshData(videos);
            }
        }
        mVideoAdapter.setLoadStatus(isLastPage ? AbstractCommonAdapter.LoadStatus.NoMore : AbstractCommonAdapter.LoadStatus.Normal);
    }

    public void showNotDataViews(boolean isAppend) {
        if (mVideoAdapter == null) {
            mLoadView.setStatus(LoadView.LoadStatus.No_Data);
        } else {
            if (isAppend) {
                mVideoAdapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
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
                mVideoAdapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
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
                mVideoAdapter.setLoadStatus(AbstractCommonAdapter.LoadStatus.Normal);
            } else {
                mRefreshLayout.setRefreshing(false);
            }
            AppToaster.show(R.string.no_connection_error);
        }
    }
}
