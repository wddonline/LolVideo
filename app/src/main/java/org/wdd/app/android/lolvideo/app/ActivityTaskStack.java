package org.wdd.app.android.lolvideo.app;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangdd on 17-1-15.
 */

public class ActivityTaskStack {

    private static ActivityTaskStack INSTANCE;

    public static ActivityTaskStack getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ActivityTaskStack();
        }
        return INSTANCE;
    }

    private List<Activity> taskStack;

    public ActivityTaskStack() {
        taskStack = new ArrayList<>();
    }

    public void addActivity(Activity activity) {
        taskStack.add(activity);
    }

    public int getActivityCount() {
        return taskStack.size();
    }

    public void removeActivity(Activity activity) {
        taskStack.remove(activity);
    }

    public void clearActivities() {
        Iterator<Activity> it = taskStack.iterator();
        while (it.hasNext()) {
            it.next().finish();
            it.remove();
        }
    }
}
