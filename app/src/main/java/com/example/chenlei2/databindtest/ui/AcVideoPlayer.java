package com.example.chenlei2.databindtest.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.basemoudle.ui.base.BaseActivity;
import com.example.basemoudle.util.DbManager;
import com.example.basemoudle.util.DbOrmHelper;
import com.example.chenlei2.databindtest.BR;
import com.example.chenlei2.databindtest.CyrusApplication;
import com.example.chenlei2.databindtest.R;
import com.example.chenlei2.databindtest.model.db.MFile;
import com.example.chenlei2.databindtest.model.db.MMediaFile;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenlei2 on 2016/9/1 0001.
 */
public class AcVideoPlayer extends BaseActivity {

    List<MMediaFile> files;


    RecyclerView rv_fileList;
    DbOrmHelper dbOrmHelper;
    VideoView videoView;
    MediaController mediaController;
    Button btn_previous,btn_pause,btn_next;
    ViewDataBinding binding;
    SeekBar sb_progress;
    Timer timer = new Timer();
    TextView tv_playTime;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
    int currentPlayPosition = 0;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.ac_video_play);
        dbOrmHelper = DbManager.getInstance().getOrmHelper(CyrusApplication.DB_NAME);
        rv_fileList = $(R.id.rv_fileList);
        btn_next =$(R.id.btn_next);
        btn_pause = $(R.id.btn_pause);
        btn_previous = $(R.id.btn_previous);
        sb_progress = $(R.id.sb_progress);
        tv_playTime = $(R.id.tv_playTime);
        videoView = $(R.id.videoView);
        handler = new Handler(getMainLooper());
        try {
            files = dbOrmHelper.getDaoEx(MMediaFile.class).queryBuilder().where().eq("type", MFile.TYPE.video).query();
            binding.setVariable(BR.video,files.get(currentPlayPosition));
            rv_fileList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            rv_fileList.setAdapter(new VideoAdapter(files,this));
            mediaController = new MediaController(this);
            mediaController.setMediaPlayer(videoView);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    videoView.start();
                    MMediaFile mediaFile = files.get(currentPlayPosition);
                    mediaFile.setTimeLong(mediaPlayer.getDuration());
                    dbOrmHelper.update(mediaFile,MMediaFile.class);
                    binding.setVariable(BR.video,mediaFile);
                    sb_progress.setMax(mediaPlayer.getDuration());
                }
            });
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_playTime.setText(simpleDateFormat.format(videoView.getCurrentPosition()));
                            sb_progress.setProgress(videoView.getCurrentPosition());
                        }
                    });
                }
            },0,1000);
            sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    videoView.seekTo(seekBar.getProgress());
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public  class VideoAdapter extends RecyclerView.Adapter {
        List<MMediaFile> values;
        Context context;

        VideoAdapter(List<MMediaFile> values, Context context) {
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
                    videoView.setVideoPath(values.get(position).getPath());
                    currentPlayPosition = position;
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
