package org.wdd.app.android.lolvideo.ui.category.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.ui.base.BaseFragment;
import org.wdd.app.android.lolvideo.ui.category.activity.NewsListActivity;
import org.wdd.app.android.lolvideo.ui.category.activity.VideoListActivity;
import org.wdd.app.android.lolvideo.ui.category.presenter.VideoCategoryPresenter;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;
import org.wdd.app.android.lolvideo.utils.AppUtils;
import org.wdd.app.android.lolvideo.views.LoadView;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by richard on 8/30/17.
 */

public class VideoCategoryFragment extends BaseFragment {

    private View mRootView;
    private LoadView mLoadView;
    private ScrollView mScrollView;
    private LinearLayout mContainer;

    private LayoutInflater mInflater;
    private VideoCategoryPresenter mPresenter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_video_category, container, false);
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
        mPresenter = new VideoCategoryPresenter(this);
    }

    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusHeight = AppUtils.getStatusHeight(getActivity());
            View statusBar = mRootView.findViewById(R.id.fragment_video_category_statusbar);
            statusBar.setVisibility(View.VISIBLE);
            statusBar.getLayoutParams().height = statusHeight;
        }

        mLoadView = mRootView.findViewById(R.id.fragment_video_category_load);
        mScrollView = mRootView.findViewById(R.id.fragment_video_category_scroll);
        mContainer = mRootView.findViewById(R.id.fragment_video_category_container);
        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected void lazyLoad() {
        mPresenter.getLolVideoMenusData(host);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelRequest();
    }

    public void showVideoMenuView(Map<String, List<HtmlHref>> menus) {
        mLoadView.setStatus(LoadView.LoadStatus.Normal);
        mScrollView.setVisibility(View.VISIBLE);
        setMenuViews(menus);
    }

    private void setMenuViews(Map<String, List<HtmlHref>> menus) {
        View headerView;
        View dividerView;
        TagFlowLayout flowLayout;
        LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        dlp.leftMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        dlp.rightMargin = dlp.leftMargin;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Set<Map.Entry<String, List<HtmlHref>>> set =  menus.entrySet();
        Iterator<Map.Entry<String, List<HtmlHref>>> it = set.iterator();
        Map.Entry<String, List<HtmlHref>> entry;
        while (it.hasNext()) {
            entry = it.next();
            headerView = mInflater.inflate(R.layout.item_hot_video_header, null, false);
            ((TextView)headerView.findViewById(R.id.item_hot_video_header_title)).setText(entry.getKey());
            headerView.findViewById(R.id.item_hot_video_header_more).setVisibility(View.GONE);
            mContainer.addView(headerView, lp);
            dividerView = new View(getContext());
            dividerView.setBackgroundColor(Color.parseColor("#CCCCCC"));
            mContainer.addView(dividerView, dlp);
            if (entry.getValue().size() > 0) {
                flowLayout = new TagFlowLayout(getContext());
                flowLayout.setAdapter(new CategoryAdapter(entry.getValue()));
                mContainer.addView(flowLayout, lp);
            }
        }
    }

    public void showRequestErrorView(String error) {
        mLoadView.setStatus(LoadView.LoadStatus.Request_Failure, error);
    }

    public void showNetworkErrorView() {
        mLoadView.setStatus(LoadView.LoadStatus.Network_Error);
    }

    public void showNoDataViews() {
        mLoadView.setStatus(LoadView.LoadStatus.No_Data);
    }

    private class CategoryAdapter extends TagAdapter<HtmlHref> {

        public CategoryAdapter(List<HtmlHref> datas) {
            super(datas);
        }

        @Override
        public View getView(FlowLayout parent, int position, final HtmlHref href) {
            View view = mInflater.inflate(R.layout.item_video_category, parent, false);
            TextView textView = view.findViewById(R.id.item_video_category_name);
            textView.setText(href.name);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (href.url.contains("/xueyuan/")) {
                        NewsListActivity.show(getActivity(), href.url);
                    } else {
                        VideoListActivity.show(getActivity(), href.url);
                    }
                }
            });
            return view;
        }
    }
}
