package com.example.learnintent.entity;

import android.app.admin.SystemUpdatePolicy;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Song implements Serializable, Parcelable {
    public Bitmap bitmap;
    public String songName;//歌曲名
    public String singer;//歌手
    public long size;//歌曲所占空间大小
    public int duration;//歌曲时间长度
    public String path;//歌曲地址
    public boolean isliked = false;

    public Song(){
        super();
    }

    protected Song(Parcel in) {
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        songName = in.readString();
        singer = in.readString();
        size = in.readLong();
        duration = in.readInt();
        path = in.readString();
        isliked = in.readByte() != 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(bitmap, flags);
        dest.writeString(songName);
        dest.writeString(singer);
        dest.writeLong(size);
        dest.writeInt(duration);
        dest.writeString(path);
        dest.writeByte((byte) (isliked ? 1 : 0));
    }
}
