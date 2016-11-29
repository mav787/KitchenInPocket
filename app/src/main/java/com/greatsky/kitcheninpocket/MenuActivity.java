package com.greatsky.kitcheninpocket;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {


    FloatingActionButton fab = null;
    boolean isFollowed = false;
    SharedPreferences shared;
    SharedPreferences.Editor editor;
    String str_username;
    MenuAdapter madapter = new MenuAdapter();
    ListView lv = null;

    class Menu
    {
        int Imagepath;
        String ownername;
        int ownerid;

        public Menu(int imagepath, int ownerid, String ownername) {
            Imagepath = imagepath;
            this.ownerid = ownerid;
            this.ownername = ownername;
        }

        public int getImagepath() {
            return Imagepath;
        }

        public int getOwnerid() {
            return ownerid;
        }

        public String getOwnername() {
            return ownername;
        }
    }

    class MenuAdapter extends BaseAdapter
    {
        private ArrayList<Menu> list;
        private LayoutInflater menuInflater;

        MenuAdapter()
        {
            list = new ArrayList<Menu>();
            Menu a = new Menu(R.drawable.what_my_followings_did, 1234, "GreatSky");
            Menu b = new Menu(R.drawable.what_my_followings_did, 1234, "GreatSky");
            Menu c = new Menu(R.drawable.what_my_followings_did, 1234, "GreatSky");
            list.add(a);
            list.add(b);
            list.add(c);
        }

        public void add(Menu x)
        {
            list.add(x);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Menu getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            menuInflater = LayoutInflater.from(getApplicationContext());
            if(convertView == null)
                convertView = (LinearLayout)menuInflater.inflate(R.layout.menu_listview_entry,null);
            ImageView image = (ImageView) convertView.findViewById(R.id.menu_listview_image);
            TextView owner = (TextView)convertView.findViewById(R.id.menu_listview_owner);
            image.setImageResource(madapter.getItem(position).getImagepath());
            owner.setText(madapter.getItem(position).getOwnername());
            return convertView;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        shared = getSharedPreferences("login", MODE_PRIVATE);
        str_username = shared.getString("user","");
        setTitle("Menu(" + str_username +")");
        lv = (ListView)findViewById(R.id.menu_listview);

        lv.setAdapter(madapter);


        fab = (FloatingActionButton) findViewById(R.id.user_follow);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFollowed == false) {
                    fab.setImageResource(R.drawable.heart1);
                    isFollowed = true;
                    Snackbar.make(view, "LIKE THIS", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    fab.setImageResource(R.drawable.heart2);
                    isFollowed = false;
                    Snackbar.make(view, "DON'T LIKE THIS", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
    }
}
