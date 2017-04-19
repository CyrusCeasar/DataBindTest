package com.example.chenlei2.databindtest.ui.alarm;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;


import com.example.basemoudle.util.LogUtil;
import com.example.chenlei2.databindtest.R;
import com.example.chenlei2.databindtest.ui.alarm.model.AlarmRule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by chenlei2 on 2017/4/12 0012.
 */

public class AlarmRuleEditFragment extends Fragment implements View.OnClickListener, NumberPicker.OnValueChangeListener, CompoundButton.OnCheckedChangeListener {


    public static final String PARAM_JSON_RULE = "json_rule";
    private static final String TAG = AlarmRuleEditFragment.class.getSimpleName();
    final String[] repeat_types = new String[]{"日", "周", "月", "年"};

    ViewGroup mRootView;
    Button btnSelectEndCondition;
    TextView tvRepeatPrompt;
    NumberPicker npRepeatInterval, npRepeatType;
    LinearLayout llWeekChoicer;
    TextView toolbarRightTv, toolbarLeftTv;
    JSONObject jsonRule;



    CheckBox cb_1, cb_2, cb_3, cb_4, cb_5, cb_6, cb_7;

    public static AlarmRuleEditFragment newInstance(String jsonRule) {
        AlarmRuleEditFragment alarmRuleEditFragment = new AlarmRuleEditFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_JSON_RULE, jsonRule);
        alarmRuleEditFragment.setArguments(bundle);
        return alarmRuleEditFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_alarm_rule, null);
            btnSelectEndCondition = (Button) mRootView.findViewById(R.id.btn_select_end_condition);
            tvRepeatPrompt = (TextView) mRootView.findViewById(R.id.tv_repeat_prompt);
            npRepeatInterval = (NumberPicker) mRootView.findViewById(R.id.np_repeat_interval);
            npRepeatType = (NumberPicker) mRootView.findViewById(R.id.np_repeat_type);
            llWeekChoicer = (LinearLayout) mRootView.findViewById(R.id.ll_week_choicer);
            toolbarRightTv = (TextView) mRootView.findViewById(R.id.toolbar_right_tv);
            toolbarLeftTv = (TextView) mRootView.findViewById(R.id.toolbar_left_tv);

            cb_1 = (CheckBox) mRootView.findViewById(R.id.cb_1);
            cb_2 = (CheckBox) mRootView.findViewById(R.id.cb_2);
            cb_3 = (CheckBox) mRootView.findViewById(R.id.cb_3);
            cb_4 = (CheckBox) mRootView.findViewById(R.id.cb_4);
            cb_5 = (CheckBox) mRootView.findViewById(R.id.cb_5);
            cb_6 = (CheckBox) mRootView.findViewById(R.id.cb_6);
            cb_7 = (CheckBox) mRootView.findViewById(R.id.cb_7);

            cb_1.setOnCheckedChangeListener(this);
            cb_2.setOnCheckedChangeListener(this);
            cb_3.setOnCheckedChangeListener(this);
            cb_4.setOnCheckedChangeListener(this);
            cb_5.setOnCheckedChangeListener(this);
            cb_6.setOnCheckedChangeListener(this);
            cb_7.setOnCheckedChangeListener(this);

            toolbarLeftTv.setOnClickListener(this);
            toolbarRightTv.setOnClickListener(this);
            npRepeatType.setOnValueChangedListener(this);
            npRepeatInterval.setOnValueChangedListener(this);
            btnSelectEndCondition.setOnClickListener(this);

            String stringrule = getArguments().getString(PARAM_JSON_RULE);
            try {
                if (!TextUtils.isEmpty(stringrule)) {
                    jsonRule = new JSONObject(stringrule);
                } else {
                    jsonRule = new JSONObject();
                    jsonRule.put(AlarmRule.PARAM_BOOL_REPEAT, true);
                    jsonRule.put(AlarmRule.PARAM_INT_REPEAT_TYPE, AlarmRule.REPEAT_TYPE_DAY);
                    jsonRule.put(AlarmRule.PARAM_INT_END_TYPE, AlarmRule.TYPE_END_NONE);
                    jsonRule.put(AlarmRule.PARAM_INT_REPEAT_INTERVAL, 1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            npRepeatInterval.setMaxValue(99);
            npRepeatInterval.setMinValue(1);
            npRepeatType.setDisplayedValues(repeat_types);
            npRepeatType.setMinValue(0);
            npRepeatType.setMaxValue(3);
        }

        try {
            boolean repeat = jsonRule.getBoolean(AlarmRule.PARAM_BOOL_REPEAT);
            if (!repeat) {//默认重复，并初始化重复数据
                jsonRule.put(AlarmRule.PARAM_BOOL_REPEAT, true);
                jsonRule.put(AlarmRule.PARAM_INT_REPEAT_TYPE, AlarmRule.REPEAT_TYPE_DAY);
                jsonRule.put(AlarmRule.PARAM_INT_END_TYPE, AlarmRule.TYPE_END_NONE);
                jsonRule.put(AlarmRule.PARAM_INT_REPEAT_INTERVAL, 1);
                jsonRule.put(AlarmRule.PARAM_LONG_REPEAT_DAYS,0);
            }
            //初始化控件
            int repeat_type = jsonRule.getInt(AlarmRule.PARAM_INT_REPEAT_TYPE);
            int repeat_interval = jsonRule.getInt(AlarmRule.PARAM_INT_REPEAT_INTERVAL);
            npRepeatType.setValue(repeat_type);
            npRepeatInterval.setValue(repeat_interval);
            if (repeat_type == AlarmRule.REPEAT_TYPE_WEEK) {//初始化重复类型为周的界面
                long repeat_days = jsonRule.getInt(AlarmRule.PARAM_LONG_REPEAT_DAYS);
                llWeekChoicer.setVisibility(View.VISIBLE);
                byte checkBoxCount = 0;
                int childCount = llWeekChoicer.getChildCount();
                LinearLayout childView;
                int mask;
                CheckBox checkBox;
                for (int i = 0; i < childCount; ++i) {//遍历所有checkbox 注意有两个linearlayout ，并根据提醒日期选择相应checkboxs
                    childView = (LinearLayout) llWeekChoicer.getChildAt(i);
                    for (int j = 0; j < childView.getChildCount(); j++) {
                        checkBox = (CheckBox) childView.getChildAt(j);
                        ++checkBoxCount;
                        mask = 1 << (checkBoxCount-1);
                        checkBox.setChecked((repeat_days & mask) == mask);
                    }
                }
            }else{
                llWeekChoicer.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            LogUtil.w(TAG, "illegal jsonrule date format");
            e.printStackTrace();
        }

        return mRootView;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_end_condition:
                selectEndCondition();
                break;
            case R.id.toolbar_right_tv:
                updateJsonRule();
            case R.id.toolbar_left_tv:
                getFragmentManager().beginTransaction().remove(this).commit();
                break;
        }
    }


    private void updateJsonRule() {
        try {
            jsonRule.put(AlarmRule.PARAM_INT_REPEAT_INTERVAL, npRepeatInterval.getValue());
            int repeat_type = npRepeatType.getValue();
            jsonRule.put(AlarmRule.PARAM_INT_REPEAT_TYPE, repeat_type);
            long repeat_days = 0;
            if (repeat_type == AlarmRule.REPEAT_TYPE_WEEK) {//初始化重复类型为周的界面
                llWeekChoicer.setVisibility(View.VISIBLE);
                byte checkBoxCount = 0;
                int childCount = llWeekChoicer.getChildCount();
                LinearLayout childView;
                int mask;
                CheckBox checkBox;
                for (int i = 0; i < childCount; ++i) {//遍历所有checkbox 注意有两个linearlayout ，并根据提醒日期选择相应checkboxs
                    childView = (LinearLayout) llWeekChoicer.getChildAt(i);
                    for (int j = 0; j < childView.getChildCount(); j++) {
                        checkBox = (CheckBox) childView.getChildAt(j);
                        ++checkBoxCount;
                        mask = 1 << (checkBoxCount-1);
                        if (checkBox.isChecked()) {
                            repeat_days |= mask;
                        }
                    }
                }
                jsonRule.put(AlarmRule.PARAM_LONG_REPEAT_DAYS, repeat_days);
            }
            ((CreateOrUpdateAlarmActivity) getActivity()).updateRuleContent(jsonRule.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void selectEndCondition() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        EndConditionDialogFragment endConditionDialogFragment = EndConditionDialogFragment.newInstance(jsonRule.toString());
        endConditionDialogFragment.setAlarmRuleEditFragment(this);
        endConditionDialogFragment.show(ft, "dialog");
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (picker.getId() == R.id.np_repeat_type) {
            if (newVal != AlarmRule.REPEAT_TYPE_WEEK) {
                llWeekChoicer.setVisibility(View.GONE);
            } else {
                llWeekChoicer.setVisibility(View.VISIBLE);
            }
            try {
                jsonRule.put(AlarmRule.PARAM_INT_REPEAT_TYPE,newVal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (picker.getId() == R.id.np_repeat_interval) {
            try {
                jsonRule.put(AlarmRule.PARAM_INT_REPEAT_INTERVAL,newVal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tvRepeatPrompt.setText(String.format(Locale.CHINA, "每 %d %s ", npRepeatInterval.getValue(), repeat_types[npRepeatType.getValue()]));
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    public void updateJsonRule(JSONObject jsonRule) {
        this.jsonRule = jsonRule;
    }
}
