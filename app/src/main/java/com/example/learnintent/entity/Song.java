package com.example.learnintent.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Song implements Serializable {
    public Bitmap bitmap;
    public String songName;//歌曲名
    public String singer;//歌手
    public long size;//歌曲所占空间大小
    public int duration;//歌曲时间长度
    public String path;//歌曲地址
    public boolean isliked = false;
}
