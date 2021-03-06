package com.example.basemoudle.ui.base;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.basemoudle.ui.base.action.IOpenActivity;
import com.example.basemoudle.util.LogUtil;

import java.util.Locale;


@SuppressLint("NewApi")
public abstract class BaseActivity extends AppCompatActivity implements IOpenActivity ,View.OnClickListener{
    private static final String  TAG = BaseActivity.class.getSimpleName();
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

      /*  ThreadUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                FirebaseCrash.log(getClass().getSimpleName()+"created");
            }
        });*/
/*
    if (MyApplication.SCREEN_HEIGHT == 0) {
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
  /*JPushInterface.onPause(BaseActivity.this);
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
        activityManager.showMsgDialog(msg, title);
    }

    @Override
    public void showConfirmDialog(String msg, String title, View.OnClickListener onClickListener) {
        activityManager.showConfirmDialog(msg, title, onClickListener);
    }


    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
   public void showMessage(String msg){
       Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
   }

    @Override
    public void onClick(View v) {
        LogUtil.i(TAG,String.format(Locale.CHINA,"view id:%d clicked",v.getId()));
    }
}
