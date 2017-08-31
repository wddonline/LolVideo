package org.wdd.app.android.lolvideo.ui.matches.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.ui.base.AbstractCommonAdapter;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 8/31/17.
 */

public class MatchMenuAdapter extends AbstractCommonAdapter<MatchMenuAdapter.MatchItem> {

    private int mLastSelectedIndex = 0;

    private View mLastNameView;
    private View mLastIconView;

    private OnMenuClickedListener mListener;

    public MatchMenuAdapter(Context context, List<HtmlHref> menus) {
        super(context);

        HtmlHref href = new HtmlHref();
        href.name = context.getString(R.string.all_videos);
        href.url = "/bisai";
        menus.add(0, href);

        data = new ArrayList<>();
        MatchItem matchItem;
        for (int i = 0; i < menus.size(); i++) {
            matchItem = new MatchItem();
            matchItem.isSelected = i == 0;
            matchItem.href = menus.get(i);
            data.add(matchItem);
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_match_menu, parent, false);
        MatchViewHolder viewHolder = new MatchViewHolder(itemView);
        return viewHolder;
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, MatchItem item, int position) {
        MatchViewHolder viewHolder = (MatchViewHolder) holder;
        viewHolder.bindData(item, position);
    }

    public void setOnMenuClickedListener(OnMenuClickedListener listener) {
        this.mListener = listener;
    }

    class MatchItem {
        boolean isSelected;
        HtmlHref href;
    }

    private class MatchViewHolder extends RecyclerView.ViewHolder {

        private View clickView;
        private TextView nameView;
        private ImageView iconView;

        public MatchViewHolder(View itemView) {
            super(itemView);
            clickView = itemView.findViewById(R.id.item_match_menu_click);
            nameView = itemView.findViewById(R.id.item_match_menu_name);
            iconView = itemView.findViewById(R.id.item_match_menu_icon);
        }

        public void bindData(final MatchItem item, final int position) {
            nameView.setText(item.href.name);
            if (item.isSelected) {
                mLastNameView = nameView;
                mLastIconView = iconView;
            }
            nameView.setSelected(item.isSelected);
            iconView.setVisibility(item.isSelected ? View.VISIBLE : View.INVISIBLE);
            clickView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.isSelected) return;
                    item.isSelected = true;
                    data.get(mLastSelectedIndex).isSelected = false;
                    mLastNameView.setSelected(false);
                    mLastIconView.setVisibility(View.INVISIBLE);
                    notifyItemChanged(mLastSelectedIndex);
                    notifyItemChanged(position);
                    mLastSelectedIndex = position;
                    if (mListener != null) {
                        mListener.onMenuClicked(item.href);
                    }
                }
            });
        }
    }

    public interface OnMenuClickedListener {
        void onMenuClicked(HtmlHref href);
    }
}
