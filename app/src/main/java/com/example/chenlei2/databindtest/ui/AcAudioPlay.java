package com.example.chenlei2.databindtest.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.example.basemoudle.ui.base.BaseActivity;
import com.example.chenlei2.databindtest.BR;
import com.example.chenlei2.databindtest.R;
import com.example.chenlei2.databindtest.ServMusicPlayer;
import com.example.chenlei2.databindtest.model.db.MMediaFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenlei2 on 2016/9/1 0001.
 */
public class AcAudioPlay extends BaseActivity implements View.OnClickListener{

    public static final String KEY_MUSIC = "music";

    ServMusicPlayer mServMusicPlayer;

    Button btn_previous,btn_pause,btn_next;
    RecyclerView rv_musicList;
    ViewDataBinding binding;
    ServiceConnection conn;
    SeekBar sb_progress;
    Timer timer = new Timer();
    TextView tv_playTime;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.ac_audio_play);
        handler = new Handler(getMainLooper());
         conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                LogUtils.i("service bind finished");

                mServMusicPlayer = ((ServMusicPlayer.MsgBinder)iBinder).getService();
                binding.setVariable(BR.music,mServMusicPlayer.getPlayingFile());
                rv_musicList.setAdapter(new MusicAdapter(mServMusicPlayer.getFileList(),AcAudioPlay.this));
                mServMusicPlayer.setMediaPlayListener(new ServMusicPlayer.MediaPlayListener() {


                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        sb_progress.setProgress(0);
                        tv_playTime.setText(String.valueOf(0));
                        sb_progress.setMax(mediaPlayer.getDuration());
                        binding.setVariable(BR.music,mServMusicPlayer.getPlayingFile());
                    }
                });


                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_playTime.setText(simpleDateFormat.format(new Date(mServMusicPlayer.getMediaPlayer().getCurrentPosition())));
                                sb_progress.setProgress(mServMusicPlayer.getMediaPlayer().getCurrentPosition());
                            }
                        });

                    }
                },0,300);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mServMusicPlayer.setMediaPlayListener(null);
            }
        };
        Intent intent = new Intent(this, ServMusicPlayer.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);

      /*  mediaFile = (MediaFile) getIntent().getExtras().getSerializable(KEY_MUSIC);
        binding.setVariable(BR.music,mediaFile);*/
        rv_musicList = (RecyclerView) findViewById(R.id.rv_musicList);
        btn_next =$(R.id.btn_next);
        btn_pause = $(R.id.btn_pause);
        btn_previous = $(R.id.btn_previous);
        sb_progress = $(R.id.sb_progress);
        tv_playTime = $(R.id.tv_playTime);

        rv_musicList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(mServMusicPlayer != null){
                    mServMusicPlayer.getMediaPlayer().seekTo(seekBar.getProgress());
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_next:
                if(mServMusicPlayer != null){
                    mServMusicPlayer.startNextMusic();
                }
                break;
            case R.id.btn_pause:
                if(mServMusicPlayer != null){
                    if(mServMusicPlayer.getMediaPlayer().isPlaying()){
                        mServMusicPlayer.stopMusic();
                    }else {
                        mServMusicPlayer.restartMusic();
                    }
                }
                break;
            case R.id.btn_previous:
                if(mServMusicPlayer != null){
                    mServMusicPlayer.startPreviousMusic();
                }
                break;
        }
    }

    public  class MusicAdapter extends RecyclerView.Adapter {
        List<MMediaFile> values;
        Context context;

        MusicAdapter(List<MMediaFile> values, Context context) {
            this.values = values;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater
                    .from(context), R.layout.item_music, parent, false);
            ViewHolder holder = new ViewHolder(binding.getRoot());
            holder.setBinding(binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder holder1 = (ViewHolder) holder;
            holder1.getBinding().setVariable(BR.musicItem, values.get(position));
            holder1.getBinding().executePendingBindings();

            ((ViewHolder) holder).getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mServMusicPlayer.startMusic(position);
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
