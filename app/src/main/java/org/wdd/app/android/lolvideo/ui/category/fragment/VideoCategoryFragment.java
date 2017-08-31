package org.wdd.app.android.lolvideo.ui.category.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.preference.HttpDataCache;
import org.wdd.app.android.lolvideo.ui.base.BaseFragment;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;
import org.wdd.app.android.lolvideo.utils.AppUtils;

import java.util.List;

/**
 * Created by richard on 8/30/17.
 */

public class VideoCategoryFragment extends BaseFragment {

    private View mRootView;
    private LinearLayout mContainer;
    private LayoutInflater mInflater;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_video_category, container, false);
            initViews();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void initViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusHeight = AppUtils.getStatusHeight(getActivity());
            View statusBar = mRootView.findViewById(R.id.fragment_video_category_statusbar);
            statusBar.setVisibility(View.VISIBLE);
            statusBar.getLayoutParams().height = statusHeight;
        }

        mContainer = mRootView.findViewById(R.id.fragment_video_category_container);
        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected void lazyLoad() {
        View headerView;
        View dividerView;
        List<HtmlHref> data;
        TagFlowLayout flowLayout;
        LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        dlp.leftMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        dlp.rightMargin = dlp.leftMargin;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        HttpDataCache cache = new HttpDataCache(getContext());
        headerView = mInflater.inflate(R.layout.item_hot_video_header, null, false);
        ((TextView)headerView.findViewById(R.id.item_hot_video_header_title)).setText(R.string.news);
        headerView.findViewById(R.id.item_hot_video_header_more).setVisibility(View.GONE);
        mContainer.addView(headerView, lp);
        dividerView = new View(getContext());
        dividerView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        mContainer.addView(dividerView, dlp);
        data = cache.getNews();
        if (data.size() > 0) {
            flowLayout = new TagFlowLayout(getContext());
            flowLayout.setAdapter(new CategoryAdapter(data));
            mContainer.addView(flowLayout, lp);
        }

        headerView = mInflater.inflate(R.layout.item_hot_video_header, null, false);
        ((TextView)headerView.findViewById(R.id.item_hot_video_header_title)).setText(R.string.video_category);
        headerView.findViewById(R.id.item_hot_video_header_more).setVisibility(View.GONE);
        mContainer.addView(headerView, lp);
        dividerView = new View(getContext());
        dividerView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        mContainer.addView(dividerView, dlp);
        data = cache.getVideoCategories();
        if (data.size() > 0) {
            flowLayout = new TagFlowLayout(getContext());
            flowLayout.setAdapter(new CategoryAdapter(data));
            mContainer.addView(flowLayout, lp);
        }

        headerView = mInflater.inflate(R.layout.item_hot_video_header, null, false);
        ((TextView)headerView.findViewById(R.id.item_hot_video_header_title)).setText(R.string.mumu_sole);
        headerView.findViewById(R.id.item_hot_video_header_more).setVisibility(View.GONE);
        mContainer.addView(headerView, lp);
        dividerView = new View(getContext());
        dividerView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        mContainer.addView(dividerView, dlp);
        data = cache.getMumuSoles();
        if (data.size() > 0) {
            flowLayout = new TagFlowLayout(getContext());
            flowLayout.setAdapter(new CategoryAdapter(data));
            mContainer.addView(flowLayout, lp);
        }

        headerView = mInflater.inflate(R.layout.item_hot_video_header, null, false);
        ((TextView)headerView.findViewById(R.id.item_hot_video_header_title)).setText(R.string.commentary);
        headerView.findViewById(R.id.item_hot_video_header_more).setVisibility(View.GONE);
        mContainer.addView(headerView, lp);
        dividerView = new View(getContext());
        dividerView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        mContainer.addView(dividerView, dlp);
        data = cache.getCommentaries();
        if (data.size() > 0) {
            flowLayout = new TagFlowLayout(getContext());
            flowLayout.setAdapter(new CategoryAdapter(data));
            mContainer.addView(flowLayout, lp);
        }

        headerView = mInflater.inflate(R.layout.item_hot_video_header, null, false);
        ((TextView)headerView.findViewById(R.id.item_hot_video_header_title)).setText(R.string.killer);
        headerView.findViewById(R.id.item_hot_video_header_more).setVisibility(View.GONE);
        mContainer.addView(headerView, lp);
        dividerView = new View(getContext());
        dividerView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        mContainer.addView(dividerView, dlp);
        data = cache.getKillers();
        if (data.size() > 0) {
            flowLayout = new TagFlowLayout(getContext());
            flowLayout.setAdapter(new CategoryAdapter(data));
            mContainer.addView(flowLayout, lp);
        }

        headerView = mInflater.inflate(R.layout.item_hot_video_header, null, false);
        ((TextView)headerView.findViewById(R.id.item_hot_video_header_title)).setText(R.string.matches);
        headerView.findViewById(R.id.item_hot_video_header_more).setVisibility(View.GONE);
        mContainer.addView(headerView, lp);
        dividerView = new View(getContext());
        dividerView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        mContainer.addView(dividerView, dlp);
        data = cache.getMatches();
        if (data.size() > 0) {
            flowLayout = new TagFlowLayout(getContext());
            flowLayout.setAdapter(new CategoryAdapter(data));
            mContainer.addView(flowLayout, lp);
        }

        headerView = mInflater.inflate(R.layout.item_hot_video_header, null, false);
        ((TextView)headerView.findViewById(R.id.item_hot_video_header_title)).setText(R.string.columns);
        headerView.findViewById(R.id.item_hot_video_header_more).setVisibility(View.GONE);
        mContainer.addView(headerView, lp);
        dividerView = new View(getContext());
        dividerView.setBackgroundColor(Color.parseColor("#CCCCCC"));
        mContainer.addView(dividerView, dlp);
        data = cache.getColumns();
        if (data.size() > 0) {
            flowLayout = new TagFlowLayout(getContext());
            flowLayout.setAdapter(new CategoryAdapter(data));
            mContainer.addView(flowLayout, lp);
        }
    }

    private class CategoryAdapter extends TagAdapter<HtmlHref> {

        public CategoryAdapter(List<HtmlHref> datas) {
            super(datas);
        }

        @Override
        public View getView(FlowLayout parent, int position, final HtmlHref htmlHref) {
            View view = mInflater.inflate(R.layout.item_video_category, parent, false);
            TextView textView = view.findViewById(R.id.item_video_category_name);
            textView.setText(htmlHref.name);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            return view;
        }
    }
}
