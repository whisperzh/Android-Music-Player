package com.example.learnintent.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.learnintent.R;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView exit;
    private Spinner myspinner;
    private String []quality=new String[]{"超高音质","中等音质","普通音质"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initUnits();
        initListeners();
        initToolBar();
    }

    private void initUnits(){
        toolbar=findViewById(R.id.setting_toolbar);
        myspinner=findViewById(R.id.quialityspinner);
        exit=findViewById(R.id.exit);
    }

    private void initListeners(){
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.myspinner,quality);
        myspinner.setAdapter(arrayAdapter);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"退出登录", Toast.LENGTH_SHORT).show();
                goBacktoLogin();
            }
        });
    }

    private void initToolBar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("设置");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    private void goBacktoLogin(){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        if(!getSharedPreferences("systemP",Context.MODE_PRIVATE).getBoolean("isLogin",false)) {
            myspinner.setSelection(2);
            myspinner.setEnabled(false);
            exit.setText("登录");
            exit.setTextColor(getResources().getColor(R.color.colorBlack));
            exit.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        }
        else {
            myspinner.setEnabled(true);
            myspinner.setSelection(1);
            exit.setText("退出登录");
            exit.setTextColor(getResources().getColor(R.color.colorWhite));
            exit.setBackgroundColor(getResources().getColor(R.color.cloudRed));
        }
        super.onResume();
    }
}
