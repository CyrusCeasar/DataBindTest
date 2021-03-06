package com.example.chenlei2.databindtest.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.basemoudle.ui.base.BaseActivity;
import com.example.basemoudle.util.DbManager;
import com.example.basemoudle.util.DbOrmHelper;
import com.example.basemoudle.util.LogUtil;
import com.example.chenlei2.databindtest.CyrusApplication;
import com.example.chenlei2.databindtest.R;
import com.example.chenlei2.databindtest.model.AlarmHelper;
import com.example.chenlei2.databindtest.model.db.Alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AcAddAlarm extends BaseActivity {

    DatePicker datePicker;
    TimePicker timePicker;
    Calendar calendar;
    Button button;
    Alarm alarm;

    public static final String KEY_ALARM = "alarm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_add_alarm);
        alarm = (Alarm) getIntent().getSerializableExtra(KEY_ALARM);
        datePicker = $(R.id.datePicker);
        timePicker = $(R.id.timePicker);
        button = $(R.id.button);
        timePicker.setIs24HourView(true);

        calendar = Calendar.getInstance();
        if(alarm != null){
            calendar.setTime(new Date(alarm.getClockTime()));
        }
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), (DatePicker view, int year, int monthOfYear, int dayOfMonth) ->{
                calendar.set(year,monthOfYear,dayOfMonth);
                LogUtil.i(calendar.toString());
        });
    /*    timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(calendar.get(Calendar.MINUTE));*/

        timePicker.setOnTimeChangedListener((view,hourOfDay,minute)-> {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                LogUtil.i(calendar.toString());
        });

        button.setOnClickListener(view->{
                DbOrmHelper dbOrmHelper = DbManager.getInstance().getOrmHelper(CyrusApplication.DB_NAME);
                if(alarm != null){
                    alarm.setClockTime(calendar.getTimeInMillis());
                    dbOrmHelper.update(alarm,Alarm.class);
                }else {
                    alarm = new Alarm();
                    alarm.setClockTime(calendar.getTimeInMillis());
                    alarm.setInterval(-1);
                    dbOrmHelper.create(alarm,Alarm.class);
                }
                Intent intent = new Intent(AcAddAlarm.this,AcAlarmAlert.class);
                intent.putExtra(KEY_ALARM,alarm);
                LogUtil.i(new SimpleDateFormat("yyy-MM-dd hh:mm:ss", Locale.CHINA).format(new Date(calendar.getTimeInMillis())));
                new AlarmHelper(AcAddAlarm.this).addClock(alarm.getClockTime(), PendingIntent.getActivity(AcAddAlarm.this,alarm.getAlarmId(),intent,PendingIntent.FLAG_UPDATE_CURRENT));
                finish();
        });
    }
}
