package com.example.chenlei2.databindtest.ui.alarm;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.example.chenlei2.databindtest.R;
import com.example.chenlei2.databindtest.ui.alarm.model.AlarmRule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by chenlei2 on 2017/4/12 0012.
 */

public class EndConditionDialogFragment extends DialogFragment implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{

    ViewGroup mRootView;
    RadioGroup rgEndType;
    Button btnYes,btnCancel;

    DatePicker dpEndDate;
    NumberPicker npEndCount;

    JSONObject ruleJson;
    Calendar calendar;

    AlarmRuleEditFragment mAlarmRuleEditFragment;
    public static EndConditionDialogFragment newInstance(String ruleJson){
        EndConditionDialogFragment endConditionDialogFragment = new EndConditionDialogFragment();
        if(!TextUtils.isEmpty(ruleJson)){
            Bundle bundle = new Bundle();
            bundle.putString(AlarmRuleEditFragment.PARAM_JSON_RULE,ruleJson);
            endConditionDialogFragment.setArguments(bundle);
        }
        return  endConditionDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(mRootView == null){
            mRootView = (ViewGroup) inflater.inflate(R.layout.dialog_end_condition,null);
            rgEndType = (RadioGroup) mRootView.findViewById(R.id.rg_end_type);

            btnYes = (Button) mRootView.findViewById(R.id.btn_yes);
            btnCancel = (Button)mRootView.findViewById(R.id.btn_cancel);
            dpEndDate = (DatePicker)mRootView.findViewById(R.id.dp_end_date);
            npEndCount = (NumberPicker)mRootView.findViewById(R.id.np_end_count);


            btnCancel.setOnClickListener(this);
            btnYes.setOnClickListener(this);
            rgEndType.setOnCheckedChangeListener(this);
            for(int i = 0 ; i<rgEndType.getChildCount();i++){
                rgEndType.getChildAt(i).setTag(i);
            }

            npEndCount.setMinValue(1);
            npEndCount.setMaxValue(99);

            calendar = Calendar.getInstance();
            dpEndDate.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));



            try {
                ruleJson = new JSONObject(getArguments().getString(AlarmRuleEditFragment.PARAM_JSON_RULE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(ruleJson != null && ruleJson.has(AlarmRule.PARAM_INT_END_TYPE)) {
            try {
                int end_type = ruleJson.getInt(AlarmRule.PARAM_INT_END_TYPE);
                ((RadioButton)rgEndType.getChildAt(end_type)).setChecked(true);
                switch (end_type) {
                    case AlarmRule.TYPE_END_NONE:
                        dpEndDate.setVisibility(View.INVISIBLE);
                        npEndCount.setVisibility(View.INVISIBLE);
                        break;
                    case AlarmRule.TYPE_END_TIME:
                        dpEndDate.setVisibility(View.VISIBLE);
                        npEndCount.setVisibility(View.INVISIBLE);
                        long end_date = ruleJson.getLong(AlarmRule.PARAM_LONG_OUT_OF_TIME);
                        calendar.setTimeInMillis(end_date);
                        dpEndDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        break;
                    case AlarmRule.TYPE_END_COUNT:
                        dpEndDate.setVisibility(View.INVISIBLE);
                        npEndCount.setVisibility(View.VISIBLE);
                        npEndCount.setValue(ruleJson.getInt(AlarmRule.PARAM_INT_MAX_COUNT));
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return  mRootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return  dialog;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_none:
                dpEndDate.setVisibility(View.GONE);
                npEndCount.setVisibility(View.GONE);
                break;
            case R.id.rb_end_date:
                dpEndDate.setVisibility(View.VISIBLE);
                npEndCount.setVisibility(View.GONE);
                break;
            case R.id.rb_end_count:
                dpEndDate.setVisibility(View.GONE);
                npEndCount.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_yes:
                try {
                    int endType = (int) (rgEndType.findViewById(rgEndType.getCheckedRadioButtonId())).getTag();
                    ruleJson.put(AlarmRule.PARAM_INT_END_TYPE,endType);
                    calendar.set(dpEndDate.getYear(),dpEndDate.getMonth(),dpEndDate.getDayOfMonth(),0,0,0);
                    ruleJson.put(AlarmRule.PARAM_LONG_OUT_OF_TIME,calendar.getTimeInMillis());
                    ruleJson.put(AlarmRule.PARAM_INT_MAX_COUNT,npEndCount.getValue());
                    if(ruleJson.has(AlarmRule.PARAM_INT_REPEATED_COUNT)){
                        ruleJson.put(AlarmRule.PARAM_INT_REPEATED_COUNT,ruleJson.getInt(AlarmRule.PARAM_INT_REPEATED_COUNT));
                    }else{
                        ruleJson.put(AlarmRule.PARAM_INT_REPEATED_COUNT,0);
                    }

                    mAlarmRuleEditFragment.updateJsonRule(ruleJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            case R.id.btn_cancel:
                mAlarmRuleEditFragment = null;
                dismiss();
                break;
        }
    }

    public void setAlarmRuleEditFragment(AlarmRuleEditFragment fg){
        this.mAlarmRuleEditFragment = fg;
    }


}
