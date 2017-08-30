package org.wdd.app.android.lolvideo.ui.main.data;

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
import java.util.List;

/**
 * Created by richard on 8/25/17.
 */

public class MainDataGetter {

    private Context mContext;
    private HttpManager mHttpManager;
    private HttpSession mSession;
    private DataCallback mCallback;

    public MainDataGetter(Context context, DataCallback callback) {
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
        final HttpDataCache cache = new HttpDataCache(mContext);
        if (cache.isMenuInited()) {
            mCallback.onRequestOk();
            return;
        }
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setMethod(HttpRequestEntry.Method.GET);
        requestEntry.setShouldCached(true);
        requestEntry.setUrl(ServerApis.BASE_URL);
        mHttpManager.sendHtmlRequest("GB2312", host, requestEntry, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                Document document = (Document) res.getData();
                List<HtmlHref> tools = null;
                List<HtmlHref> videoCategories = null;
                List<HtmlHref> mumuSoles = null;
                List<HtmlHref> commentaries = null;
                List<HtmlHref> killers = null;
                List<HtmlHref> matches = null;
                List<HtmlHref> columns = null;
                List<HtmlHref> news = null;

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

                aNodes = nodes.first().getElementsByTag("a");
                tools = new ArrayList<>();
                for (int i = 0; i < aNodes.size(); i++) {
                    node = aNodes.get(i);
                    tools.add(new HtmlHref(node.text(), node.attr("href")));
                }
                cache.saveToolMenu(tools);

                aNodes = nodes.get(1).getElementsByTag("a");
                videoCategories = new ArrayList<>();
                for (int i = 0; i < aNodes.size(); i++) {
                    node = aNodes.get(i);
                    videoCategories.add(new HtmlHref(node.text(), node.attr("href")));
                }
                cache.saveVideoCategories(videoCategories);

                aNodes = nodes.get(2).getElementsByTag("a");
                mumuSoles = new ArrayList<>();
                for (int i = 0; i < aNodes.size(); i++) {
                    node = aNodes.get(i);
                    mumuSoles.add(new HtmlHref(node.text(), node.attr("href")));
                }
                cache.saveMumuSoles(mumuSoles);

                aNodes = nodes.get(3).getElementsByTag("a");
                commentaries = new ArrayList<>();
                for (int i = 0; i < aNodes.size(); i++) {
                    node = aNodes.get(i);
                    commentaries.add(new HtmlHref(node.text(), node.attr("href")));
                }
                cache.saveCommentaries(commentaries);

                aNodes = nodes.get(4).getElementsByTag("a");
                killers = new ArrayList<>();
                for (int i = 0; i < aNodes.size(); i++) {
                    node = aNodes.get(i);
                    killers.add(new HtmlHref(node.text(), node.attr("href")));
                }
                cache.saveKillers(killers);

                aNodes = nodes.get(4).getElementsByTag("a");
                matches = new ArrayList<>();
                for (int i = 0; i < aNodes.size(); i++) {
                    node = aNodes.get(i);
                    matches.add(new HtmlHref(node.text(), node.attr("href")));
                }
                cache.saveMatches(matches);

                aNodes = nodes.get(5).getElementsByTag("a");
                columns = new ArrayList<>();
                for (int i = 0; i < aNodes.size(); i++) {
                    node = aNodes.get(i);
                    columns.add(new HtmlHref(node.text(), node.attr("href")));
                }
                cache.saveColumns(columns);

                aNodes = nodes.last().getElementsByTag("a");
                news = new ArrayList<>();
                for (int i = 0; i < aNodes.size(); i++) {
                    node = aNodes.get(i);
                    news.add(new HtmlHref(node.text(), node.attr("href")));
                }
                cache.saveNews(news);

                cache.initMenu();
                mCallback.onRequestOk();
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

        void onRequestOk();
        void onRequestError(String error);
        void onNetworkError();

    }
}
