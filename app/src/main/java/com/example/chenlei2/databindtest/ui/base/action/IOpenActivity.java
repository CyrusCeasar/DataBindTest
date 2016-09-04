
package com.example.chenlei2.databindtest.ui.base.action;

import android.os.Bundle;
import android.view.View;

public interface IOpenActivity {

    void startActivity(Class<?> activity);

    void startActivity(Class<?> activityClass,
                       Bundle bundle);

    void startActivityForResult(Class<?> activityClass,
                                int requestCode);

    void startActivityForResult(
            Class<?> activityClass,
            Bundle bundle, int requestCode);

    void showMsgDialog(String msg, String title);

    void showConfirmDialog(String msg, String title, View.OnClickListener onClickListener);

}
