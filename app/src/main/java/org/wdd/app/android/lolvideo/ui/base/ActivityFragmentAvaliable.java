package org.wdd.app.android.lolvideo.ui.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

import org.wdd.app.android.lolvideo.utils.AppUtils;

/**
 * Created by richard on 1/20/17.
 */

public class ActivityFragmentAvaliable {

    private Activity activity;
    private Fragment fragment;

    public ActivityFragmentAvaliable(Activity activity) {
        this.activity = activity;
    }

    public ActivityFragmentAvaliable(Fragment fragment) {
        this.fragment = fragment;
    }

    public boolean isAvaliable() {
        if (activity != null) return AppUtils.isActivityAvaliable(activity);
        if (fragment != null) return AppUtils.isFragmentAvaliable(fragment);
        return false;
    }
}
