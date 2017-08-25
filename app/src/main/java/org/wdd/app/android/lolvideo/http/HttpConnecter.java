package org.wdd.app.android.lolvideo.http;

import org.wdd.app.android.lolvideo.ui.base.ActivityFragmentAvaliable;

/**
 * Created by wangdd on 16-11-26.
 */

public interface HttpConnecter {

    HttpSession sendImageRequest(ActivityFragmentAvaliable host, HttpRequestEntry entry, HttpConnectCallback callback);

    HttpSession sendHttpRequest(ActivityFragmentAvaliable host, HttpRequestEntry entry, Class clazz, HttpConnectCallback callback);

    HttpSession sendHttpRequest(ActivityFragmentAvaliable host, HttpRequestEntry entry, HttpConnectCallback callback);

    HttpSession sendHtmlRequest(ActivityFragmentAvaliable host, HttpRequestEntry entry, HttpConnectCallback callback);

    HttpSession sendHtmlRequest(String enocde, ActivityFragmentAvaliable host, HttpRequestEntry entry, HttpConnectCallback callback);

    void stopAllSession();

    void stopSessionByTag(String tag);
}
