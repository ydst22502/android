package com.turtle920.ohmycto;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    String userid;
    String token;
    Bundle bundle;
    private ListView listView;
    private ArrayList<HashMap<String, Object>> listItem;
    private SimpleAdapter mSimpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent = this.getIntent();
        Log.d("TAG", "111");
        bundle = intent.getExtras();
        Log.d("TAG", bundle.toString());

        userid = bundle.getString("userid");
        token = bundle.getString("token");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView_homePageActivity_postContent);
        listItem = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", R.drawable.sample_avatar);//加入图片
            map.put("ItemTitle", "第" + i + "行");
            map.put("ItemText", "这是第" + i + "行");
            map.put("ItemTime", "time");
            listItem.add(map);
        }

        mSimpleAdapter = new SimpleAdapter(this, listItem,//需要绑定的数据
                R.layout.item_home_page,//每一行的布局
                //动态数组中的数据源的键对应到定义布局的View中
                new String[]{"ItemImage", "ItemTitle", "ItemText", "ItemTime"},
                new int[]{R.id.imageView_homePageList_avatar,
                        R.id.textView_homePageList_title,
                        R.id.textView_homePageList_content,
                        R.id.textView_homePageList_postTime
                }
        );

        listView.setAdapter(mSimpleAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);//发帖按钮
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, PostActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "post/ask-by-offset-and-limit",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TextView textView = (TextView) findViewById(R.id.textView_homePageActivity_info);
                        textView.setText(response);

                        Gson gson = new Gson();
                        List<JsonHomePageList> homePageLists = gson.fromJson(response, new TypeToken<List<JsonHomePageList>>() {
                        }.getType());

                        listItem.clear();
                        for (int i = 0; i < homePageLists.size(); i++) {
                            JsonHomePageList homePageList = homePageLists.get(i);
                            Log.d("TAG", homePageList.toString());

                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("ItemImage", R.drawable.sample_avatar);//加入图片
                            map.put("ItemTitle", homePageList.title);
                            map.put("ItemText", homePageList.content);
                            map.put("ItemTime", homePageList.posttime);
                            listItem.add(map);
                        }

                        mSimpleAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("offset", "-1");
                map.put("limit", "10");
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.textView_homePageActivity_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("login", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userid");
            editor.remove("token");
            editor.apply();

            Intent intent = new Intent(HomePageActivity.this, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
        /*********
         * 这个地方很奇怪为什么是在option栏创建的时候才有这货的对象呢。。
         */
        getMenuInflater().inflate(R.menu.home_page, menu);

        TextView textView1 = (TextView) findViewById(R.id.textView_navHeaderHomePage_username);
        textView1.setText("userid: " + userid);

        TextView textView2 = (TextView) findViewById(R.id.textView_navHeaderHomePage_selfIntroduction);
        textView2.setText("token: " + token);

        TextView textView3 = (TextView) findViewById(R.id.textView_homePageActivity_logout);
        textView3.setOnClickListener(this);
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

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
