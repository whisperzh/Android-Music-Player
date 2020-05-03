package com.example.learnintent.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.learnintent.Fragments.PlayBarFragment;
import com.example.learnintent.Fragments.singer_Fragment;
import com.example.learnintent.Fragments.singleSong_Fragments;
import com.example.learnintent.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class LocalActivity extends BaseActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private List<String> titleList = new ArrayList<>(2);
    private List<Fragment> fragments = new ArrayList<>(2);
    private TabLayout tabLayout;
    private singleSong_Fragments singleSong;
    private singer_Fragment singer_fragment;
    private PlayBarFragment pbf;
    private Button iinit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_music_page);
        initTabs();
        initUnits();

//        Bundle bundle = getIntent().getExtras();
//        String name = bundle.getString(Constants.NAME);
//        String userid = bundle.getString(Constants.USERID);
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put(Constants.NAME, name);
//        map.put(Constants.USERID,userid);
//        PlayBarFragment fragment = PlayBarFragment.newInstance();
//        getSupportFragmentManager().beginTransaction().add(R.id.fragment_playbar,fragment).commit();

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("本地音乐");
        }
    }

    private void initTabs() {
        titleList.add("单曲");
        titleList.add("歌手");
        if (singleSong == null) {
            singleSong = new singleSong_Fragments();
            fragments.add(singleSong);
        }
        if (singer_fragment == null) {
            singer_fragment = new singer_Fragment();
            fragments.add(singer_fragment);
        }
    }

    private void initUnits() {
        toolbar = findViewById(R.id.local_music_toolbar);
        tabLayout = findViewById(R.id.local_tab);
        viewPager = findViewById(R.id.local_viewPager);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * 用来显示tab上的名字
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }


}
