package com.turtle920.ohmycto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        mQueue = Volley.newRequestQueue(getApplicationContext());

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String userid = bundle.getString("userid");

        requestUserInfo(userid);

        TextView textView = (TextView) findViewById(R.id.textView_personActivity_message);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.textView_personActivity_message) {

        }
    }

    private void requestUserInfo(final String userid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "user/get-userinfo",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        JsonUserinfo userinfo = gson.fromJson(response, JsonUserinfo.class);

                        TextView textView1 = (TextView) findViewById(R.id.textView_personActivity_username);
                        textView1.setText(userinfo.username);
                        TextView textView2 = (TextView) findViewById(R.id.textView_personActivity_email);
                        textView2.setText(userinfo.email);
                        TextView textView3 = (TextView) findViewById(R.id.textView_personActivity_introduction);

                        if (userinfo.introduction.equals("empty")) {
                            textView3.setText("ta很懒什么也没留下");
                        } else {
                            textView3.setText(userinfo.introduction);
                        }

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
                map.put("userid", userid);
                return map;
            }
        };
        mQueue.add(stringRequest);
    }


}
