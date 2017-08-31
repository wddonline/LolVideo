package org.wdd.app.android.lolvideo.ui.matches.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;
import org.wdd.app.android.lolvideo.utils.AppUtils;
import org.wdd.app.android.lolvideo.views.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 8/25/17.
 */

public class MatchVideoAdapter extends AbstractCommonAdapter<MatchVideoAdapter.MatchItem> {

    private LayoutInflater mInflater;
    private int mItemWidth;
    private int mItemHeight;

    public MatchVideoAdapter(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        int margin = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        mItemWidth = (int) ((AppUtils.getScreenWidth(context) - margin * 3) / 2f);
        mItemHeight = (int) (mItemWidth * 0.7f);
    }

    public void refreshData(List<Video> videos) {
        if (data == null) {
            data = new ArrayList<>();
        } else {
            data.clear();
        }
        MatchItem item;
        int count = videos.size() / 2;
        for (int i = 0; i < count; i++) {
            item = new MatchItem();
            item.data = new ArrayList<>();
            item.data.add(videos.get(i * 2));
            item.data.add(videos.get(i * 2 + 1));
            data.add(item);
        }
        if (videos.size() % 2 != 0) {
            item = new MatchItem();
            item.data = new ArrayList<>();
            item.data.add(videos.get(videos.size() - 1));
            data.add(item);
        }

        notifyDataSetChanged();
    }

    public void appendVideos(List<Video> videos) {
        int startIndex = data.size() - 1;
        MatchItem item;
        int count = videos.size() / 2;
        for (int i = 0; i < count; i++) {
            item = new MatchItem();
            item.data = new ArrayList<>();
            item.data.add(videos.get(i * 2));
            item.data.add(videos.get(i * 2 + 1));
            data.add(item);
        }
        if (videos.size() % 2 != 0) {
            item = new MatchItem();
            item.data = new ArrayList<>();
            item.data.add(videos.get(videos.size() - 1));
            data.add(item);
        }

        notifyItemChanged(startIndex, videos.size());
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = createVideoHolder(parent);
        return holder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, MatchItem item, int position) {
        ((VideoViewHolder)holder).bindData(item);
    }

    private RecyclerView.ViewHolder createVideoHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_hot_video_video, parent, false);
        VideoViewHolder holder = new VideoViewHolder(view);
        return holder;
    }

    class MatchItem {
        List<Video> data;
    }

    private class VideoViewHolder extends RecyclerView.ViewHolder {

        View[] clickViews;
        NetworkImageView[] imageViews;
        TextView[] titleViews;
        TextView[] dateViews;

        public VideoViewHolder(View itemView) {
            super(itemView);
            clickViews = new View[2];
            imageViews = new NetworkImageView[2];
            titleViews = new TextView[2];
            dateViews = new TextView[2];

            clickViews[0] = itemView.findViewById(R.id.item_hot_video_video_click1);
            imageViews[0] = itemView.findViewById(R.id.item_hot_video_video_img1);
            titleViews[0] = itemView.findViewById(R.id.item_hot_video_video_name1);
            dateViews[0] = itemView.findViewById(R.id.item_hot_video_video_date1);

            clickViews[1] = itemView.findViewById(R.id.item_hot_video_video_click2);
            imageViews[1] = itemView.findViewById(R.id.item_hot_video_video_img2);
            titleViews[1] = itemView.findViewById(R.id.item_hot_video_video_name2);
            dateViews[1] = itemView.findViewById(R.id.item_hot_video_video_date2);
        }

        public void bindData(MatchItem item) {
            List<Video> items = item.data;

            Video video = item.data.get(0);
            imageViews[0].setImageUrl(video.img);
            titleViews[0].setText(video.title);
            dateViews[0].setText(video.date);
            clickViews[0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            imageViews[0].getLayoutParams().width = mItemWidth;
            imageViews[0].getLayoutParams().height = mItemHeight;

            if (items.size() > 1) {
                video = item.data.get(1);
                clickViews[1].setVisibility(View.VISIBLE);
                imageViews[1].setImageUrl(video.img);
                titleViews[1].setText(video.title);
                dateViews[1].setText(video.date);
                clickViews[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                imageViews[1].getLayoutParams().width = mItemWidth;
                imageViews[1].getLayoutParams().height = mItemHeight;
            } else {
                clickViews[1].setVisibility(View.GONE);
            }
        }

    }
}
