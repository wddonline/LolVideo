package org.wdd.app.android.lolvideo.ui.detail.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.umeng.socialize.UMShareAPI;
import com.youku.cloud.module.PlayerErrorInfo;
import com.youku.cloud.player.PlayerListener;
import com.youku.cloud.player.VideoDefinition;
import com.youku.cloud.player.YoukuPlayerView;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.ui.base.BaseActivity;
import org.wdd.app.android.lolvideo.ui.detail.presenter.VideoDetailPresenter;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;
import org.wdd.app.android.lolvideo.utils.AppToaster;
import org.wdd.app.android.lolvideo.views.LoadView;

import java.util.List;

public class VideoDetailActivity extends BaseActivity {

    public static void show(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, VideoDetailActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    private LoadView mLoadView;
    private View mContainerView;
    private YoukuPlayerView mPlayerView;

    private String mUrl;
    private String mTitle;

    private VideoDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        initTitles();
        initData();
        initViews();
    }

    private void initTitles() {

    }

    private void initData() {
        mPresenter = new VideoDetailPresenter(this);

        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
    }

    private void initViews() {
        mLoadView = (LoadView) findViewById(R.id.activity_video_detail_loadview);
        mContainerView = findViewById(R.id.activity_video_detail_container);
        mPlayerView = (YoukuPlayerView) findViewById(R.id.activity_video_detail_player);

        mPresenter.getVideoDetailData(mUrl, host);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancelRequest();
        mPlayerView.onDestroy();
    }

    public void showVideoDetailViews(Video video, List<Video> videos) {
        mLoadView.setStatus(LoadView.LoadStatus.Normal);

        mPlayerView.attachActivity(this);
        mPlayerView.setPreferVideoDefinition(VideoDefinition.VIDEO_HD);
        mPlayerView.setShowBackBtn(false);
        mPlayerView.setPlayerListener(new VideoPlayerListener());
        mPlayerView.playYoukuVideo(video.url);
    }

    public void showRequestErrorView(String error) {
        mLoadView.setStatus(LoadView.LoadStatus.Request_Failure, error);
    }

    public void showNetworkErrorView() {
        mLoadView.setStatus(LoadView.LoadStatus.Network_Error);
    }

    private class VideoPlayerListener extends PlayerListener {
        @Override
        public void onComplete() {
            super.onComplete();
        }

        @Override
        public void onError(int code, PlayerErrorInfo info) {
            switch(code) {
                case 3001://无版权
                case 3002://被禁止播放
                case 3004://订阅才能观看
                default:
                    AppToaster.show(info.getDesc());
                    break;
            }

        }

        @Override
        public void OnCurrentPositionChanged(int msec) {
            super.OnCurrentPositionChanged(msec);
        }

        @Override
        public void onVideoNeedPassword(int code) {
            super.onVideoNeedPassword(code);
        }

        @Override
        public void onVideoSizeChanged(int width, int height) {
            super.onVideoSizeChanged(width, height);
        }
    }

}
