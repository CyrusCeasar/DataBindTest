
package com.example.chenlei2.databindtest.ui.plugin;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by chenlei2 on 2015/11/27 0027.
 */
public class InitingView extends LinearLayout {

    public InitingView(Context context) {
        super(context);
        init();
    }

    public InitingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initParams();
        initPromptContent();
    }

    private void initParams() {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
    }

    private void initPromptContent() {
        ProgressBar progressBar = new ProgressBar(getContext());
        addView(progressBar);
        TextView textView = new TextView(getContext());
        textView.setText("初始化中....");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        addView(textView);

    }

}
