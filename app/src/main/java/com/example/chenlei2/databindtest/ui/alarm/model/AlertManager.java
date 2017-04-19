package com.example.chenlei2.databindtest.ui.alarm.model;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by wenlu on 2016/6/30 0030.
 */
public class AlertManager implements MediaPlayer.OnErrorListener {

    private MediaPlayer mMediaPlayer;


    private Context mContext;





    public AlertManager(Context context) {
        this.mContext = context;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);//循环
        mMediaPlayer.setOnErrorListener(this);
    }


   /* */

    /**
     * 初始化Player, Vibrator
     *
     *//*
    public void initMidiaPlayer(Context context, String ringName, boolean isLooping) {
        this.mContext = context;
        this.mRingNames = transitionRingName(ringName);
        this.mIsLooping = isLooping;

        AssetManager assetManager = context.getAssets();
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(ringName + ".mp3");
            if (mMediaPlayer == null) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.setLooping(isLooping);//循环

        } catch (Exception e) {
        }

    }*/
    private synchronized void resetAlert() {
        mMediaPlayer.stop();
        mMediaPlayer.reset();
    }

    public synchronized void pauseAlert() {
        try {
            if (mMediaPlayer.isPlaying()) {
                resetAlert();
            }else{
                mMediaPlayer.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mMediaPlayer.reset();
        }

    }

    public synchronized void startAlert(final String ringName) {
        pauseAlert();
        AssetManager assetManager = mContext.getAssets();
        try {
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(transitionRingName(ringName) + ".mp3");
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public synchronized void stopAlert() {
        pauseAlert();
    }




    private String transitionRingName(String ringName) {
        String ring = "ring";
        switch (ringName) {
            case "默认铃声":
                ring = "eat";
                break;
            case "该吃药了":
                ring = "ring";
                break;
        }
        return ring;
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mediaPlayer.reset();
        return true;
    }


}
