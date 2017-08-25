package org.wdd.app.android.lolvideo.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;

import org.wdd.app.android.lolvideo.http.HttpManager;
import org.wdd.app.android.lolvideo.utils.BmobUtils;
import org.wdd.app.android.lolvideo.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdd on 16-11-27.
 */

public class LolApplication extends Application {

    private static LolApplication INSTANCE;

    public static LolApplication getInstance() {
        return INSTANCE;
    }

    private Handler uiHandler;
    private Map<String, Object> tempZone;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        tempZone = new HashMap<>();

        uiHandler = new Handler(Looper.getMainLooper());
        BmobUtils.initBombClient(this);
        //设置umeng统计场景
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setSessionContinueMillis(10 * 60 * 1000);

        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = Constants.DEBUG;
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(getBaseContext());
//    }

    public Handler getUiHandler() {
        return uiHandler;
    }

    public void putTempData(String key, Object data) {
        tempZone.put(key, data);
    }

    public Object getTempData(String key) {
        Object data = tempZone.get(key);
        tempZone.remove(key);
        return data;
    }

    public void exitApp() {
        MobclickAgent.onKillProcess(this);
        ActivityTaskStack.getInstance().clearActivities();
        HttpManager.getInstance(this).stopAllSession();
        Process.killProcess(Process.myPid());
    }

    {
        PlatformConfig.setWeixin("wx6938882f4e2d826e", "97c20682f3b4e807d1291fc47f0e147d");
        PlatformConfig.setSinaWeibo("1639325447", "c7e8cdd62fb00a8b802de8174c30f6ad","http://www.pgyer.com/pkf3");
        PlatformConfig.setQQZone("1105971926", "guZje5HyHBfP6jnO");
    }
}
