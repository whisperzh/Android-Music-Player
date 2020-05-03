package com.example.learnintent.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.learnintent.R;
import com.example.learnintent.entity.Song;

import java.util.List;

public class MusicListAdaptor extends BaseAdapter {
    private Context context;
    private List<Song> list;

    public MusicListAdaptor(Context context, List<Song> list) {
        this.context = context;
        this.list = list;
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
    public View getView(int position, View view, ViewGroup parent) {


        Myholder myholder;

        if (view == null) {
            myholder = new Myholder();
            view = LayoutInflater.from(context).inflate(R.layout.song_item, null);

            //myholder.t_position = view.findViewById(R.id.t_postion);
            myholder.t_song = view.findViewById(R.id.musicitem_title_tv);
            myholder.t_singer = view.findViewById(R.id.musicitem_artist_tv);
            myholder.v = view.findViewById(R.id.musicitem_playing_v);
            // myholder.t_duration = view.findViewById(R.id.t_duration);

            view.setTag(myholder);

        } else {
            myholder = (Myholder) view.getTag();
        }
        myholder.t_song.setText(list.get(position).songName);
        myholder.t_singer.setText(list.get(position).singer);
        // String time = ResUtils.formatTime(list.get(position).duration);
        return view;
    }


    class Myholder {
        public TextView t_position, t_song, t_singer, t_duration;
        public View v;
    }
}
