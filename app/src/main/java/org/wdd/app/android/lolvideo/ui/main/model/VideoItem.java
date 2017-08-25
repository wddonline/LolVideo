package org.wdd.app.android.lolvideo.ui.main.model;

/**
 * Created by richard on 8/25/17.
 */

/**
 * <a href="/201708/53018.html">
 <div class="pic">
 <img src="http://www.lolshipin.com/uploads/allimg/170825/28-1FR50952310-L.jpg" alt="Miss排位日记：中单暴力丽桑卓！无解双控冰封全局！" ">
 <span>2017-08-25</span>
 </div>
 <div class="text">
 <p>Miss排位日记：中单暴力丽桑卓！无解双控冰封全局！</p>
 </div> </a> </li>
 */
public class VideoItem {

    public String url;
    public String img;
    public String date;
    public String title;

    public VideoItem() {
    }

    public VideoItem(String url, String img, String date, String title) {
        this.url = url;
        this.img = img;
        this.date = date;
        this.title = title;
    }
}
