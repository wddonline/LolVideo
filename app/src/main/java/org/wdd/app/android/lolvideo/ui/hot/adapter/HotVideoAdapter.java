package org.wdd.app.android.lolvideo.ui.hot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.lolvideo.ui.main.model.VideoItem;

import java.util.List;

/**
 * Created by richard on 8/25/17.
 */

public class HotVideoAdapter extends AbstractCommonAdapter<HotVideoAdapter.HotItem> {

    private final int TYPE_HEADER = 0;
    private final int TYPE_NEWS_HOT = 1;
    private final int TYPE_NEWS_NORMAL = 2;
    private final int TYPE_VIDEO = 3;

    private LayoutInflater mInflater;

    public HotVideoAdapter(Context context, List<HotItem> data) {
        super(context, data);
        mInflater = LayoutInflater.from(context);
        refreshData(data);
    }

    public void refreshData(List<HotItem> data) {

    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_HEADER:
                holder = createHeaderHolder(parent);
                break;
            case TYPE_NEWS_HOT:
                holder = createHotNewsHolder(parent);
                break;
            case TYPE_NEWS_NORMAL:
                holder = createNormalNewsHolder(parent);
                break;
            case TYPE_VIDEO:
                holder = createVideoHolder(parent);
                break;
        }
        return holder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, HotItem item, int position) {
        switch (item.type) {
            case TYPE_HEADER:
                ((HeaderViewHolder)holder).bindData(item);
                break;
            case TYPE_NEWS_HOT:
                ((HotNewsViewHolder)holder).bindData(item);
                break;
            case TYPE_NEWS_NORMAL:
                ((NormalNewsViewHolder)holder).bindData(item);
                break;
            case TYPE_VIDEO:
                ((VideoViewHolder)holder).bindData(item);
                break;
        }
    }

    private RecyclerView.ViewHolder createHeaderHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_hot_video_header, parent, false);
        HeaderViewHolder holder = new HeaderViewHolder(view);
        return holder;
    }

    private RecyclerView.ViewHolder createHotNewsHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_hot_video_hot_news, parent, false);
        HotNewsViewHolder holder = new HotNewsViewHolder(view);
        return holder;
    }

    private RecyclerView.ViewHolder createNormalNewsHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_hot_video_normal_news, parent, false);
        NormalNewsViewHolder holder = new NormalNewsViewHolder(view);
        return holder;
    }

    private RecyclerView.ViewHolder createVideoHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_hot_video_video, parent, false);
        VideoViewHolder holder = new VideoViewHolder(view);
        return holder;
    }

    @Override
    protected int getSubItemViewType(int position) {
        return data.get(position).type;
    }

    class HotItem {
        int type;
        List<VideoItem> data;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(HotItem item) {

        }

    }

    private class HotNewsViewHolder extends RecyclerView.ViewHolder {

        public HotNewsViewHolder(View itemView) {
            super(itemView);
        }

        public void bindViews() {

        }

        public void bindData(HotItem item) {

        }

    }

    private class NormalNewsViewHolder extends RecyclerView.ViewHolder {

        public NormalNewsViewHolder(View itemView) {
            super(itemView);
        }

        public void bindViews() {

        }

        public void bindData(HotItem item) {

        }

    }

    private class VideoViewHolder extends RecyclerView.ViewHolder {

        public VideoViewHolder(View itemView) {
            super(itemView);
        }

        public void bindViews() {

        }

        public void bindData(HotItem item) {

        }

    }
}
