package com.example.learnintent.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learnintent.R;
import com.example.learnintent.entity.Song;
import com.example.learnintent.services.MusicService;
import com.example.learnintent.utils.MergeImage;
import com.example.learnintent.utils.ResUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.learnintent.utils.ResUtils.loadCover;

public class LockScreenActivity extends AppCompatActivity {
    private static TextView dateText;
    private static TextView timeText;
    private static TextView songtext;
    private static TextView singertext;
    public static LockScreenActivity lockScreenActivity;
    public static LockScreenActivity getInstance(){
        if(lockScreenActivity==null)
            lockScreenActivity=new LockScreenActivity();
        return lockScreenActivity;
    }
    private static ImageView lockView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使该Activity在锁屏界面上面显示，别忘了给视频通话的Activity也加上
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.lock);
        init();
        new TimeThread().start();
        lockScreenActivity=this;
    }

    private void init(){
        lockView=findViewById(R.id.lockImage);
        dateText=findViewById(R.id.datetext);
        timeText =findViewById(R.id.timetext);
        songtext =findViewById(R.id.coversongName);
        singertext =findViewById(R.id.coversingerName);
    }

    public static void ChangeCover(){
        try {
            Song t=ResUtils.songlist.get(MusicService.position);
            String path=t.path;
            Bitmap m=MergeImage.FitTheScreenSizeImage(loadCover(path),getInstance());
            lockView.setImageBitmap(loadCover(path));
            songtext.setText(t.songName);
            singertext.setText(t.singer);
        }catch (Exception e){}

    }

    public static class TimeThread extends Thread{
        @Override
        public void run() {
            super.run();
            do{
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);

        }
    }
    private static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    ChangeCover();
                    Date date=new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM");
                    String currentDate=formatter.format(date);
                    String day=currentDate.substring(0,2);
                    String month=currentDate.substring(3);
                    Calendar calendar=Calendar.getInstance();
                    int week=calendar.get(calendar.DAY_OF_WEEK)-1;

                    String str=month+"月"+day+"日"+" "+intToWeek(week);
                    dateText.setText(str);
                    timeText.setText(new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis())));
                    break;
            }
            return false;
        }
    });

    private static String intToWeek(int i){
        switch (i)
        {
            case 0:
                return "星期日";
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";

        }
        return "星期一";
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
