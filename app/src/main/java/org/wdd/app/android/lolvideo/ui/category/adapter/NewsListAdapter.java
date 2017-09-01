package org.wdd.app.android.lolvideo.ui.category.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;

import java.util.List;

/**
 * Created by richard on 9/1/17.
 */

public class NewsListAdapter extends AbstractCommonAdapter<Video> {

    private OnNewsClickedListener mListener;
    private LayoutInflater mInflater;

    public NewsListAdapter(Context context, List<Video> data) {
        super(context, data);
        mInflater = LayoutInflater.from(context);
    }

    public void refreshData(List<Video> videos) {
        data.clear();
        data.addAll(videos);
        notifyDataSetChanged();
    }

    public void appendVideos(List<Video> videos) {
        int startIndex = data.size();
        data.addAll(videos);
        notifyItemRangeChanged(startIndex, videos.size());
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        return createNewsHolder(parent);
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, Video item, int position) {
        ((NewsViewHolder)holder).bindData(item);
    }

    private RecyclerView.ViewHolder createNewsHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_hot_video_normal_news, parent, false);
        NewsViewHolder holder = new NewsViewHolder(view);
        return holder;
    }

    public void setOnNewsClickedListener(OnNewsClickedListener listener) {
        this.mListener = listener;
    }

    private class NewsViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        TextView titleView;
        TextView dateView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_hot_video_normal_news_click);
            titleView = itemView.findViewById(R.id.item_hot_video_normal_news_title);
            dateView = itemView.findViewById(R.id.item_hot_video_normal_news_date);
        }

        public void bindData(final Video video) {
            titleView.setText(video.title);
            dateView.setText(video.date);
            clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener == null) return;
                    mListener.onNewsClicked(video);
                }
            });
        }

    }

    public interface OnNewsClickedListener {

        void onNewsClicked(Video video);

    }
}
