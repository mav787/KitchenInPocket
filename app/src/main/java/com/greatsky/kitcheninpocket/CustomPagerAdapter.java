package com.greatsky.kitcheninpocket;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static android.R.color.black;

/**
 * Created by fangwenli on 13/11/2016.
 */

//the adapter to inflate the images on the homepage "Weekly Recommended"
class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    List<CustomObject> items;
    LayoutInflater mLayoutInflater;
    int[] mResources = {
            R.drawable.first,
            R.drawable.second,
            R.drawable.third
    };


    @Override
    public void destroyItem(View container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    public CustomPagerAdapter(Context context, List<CustomObject> items) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        View dot0 = (View) itemView.findViewById(R.id.v_dot0);
        View dot1 = (View) itemView.findViewById(R.id.v_dot1);
        View dot2 = (View) itemView.findViewById(R.id.v_dot2);


        imageView.setImageResource(mResources[position]); //set the image resource

        switch (position){
            case 0: dot0.setBackgroundResource(R.drawable.dot_focused);
                dot1.setBackgroundResource(R.drawable.dot_normal);
                dot2.setBackgroundResource(R.drawable.dot_normal);
                break;
            case 1: dot1.setBackgroundResource(R.drawable.dot_focused);
                dot0.setBackgroundResource(R.drawable.dot_normal);
                dot2.setBackgroundResource(R.drawable.dot_normal);
                break;
            case 2: dot2.setBackgroundResource(R.drawable.dot_focused);
                dot0.setBackgroundResource(R.drawable.dot_normal);
                dot1.setBackgroundResource(R.drawable.dot_normal);
                break;
        }

        TextView bottomTextItem = (TextView) itemView.findViewById(R.id.bottomText);
        CustomObject customObject = items.get(position);
        bottomTextItem.setText(customObject.bottom);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

