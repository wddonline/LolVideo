package org.wdd.app.android.lolvideo.utils;

import android.widget.Toast;

import org.wdd.app.android.lolvideo.app.LolApplication;

/**
 * Created by wangdd on 16-11-27.
 */

public class AppToaster {

    public static void show(int res) {
        Toast.makeText(LolApplication.getInstance(), LolApplication.getInstance().getText(res), Toast.LENGTH_SHORT).show();
    }

    public static void show(String txt) {
        Toast.makeText(LolApplication.getInstance(), txt, Toast.LENGTH_SHORT).show();
    }
}
