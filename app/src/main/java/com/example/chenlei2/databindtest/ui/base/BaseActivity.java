
package com.example.chenlei2.databindtest.ui.base;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import com.example.chenlei2.databindtest.ui.base.action.IOpenActivity;


@SuppressLint("NewApi")
public class BaseActivity extends FragmentActivity implements IOpenActivity {
    protected LayoutInflater mLayoutInflater;
    private ActivityManager activityManager;

    public <T extends View> T $(int resid) {
        return (T) findViewById(resid);
    }

    public <T extends View> T $(View mView, int resId) {
        return (T) mView.findViewById(resId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutInflater = LayoutInflater.from(this);
        activityManager = new ActivityManager(this);

    /*    if (MyApplication.SCREEN_HEIGHT == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            MyApplication.SCREEN_WIDTH = dm.widthPixels;
            MyApplication.SCREEN_HEIGHT = dm.heightPixels;
        }*/
        ActivityStack.getScreenManager().pushActivity(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        /*JPushInterface.onResume(BaseActivity.this);
        AnalyzeToolChooser.getInstance().getAnalyzeTool().probePageOpen(this);
        AnalyzeToolChooser.getInstance().getAnalyzeTool().probeTimeOpen(this);*/
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
      /*  JPushInterface.onPause(BaseActivity.this);
        AnalyzeToolChooser.getInstance().getAnalyzeTool().probePageClose(this);
        AnalyzeToolChooser.getInstance().getAnalyzeTool().probeTimeClose(this);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.getScreenManager().popActivity(this);
    }

    @Override
    public void startActivity(Class<?> activity) {
        activityManager.startActivity(activity);

    }

    @Override
    public void startActivity(Class<?> activityClass, Bundle bundle) {
        activityManager.startActivity(activityClass, bundle);
    }

    @Override
    public void startActivityForResult(Class<?> activityClass, int requestCode) {
        activityManager.startActivityForResult(activityClass, requestCode);

    }

    @Override
    public void startActivityForResult(Class<?> activityClass, Bundle bundle, int requestCode) {
        activityManager.startActivityForResult(activityClass, bundle, requestCode);

    }

    @Override
    public void showMsgDialog(String msg, String title) {
        activityManager.showMsgDialog(msg,title);
    }

    @Override
    public void showConfirmDialog(String msg, String title, View.OnClickListener onClickListener) {
        activityManager.showConfirmDialog(msg,title,onClickListener);
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}
