package com.example.learnintent.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.learnintent.Activities.MusicActivity;
import com.example.learnintent.R;
import com.example.learnintent.entity.Song;
import com.example.learnintent.services.MusicService;
import com.example.learnintent.utils.ResUtils;

public class PlayBarFragment extends Fragment implements View.OnClickListener {

    LinearLayout bottom_Bar;
    public static TextView bottom_songName, bottom_singer;
    ImageView bottom_moreinfo, bottom_play, bottom_next;
    SharedPreferences sharedPreferences;

    public static PlayBarFragment newInstance() {
        return new PlayBarFragment();//synchronized
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("whatisplaying.txt", Context.MODE_PRIVATE);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playbar, container, false);
        init(view);
        try {
            updateSonginFG();
        } catch (NullPointerException e) {
        }
        return view;
    }

    public void init(View view) {
        bottom_Bar = view.findViewById(R.id.home_activity_playbar_ll);
        bottom_moreinfo = view.findViewById(R.id.play_menu_iv);
        bottom_next = view.findViewById(R.id.next_iv);
        bottom_play = view.findViewById(R.id.play_iv);
        bottom_singer = view.findViewById(R.id.home_singer_name_tv);
        bottom_songName = view.findViewById(R.id.home_music_name_tv);
        bottom_Bar.setOnClickListener(this);
        bottom_next.setOnClickListener(this);
        bottom_play.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_activity_playbar_ll:
                if (MusicService.mediaPlayer != null) {
                    //if(sharedPreferences.getString("path","null")!=null&&sharedPreferences.getInt("wholeDuration",0)!=0){
                    Intent intent = new Intent(getActivity(), MusicActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this.getContext(), "当前没有音乐在播放哦", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.play_menu_iv:
                break;
            case R.id.next_iv:
                try{
                    MusicService.changeMusic((MusicService.position + 1) % ResUtils.songlist.size());
                    initPlayBotton();
                    updateSonginFG();
                }catch (Exception e){
                    Toast.makeText(this.getContext(), "发生了一个错误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.play_iv:
                try {
                    if (MusicService.mediaPlayer != null) {
                        if (!MusicService.isStop) {
                            MusicService.Pause();
                            bottom_play.setImageResource(R.drawable.play);
                        } else {
                            bottom_play.setImageResource(R.drawable.pause);
                            MusicService.CONTINUE();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(this.getContext(), "当前没有音乐在播放哦", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }


    }

    public void updateSonginFG() {//static
        try {
            String singer = sharedPreferences.getString("singerName", "-");
            String songName = sharedPreferences.getString("songName", "-");
            bottom_singer.setText(singer);
            bottom_songName.setText(songName);
        } catch (Exception e) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            initPlayBotton();
            updateSonginFG();
        } catch (Exception e) {
        }
    }

    private void initPlayBotton(){
        try{
            if (MusicService.mediaPlayer != null) {
                if(MusicService.isStop)
                    bottom_play.setImageResource(R.drawable.play);
                else
                    bottom_play.setImageResource(R.drawable.pause);
            }
        }catch (Exception e){}

    }
}
