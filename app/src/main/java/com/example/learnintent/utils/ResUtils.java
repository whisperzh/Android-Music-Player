package com.example.learnintent.utils;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.example.learnintent.Activities.MainActivity;
import com.example.learnintent.Activities.MusicActivity;
import com.example.learnintent.DataBase.DatabaseHelper;
import com.example.learnintent.entity.Song;
import com.example.learnintent.services.MusicService;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ResUtils {
    public static List<Song> songlist;
    public static Song song;
    public static DatabaseHelper dbHelper;
    private static ProgressDialog progressDialog;

    public static List<Song> getmusic(Context context) {
        dbHelper = new DatabaseHelper(context, "LOCAL", null, 1);
        SQLiteDatabase SONGBASE = dbHelper.getWritableDatabase();
        songlist = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                , null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor.moveToFirst()) {
            do {
                song = new Song();
                song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
//                把歌曲名字和歌手切割开
                song.songName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                if (song.size > 1000 * 800) {
                    if (song.songName != null && song.songName.contains("-") && !song.songName.matches(".[0,9]{4,}.")) {

                        song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                        song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                        String[] str = song.songName.split("-");
                        song.singer = str[0];
                        song.songName = str[1];
                        song.bitmap = loadCover(song.path);
                        songlist.add(song);
                        SONGBASE.insert("local_base", null, songToValue(song,false));
                        // Cursor sc=SONGBASE.query("local_base",new String[]{"songName"},null,null,null,null,null);
//                        if(sc.moveToNext())
//                        {
//                            String st6r=sc.getString(sc.getColumnIndex("songName"));
//                        }
                    }

                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return songlist;
    }

    //    转换歌曲时间的格式
//    public static String formatTime(int time) {
//        if (time / 1000 % 60 < 10) {
//            String tt = time / 1000 / 60 + ":0" + time / 1000 % 60;
//            return tt;
//        } else {
//            String tt = time / 1000 / 60 + ":" + time / 1000 % 60;
//            return tt;
//        }
//    }

    public static Bitmap loadCover(String path) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(path);
        byte[] cover = null;
        if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
            cover = mediaMetadataRetriever.getEmbeddedPicture();
            Bitmap bitmap = BitmapFactory.decodeByteArray(cover, 0, cover.length);
            return bitmap;
        }
        return null;
    }

    public static class getMusicAsync extends AsyncTask<Context, Integer, List> {
        private Context ct;
        public getMusicAsync(Context ct){
            super();
            this.ct=ct;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("sadsad","startloqding");
            initProgressDialog(ct);
            initProgressDialog(ct);
            progressDialog.show();
        }

        @Override
        protected List doInBackground(Context... contexts) {
            Context c = contexts[0];
            //initSTACK();
            return getmusic(c);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
           // super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            progressDialog.dismiss();
        }
    }

    public static ContentValues songToValue(Song song, boolean isLiked) {
        ContentValues values = new ContentValues();
        values.put("songName", song.songName);
        values.put("singerName", song.singer);
        values.put("duration",song.duration);
        values.put("path", song.path);
        values.put("isLiked", isLiked);
        return values;
    }

    public static List<Song> fetchfromDB(SQLiteDatabase db) {
        List<Song> l = new ArrayList();
        Cursor cursor = db.query("local_base", null, null, null, null, null, null);
        Song temp;
        if (cursor.moveToFirst()) {
            do {
                temp = new Song();
                temp.songName = cursor.getString(cursor.getColumnIndex("songName"));
                temp.singer = cursor.getString(cursor.getColumnIndex("singerName"));
                temp.path = cursor.getString(cursor.getColumnIndex("path"));
                temp.duration=Integer.parseInt(cursor.getString(cursor.getColumnIndex("duration")));
                temp.isliked = cursor.getInt(cursor.getColumnIndex("isLiked")) != 0;
                l.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return l;
    }

    public static List<Song> fetchfromDB(Context context) {
        List<Song> l = new ArrayList();
        if(dbHelper==null)
            dbHelper = new DatabaseHelper(context, "LOCAL", null, 1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor = db.query("local_base", null, null, null, null, null, null);
        Song temp;
        if (cursor.moveToFirst()) {
            do {
                temp = new Song();
                temp.songName = cursor.getString(cursor.getColumnIndex("songName"));
                temp.singer = cursor.getString(cursor.getColumnIndex("singerName"));
                temp.path = cursor.getString(cursor.getColumnIndex("path"));
                temp.duration=Integer.parseInt(cursor.getString(cursor.getColumnIndex("duration")));
                temp.isliked = cursor.getInt(cursor.getColumnIndex("isLiked")) != 0;
                l.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return l;
    }

    private static void initProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("提示信息");
        progressDialog.setMessage("正在加载哦，请稍后...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

    }

    public static void updateSTACK(int song){
        SharedPreferences sharedPreferences= MusicService.sp;
        String str="song";

        for(int i=1;i<=3;i++)
        {
            String tstr=str+i;
            if(sharedPreferences.getInt(tstr,-1)==song)
            {
                return;
            }
        }

       for(int i=1;i<=3;i++)
       {
        String tstr=str+i;
           if(sharedPreferences.getInt(tstr,-1)==-1)
           {
               sharedPreferences.edit().putInt(tstr,song).commit();
               return;
           }
       }
       int t1=sharedPreferences.getInt("song1",-1);
       int t2=sharedPreferences.getInt("song2",-1);
        sharedPreferences.edit().putInt("song2",t2).commit();
        sharedPreferences.edit().putInt("song3",t1).commit();
        sharedPreferences.edit().putInt("song1",song).commit();

    }

    public static List popSTACK(Context context){
        List poplist=new ArrayList();
        SharedPreferences sharedPreferences;
        if(MusicService.sp!=null)
            sharedPreferences= MusicService.sp;
        else
            sharedPreferences=context.getSharedPreferences("whatisplaying.txt", Context.MODE_PRIVATE);
        String str="song";
        for(int i=1;i<=3;i++)
        {
            String tstr=str+i;
            int t=sharedPreferences.getInt(tstr,-1);
            if(t==-1)
            {
              return poplist;
            }
            else
            {

                Song song=ResUtils.fetchfromDB(ResUtils.dbHelper.getWritableDatabase()).get(t);
                poplist.add(song);
            }
        }
        return poplist;
    }

    public static List GetLovedSong(SQLiteDatabase db)
    {
        List<Song> l = new ArrayList();
        Cursor cursor = db.query("liked_base", null, null, null, null, null, null);
        Song temp;
        if (cursor.moveToFirst()) {
            do {
                temp = new Song();
                temp.songName = cursor.getString(cursor.getColumnIndex("songName"));
                temp.singer = cursor.getString(cursor.getColumnIndex("singerName"));
                temp.path = cursor.getString(cursor.getColumnIndex("path"));
                temp.duration=Integer.parseInt(cursor.getString(cursor.getColumnIndex("duration")));
                temp.isliked = cursor.getInt(cursor.getColumnIndex("isLiked")) != 0;
                l.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return l;
    }

    public static void putLovedSong(SQLiteDatabase db,Song s){
        db.insert("liked_base", null, songToValue(s,true));
        String []sn=new String[]{s.songName};

        db.update("local_base",songToValue(s,true),"songName=?",sn);
        db.close();
    }

    public static void deletLovedSong(SQLiteDatabase db,Song song)
    {
        String []songName=new String[]{song.songName};
        db.delete("liked_base","songName=?",songName);
        db.update("local_base",songToValue(song,false),"songName=?",songName);
        String deleSQL="DELETE FROM Websites WHERE name='"+songName+"'";
        db.close();
    }

    public static boolean ifLike(Song song,SQLiteDatabase db)
    {
        String sql = "select * from liked_base where songName=?";
        Cursor cursor = db.rawQuery(sql, new String[]{song.songName});

      if(cursor.moveToFirst())
       do{
           cursor.close();
           db.close();
           return true;
       }while(cursor.moveToNext());
        cursor.close();
        db.close();
        return false;
    }
}
