package org.wdd.app.android.lolvideo.ui.hot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.lolvideo.ui.hot.model.HotCategory;
import org.wdd.app.android.lolvideo.ui.hot.model.HotVideo;
import org.wdd.app.android.lolvideo.utils.AppUtils;
import org.wdd.app.android.lolvideo.views.NetworkImageView;

import java.util.ArrayList;
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
    private int mItemWidth;
    private int mItemHeight;

    public HotVideoAdapter(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        int margin = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        mItemWidth = (int) ((AppUtils.getScreenWidth(context) - margin * 3) / 2f);
        mItemHeight = (int) (mItemWidth * 0.7f);
    }

    public void refreshData(List<HotCategory> cates) {
        if (data == null) {
            data = new ArrayList<>();
        } else {
            data.clear();
        }
        HotItem item;
        HotVideo video;
        for (HotCategory cate : cates) {
            if (cate.type == HotCategory.HotType.NEWS) {
                item = new HotItem();
                item.type = TYPE_HEADER;
                item.data = new ArrayList<>();
                video = new HotVideo();
                video.url = cate.url;
                video.title = cate.name;
                item.data.add(video);
                data.add(item);

                for (HotVideo v : cate.data) {
                    item = new HotItem();
                    item.type = TextUtils.isEmpty(v.img) ? TYPE_NEWS_NORMAL : TYPE_NEWS_HOT;
                    item.data = new ArrayList<>();
                    item.data.add(v);
                    data.add(item);
                }
            } else {
                item = new HotItem();
                item.type = TYPE_HEADER;
                item.data = new ArrayList<>();
                video = new HotVideo();
                video.url = cate.url;
                video.title = cate.name;
                item.data.add(video);
                data.add(item);

                int count = cate.data.size() / 2;
                for (int i = 0; i < count; i++) {
                    item = new HotItem();
                    item.type = TYPE_VIDEO;
                    item.data = new ArrayList<>();
                    item.data.add(cate.data.get(i));
                    item.data.add(cate.data.get(i + 1));
                    data.add(item);
                }
                if (cate.data.size() % 2 != 0) {
                    item = new HotItem();
                    item.type = TYPE_VIDEO;
                    item.data = new ArrayList<>();
                    item.data.add(cate.data.get(cate.data.size() - 1));
                    data.add(item);
                }
            }
        }
        notifyItemChanged(0, data.size());
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
        List<HotVideo> data;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView titleView;
        View clickView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.item_hot_video_header_title);
            clickView = itemView.findViewById(R.id.item_hot_video_header_more);
        }

        public void bindData(HotItem item) {
            final HotVideo video = item.data.get(0);
            titleView.setText(video.title);
            clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }

    private class HotNewsViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        NetworkImageView imageView;
        TextView titleView;
        TextView descView;

        public HotNewsViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_hot_video_hot_news_click);
            imageView = itemView.findViewById(R.id.item_hot_video_hot_news_img);
            titleView = itemView.findViewById(R.id.item_hot_video_hot_news_title);
            descView = itemView.findViewById(R.id.item_hot_video_hot_news_desc);
        }

        public void bindData(HotItem item) {
            final HotVideo video = item.data.get(0);
            imageView.setImageUrl(video.img);
            titleView.setText(video.title);
            descView.setText(video.desc);
            clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }

    private class NormalNewsViewHolder extends RecyclerView.ViewHolder {

        View clickView;
        TextView titleView;
        TextView dateView;

        public NormalNewsViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_hot_video_normal_news_click);
            titleView = itemView.findViewById(R.id.item_hot_video_normal_news_title);
            dateView = itemView.findViewById(R.id.item_hot_video_normal_news_date);
        }

        public void bindData(HotItem item) {
            final HotVideo video = item.data.get(0);
            titleView.setText(video.title);
            dateView.setText(video.date);
            clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

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

        public void bindData(HotItem item) {
            List<HotVideo> items = item.data;

            HotVideo video = item.data.get(0);
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
