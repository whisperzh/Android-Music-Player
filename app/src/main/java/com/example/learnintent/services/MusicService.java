package com.example.learnintent.services;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.learnintent.Activities.LockScreenActivity;
import com.example.learnintent.Activities.MusicActivity;
import com.example.learnintent.BroadcastReceivers.BroadcastReceiverListener;
import com.example.learnintent.R;
import com.example.learnintent.entity.Song;
import com.example.learnintent.utils.ResUtils;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {

    private BroadcastReceiverListener screenListener;
    public static boolean isStop = true;
    public static MediaPlayer mediaPlayer;
    public static Song song;
    public static int position = -1;
    public static SharedPreferences sp;
    static Timer timer = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setScreenListener(getBaseContext());
        sp = getSharedPreferences("whatisplaying.txt", Context.MODE_PRIVATE);
        if (ResUtils.songlist == null)
            ResUtils.songlist = ResUtils.fetchfromDB(ResUtils.dbHelper.getReadableDatabase());
        mediaPlayer = new MediaPlayer();
        try {
            String path = sp.getString("path", null);
            int progression = sp.getInt("progression", 0);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.seekTo(progression);
        } catch (Exception e) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        position = intent.getIntExtra("seq", -1);
        if (isStop) {
            Play();
        } else {
            Pause();
            Play();
        }
        return START_NOT_STICKY;
    }

    public static void Play() {
        if(timer!=null)
            timer.cancel();
        try {
            MusicActivity.DurationBar.setProgress(0);
        }catch (Exception e){}
        isStop = false;
            mediaPlayer.reset();//会报-38
            Log.e("-38","第82行，偶发性错误");
            //mediaPlayer.release();
        if (position >= 0 && position <= ResUtils.songlist.size()) {
            song = ResUtils.songlist.get(position);
            updateSharedPreference();Log.e("-38","87行");
            mediaPlayer = new MediaPlayer();
            try {
                try {
                    MusicActivity.pauseImgv.setImageResource(R.drawable.play_pause);
                    Log.e("-38","92行");
                } catch (NullPointerException e) {
                }
                //MainActivity.Songname.setText(song.songName);
                mediaPlayer.setDataSource(song.path);Log.e("-38","95行");
                mediaPlayer.prepare();
                mediaPlayer.start();
                //new MusicActivity.MusicThread().start();
                updataSeekbar();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void Pause() {
        isStop = true;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            MusicActivity.pauseImgv.setImageResource(R.drawable.play_btn_play);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void CONTINUE() {
        if (!mediaPlayer.isPlaying() && isStop) {
            mediaPlayer.start();
            MusicActivity.pauseImgv.setImageResource(R.drawable.play_pause);
        }
        isStop = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void changeMusic(int songnum) {
        position=songnum;
        mediaPlayer.stop();
        Play();
    }

    public void setIconPlayandStop(int input) {
        if (input == 0)//开始播放
        {
            try {
                MusicActivity.pauseImgv.setImageResource(R.drawable.play_pause);
            } catch (NullPointerException e) {
            }
        } else {
            try {
                MusicActivity.pauseImgv.setImageResource(R.drawable.play_btn_play);
            } catch (NullPointerException e) {
            }
        }
    }

    public static void updateSharedPreference() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("songName", song.songName);
        editor.putString("singerName", song.singer);
        editor.putString("path", song.path);
        editor.putInt("wholeDuration", song.duration);
        //editor.putInt("progression", mediaPlayer.getCurrentPosition());
        ResUtils.updateSTACK(position);
        editor.commit();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        screenListener.unregisterBroadcastReceiver();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        super.onDestroy();
        //先停止 再释放


    }

    public static void updataSeekbar() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int duration = mediaPlayer.getDuration();
                int currentPosition = mediaPlayer.getCurrentPosition();

                Message msg = Message.obtain();
                msg.what = 0;

                Bundle bundle = new Bundle();
                bundle.putInt("duration", duration);
                bundle.putInt("currentPosition", currentPosition);
                msg.setData(bundle);
                MusicActivity.handler.sendMessage(msg);
            }
        };

        timer = new Timer();
        timer.schedule(task, 0, 1000);
    }

    private void setScreenListener(final Context ct) {
        screenListener=new BroadcastReceiverListener(MusicService.this);
        screenListener.start(new BroadcastReceiverListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {// 开屏

            }
            @Override
            public void onScreenOff() {// 锁屏
                //你的业务逻辑
                 Intent it=new Intent(ct, LockScreenActivity.class);
                 it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                 startActivity(it);
            }
            @Override
            public void onUserPresent() {// 解锁
                //你的业务逻辑
                LockScreenActivity.lockScreenActivity.finish();
            }

            @Override
            public void onHome() {//home主页键
                //你的业务逻辑
            }
        });
    }


}
