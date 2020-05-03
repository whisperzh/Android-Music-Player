package com.example.learnintent.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.learnintent.Fragments.PlayBarFragment;
import com.example.learnintent.R;
import com.example.learnintent.services.MusicService;

public abstract class BaseActivity extends AppCompatActivity {

    public PlayBarFragment playBarFragment;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show();
    }

    public void show(){
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (playBarFragment == null) {
            playBarFragment = PlayBarFragment.newInstance();
            ft.replace(R.id.fragment_playbar, playBarFragment).commit();
        }else {
//            playBarFragment.getFragmentManager().beginTransaction().remove(playBarFragment);
//            playBarFragment.isDetached();
//           ft.show(playBarFragment).commit();
            playBarFragment = PlayBarFragment.newInstance();
            ft.replace(R.id.fragment_playbar, playBarFragment).commit();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        show();
    }


}
