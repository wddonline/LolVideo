package org.wdd.app.android.lolvideo.ui.me.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.ui.base.BaseFragment;
import org.wdd.app.android.lolvideo.views.RoundedNetworkImageView;

/**
 * Created by richard on 8/30/17.
 */

public class MeFragment extends BaseFragment {

    private View mRootView;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_me, container, false);
            initViews();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void initViews() {
        RoundedNetworkImageView headView = mRootView.findViewById(R.id.fragment_me_head);
        headView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.me_head_img));
    }

    @Override
    protected void lazyLoad() {

    }
}
