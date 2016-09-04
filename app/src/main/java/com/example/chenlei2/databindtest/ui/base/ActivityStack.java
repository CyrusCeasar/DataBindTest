
package com.example.chenlei2.databindtest.ui.base;

import android.app.Activity;

import java.util.Stack;

public class ActivityStack {
    private static Stack<Activity> mActivityStack;
    private static ActivityStack instance;

    private ActivityStack() {
    }

    public static ActivityStack getScreenManager() {
        if (instance == null) {
            instance = new ActivityStack();
        }
        return instance;
    }

    // 退出栈顶Activity
    public synchronized void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            mActivityStack.remove(activity);
            // mActivityStack.pop();
            activity = null;
        }
    }

    // 获得当前栈顶Activity
    public Activity currentActivity() {
        Activity activity = mActivityStack.lastElement();
        // Activity activity = mActivityStack.pop();
        return activity;
    }

    // 将当前Activity推入栈中
    public synchronized void pushActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.add(activity);
        // mActivityStack.push(activity);
    }

    // 退出栈中所有Activity
    public void popAllActivityExceptOne(Class<Activity> cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    } // 退出栈中所有Activity

    public synchronized Activity popToRootActivity() {
        while (mActivityStack.size() > 1) {
            Activity activity = mActivityStack.pop();
            if (activity != null) {
                activity.finish();
            }
            activity = null;
        }
        if(!mActivityStack.isEmpty()){
            return mActivityStack.peek();
        }else {
            return  null;
        }

    }

    public Activity getBottomActivity() {
        return mActivityStack.firstElement();
    }

}
