package com.example.learnintent.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.core.app.NavUtils;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mInstance = null;
    private static final int version = 1;
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public DatabaseHelper(Context context) {
        super(context, "LOCAL", null, version);
    }
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "create table local_base(_id Integer primary key autoincrement,songName varchar(20),singerName varchar(20),path text,duration int,isLiked boolean)";
        String sql2 = "create table liked_base(_id Integer primary key autoincrement,songName varchar(20),singerName varchar(20),path text,duration int,isLiked boolean)";
        db.execSQL(sql1);
        db.execSQL(sql2);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+"local_base");
        db.execSQL("drop table if exists "+"liked_base");
        onCreate(db);
    }
   public static synchronized DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }

}
