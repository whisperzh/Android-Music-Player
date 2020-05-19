package com.example.learnintent.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnintent.R;
import com.example.learnintent.entity.Song;
import com.example.learnintent.utils.ResUtils;

import java.util.List;

public class myPopupWindow extends PopupWindow {
    private View view;
    private Activity activity;
    private TextView nameTv;
    private View parentView;
    private TextView iflikes;
    private LinearLayout addLl;
    private LinearLayout loveLl;
    //    private LinearLayout ringLl;
    private LinearLayout deleteLl;
    private LinearLayout cancelLl;
    private Song song;

    public myPopupWindow(Activity activity, Song song,View parentView) {
        super(activity);
        this.activity = activity;
        this.song = song;
        this.parentView = parentView;
        initView();
    }

    private void initView(){
        this.view = LayoutInflater.from(activity).inflate(R.layout.pop_window_menu, null);
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高,不设置显示不出来
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 设置外部可点击
        this.setOutsideTouchable(true);

        // 设置弹出窗体的背景
        this.setBackgroundDrawable(activity.getResources().getDrawable(R.color.colorWhite));

        // 设置弹出窗体显示时的动画，从底部向上弹出
        //this.setAnimationStyle(R.style.pop_window_animation);


        // 添加OnTouchListener监听判断获取触屏位置，如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = view.getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        iflikes=view.findViewById(R.id.popwin_love_tv);
        nameTv = view.findViewById(R.id.popwin_name_tv);
        addLl =  view.findViewById(R.id.popwin_add_rl);
        loveLl =  view.findViewById(R.id.popwin_love_ll);
        deleteLl =  view.findViewById(R.id.popwin_delete_ll);
        cancelLl = view.findViewById(R.id.popwin_cancel_ll);
        nameTv.setText("歌曲： " + song.songName);
        boolean temparyParameter=false;
        if(ResUtils.ifLike(song,ResUtils.dbHelper.getWritableDatabase()))
        {
            iflikes.setText("取消喜欢");
            temparyParameter=true;
        }


        addLl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        final boolean finalTemparyParameter = temparyParameter;
        loveLl.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(finalTemparyParameter)
                {
                    Toast.makeText(activity.getBaseContext(),"取消喜爱",Toast.LENGTH_SHORT).show();
                    ResUtils.deletLovedSong(ResUtils.dbHelper.getWritableDatabase(),song);
                    dismiss();
                    return;
                }
                Toast.makeText(activity.getBaseContext(),"添加喜爱成功",Toast.LENGTH_SHORT).show();
                ResUtils.putLovedSong(ResUtils.dbHelper.getWritableDatabase(),song);
                dismiss();
            }
        });


        deleteLl.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dismiss();
            }
        });

        cancelLl.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
