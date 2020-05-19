package com.example.learnintent.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.learnintent.Adaptor.SingerListAdaptor;
import com.example.learnintent.DataBase.DatabaseHelper;
import com.example.learnintent.R;
import com.example.learnintent.entity.Singer;
import com.example.learnintent.utils.ResUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.learnintent.utils.ResUtils.dbHelper;

public class singer_Fragment extends Fragment {

    ListView singerList;
    List<Singer> singers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext(), "LOCAL", null, 1);
        initList();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_page2, container, false);
        initUnits(view);
        return view;
    }

    private void initUnits(View v) {
        singerList = v.findViewById(R.id.SINGERLIST);
        SingerListAdaptor singerListAdaptor = new SingerListAdaptor(getContext(), singers);
        singerList.setAdapter(singerListAdaptor);
    }

    public void initList() {
        SQLiteDatabase singerbase = dbHelper.getWritableDatabase();
        singers = new ArrayList<>();
        Singer temp;
        String sql = "select * from local_base";
        Cursor cursor = singerbase.rawQuery(sql, null);
        // singerbase.query("local_base", new String[]{"singerName"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                temp = new Singer();
                String name = cursor.getString(cursor.getColumnIndex("singerName"));
                temp.name = name;
                temp.songnum = howManySongs(name);
                if (!ifhas(singers, temp))
                    singers.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();
        singerbase.close();
//        Iterator<Singer> it = singers.iterator();
//        while (it.hasNext()) {
//            String str = it.next().name;
//            int t=howManySongs("local_base",str);
//            it.next().songnum=t;
//        }


    }


    public int howManySongs(String name) {
        int count = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from local_base where singerName=?";
        Cursor c = db.rawQuery(sql, new String[]{name});
        while (c.moveToNext()) {
            count++;
        }
        c.close();
        db.close();
        return count;
    }

    private boolean ifhas(List l, Singer singer) {
        String name = singer.name;
        Iterator<Singer> it = l.iterator();
        while (it.hasNext()) {
            String str = it.next().name;
            if (str.equals(name))
                return true;
        }
        return false;
    }


}
