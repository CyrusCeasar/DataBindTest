package com.example.chenlei2.databindtest.ui;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basemoudle.ui.base.BaseActivity;
import com.example.basemoudle.util.DbManager;
import com.example.chenlei2.databindtest.BR;
import com.example.chenlei2.databindtest.CyrusApplication;
import com.example.chenlei2.databindtest.R;
import com.example.chenlei2.databindtest.model.db.Alarm;

import java.sql.SQLException;
import java.util.List;

public class AcAlarm extends BaseActivity {

    RecyclerView rv_alarms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rv_alarms = $(R.id.rv_alarms);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          /*      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(AcAlarm.this,AcAddAlarm.class);
                startActivity(intent);

            }
        });
        rv_alarms.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            List<Alarm> alarms = DbManager.getInstance().getOrmHelper(CyrusApplication.DB_NAME).getDaoEx(Alarm.class).queryBuilder().query();
            rv_alarms.setAdapter(new AlarmAdapter(alarms,this));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  class AlarmAdapter extends RecyclerView.Adapter {
        List<Alarm> values;
        Context context;

        AlarmAdapter(List<Alarm> values, Context context) {
            this.values = values;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater
                    .from(context), R.layout.ac_alarm_item, parent, false);
            ViewHolder holder = new ViewHolder(binding.getRoot());
            holder.setBinding(binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder holder1 = (ViewHolder) holder;
            holder1.getBinding().setVariable(BR.alarm, values.get(position));
            holder1.getBinding().executePendingBindings();

            ((ViewHolder) holder).getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AcAlarm.this,AcAddAlarm.class);
                    intent.putExtra(AcAddAlarm.KEY_ALARM,values.get(position));
                    startActivity(intent);
                }
            });


        }


        @Override
        public int getItemCount() {
            return values.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ViewDataBinding binding;

            public ViewHolder(View itemView) {
                super(itemView);
            }

            public ViewDataBinding getBinding() {
                return this.binding;
            }

            public void setBinding(ViewDataBinding binding) {
                this.binding = binding;
            }
        }

    }
}
