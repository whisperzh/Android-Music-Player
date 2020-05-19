package com.example.learnintent.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnintent.Activities.MainActivity;
import com.example.learnintent.R;
import com.example.learnintent.entity.Song;
import com.example.learnintent.utils.ResUtils;

import java.util.List;

public class MusicListAdaptor extends BaseAdapter {
    private Context context;
    private List<Song> list;
    private PopupWindow mPopWindow;
    private Activity activity;
    private int parentView;

    public MusicListAdaptor(Context context, List<Song> list,int parentView,Activity activity) {
        this.context = context;
        this.list = list;
        this.parentView=parentView;
        this.activity=activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {


        final Myholder myholder;

        if (view == null) {
            myholder = new Myholder();
            view = LayoutInflater.from(context).inflate(R.layout.song_item, null);

            //myholder.t_position = view.findViewById(R.id.t_postion);
            myholder.moreinfo=view.findViewById(R.id.moreinfo);
            myholder.t_song = view.findViewById(R.id.musicitem_title_tv);
            myholder.t_singer = view.findViewById(R.id.musicitem_artist_tv);
            myholder.v = view.findViewById(R.id.musicitem_playing_v);
            // myholder.t_duration = view.findViewById(R.id.t_duration);

            view.setTag(myholder);

        } else {
            myholder = (Myholder) view.getTag();
        }
        final List<Song> temp=ResUtils.fetchfromDB(ResUtils.dbHelper.getWritableDatabase());
//        if(ResUtils.ifLike(temp.get(position),ResUtils.dbHelper.getWritableDatabase()))
//        {
//            myholder.loves.setImageResource(R.drawable.love_hover);
//        }else
//        {
//            myholder.loves.setImageResource(R.drawable.love);
//        }
//
//        myholder.loves.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(ResUtils.ifLike(temp.get(position),ResUtils.dbHelper.getWritableDatabase()))
//                {
//                    Toast.makeText(context,"取消喜爱",Toast.LENGTH_SHORT).show();
//                    myholder.loves.setImageResource(R.drawable.love);
//                    ResUtils.deletLovedSong(ResUtils.dbHelper.getWritableDatabase(),list.get(position));
//                    return;
//                }
//                Toast.makeText(context,"添加喜爱成功",Toast.LENGTH_SHORT).show();
//                myholder.loves.setImageResource(R.drawable.love_hover);
//                ResUtils.putLovedSong(ResUtils.dbHelper.getWritableDatabase(),list.get(position));
//            }
//        });
        myholder.t_song.setText(list.get(position).songName);
        myholder.t_singer.setText(list.get(position).singer);
        // String time = ResUtils.formatTime(list.get(position).duration);
        myholder.moreinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(temp.get(position));
            }
        });
        return view;
    }


    class Myholder {
        public TextView  t_song, t_singer;
        public ImageView moreinfo;
        public View v;
    }

    private void showPopupWindow(Song song) {
        //设置contentView
        mPopWindow=new myPopupWindow(activity,song,LayoutInflater.from(context).inflate(parentView,null));
        mPopWindow.showAtLocation(LayoutInflater.from(context).inflate(parentView, null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha=0.7f;
        activity.getWindow().setAttributes(params);

        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha=1f;
                activity.getWindow().setAttributes(params);
            }
        });
    }


}
