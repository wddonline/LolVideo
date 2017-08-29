package org.wdd.app.android.lolvideo.ui.hot.model;

import java.util.List;

/**
 * Created by richard on 8/29/17.
 */

public class HotCategory {

    public enum HotType {
        NEWS, VIDEO
    }

    public String name;
    public String url;
    public HotType type;
    public List<HotVideo> data;

    public HotCategory() {
    }
}
