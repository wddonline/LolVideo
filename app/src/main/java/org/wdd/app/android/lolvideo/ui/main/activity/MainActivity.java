package org.wdd.app.android.lolvideo.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.app.LolApplication;
import org.wdd.app.android.lolvideo.ui.base.BaseActivity;
import org.wdd.app.android.lolvideo.ui.category.fragment.VideoCategoryFragment;
import org.wdd.app.android.lolvideo.ui.hot.fragment.HotVideoFragment;
import org.wdd.app.android.lolvideo.ui.matches.fragment.MatchesFragment;
import org.wdd.app.android.lolvideo.ui.me.fragment.MeFragment;
import org.wdd.app.android.lolvideo.utils.AppToaster;
import org.wdd.app.android.lolvideo.utils.AppUtils;
import org.wdd.app.android.lolvideo.views.FragmentTabHost;

public class MainActivity extends BaseActivity implements Runnable {

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    private FragmentTabHost mTabHost;

    private Handler handler = new Handler();

    private final long TIME_LIMIT = 3000;
    private int backPressedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUtils.setImmersiveStatusBar(this);
        MobclickAgent.openActivityDurationTrack(false);
        initViews();
//        BmobUtils.autoUpdateApp(this);
    }



    private void initViews() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        View tabLayout;
        ImageView tabImgView;
        TextView tabTxtView;

        int[] tabIcons;
        int[] tabTxts;
        String[] tabTags;
        Class[] tabClasses;

        tabIcons = new int[]{R.drawable.ic_today_focus, R.drawable.ic_category, R.drawable.ic_matches, R.drawable.ic_me};
        tabTxts = new int[]{R.string.today_focus, R.string.category, R.string.matches, R.string.me};
        tabTags = new String[]{"today_focus", "category", "matches", "me"};
        tabClasses = new Class[]{HotVideoFragment.class, VideoCategoryFragment.class, MatchesFragment.class, MeFragment.class};

        int tabCount = tabIcons.length;

        for (int i = 0; i < tabCount; i++) {
            tabLayout = getLayoutInflater().inflate(R.layout.layout_main_tab, null, false);
            tabImgView = tabLayout.findViewById(R.id.layout_main_tab_img);
            tabTxtView = tabLayout.findViewById(R.id.layout_main_tab_txt);
            tabTxtView.setText(tabTxts[i]);
            tabImgView.setImageResource(tabIcons[i]);
            mTabHost.addTab(mTabHost.newTabSpec(tabTags[i]).setIndicator(tabLayout), tabClasses[i], null);
        }

        int tabWidth = getResources().getDisplayMetrics().widthPixels / 4;
        TabWidget tabWidget = mTabHost.getTabWidget();
        tabWidget.setBackgroundColor(ActivityCompat.getColor(this, R.color.colorNavigationBar));
        for (int i = 0; i < tabCount; i++) {
            tabWidget.getChildTabViewAt(i).getLayoutParams().width = tabWidth;
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedCount < 1) {
            handler.postDelayed(this, TIME_LIMIT);
            AppToaster.show(R.string.back_to_exit);
            backPressedCount++;
        } else {
            handler.removeCallbacks(this);
            LolApplication.getInstance().exitApp();
        }
    }

    @Override
    public void run() {
        backPressedCount = 0;
    }

}
