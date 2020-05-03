package com.example.learnintent.Adaptor;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;


public class MyPageAdaptor extends PagerAdapter {

    public MyPageAdaptor(List<View> pages) {
        this.pages = pages;

    }

    private List<View> pages;

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(pages.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        container.addView(pages.get(position));

        return pages.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }



}



