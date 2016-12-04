package com.greatsky.kitcheninpocket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.greatsky.kitcheninpocket.AddRecipe.AddRecipeActivity;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private int login = 0;
    private String username = "";
    private String password = "";
    FloatingActionButton addRecipeButton;
    MenuAdapter mAdapter;
    String access_token = "";
    String result = "";
    String url = "";
    String userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set actionbar's color
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#f76755")));



        SharedPreferences sharedPreferences =getSharedPreferences("login", MODE_PRIVATE);
        login = sharedPreferences.getInt("login", 0);
        access_token = sharedPreferences.getString("access_token","");
        userid = sharedPreferences.getString("userid", "");
        SharedPreferences.Editor editor = sharedPreferences.edit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            public void onDrawerOpened(View drawerView) {
                SharedPreferences sharedPreferences =getSharedPreferences("login", MODE_PRIVATE);
                if(sharedPreferences.getInt("login", 0) == 1)
                {
                    username = sharedPreferences.getString("user","");

                    TextView uname = (TextView) findViewById(R.id.username);
                    uname.setText(username);
                }
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //View Pager, the image on homepage changes, weekly recommended
        List<CustomObject> items = new ArrayList<CustomObject>();
        items.add(new CustomObject("Homemade Cake"));
        items.add(new CustomObject("Afternoon Tea"));
        items.add(new CustomObject("Seafood"));
        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this, items);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        
        mViewPager.setAdapter(mCustomPagerAdapter);

        //addRecipe button
        addRecipeButton = (FloatingActionButton)findViewById(R.id.addRecipeButton);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Kitchen in Pocket");

        //listview
        ListView listView = (ListView)findViewById(R.id.listview);
        if(login == 0)
        {
            listView.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this,R.string.login_first,Toast.LENGTH_SHORT).show();
        }
        else {
//            ArrayList<Integer> images = new ArrayList<>();
//            images.add(R.drawable.what_my_followings_did);
//            images.add(R.drawable.what_my_followings_did);
//            MyListviewAdapter adapter = new MyListviewAdapter(MainActivity.this, images);
//            listView.setAdapter(adapter);
            mAdapter = new MenuAdapter();
            listView.setAdapter(mAdapter);

//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
//                    startActivity(intent);
////                Toast.makeText(MainActivity.this,"你单击的是第"+(position+1)+"条数据",Toast.LENGTH_SHORT).show();
//                }
//            });
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("search recipes or ingredients...");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            notAuto();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_myMenu) {
            SharedPreferences sharedPreferences =getSharedPreferences("login", MODE_PRIVATE);
            if(sharedPreferences.getInt("login", 0) == 1) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("userid", sharedPreferences.getString("userid",""));
                intent.putExtra("isfollowed", 0);
                startActivity(intent);
            }
            else
                Toast.makeText(MainActivity.this, getResources().getString(R.string.login_first), Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_myFavor) {
            SharedPreferences sharedPreferences =getSharedPreferences("login", MODE_PRIVATE);
            if(sharedPreferences.getInt("login", 0) == 1) {
                Intent intent = new Intent(MainActivity.this, FavorActivity.class);
                intent.putExtra("username", "favored");
                intent.putExtra("userid", sharedPreferences.getString("userid",""));
                intent.putExtra("isfavored", 1);
                startActivity(intent);
            }
            else
                Toast.makeText(MainActivity.this, getResources().getString(R.string.login_first), Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_myInfo) {
            SharedPreferences sharedPreferences =getSharedPreferences("login", MODE_PRIVATE);
            if(sharedPreferences.getInt("login", 0) == 1) {
                Intent intent = new Intent(MainActivity.this, MyInfoActivity.class);
                startActivity(intent);
            }
            else
                Toast.makeText(MainActivity.this, getResources().getString(R.string.login_first), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_instruction) {

        } else if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean notAuto()
    {
        SharedPreferences shared = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("login", 0);
        editor.putInt("auto", 0);
        editor.commit();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }

    //the click event of addRecipeButton, go to add recipe activity
    public void addRecipe(View view){
        Intent intent = new Intent(this, AddRecipeActivity.class);
        startActivity(intent);
    }

    //go to recipe
    public void goToRecipe(View view){
        Intent intent = new Intent(this, RecipeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences =getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("login", 0);
        editor.commit();
    }

    class MenuAdapter extends BaseAdapter
    {
        private ArrayList<com.greatsky.kitcheninpocket.object.Menu> list;
        private LayoutInflater menuInflater;

        MenuAdapter()
        {
            list = new ArrayList<com.greatsky.kitcheninpocket.object.Menu>();
            asynchronousRequest();
        }

        public void add(com.greatsky.kitcheninpocket.object.Menu x)
        {
            list.add(x);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public com.greatsky.kitcheninpocket.object.Menu getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            menuInflater = LayoutInflater.from(getApplicationContext());
            if(convertView == null)
                convertView = (LinearLayout)menuInflater.inflate(R.layout.menu_listview_entry,null);

            final ImageView image = (ImageView) convertView.findViewById(R.id.menu_listview_image);
            TextView name = (TextView)convertView.findViewById(R.id.menu_listview_name);

            name.setText(mAdapter.getItem(position).getName());

            image.setImageBitmap(mAdapter.getItem(position).getImage());
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getRecipe(mAdapter.getItem(position).getId());

                }
            });

            //image.setImageResource(mAdapter.getItem(position).getImagepath());
            //owner.setText(mAdapter.getItem(position).getOwnername());
            return convertView;
        }

        public void afterGetRecipe(String result)
        {
            if(result.contains("success"))
            {
                Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
                if(result.contains("true"))
                    intent.putExtra("is_favor", "true");
                else
                    intent.putExtra("is_favor", "false");
                String[] split = result.split("\\}|\\{",5);
                String temp = split[3].replaceAll("\"","");
                String[] msg = temp.split(":|,");
                for(int i = 0; i < msg.length; i= i + 2) {
                    if(msg[i].equals("picture")) {
                        intent.putExtra(msg[i], msg[i + 1] + ":" + msg[i + 2]);
                        i++;
                    }
                    else intent.putExtra(msg[i], msg[i + 1]);

                }
                String[]detail = split[4].split("\\[|\\]");
                String ingredient = detail[1].replaceAll("\"","");
                String step = detail[3].replaceAll("\"","");
                intent.putExtra("ingredient", ingredient);
                intent.putExtra("step", step);
                startActivity(intent);
            }
            else if(result.contains("error"))
            {
                String[] msg = result.split("\"");
                Toast.makeText(MainActivity.this, msg[7], Toast.LENGTH_SHORT).show();
            }
        }



        public void getRecipe(String id)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            HerokuService restAPI = retrofit.create(HerokuService.class);
            Call<ResponseBody> call = restAPI.getrecipe(id, access_token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //Log.e("===","return:" + response.body().toString());
                    BufferedSource source = response.body().source();
                    try {
                        source.request(Long.MAX_VALUE); // Buffer the entire body.
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Buffer buffer = source.buffer();
                    result = buffer.clone().readString(Charset.forName("UTF-8"));
                    afterGetRecipe(result);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("===","failed");
                }
            });
        }


        public void asynchronousImageRequest(final String[] info)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://res.cloudinary.com/hsayf1nxm/image/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            HerokuService restAPI = retrofit.create(HerokuService.class);
            String[] temp = url.split("image");
            Call<ResponseBody> call = restAPI.loadimage(temp[1].substring(1, temp[1].length()));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //Log.e("===","return:" + response.body().toString());
                    Bitmap source = BitmapFactory.decodeStream(response.body().byteStream());
                    com.greatsky.kitcheninpocket.object.Menu f = new com.greatsky.kitcheninpocket.object.Menu(info[1],info[3],info[5],info[7],info[9],info[11]);
                    f.setImage(source);
                    list.add(f);
                    update();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("===","failed");
                }
            });
        }

        public void asynchronousRequest()  {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://kitchen-in-pocket.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            HerokuService restAPI = retrofit.create(HerokuService.class);
            Call<ResponseBody> call = restAPI.gethomeline(access_token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //Log.e("===","return:" + response.body().toString());
                    BufferedSource source = response.body().source();
                    try {
                        source.request(Long.MAX_VALUE); // Buffer the entire body.
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Buffer buffer = source.buffer();
                    result = buffer.clone().readString(Charset.forName("UTF-8"));
                    handleresponse();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("===","failed");
                }
            });
        }

        public void handleresponse()
        {
            if(result.contains("success")) {
                String[] split = result.split("\\}|\\{");
                int i = 3;
                for(; i < split.length; i+= 2)
                {
                    String[] info = split[i].split(":|,");
                    url = info[11].substring(1, info[11].length()) + ":" + info[12].substring(0,info[12].length()-1);
                    asynchronousImageRequest(info);
                }
                update();
            }
            else
            if(result.contains("error"))
            {
                String[] msg = result.split("\"");
                Toast.makeText(MainActivity.this, msg[7], Toast.LENGTH_SHORT).show();
            }


        }

        public void update(){
            mAdapter.notifyDataSetChanged();
        }



    }

}
