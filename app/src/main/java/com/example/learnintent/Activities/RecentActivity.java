package com.example.learnintent.Activities;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.example.learnintent.Adaptor.MusicListAdaptor;
import com.example.learnintent.DataBase.DatabaseHelper;
import com.example.learnintent.R;
import com.example.learnintent.utils.ResUtils;

import java.util.List;

public class RecentActivity  extends BaseActivity  {
    private Toolbar toolbar;
private ListView recentList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent_music_page);
        initView();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("最近播放");
        }
        makelist();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            this.finish();
        }
        return true;
    }

    private void initView(){
        toolbar=findViewById(R.id.loved_toolbar);
        recentList=findViewById(R.id.lovedlist);
    }

    private void makelist(){
        if(ResUtils.dbHelper==null)
            ResUtils.dbHelper= DatabaseHelper.getInstance(this);
        List temp= ResUtils.popSTACK(getBaseContext());
        MusicListAdaptor adaptor=new MusicListAdaptor(this,temp,R.layout.recent_music_page,this);
        recentList.setAdapter(adaptor);
    }

}
