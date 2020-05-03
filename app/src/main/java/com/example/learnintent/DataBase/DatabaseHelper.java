package com.example.learnintent.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table local_base(_id Integer primary key autoincrement,songName varchar(20),singerName varchar(20),path text,duration int,isLiked boolean)";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+"local_base");
        onCreate(db);
    }


}
