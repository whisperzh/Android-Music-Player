package com.example.learnintent.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.learnintent.R;
import com.example.learnintent.entity.Singer;

import java.util.List;

public class SingerListAdaptor extends BaseAdapter {

    private Context context;
    private List<Singer> list;

    public SingerListAdaptor(Context context, List<Singer> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Myholder myholder;
        if (convertView == null) {
            myholder = new Myholder();
            convertView = LayoutInflater.from(context).inflate(R.layout.singer_item, null);
            myholder.name = convertView.findViewById(R.id.singername);
            myholder.howmanySongs = convertView.findViewById(R.id.howmanysongs);
            convertView.setTag(myholder);

        } else {
            myholder = (Myholder) convertView.getTag();

        }
        myholder.name.setText(list.get(position).name);
        myholder.howmanySongs.setText(list.get(position).songnum + " 首歌曲");
        return convertView;
    }

    class Myholder {
        TextView name;
        TextView howmanySongs;
    }


}
