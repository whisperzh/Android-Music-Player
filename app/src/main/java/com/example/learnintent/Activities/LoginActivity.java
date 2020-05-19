package com.example.learnintent.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.learnintent.R;

public class LoginActivity extends AppCompatActivity {
    private TextView jump;
    private final String correctUsername="admin";
    private final String getUsername(){
        String str=new String();
        str=username.getText().toString();
        return str;
    }
    private String correctPassword="123";
    private String getPassword(){
        String str=new String();
        str=password.getText().toString();
        return str;
    }
    protected SharedPreferences sharedPreferences;
    private ImageView imageView;
    private Button LoginButton;
    private EditText username;
    private EditText password;
    public static int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initUnits();
       sharedPreferences=getSharedPreferences("userInfo",Context.MODE_PRIVATE);
       String rightUser=sharedPreferences.getString("username","");
       String rightKey=sharedPreferences.getString("password","");
       try {
           username.setText(rightUser);
           password.setText(rightKey);
         }catch(NullPointerException e){
        e.printStackTrace();
        }

    }

    public void initUnits(){
        jump=findViewById(R.id.jumplogin);
        username=findViewById(R.id.username);
        LoginButton=findViewById(R.id.login);
        imageView=findViewById(R.id.startimg);
        password=findViewById(R.id.password);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(false);
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String currentUser= getUsername();
                String currentPassword= getPassword();
                if(currentPassword.equals(correctPassword)&& currentUser.equals(correctUsername)) {
                    sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("username",correctUsername);
                    editor.putString("password",correctPassword);
                    editor.commit();
                    imageView.setImageDrawable(setTint(imageView.getDrawable(),ColorStateList.valueOf(getColor(R.color.colorGreen))));
                    Toast.makeText(LoginActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    Login(true);

                }else {
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                    imageView.setImageDrawable(setTint(imageView.getDrawable(),ColorStateList.valueOf(getColor(R.color.cloudRed))));
                }
            }
        });
    }

    Drawable setTint(Drawable drawable,ColorStateList colors){
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    private void Login(boolean ifHasAccount){
        Intent intent=new Intent(this,MainActivity.class);
        Bundle bundle=new Bundle();
        bundle.putBoolean("isVIP",ifHasAccount);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }




}
