package com.example.learnintent.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.learnintent.Adaptor.MyPageAdaptor;
import com.example.learnintent.DataBase.DatabaseHelper;
import com.example.learnintent.Fragments.PlayBarFragment;
import com.example.learnintent.R;
import com.example.learnintent.utils.ResUtils;
import com.master.permissionhelper.PermissionHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //===============学习drawerLayerOUT=========================//
    private DrawerLayout drawerLayout;
    private ConstraintLayout constraintLayout;
    private TextView Mine, Search;
    public static int oldclick = -1;
    private PlayBarFragment pbf;
    private View view1, view2;
    private ViewPager viewPager;  //对应的viewPager
    //public List<Song> songs;
    private List<View> pages;//view数组
    private LinearLayout local, recent, love;
    private SharedPreferences sharedPreferences;
    private boolean isFirst;
    //=====================以下为权限变量====================//
    private PermissionHelper permissionHelper;
    //========================================================//



    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layoutlearn);
        sharedPreferences=getSharedPreferences("FirstTime", Context.MODE_PRIVATE);
        isFirst= sharedPreferences.getBoolean("isFirst",true);
        getPemitted();
        init();
        viewPager.setAdapter(new MyPageAdaptor(pages));
        local.setOnClickListener(this);
        recent.setOnClickListener(this);
        love.setOnClickListener(this);
        //点击事件
        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        // songs = new ArrayList<>();

        drawerLayout.setOnClickListener(this);
       /*  MusicListAdaptor myAdapter = new MusicListAdaptor(this, songs);
       SongsView.setAdapter(myAdapter);
        SongsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //启动service
                if(oldclick!=position){
                Intent musicit = new Intent(MainActivity.this, MusicService.class);
                musicit.putExtra("seq",position);
                startService(musicit);
                }
                oldclick=position;
                //启动第二个activity
                Intent atit=new Intent(MainActivity.this,MusicActivity.class);
                atit.putExtra("seq",position);
                startActivity(atit);
               // renewAllunitsinView();
            }
        });*/

    }



    private void init() {
        Mine = findViewById(R.id.MINE);
        Search = findViewById(R.id.SEARCH);
        Mine.setOnClickListener(this);
        Search.setOnClickListener(this);
        drawerLayout = findViewById(R.id.drawer_menu);
        constraintLayout = findViewById(R.id.nav);
        viewPager = findViewById(R.id.viewpager1);
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.home_p1, null);
        view2 = inflater.inflate(R.layout.layout2, null);
        //view3 = inflater.inflate(R.layout.layout3, null);
        pages = new ArrayList<View>();// 将要分页显示的View装入数组中
        pages.add(view1);
        pages.add(view2);
       // pages.add(view3);
        local = view1.findViewById(R.id.home_local_music_ll);
        recent = view1.findViewById(R.id.home_recently_music_ll);
        love = view1.findViewById(R.id.home_my_love_music_ll);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
;
            }

            @Override
            public void onPageSelected(int position) {//当前页

                if (position == 0) {

                    Mine.setTextColor(0xfffefefe);
                    Search.setTextColor(0x88fefefe);
                } else {
                    Search.setTextColor(0xfffefefe);
                    Mine.setTextColor(0x88fefefe);
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.MINE:
                //实现点击TextView切换fragment
                viewPager.setCurrentItem(0);
                break;
            case R.id.SEARCH:
                viewPager.setCurrentItem(1);
                break;
            case R.id.home_local_music_ll:
                Intent intent1 = new Intent(this, LocalActivity.class);
                startActivity(intent1);
                break;
            case R.id.home_recently_music_ll:
                Intent intent2 = new Intent(this, RecentActivity.class);
                startActivity(intent2);
                break;
            case R.id.home_my_love_music_ll:
                Intent intent3 = new Intent(this, LovedActivity.class);
                startActivity(intent3);
                break;
            case R.id.fragment_playbar:
                break;
            default:
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        pbf = PlayBarFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_playbar, pbf).commit();
    }

    public void getPemitted(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!=  PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=  PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=  PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){//
            case 0://如果申请权限回调的参数
                new ResUtils.getMusicAsync(MainActivity.this).execute(MainActivity.this);
                Log.e("LLLLLLLLLLLLL","oading");
//                if (grantResults.length > 0 ) {
//                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    Toast.makeText(this,"申请成功",Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this,"拒绝权限",Toast.LENGTH_SHORT).show();
//                }
               break;

        }

    }

}
