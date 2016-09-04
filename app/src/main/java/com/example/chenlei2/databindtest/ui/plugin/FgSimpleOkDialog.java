package com.example.chenlei2.databindtest.ui.plugin;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.chenlei2.databindtest.R;


/**
 * Created by chenlei2 on 2016/8/7 0007.
 */
public class FgSimpleOkDialog extends DialogFragment{

    private ViewGroup mRootView;
    private View.OnClickListener onClickListener;

    public static FgSimpleOkDialog newInstance(String msg, String title){
        FgSimpleOkDialog fgSimpleDialog = new FgSimpleOkDialog();
        Bundle bundle = new Bundle();
        bundle.putString("msg",msg);
        bundle.putString("title",title);
        fgSimpleDialog.setArguments(bundle);
        return  fgSimpleDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        int styleNum = getArguments().getInt("", 0);
      /*  int style = 0;
        switch (0) {
            case 0:
                style = DialogFragment.STYLE_NORMAL;//默认样式
                break;
            case 1:
                style = DialogFragment.STYLE_NO_TITLE;//无标题样式
                break;
            case 2:
                style = DialogFragment.STYLE_NO_FRAME;//无边框样式
                break;
            case 3:
                style = DialogFragment.STYLE_NO_INPUT;//不可输入，不可获得焦点样式
                break;
        }*/
        setStyle( DialogFragment.STYLE_NORMAL, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mRootView == null){
            mRootView = (ViewGroup) inflater.inflate(R.layout.custom_dialog,null);
            Button  dialog_btn_confirms = (Button) mRootView.findViewById(R.id.ok);
            Button  dialog_btn_cancel = (Button)mRootView.findViewById(R.id.cancel);
            dialog_btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            dialog_btn_confirms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickListener!=null) {
                        onClickListener.onClick(view);
                    }
                    dismiss();
                }
            });
        }
        TextView tv_title = (TextView) mRootView.findViewById(R.id.title);
        TextView tv_msg = (TextView) mRootView.findViewById(R.id.message);

        String title = getArguments().getString("title");
        String msg = getArguments().getString("msg");
        tv_title.setText(title);
        tv_msg.setText(msg);
        return  mRootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return  dialog;
    }

    public void setConfirmListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
}
