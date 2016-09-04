package com.example.chenlei2.databindtest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;
import com.example.chenlei2.databindtest.model.db.MFile;
import com.example.chenlei2.databindtest.model.db.MMediaFile;
import com.example.chenlei2.databindtest.model.util.DbManager;
import com.example.chenlei2.databindtest.model.util.DbOrmHelper;
import com.example.chenlei2.databindtest.model.util.ThreadUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by chenlei2 on 2016/9/1 0001.
 */
public class ServMusicPlayer extends Service{

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    List<MMediaFile> fileList;

    final MediaPlayer mediaPlayer = new MediaPlayer();
    volatile boolean isStop = false;

    DbOrmHelper dbOrmHelper = DbManager.getInstance().getOrmHelper(CyrucApplication.DB_NAME);

    private int currentPlayPostion;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();
    }

    public class MsgBinder extends Binder{
        public ServMusicPlayer getService(){
            return ServMusicPlayer.this;
        }
    }

    MediaPlayListener mediaPlayListener;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i("service created");
        try {
            fileList = dbOrmHelper.getDaoEx(MMediaFile.class).queryBuilder().where().eq("type", MFile.TYPE.audio).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                fileList.get(currentPlayPostion).setTimeLong(mediaPlayer.getDuration());
                dbOrmHelper.update(fileList.get(currentPlayPostion),MMediaFile.class);
                if(mediaPlayListener != null){
                    mediaPlayListener.onPrepared(mediaPlayer);
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                    startMusic(currentPlayPostion+1);
            }
        });

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(fileList.get(currentPlayPostion).getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }



    public void stopMusic(){
        mediaPlayer.pause();
    }

    public void startMusic(int position){
        currentPlayPostion = position;
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(fileList.get(position).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPreviousMusic(){
        currentPlayPostion = (currentPlayPostion -1 + fileList.size())%fileList.size();
        startMusic(currentPlayPostion);
    }

    public void startNextMusic(){
        currentPlayPostion = (currentPlayPostion +1 + fileList.size())%fileList.size();
        startMusic(currentPlayPostion);
    }

    public MediaPlayListener getMediaPlayListener() {
        return mediaPlayListener;
    }

    public void setMediaPlayListener(MediaPlayListener mediaPlayListener) {
        this.mediaPlayListener = mediaPlayListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("service destoryed");
    }

    public List<MMediaFile> getFileList() {
        return fileList;
    }

    public void restartMusic(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    public MMediaFile getPlayingFile(){
        return  fileList.get(currentPlayPostion);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static abstract class MediaPlayListener implements MediaPlayer.OnPreparedListener{

    }

}
