package org.wdd.app.android.lolvideo.ui.category.data;

import android.content.Context;
import android.text.TextUtils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.wdd.app.android.lolvideo.http.HttpConnectCallback;
import org.wdd.app.android.lolvideo.http.HttpManager;
import org.wdd.app.android.lolvideo.http.HttpRequestEntry;
import org.wdd.app.android.lolvideo.http.HttpResponseEntry;
import org.wdd.app.android.lolvideo.http.HttpSession;
import org.wdd.app.android.lolvideo.http.error.ErrorCode;
import org.wdd.app.android.lolvideo.http.error.HttpError;
import org.wdd.app.android.lolvideo.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;
import org.wdd.app.android.lolvideo.utils.HttpUtils;
import org.wdd.app.android.lolvideo.utils.ServerApis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 8/25/17.
 */

public class VideoListDataGetter {

    private Context mContext;
    private HttpManager mHttpManager;
    private HttpSession mSession;
    private DataCallback mCallback;

    private int mPage;
    private boolean inited = false;

    private String mUrl;
    private String mSubfix;

    public VideoListDataGetter(Context context, String url, DataCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
        if (url.contains("gaoshou")) {
            url = "/gaoshou/bubba/";
        }
        this.mUrl = ServerApis.BASE_URL + url;
        mHttpManager = HttpManager.getInstance(context);
    }

    public void cancelSession() {
        if (mSession != null) {
            mSession.cancelRequest();
            mSession = null;
        }
    }

    public void requestVideoList(boolean isAppend, ActivityFragmentAvaliable host) {
        doRequest(mUrl, isAppend, host);
    }

    public void requestVideoList(String url, final boolean isAppend, final ActivityFragmentAvaliable host) {
        this.mUrl = ServerApis.BASE_URL + url;
        doRequest(mUrl, isAppend, host);
    }

    private void doRequest(String url, final boolean isAppend, final ActivityFragmentAvaliable host) {
        if (mSession != null) {
            mSession.cancelRequest();
            mSession = null;
        }
        if (!isAppend) {
            mPage = 1;
        }
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setMethod(HttpRequestEntry.Method.GET);
        if (TextUtils.isEmpty(mSubfix)) {
            requestEntry.setUrl(url);
        } else {
            requestEntry.setUrl(url + mSubfix + mPage + ".html");
        }
        requestEntry.setShouldCached(false);
        mHttpManager.sendHtmlRequest("GB2312", host, requestEntry, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                mSession = null;

                Document document = (Document) res.getData();

                List<HtmlHref> menus = null;
                List<Video> videos = null;

                Elements nodes;
                Element node;

                if (!inited) {
                    nodes = document.getElementsByAttributeValue("class", "part_item");
                    if (nodes.size() > 0) {
                        nodes = nodes.first().getElementsByTag("a");
                        if (nodes.size() > 0) {
                            menus = new ArrayList<>();
                            HtmlHref href;
                            for (int i = 0; i < nodes.size(); i++) {
                                node = nodes.get(i);
                                href = new HtmlHref();
                                href.url = node.attr("href");
                                href.name = node.text();
                                menus.add(href);
                            }
                            inited = true;
                        }
                    }
                }

                nodes = document.getElementsByAttributeValue("class", "item_list");
                if (nodes.size() > 0) {
                    nodes = nodes.first().getElementsByTag("li");
                    if (nodes.size() > 0) {
                        videos = new ArrayList<>();
                        Video video;
                        for (int i = 0; i < nodes.size(); i++) {
                            node = nodes.get(i);
                            video = new Video();
                            video.url = node.getElementsByTag("a").first().attr("href");
                            video.img = node.getElementsByTag("img").first().attr("src");
                            video.date = node.getElementsByTag("span").first().text();
                            video.title = node.getElementsByTag("p").first().text();
                            videos.add(video);
                        }
                    }
                }

                boolean isLastPage = true;
                nodes = document.getElementsByAttributeValue("class", "pageNum");
                if (nodes.size() > 0) {
                    nodes = nodes.first().getElementsByTag("a");
                    if (nodes.size() > 0) {
                        node = nodes.last();
                        mSubfix = node.attr("href");
                        if (mSubfix.contains("_")) {
                            mSubfix = mSubfix.substring(0, mSubfix.lastIndexOf('_') + 1);
                        } else {
                            mSubfix = mSubfix.substring(0, mSubfix.lastIndexOf('-') + 1);
                        }
                        if (node.text().contains("下一页")) {
                            isLastPage = false;
                            mPage++;
                        }
                    }
                }
                mCallback.onRequestOk(isAppend, isLastPage, menus, videos);
            }

            @Override
            public void onRequestFailure(HttpError error) {
                mSession = null;
                if (error.getErrorCode() == ErrorCode.NO_CONNECTION_ERROR) {
                    mCallback.onNetworkError(isAppend);
                } else {
                    mCallback.onRequestError(isAppend, HttpUtils.getErrorDesc(mContext, error.getErrorCode()));
                }
            }
        });

    }

    public interface DataCallback {

        void onRequestOk(boolean isAppend, boolean isLastPage, List<HtmlHref> menus, List<Video> videos);
        void onRequestError(boolean isAppend, String error);
        void onNetworkError(boolean isAppend);

    }
}
