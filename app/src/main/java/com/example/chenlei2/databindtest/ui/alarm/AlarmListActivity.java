package com.example.chenlei2.databindtest.ui.alarm;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.example.basemoudle.ui.base.BaseActivity;
import com.example.basemoudle.util.LogUtil;
import com.example.basemoudle.util.rxjava.RxJavaUtil;
import com.example.basemoudle.util.rxjava.SimpleObersever;
import com.example.chenlei2.databindtest.R;
import com.example.chenlei2.databindtest.ui.alarm.model.AlarmRule;
import com.example.chenlei2.databindtest.ui.alarm.model.AlarmService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by chenlei2 on 2017/4/10 0010.
 */

public class AlarmListActivity extends BaseActivity {

    private static final String TAG = AlarmListActivity.class.getSimpleName();

    private static final int CMD_ADD_ALARM = 1;
    private static final int CMD_UPDATE_ALARM = 2;


    List<AlarmRule> alarmRules;
    ListView lvAlarmList;
    ArrayAdapter<AlarmRule> adapter;
    TextView toolbarRightTv, toolbarLeftTv;
    AlarmService mAlarmService;



    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            LogUtil.d(TAG,"alarmService connected");
            mAlarmService = ((AlarmService.LocalBinder) iBinder).getService();
            createAlarmRuleList();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mAlarmService = null;
            showMessage("AlarmService unbind");
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        lvAlarmList = (ListView) findViewById(R.id.lv_alarm_list);
        toolbarRightTv = (TextView) findViewById(R.id.toolbar_right_tv);
        toolbarLeftTv = (TextView) findViewById(R.id.toolbar_left_tv);
        toolbarLeftTv.setOnClickListener(this);
        toolbarRightTv.setOnClickListener(this);
        lvAlarmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startAddOrUpdateActivity(alarmRules.get(i),CMD_UPDATE_ALARM);
            }
        });
        lvAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {


                final AlarmRule alarmRule = alarmRules.get(i);

                ObservableOnSubscribe<Boolean> observable = new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                        e.onNext(mAlarmService.delAlarmRule(alarmRule));
                    }
                };
                SimpleObersever<Boolean> observer = new SimpleObersever<Boolean>() {
                    @Override
                    public void onNext(Boolean value) {
                        if(value){
                            alarmRules.remove(alarmRule);
                            adapter.notifyDataSetChanged();
                        }
                    }
                };
                RxJavaUtil.asyncRequest(observable,observer);

                return false;
            }
        });

        bindService(new Intent(this, AlarmService.class), mServiceConnection, BIND_AUTO_CREATE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            AlarmRule alarmRule = data.getParcelableExtra(CreateOrUpdateAlarmActivity.EXTRA_DATAS);
            switch (requestCode) {
                case CMD_ADD_ALARM:
                    addAlarmRule(alarmRule);
                    break;
                case CMD_UPDATE_ALARM:
                    updateAlarmRule(alarmRule);
                    break;
            }
        }
    }

    private void createAlarmRuleList(){
        ObservableOnSubscribe<List<AlarmRule>> observable = new ObservableOnSubscribe<List<AlarmRule>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AlarmRule>> e) throws Exception {
                e.onNext((List<AlarmRule>) mAlarmService.queryForAll(AlarmRule.class));
            }
        };
        SimpleObersever<List<AlarmRule>> observer = new SimpleObersever<List<AlarmRule>>() {
            @Override
            public void onNext(List<AlarmRule> value) {
                alarmRules = (List<AlarmRule>) mAlarmService.queryForAll(AlarmRule.class);
                if (alarmRules != null) {
                    adapter = new ArrayAdapter<AlarmRule>(AlarmListActivity.this,android.R.layout.simple_list_item_1,alarmRules);
                    lvAlarmList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        RxJavaUtil.asyncRequest(observable,observer);

  }

  private void addAlarmRule(final AlarmRule alarmRule){
      ObservableOnSubscribe<Boolean> observable = new ObservableOnSubscribe<Boolean>() {
          @Override
          public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
              e.onNext(mAlarmService.addAlarmRule(alarmRule));
          }
      };
      SimpleObersever<Boolean> observer = new SimpleObersever<Boolean>() {
          @Override
          public void onNext(Boolean value) {
              if(value){
                  if (alarmRules == null) {
                      alarmRules = new ArrayList<>();
                      adapter = new ArrayAdapter<AlarmRule>(AlarmListActivity.this, android.R.layout.simple_list_item_1, alarmRules);
                      lvAlarmList.setAdapter(adapter);
                  }
                  alarmRules.add(alarmRule);
                  adapter.notifyDataSetChanged();
              }
          }
      };
      RxJavaUtil.asyncRequest(observable,observer);
  }


  private void updateAlarmRule(final AlarmRule alarmRule){

      ObservableOnSubscribe<Boolean> observable = new ObservableOnSubscribe<Boolean>() {
          @Override
          public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
              e.onNext(mAlarmService.updateAlarmRule(alarmRule));
          }
      };
      SimpleObersever<Boolean> observer = new SimpleObersever<Boolean>() {
          @Override
          public void onNext(Boolean value) {
              if(value){
                  int size = alarmRules.size();
                  for(int i = 0 ; i<size;i++){
                      if(alarmRules.get(i).getId() == alarmRule.getId()){
                          alarmRules.set(i,alarmRule);
                      }
                  }
                  adapter.notifyDataSetChanged();
              }
          }
      };
      RxJavaUtil.asyncRequest(observable,observer);
  }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_tv:
                finish();
                break;
            case R.id.toolbar_right_tv:
                startAddOrUpdateActivity(null,CMD_ADD_ALARM);
                break;
        }
    }

    private void startAddOrUpdateActivity(AlarmRule alarmRule,int cmd) {
        Intent intent = new Intent(this, CreateOrUpdateAlarmActivity.class);
        if (alarmRule != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(CreateOrUpdateAlarmActivity.EXTRA_DATAS, alarmRule);
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,cmd);
    }




}
