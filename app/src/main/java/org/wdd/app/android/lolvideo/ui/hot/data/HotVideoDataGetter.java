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
import org.wdd.app.android.lolvideo.ui.hot.model.HotVideo;
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
        HttpRequestEntry requestEntry = new HttpRequestEntry();
        requestEntry.setMethod(HttpRequestEntry.Method.GET);
        requestEntry.setUrl(ServerApis.BASE_URL);
        mHttpManager.sendHtmlRequest("GB2312", host, requestEntry, new HttpConnectCallback() {
            @Override
            public void onRequestOk(HttpResponseEntry res) {
                Document document = (Document) res.getData();
                List<HotCategory> cates = null;

                Elements nodes;
                Element node;
                HotCategory category;
                HotVideo video;
                Elements titleNodes;

                nodes = document.getElementsByAttributeValue("class", "news_item");
                if (nodes.size() > 0) {
                    node = nodes.first();
                    titleNodes = node.getElementsByAttributeValue("class", "item_title");
                    if (titleNodes.size() > 0) {
                        Element titleNode = titleNodes.first();
                        category = new HotCategory();
                        category.type = HotCategory.HotType.NEWS;
                        category.name = titleNode.getElementsByTag("h3").first().text();
                        category.url = titleNode.getElementsByTag("a").first().attr("href");

                        Elements itemsNodes = node.getElementsByAttributeValue("class", "item_list");
                        if (itemsNodes.size() > 0) {
                            category.data = new ArrayList<>();
                            Element itemsNode = itemsNodes.first();
                            Elements newsNodes = itemsNode.getElementsByAttributeValue("class", "hot");
                            if (newsNodes.size() > 0) {
                                Element newsNode = newsNodes.first();
                                video = new HotVideo();
                                video.img = newsNode.getElementsByTag("img").first().attr("src");
                                Element aNode = newsNode.getElementsByTag("a").last();
                                video.url = aNode.attr("href");
                                video.title = aNode.getElementsByTag("h3").first().text();
                                video.desc = aNode.getElementsByTag("p").first().text();
                                category.data.add(video);

                                newsNodes = itemsNode.getElementsByTag("li");
                                if (newsNodes.size() > 0) {
                                    String content;
                                    for (int i = 0; i < newsNodes.size(); i++) {
                                        newsNode = newsNodes.get(i);
                                        video = new HotVideo();
                                        aNode = newsNode.getElementsByTag("a").first();
                                        video.url = aNode.attr("href");
                                        content = aNode.html();
                                        if (content.contains("<span>")) {
                                            video.title = content;
                                        } else {
                                            video.title = content.substring(0, content.indexOf("<span>"));
                                            video.date = content.substring(content.indexOf("<span>") + 6);
                                        }
                                        category.data.add(video);
                                    }
                                }
                                cates.add(category);
                            }
                        }
                    }

                }
/*
				<div class="item_list">
					<ul>
					<li>
							<a href="/201708/53018.html">
								<div class="pic">
									<img src="http://www.lolshipin.com/uploads/allimg/170825/28-1FR50952310-L.jpg"  alt="Miss排位日记：中单暴力丽桑卓！无解双控冰封全局！"">
									<span>2017-08-25</span>
								</div>
								<div class="text">
									<p>Miss排位日记：中单暴力丽桑卓！无解双控冰封全局！</p>
								</div>
							</a>
						</li>

 */
                nodes = document.getElementsByAttributeValue("class", "video_item");
                if (nodes.size() > 0) {
                    for (int i = 0; i < nodes.size(); i++) {
                        node = nodes.get(i);
                        titleNodes = node.getElementsByAttributeValue("class", "item_title");
                        if (titleNodes.size() > 0) {
                            Element titleNode = titleNodes.first();
                            category = new HotCategory();
                            category.type = HotCategory.HotType.VIDEO;
                            category.name = titleNode.getElementsByTag("h3").first().text();
                            category.url = titleNode.getElementsByTag("a").first().attr("href");

                            Elements itemsNodes = node.getElementsByAttributeValue("class", "item_list");
                            if (itemsNodes.size() > 0) {
                                category.data = new ArrayList<>();
                                Element itemsNode = itemsNodes.first();
                                Elements newsNodes = itemsNode.getElementsByAttributeValue("class", "hot");

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
