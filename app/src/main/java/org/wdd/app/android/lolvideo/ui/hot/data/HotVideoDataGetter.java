package org.wdd.app.android.lolvideo.ui.hot.data;

import android.content.Context;
import android.util.SparseArray;

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
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;
import org.wdd.app.android.lolvideo.ui.main.model.VideoItem;
import org.wdd.app.android.lolvideo.utils.HttpUtils;
import org.wdd.app.android.lolvideo.utils.ServerApis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 8/25/17.
 */

public class HotVideoDataGetter {

    private Context mContext;
    private HttpManager mHttpManager;
    private HttpSession mSession;
    private DataCallback mCallback;

    public HotVideoDataGetter(Context context, DataCallback callback) {
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

    public void requestHotVideo(ActivityFragmentAvaliable host) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setMethod(HttpRequestEntry.Method.GET);
        requestEntry.setUrl(ServerApis.BASE_URL);
        mHttpManager.sendHtmlRequest("GB2312", host, requestEntry, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                Document document = (Document) res.getData();

                List<String> titiles = null;
                List<HtmlHref> mores = null;
                SparseArray<List<VideoItem>> hots = null;

                Elements nodes;
                Element node;
                Elements aNodes;

                nodes = document.getElementsByAttributeValue("class", "menu_list");
                if (nodes == null || nodes.size() == 0) {
                    mCallback.onRequestError(HttpUtils.getErrorDesc(mContext, ErrorCode.PARSE_ERROR));
                    return;
                }
                node = nodes.first();
                nodes = node.getElementsByTag("li");
                if (nodes == null || nodes.size() == 0) {
                    mCallback.onRequestError(HttpUtils.getErrorDesc(mContext, ErrorCode.PARSE_ERROR));
                    return;
                }

                nodes = document.getElementsByTag("h3");
                if (nodes == null || nodes.size() == 0) {
                    mCallback.onRequestError(HttpUtils.getErrorDesc(mContext, ErrorCode.PARSE_ERROR));
                    return;
                }
                titiles = new ArrayList<>();
                for (int i = 0; i < nodes.size(); i++) {
                    titiles.add(nodes.get(i).text());
                }

                nodes = document.getElementsByAttributeValue("class", "right_more");
                if (nodes == null || nodes.size() == 0) {
                    mCallback.onRequestError(HttpUtils.getErrorDesc(mContext, ErrorCode.PARSE_ERROR));
                    return;
                }
                mores = new ArrayList<>();
                for (int i = 0; i < nodes.size(); i++) {
                    mores.add(new HtmlHref(nodes.get(i).getElementsByTag("a").attr("href")));
                }

                nodes = document.getElementsByAttributeValue("class", "item_list");
                if (nodes == null || nodes.size() == 0) {
                    mCallback.onRequestError(HttpUtils.getErrorDesc(mContext, ErrorCode.PARSE_ERROR));
                    return;
                }
                hots = new SparseArray<>();
                List<VideoItem> items;
                VideoItem item;
                Element aNode;
                for (int i = 0; i < nodes.size(); i++) {
                    node = nodes.get(i);
                    aNodes = node.getElementsByTag("li");
                    items = new ArrayList<>();
                    for (int j = 0; j < aNodes.size(); j++) {
                        aNode = aNodes.get(j);
                        item = new VideoItem();
                        item.url = aNode.getElementsByTag("a").attr("href");
                        item.img = aNode.getElementsByTag("img").attr("src");
                        item.date = aNode.getElementsByTag("span").text();
                        item.title = aNode.getElementsByTag("p").text();
                        items.add(item);
                    }
                    hots.put(i, items);
                }

                mCallback.onRequestOk(titiles, mores, hots);
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

        void onRequestOk(List<String> titiles, List<HtmlHref> mores, SparseArray<List<VideoItem>> hots);
        void onRequestError(String error);
        void onNetworkError();

    }
}
