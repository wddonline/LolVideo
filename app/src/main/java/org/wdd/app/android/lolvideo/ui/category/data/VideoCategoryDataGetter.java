package org.wdd.app.android.lolvideo.ui.category.data;

import android.content.Context;

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
import org.wdd.app.android.lolvideo.preference.HttpDataCache;
import org.wdd.app.android.lolvideo.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;
import org.wdd.app.android.lolvideo.utils.HttpUtils;
import org.wdd.app.android.lolvideo.utils.ServerApis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by richard on 8/25/17.
 */

public class VideoCategoryDataGetter {

    private Context mContext;
    private HttpManager mHttpManager;
    private HttpSession mSession;
    private DataCallback mCallback;

    public VideoCategoryDataGetter(Context context, DataCallback callback) {
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

    public void requestLolVideoMenus(ActivityFragmentAvaliable host) {
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setMethod(HttpRequestEntry.Method.GET);
        requestEntry.setUrl(ServerApis.BASE_URL);
        mHttpManager.sendHtmlRequest("GB2312", host, requestEntry, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                Document document = (Document) res.getData();

                Map<String, List<HtmlHref>> data;

                List<HtmlHref> list;
                String name;
                Elements nodes;
                Element node;
                Elements aNodes;

                nodes = document.getElementsByAttributeValue("class", "menu_list");
                if (nodes == null || nodes.size() == 0) {
                    mCallback.onRequestError(HttpUtils.getErrorDesc(mContext, ErrorCode.PARSE_ERROR));
                    return;
                }
                nodes = nodes.first().getElementsByTag("li");
                if (nodes == null || nodes.size() == 0) {
                    mCallback.onRequestError(HttpUtils.getErrorDesc(mContext, ErrorCode.PARSE_ERROR));
                    return;
                }
                data = new LinkedHashMap<>();

                node = nodes.last();
                name = node.getElementsByTag("p").first().text();
                aNodes = node.getElementsByTag("a");
                list = new ArrayList<>();
                for (int i = 0; i < aNodes.size(); i++) {
                    node = aNodes.get(i);
                    list.add(new HtmlHref(node.text(), node.attr("href")));
                }
                data.put(name, list);

                node = nodes.first();
                aNodes = node.getElementsByTag("a");
                list = new ArrayList<>();
                for (int i = 0; i < aNodes.size(); i++) {
                    node = aNodes.get(i);
                    list.add(new HtmlHref(node.text(), node.attr("href")));
                }
                HttpDataCache cache = new HttpDataCache(mContext);
                cache.saveToolMenu(list);

                for (int i = 1; i < nodes.size() - 1; i++) {
                    node = nodes.get(i);
                    name = node.getElementsByTag("p").first().text();
                    aNodes = node.getElementsByTag("a");
                    list = new ArrayList<>();
                    for (int j = 0; j < aNodes.size(); j++) {
                        node = aNodes.get(j);
                        list.add(new HtmlHref(node.text(), node.attr("href")));
                    }
                    data.put(name, list);
                }

                mCallback.onRequestOk(data);
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

        void onRequestOk(Map<String, List<HtmlHref>> menus);
        void onRequestError(String error);
        void onNetworkError();

    }
}
