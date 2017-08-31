package org.wdd.app.android.lolvideo.ui.hot.data;

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
import org.wdd.app.android.lolvideo.ui.base.ActivityFragmentAvaliable;
import org.wdd.app.android.lolvideo.ui.hot.model.HotCategory;
import org.wdd.app.android.lolvideo.ui.hot.model.Video;
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

    public void requestHotVideo(final ActivityFragmentAvaliable host) {
        if (mSession != null) {
            mSession.cancelRequest();
            mSession = null;
        }
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setMethod(HttpRequestEntry.Method.GET);
        requestEntry.setUrl(ServerApis.BASE_URL);
        requestEntry.setShouldCached(false);
        mHttpManager.sendHtmlRequest("GB2312", host, requestEntry, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                mSession = null;

                Document document = (Document) res.getData();
                List<HotCategory> cates = new ArrayList<>();

                Elements nodes;
                Element node;
                HotCategory category;
                Video video;
                Elements titleNodes;
                Element titleNode;
                Element aNode;
                Elements itemsNodes;
                Element itemsNode;
                Elements videoNodes;
                Element videoNode;

                nodes = document.getElementsByAttributeValue("class", "news_item");
                if (nodes.size() > 0) {
                    node = nodes.first();
                    titleNodes = node.getElementsByAttributeValue("class", "item_title");
                    if (titleNodes.size() > 0) {
                        titleNode = titleNodes.first();
                        category = new HotCategory();
                        category.type = HotCategory.HotType.NEWS;
                        category.name = titleNode.getElementsByTag("h3").first().text();
                        category.url = titleNode.getElementsByTag("a").first().attr("href");

                        itemsNodes = node.getElementsByAttributeValue("class", "item_list");
                        if (itemsNodes.size() > 0) {
                            category.data = new ArrayList<>();
                            itemsNode = itemsNodes.first();
                            videoNodes = itemsNode.getElementsByAttributeValue("class", "hot");
                            if (videoNodes.size() > 0) {
                                videoNode = videoNodes.first();
                                video = new Video();
                                video.img = videoNode.getElementsByTag("img").first().attr("src");
                                aNode = videoNode.getElementsByTag("a").last();
                                video.url = aNode.attr("href");
                                video.title = aNode.getElementsByTag("h3").first().text();
                                video.desc = aNode.getElementsByTag("p").first().text();
                                category.data.add(video);

                                videoNodes = itemsNode.getElementsByTag("li");
                                if (videoNodes.size() > 0) {
                                    String content;
                                    for (int i = 0; i < videoNodes.size(); i++) {
                                        videoNode = videoNodes.get(i);
                                        video = new Video();
                                        aNode = videoNode.getElementsByTag("a").first();
                                        video.url = aNode.attr("href");
                                        content = aNode.html();
                                        if (content.contains("<span>")) {
                                            video.title = content.substring(0, content.indexOf("<span>"));
                                            video.date = content.substring(content.indexOf("<span>") + 6, content.indexOf("</span>"));
                                        } else {
                                            video.title = content;
                                        }
                                        category.data.add(video);
                                    }
                                }
                                cates.add(category);
                            }
                        }
                    }

                }

                nodes = document.getElementsByAttributeValue("class", "video_item");
                if (nodes.size() > 0) {
                    for (int i = 0; i < nodes.size(); i++) {
                        node = nodes.get(i);
                        titleNodes = node.getElementsByAttributeValue("class", "item_title");
                        if (titleNodes.size() > 0) {
                            titleNode = titleNodes.first();
                            category = new HotCategory();
                            category.type = HotCategory.HotType.VIDEO;
                            category.name = titleNode.getElementsByTag("h3").first().text();
                            category.url = titleNode.getElementsByTag("a").first().attr("href");

                            itemsNodes = node.getElementsByAttributeValue("class", "item_list");
                            if (itemsNodes.size() > 0) {
                                videoNodes = itemsNodes.first().getElementsByTag("li");
                                if (videoNodes.size() > 0) {
                                    category.data = new ArrayList<>();
                                    for (int j = 0; j < videoNodes.size(); j++) {
                                        videoNode = videoNodes.get(j);
                                        video = new Video();
                                        video.url = videoNode.getElementsByTag("a").first().attr("href");
                                        video.img = videoNode.getElementsByTag("img").first().attr("src");
                                        video.date = videoNode.getElementsByTag("span").first().text();
                                        video.title = videoNode.getElementsByTag("p").first().text();
                                        category.data.add(video);
                                    }
                                }
                            }
                            cates.add(category);
                        }
                    }
                }
                mCallback.onRequestOk(cates);
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

        void onRequestOk(List<HotCategory> cates);
        void onRequestError(String error);
        void onNetworkError();

    }
}
