package org.wdd.app.android.lolvideo.ui.detail.data;

import android.content.Context;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.wdd.app.android.lolvideo.R;
import org.wdd.app.android.lolvideo.http.HttpConnectCallback;
import org.wdd.app.android.lolvideo.http.HttpManager;
import org.wdd.app.android.lolvideo.http.HttpRequestEntry;
import org.wdd.app.android.lolvideo.http.HttpResponseEntry;
import org.wdd.app.android.lolvideo.http.HttpSession;
import org.wdd.app.android.lolvideo.http.error.ErrorCode;
import org.wdd.app.android.lolvideo.http.error.HttpError;
import org.wdd.app.android.lolvideo.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;
import org.wdd.app.android.lolvideo.utils.HttpUtils;
import org.wdd.app.android.lolvideo.utils.ServerApis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 8/25/17.
 */

public class VideoDeatilDataGetter {

    private Context mContext;
    private HttpManager mHttpManager;
    private HttpSession mSession;
    private DataCallback mCallback;

    public VideoDeatilDataGetter(Context context, DataCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
        mHttpManager = HttpManager.getInstance(context);
    }

    public void cancelSession() {
        if (mSession != null) {
            mSession.cancelRequest();
            mSession = null;
        }
    }

    public void requestVideoDetail(String url, ActivityFragmentAvaliable host) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setMethod(HttpRequestEntry.Method.GET);
        requestEntry.setUrl(ServerApis.BASE_URL + url);
        mHttpManager.sendHtmlRequest("GB2312", host, requestEntry, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                Document document = (Document) res.getData();

                Video video = null;
                List<Video> videos = null;

                Elements nodes;
                Element node;

                nodes = document.getElementsByAttributeValue("class", "deta_video");
                if (nodes.size() == 0) {
                    mCallback.onRequestError(mContext.getString(R.string.parse_error));
                    return;
                }
                node = nodes.first();
                video = new Video();
                video.url = node.getElementsByTag("iframe").first().attr("src");
                video.url = video.url.substring(video.url.lastIndexOf('/') + 1);
                node = node.getElementsByAttributeValue("class", "deta_title").first();
                video.title = node.getElementsByTag("h1").first().text();
                video.date = node.getElementsByAttributeValue("class", "writer").first().text();

                nodes = document.getElementsByAttributeValue("class", "item_list");
                if (nodes.size() > 0) {
                    nodes = nodes.first().getElementsByTag("li");
                    if (nodes.size() > 0) {
                        videos = new ArrayList<>();
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

                mCallback.onRequestOk(video, videos);
            }

            @Override
            public void onRequestFailure(HttpError error) {
                mSession = null;
                if (error.getErrorCode() == ErrorCode.NO_CONNECTION_ERROR) {
                    mCallback.onNetworkError();
                } else {
                    mCallback.onRequestError(HttpUtils.getErrorDesc(mContext, error.getErrorCode()));
                }
            }
        });
    }

    public interface DataCallback {

        void onRequestOk(Video video, List<Video> videos);
        void onRequestError(String error);
        void onNetworkError();

    }
}
