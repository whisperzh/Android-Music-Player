package com.example.learnintent.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.learnintent.Activities.MainActivity;
import com.example.learnintent.Activities.MusicActivity;
import com.example.learnintent.Adaptor.MusicListAdaptor;
import com.example.learnintent.R;
import com.example.learnintent.entity.Song;
import com.example.learnintent.services.MusicService;
import com.example.learnintent.utils.ResUtils;
import com.master.permissionhelper.PermissionHelper;

import java.util.ArrayList;
import java.util.List;

public class singleSong_Fragments extends Fragment {
    private String TAG = "singleSong";
    ListView SongsView;
    int oldclick = -1;
    List<Song> songs;
    private PermissionHelper permissionHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        songs=getArguments().getParcelableArrayList("list");
        final View view = inflater.inflate(R.layout.list_view_page1, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        SongsView = view.findViewById(R.id.SONGLIST);
        fetchSongs();
    }

    public void fetchSongs() {
        //SQLiteDatabase db = ResUtils.dbHelper.getReadableDatabase();
        //songs = ResUtils.fetchfromDB(db);
        try {
            MusicListAdaptor myAdapter = new MusicListAdaptor(getContext(), songs,R.layout.local_music_page,this.getActivity());
            SongsView.setAdapter(myAdapter);
            SongsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //启动service
                    if (oldclick != position) {
                        Intent musicit = new Intent(getActivity(), MusicService.class);
                        musicit.putExtra("seq", position);
                        getActivity().startService(musicit);
                    }
                    oldclick = position;
                    Intent atit = new Intent(getActivity(), MusicActivity.class);
                    atit.putExtra("seq", position);
                    startActivity(atit);
                    //启动第二个activity
                }
            });
        } catch (Exception e) {
        }
    }
    //申请权限

}
