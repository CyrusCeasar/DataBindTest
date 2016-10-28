
package com.example.basemoudle.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.basemoudle.ui.base.action.IOpenActivity;
import com.example.basemoudle.ui.plugin.FgSimpleDialog;
import com.example.basemoudle.ui.plugin.FgSimpleOkDialog;


public class ActivityManager implements IOpenActivity {

    public static final String BUNDLE_KEY = "bundle";
    protected final Context context;

    protected ActivityManager(Context context) {
        this.context = context;
    }


    @Override
    public void startActivity(Class<?> activity) {
        startActivity(activity, null);
    }


    @Override
    public void startActivity(Class<?> activityClass,
                              Bundle bundle) {
        Intent intent = new Intent(context, activityClass);
        if (bundle != null) {
            intent.putExtra(BUNDLE_KEY, bundle);
        }
        context.startActivity(intent);
    }


    @Override
    public void startActivityForResult(Class<?> activityClass,
                                       int requestCode) {
        startActivityForResult(activityClass, null, requestCode);

    }


    @Override
    public void startActivityForResult(
            Class<?> activityClass,
            Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, activityClass);
        if (bundle != null) {
            intent.putExtra(BUNDLE_KEY, bundle);
        }
        ((BaseActivity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    public void showMsgDialog(String msg, String title) {
        FragmentTransaction ft = ((BaseActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment fragment = ((BaseActivity) context).getSupportFragmentManager().findFragmentByTag(DialogFragment.class.getSimpleName());
        if (null != fragment) {
            ft.remove(fragment);
        }




   /*      * 0:默认样式
         * 1：无标题样式
         * 2：无边框样式
         * 3：不可输入，不可获得焦点样式
         * 可根据参数不同执行测试这几种样式的对话框。
*/

        FgSimpleDialog dialogFragment = FgSimpleDialog.newInstance(msg, title);
        dialogFragment.show(ft, DialogFragment.class.getSimpleName());
    }

    @Override
    public void showConfirmDialog(String msg, String title, View.OnClickListener onClickListener) {
        FragmentTransaction ft = ((BaseActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment fragment = ((BaseActivity) context).getSupportFragmentManager().findFragmentByTag(DialogFragment.class.getSimpleName());
        if (null != fragment) {
            ft.remove(fragment);
        }




   /*      * 0:默认样式
         * 1：无标题样式
         * 2：无边框样式
         * 3：不可输入，不可获得焦点样式
         * 可根据参数不同执行测试这几种样式的对话框。
*/

        FgSimpleOkDialog dialogFragment = FgSimpleOkDialog.newInstance(msg, title);
        dialogFragment.setConfirmListener(onClickListener);
        dialogFragment.show(ft, DialogFragment.class.getSimpleName());
    }

}

