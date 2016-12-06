package com.greatsky.kitcheninpocket;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class InstrctionActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    CustomPagerAdapter mCustomPagerAdapter;
    private static ImageView imageView;
    private static TextView textView;
    private static String[] restext;

    View view;
    private static int[] resimage = {R.drawable.register_1, R.drawable.login_1, R.drawable.home_img,
    R.drawable.add_recipe, R.drawable.follow_1, R.drawable.my_menu, R.drawable.favored, R.drawable.recipe_1};
    static int pos = 0;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrction);


        restext = getResources().getStringArray(R.array.instruction_text);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        mCustomPagerAdapter = new CustomPagerAdapter(this);

        for(int i = 0; i < 8; i++)
            mCustomPagerAdapter.recommends.add(i);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mCustomPagerAdapter);



    }





    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        List<Integer> recommends;
        LayoutInflater mLayoutInflater;


        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            recommends = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return recommends.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.fragment_instrction, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.instruction_image);
            TextView textView = (TextView) itemView.findViewById(R.id.instruction_text);


            imageView.setImageResource(resimage[recommends.get(position)]);
            textView.setText(restext[recommends.get(position)]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }


    }
}
