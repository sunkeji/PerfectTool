package com.skj.wheel.util;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 孙科技 on 2018/2/8.
 */

public class ActivityListUtil {

    private static ActivityListUtil activityListUtil;

    public static ActivityListUtil getInstance() {
        if (activityListUtil == null)
            activityListUtil = new ActivityListUtil();
        return activityListUtil;
    }

    /**
     * 建一个容器储存每一个activity页面
     */
    public static List<Activity> activityList = new LinkedList<Activity>();

    /**
     * 添加到Activity容器中
     */
    public static void addActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    /**
     * 遍历所有Activity并finish
     */
    public static void finishAllActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    /**
     * 通过类名finish某一个activity
     *
     * @param cls
     */
    public static void finishActivity(Class<?> cls) {
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                activity.finish();
                break;
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        finishActivity(activity.getClass());
    }


    /**
     * 通过类名finish除该类名的activity外的所有activity
     *
     * @param cls
     */
    public static void finishOtherActivity(Class<?> cls) {
        for (Activity activity : activityList) {
            if (!activity.getClass().equals(cls)) {
                activity.finish();
            }
        }
    }

    /**
     * 结束除该Activity外的所有activity
     */
    public static void finishOtherActivity(Activity activity) {
        finishOtherActivity(activity.getClass());
    }

}
