package com.example.learnintent.Activities;

import android.animation.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learnintent.R;
import com.example.learnintent.entity.Song;
import com.example.learnintent.services.MusicService;
import com.example.learnintent.utils.MergeImage;
import com.example.learnintent.utils.ResUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.animation.Animation.INFINITE;
import static com.example.learnintent.utils.ResUtils.getmusic;
import static com.example.learnintent.utils.ResUtils.loadCover;

public class MusicActivity extends AppCompatActivity {
    //public  static ObjectAnimator objectAnimator;
    private static ImageView diskView;
    private ImageView pre, next;
    private ImageView back;
    private static Context context;
    public static ImageView pauseImgv;
    private static TextView currentTime, WholeDuration;
    public static SeekBar DurationBar;
    private static TextView titleTv;
    private static TextView ArtistTv;
    private static SharedPreferences sharedPreferences;
    //public static int position;

    //Handler实现向主线程进行传值
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int states=msg.what;
            if(states==0){
                try{
                Bundle bundle=msg.getData();
                int duration= bundle.getInt("duration");
                int currentPosition= bundle.getInt("currentPosition");
                DurationBar.setProgress(currentPosition);
                currentTime.setText(formatTime(currentPosition));
                if(currentPosition>=duration-1000) {
                    MusicService.changeMusic((MusicService.position + 1) % ResUtils.songlist.size());
                    upDateUi();
                    LockScreenActivity.ChangeCover();
                }
                }catch (Exception e){}
                //
            }
            //currentTime.setText(formatTime(msg.what));
        }
    };

    //创建一个类MusicThread实现Runnable接口，实现多线程
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_page);
        context=getBaseContext();
        sharedPreferences = this.getSharedPreferences("whatisplaying.txt", Context.MODE_PRIVATE);
        bindID();
        getmusic(getBaseContext());//绑定控件！！！
        upDateUi();
        //new MusicThread().start();//启动线程
        setPicForDiskView();
        pauseImgv.setOnClickListener(new pauseButtonListner());
    }


    private void bindID() {
        diskView = findViewById(R.id.diskview);
        next = findViewById(R.id.next_button);
        pre = findViewById(R.id.preview_button);
        titleTv = findViewById(R.id.music_title_tv);
        ArtistTv = findViewById(R.id.music_artist_tv);
        currentTime = findViewById(R.id.current_time);
        WholeDuration = findViewById(R.id.whole_Duration);
        DurationBar = findViewById(R.id.Duration);
        pauseImgv = findViewById(R.id.play_button);
        back = findViewById(R.id.hide_music_page);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                MusicService.changeMusic((MusicService.position + 1) % ResUtils.songlist.size());
                upDateUi();
                setPicForDiskView();
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                MusicService.changeMusic((MusicService.position - 1 + ResUtils.songlist.size()) % ResUtils.songlist.size());
                upDateUi();
                setPicForDiskView();

            }
        });

        DurationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {               //seekbar设置监听，实现指哪放到哪
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicService.Pause();
                seekBar.setProgress(seekBar.getProgress());
                MusicService.mediaPlayer.seekTo(seekBar.getProgress());
                MusicService.CONTINUE();
            }
        });//改变进度条
    }



//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public static void diskRotationSeting() {
//       if(objectAnimator==null) {
//           objectAnimator = ObjectAnimator.ofFloat(diskView, "rotation", 0f, 360f);
//           //设置转一圈要多长时间
//           objectAnimator.setDuration(8000);
//           //设置旋转速率
//           objectAnimator.setInterpolator(new LinearInterpolator());
//           //设置循环次数 -1为一直循环
//           objectAnimator.setRepeatCount(INFINITE);
//       }
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static void setPicForDiskView() {
        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.play_page_disc);
        Bitmap bm;
        Song song = MusicService.song;
        if (loadCover(song.path) != null) {
            //Bitmap bgbm = BlurUtil.doBlur(Common.musicList.get(position).albumBip, 10, 5);//将专辑虚化
            //bgImgv.setImageBitmap(bgbm);                                    //设置虚化后的专辑图片为背景
            //BitmapFactory.decodeResource用于根据给定的资源ID从指定的资源文件中解析、创建Bitmap对象。
            bm = MergeImage.mergeThumbnailBitmap(bitmap1, loadCover(song.path));//将专辑图片放到圆盘中
        } else {
            Bitmap temp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.innerdisk);
            bm = MergeImage.mergeThumbnailBitmap(bitmap1, temp);//将专辑图片放到圆盘中
        }
        diskView.setImageBitmap(bm);
       // diskRotationSeting();
        //实例化，设置旋转对象
        /*

        //实例化，设置旋转对象
        objectAnimator = ObjectAnimator.ofFloat(discImagv, "rotation", 0f, 360f);
        //设置转一圈要多长时间
        objectAnimator.setDuration(8000);
        //设置旋转速率
        objectAnimator.setInterpolator(new LinearInterpolator());
        //设置循环次数 -1为一直循环
        objectAnimator.setRepeatCount(-1);
        //设置转一圈后怎么转
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.start();

        rotateAnimation = new RotateAnimation(-25f, 0f, Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF, 0.1f);
        rotateAnimation.setDuration(500);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setStartOffset(500);
        needleImagv.setAnimation(rotateAnimation);
        rotateAnimation.cancel();


        rotateAnimation2 = new RotateAnimation(0f, -25f, Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF, 0.1f);
        rotateAnimation2.setDuration(500);
        rotateAnimation2.setInterpolator(new LinearInterpolator());
        rotateAnimation2.setRepeatCount(0);
        rotateAnimation2.setFillAfter(true);
        needleImagv.setAnimation(rotateAnimation2);
        rotateAnimation2.cancel();

*/
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void upDateUi() {
        Song song=ResUtils.songlist.get(MusicService.position);
        titleTv.setText(song.songName);     //更新标题
        ArtistTv.setText(song.singer);      //更新歌手
        WholeDuration.setText(formatTime(song.duration));
        DurationBar.setMax(song.duration);
        currentTime.setText(formatTime(MusicService.mediaPlayer.getCurrentPosition()));
        if (MusicService.mediaPlayer.isPlaying())
            pauseImgv.setImageResource(R.drawable.icon_pause);
        else
            pauseImgv.setImageResource(R.drawable.icon_play);
        DurationBar.setProgress(MusicService.mediaPlayer.getCurrentPosition());
//        if(objectAnimator!=null)
//            objectAnimator.pause();
        setPicForDiskView();
        //objectAnimator.start();
    }

    public static class pauseButtonListner implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
            if (MusicService.song != null) {
                if (!MusicService.isStop) {
                    //objectAnimator.pause();
                    MusicService.isStop=true;
                    MusicService.Pause();
                } else {
                    MusicService.CONTINUE();
                    //objectAnimator.resume();
                }

            }

        }
    }


//    //格式化数字
    private static String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");    //规定固定的格式
        String totaltime = simpleDateFormat.format(date);
        return totaltime;
    }



}
