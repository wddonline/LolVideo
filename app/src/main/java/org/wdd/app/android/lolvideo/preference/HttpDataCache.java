package org.wdd.app.android.lolvideo.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.wdd.app.android.lolvideo.ui.main.model.HtmlHref;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by richard on 2/10/17.
 */

public class HttpDataCache {

    private final String KEY_INIT = "key_init";

    private final String KEY_TOOL_MENU = "key_tool_menu";
    private final String KEY_VIDEO_CATEGORIES_MENU = "key_video_categories_menu";
    private final String KEY_MUMU_SOLES_MENU = "key_mumu_soles_menu";
    private final String KEY_COMMENTARIES_MENU = "key_commentaries_menu";
    private final String KEY_KILLERS_MENU = "key_killers_menu";
    private final String KEY_MATCHES_MENU = "key_matches_menu";
    private final String KEY_COLUMNS_MENU = "key_columns_menu";
    private final String KEY_NEWS_MENU = "key_news_menu";

    private SharedPreferences mPref;

    public HttpDataCache(Context context) {
        mPref = context.getSharedPreferences("http_data", Context.MODE_PRIVATE);
    }

    public void initMenu() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(KEY_INIT, true);
        editor.commit();
    }

    public boolean isMenuInited() {
        return mPref.getBoolean(KEY_INIT, false);
    }

    public void saveToolMenu(List<HtmlHref> hrefs) {
        save(KEY_TOOL_MENU, hrefs);
    }

    public List<HtmlHref> getToolMenu() {
        return read(KEY_TOOL_MENU);
    }

    public void saveVideoCategories(List<HtmlHref> hrefs) {
        save(KEY_VIDEO_CATEGORIES_MENU, hrefs);
    }

    public List<HtmlHref> getVideoCategories() {
        return read(KEY_VIDEO_CATEGORIES_MENU);
    }

    public void saveMumuSoles(List<HtmlHref> hrefs) {
        save(KEY_MUMU_SOLES_MENU, hrefs);
    }

    public List<HtmlHref> getMumuSoles() {
        return read(KEY_MUMU_SOLES_MENU);
    }

    public void saveCommentaries(List<HtmlHref> hrefs) {
        save(KEY_COMMENTARIES_MENU, hrefs);
    }

    public List<HtmlHref> getCommentaries() {
        return read(KEY_COMMENTARIES_MENU);
    }

    public void saveKillers(List<HtmlHref> hrefs) {
        save(KEY_KILLERS_MENU, hrefs);
    }

    public List<HtmlHref> getKillers() {
        return read(KEY_KILLERS_MENU);
    }

    public void saveMatches(List<HtmlHref> hrefs) {
        save(KEY_MATCHES_MENU, hrefs);
    }

    public List<HtmlHref> getMatches() {
        return read(KEY_MATCHES_MENU);
    }

    public void saveColumns(List<HtmlHref> hrefs) {
        save(KEY_COLUMNS_MENU, hrefs);
    }

    public List<HtmlHref> getColumns() {
        return read(KEY_COLUMNS_MENU);
    }

    public void saveNews(List<HtmlHref> hrefs) {
        save(KEY_NEWS_MENU, hrefs);
    }

    public List<HtmlHref> getNews() {
        return read(KEY_NEWS_MENU);
    }

    private void save(String key, List<HtmlHref> list) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        for (int i = 0; i < list.size(); i++) {
            jsonObject = new JSONObject();
            jsonObject.put("url", list.get(i).url);
            jsonObject.put("name", list.get(i).name);
            jsonArray.add(jsonObject);
        }
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(key, jsonArray.toJSONString());
        editor.commit();
    }

    private List<HtmlHref> read(String key) {
        String str = mPref.getString(key, "[]");
        JSONArray jsonArray = JSONArray.parseArray(str);
        List<HtmlHref> list = new ArrayList<>();
        JSONObject jsonObject;
        HtmlHref htmlHref;
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            htmlHref = new HtmlHref(jsonObject.getString("name"), jsonObject.getString("url"));
            list.add(htmlHref);
        }
        return list;
    }
}
