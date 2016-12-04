package com.greatsky.kitcheninpocket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by fangwenli on 29/11/2016.
 */

public class MyListviewAdapter extends BaseAdapter {

        Context context;

        ArrayList<Integer> imageList;


        public MyListviewAdapter(Context context, ArrayList<Integer> arrayList){
            this.context = context;
//        sample.add("apple");
//        sample.add("banana");
            this.imageList = arrayList;
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int i) {
            return imageList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.my_listview, viewGroup, false);

            ImageView cover = (ImageView)row.findViewById(R.id.imageView2);

            cover.setBackgroundResource(imageList.get(i));

            return row;
        }


    }




