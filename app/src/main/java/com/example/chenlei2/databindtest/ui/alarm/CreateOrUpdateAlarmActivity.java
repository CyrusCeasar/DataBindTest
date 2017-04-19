package com.example.chenlei2.databindtest.ui.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;


import com.example.basemoudle.ui.base.BaseActivity;
import com.example.basemoudle.util.LogUtil;
import com.example.chenlei2.databindtest.R;
import com.example.chenlei2.databindtest.ui.alarm.model.AlarmRule;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by chenlei2 on 2017/4/10 0010.
 */

public class CreateOrUpdateAlarmActivity extends BaseActivity {

    public static final String TAG = CreateOrUpdateAlarmActivity.class.getSimpleName();

    public static final String EXTRA_DATAS = "alarm_rule";

    TextView tvTime, tvRule, tvRingRule;
    RelativeLayout rlTime, rlRule, rlRingRule;
    EditText etLabel;
    SimpleDateFormat simpleDateFormat;


    Dialog mTimeDialog;
    ViewGroup mTimeContentView;
    DatePicker dpDate;
    TimePicker tpTime;

    AlarmRule alarmRule;
    TextView toolbarRightTv, toolbarLeftTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_create_update);
        tvRule = (TextView) findViewById(R.id.tv_rule);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvRingRule = (TextView) findViewById(R.id.tv_ring_rule);
        rlTime = (RelativeLayout) findViewById(R.id.rl_time);
        rlRule = (RelativeLayout) findViewById(R.id.rl_rule);
        rlRingRule = (RelativeLayout) findViewById(R.id.rl_ring_rule);

        etLabel = (EditText) findViewById(R.id.et_label);
        toolbarRightTv = (TextView) findViewById(R.id.toolbar_right_tv);
        toolbarLeftTv = (TextView) findViewById(R.id.toolbar_left_tv);



        rlRule.setOnClickListener(this);
        rlRingRule.setOnClickListener(this);
        rlTime.setOnClickListener(this);
        toolbarRightTv.setOnClickListener(this);
        toolbarLeftTv.setOnClickListener(this);

        etLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alarmRule.setLabel(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //初始化alarmRule数据
        alarmRule = getIntent().getParcelableExtra(EXTRA_DATAS);
        if (alarmRule == null) {
            alarmRule = new AlarmRule();
            alarmRule.setBeginTime(Calendar.getInstance().getTimeInMillis());
            alarmRule.setRingRule("重复播报");
            alarmRule.setLabel("普通闹钟");
            JSONObject jsonRule = new JSONObject();
            try {
                jsonRule.put(AlarmRule.PARAM_BOOL_REPEAT, false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            alarmRule.setRuleContent(jsonRule.toString());
        }
        simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
    }

    @Override
    protected void onResume() {
        super.onResume();

        etLabel.setText(alarmRule.getLabel());
        tvRule.setText(alarmRule.getRuleContent());
        tvRingRule.setText(alarmRule.getRingRule());
        tvTime.setText(simpleDateFormat.format(alarmRule.getBeginTime()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_DATAS, alarmRule);
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        alarmRule = savedInstanceState.getParcelable(EXTRA_DATAS);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_time:
                editTime();
                break;
            case R.id.rl_rule:
                editAlarmRule();
                break;
            case R.id.toolbar_right_tv:
                setReuslt();
            case R.id.toolbar_left_tv:
                finish();
                break;
        }
    }

    private void editAlarmRule() {
        AlarmRuleEditFragment alarmRuleEditFragment = AlarmRuleEditFragment.newInstance(alarmRule.getRuleContent());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fg = getFragmentManager().findFragmentByTag(AlarmRuleEditFragment.class.getSimpleName());
        if (fg != null) {
            fragmentTransaction.remove(fg);
        }
        fragmentTransaction.add(R.id.fl_container, alarmRuleEditFragment, AlarmRuleEditFragment.class.getSimpleName()).commit();
    }

    private void setReuslt() {
        alarmRule.setRingRule(tvRingRule.getText().toString());
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATAS, alarmRule);
        setResult(RESULT_OK, intent);
    }

    public void updateRuleContent(String jsonRule) {
        LogUtil.d(TAG, "update jsonRule:" + jsonRule);
        alarmRule.setRuleContent(jsonRule);
        tvRule.setText(jsonRule);
    }


    private void editTime() {
        if (mTimeDialog == null) {
            mTimeDialog = new AlertDialog.Builder(this).create();
        }
        mTimeDialog.show();
        if (mTimeContentView == null) {
            mTimeContentView = (ViewGroup) getLayoutInflater().inflate(R.layout.dialog_select_time, null);
            mTimeDialog.setContentView(mTimeContentView);
            dpDate = (DatePicker) mTimeContentView.findViewById(R.id.dp_date);
            tpTime = (TimePicker) mTimeContentView.findViewById(R.id.tp_time);
            tpTime.setIs24HourView(true);
            Button btnYes = (Button) mTimeContentView.findViewById(R.id.btn_yes);
            Button btnCancle = (Button) mTimeContentView.findViewById(R.id.btn_cancel);
            btnYes.setOnClickListener(view -> {
                //更新数据
                Calendar date = Calendar.getInstance();
                date.set(dpDate.getYear(), dpDate.getMonth(), dpDate.getDayOfMonth(), tpTime.getCurrentHour(), tpTime.getCurrentMinute(),0);
                alarmRule.setBeginTime(date.getTimeInMillis());
                tvTime.setText(simpleDateFormat.format(date.getTimeInMillis()));
                mTimeDialog.dismiss();
            });
            btnCancle.setOnClickListener(view -> mTimeDialog.dismiss());
        }


        //初始化ui控件数据
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(alarmRule.getBeginTime());
        LogUtil.d(TAG, String.format("yyyy:%d,mm:%d,dd:%d", date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)));
        dpDate.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        tpTime.setCurrentHour(date.get(Calendar.HOUR_OF_DAY));
        tpTime.setCurrentMinute(date.get(Calendar.MINUTE));
        alarmRule.setBeginTime(date.getTimeInMillis());
    }


}
