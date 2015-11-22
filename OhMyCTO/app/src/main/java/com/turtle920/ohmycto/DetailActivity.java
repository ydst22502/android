package com.turtle920.ohmycto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    String postid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        postid = bundle.getString("postid");

        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "post/ask-by-postid",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                        Gson gson = new Gson();
                        JsonDetail detail = gson.fromJson(response, JsonDetail.class);

                        TextView textView1 = (TextView)findViewById(R.id.textView_detailActivity_username);
                        textView1.setText("功能待完善"+detail.userid);

                        TextView textView2 = (TextView) findViewById(R.id.textView_detailActivity_userinfo);
                        textView2.setText("功能待完善"+detail.userid);

                        TextView textView3= (TextView) findViewById(R.id.textView_detailActivity_title);
                        textView3.setText(detail.title);

                        TextView textView4 = (TextView) findViewById(R.id.textView_detailActivity_content);
                        textView4.setText(detail.content);

                        TextView textView5 = (TextView) findViewById(R.id.textView_detailActivity_posttime);
                        textView5.setText(detail.posttime);
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
                map.put("postid", postid);
                return map;
            }
        };

        mQueue.add(stringRequest);
    }
}
