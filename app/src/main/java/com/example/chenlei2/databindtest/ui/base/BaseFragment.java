
package com.example.chenlei2.databindtest.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.chenlei2.databindtest.ui.base.action.IOpenActivity;


public abstract class BaseFragment extends Fragment implements IOpenActivity {

    protected LayoutInflater mLayoutInflater;

    private ActivityManager mActivityManager;

    public void onAttach(Activity context) {
        super.onAttach(context);
        mLayoutInflater = LayoutInflater.from(getActivity());
        mActivityManager = new ActivityManager(getActivity());
    }

    @Override
    public void startActivity(Class<?> activity) {
        mActivityManager.startActivity(activity);
    }

    @Override
    public void startActivity(Class<?> activityClass, Bundle bundle) {
        mActivityManager.startActivity(activityClass, bundle);
    }

    @Override
    public void startActivityForResult(Class<?> activityClass, int requestCode) {
        mActivityManager.startActivityForResult(activityClass, requestCode);
    }

    @Override
    public void startActivityForResult(Class<?> activityClass, Bundle bundle, int requestCode) {
        mActivityManager.startActivityForResult(activityClass, bundle, requestCode);
    }

    public <T extends View> T $(View mView, int resId) {
        return (T) mView.findViewById(resId);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        AnalyzeToolChooser.getInstance().getAnalyzeTool().probePageOpen(getActivity());
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        AnalyzeToolChooser.getInstance().getAnalyzeTool().probePageClose(getActivity());
    }
    @Override
    public void showMsgDialog(String msg, String title) {
        mActivityManager.showMsgDialog(msg,title);
    }

    @Override
    public void showConfirmDialog(String msg, String title, View.OnClickListener onClickListener) {
        mActivityManager.showConfirmDialog(msg,title,onClickListener);
    }
}
