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

    private final String KEY_TOOL_MENU = "key_tool_menu";

    private SharedPreferences mPref;

    public HttpDataCache(Context context) {
        mPref = context.getSharedPreferences("http_data", Context.MODE_PRIVATE);
    }

    public void saveToolMenu(List<HtmlHref> hrefs) {
        save(KEY_TOOL_MENU, hrefs);
    }

    public List<HtmlHref> getToolMenu() {
        return read(KEY_TOOL_MENU);
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
