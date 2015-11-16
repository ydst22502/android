package com.turtle920.ohmycto;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ServerCommunication {

    private class VolleyResponse {
        private int flag = 0;
        private long userid = 0;
        private String token = "";
        VolleyResponse() {
            // no-args constructor
        }
    }

    final private String BASE_URL = "http://10.0.2.2/cto/web/index.php?r=api/";
    //private HashMap<String, String> response_1;

    public boolean login(final String email, final String password, final HashMap<String, String> responseBack, Context context) {

        RequestQueue mQueue = Volley.newRequestQueue(context);
        //this.response_1 = response;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"tb-userinfo/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Volley response: "+response);//response为服务器返回的string
                        Gson gson = new Gson();
                        VolleyResponse volleyResponse = gson.fromJson(response, VolleyResponse.class);
                        Log.d("TAG", "flag: "+volleyResponse.flag);
                        Log.d("TAG", "userid: "+volleyResponse.userid);
                        Log.d("TAG", "token: "+volleyResponse.token);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("email", email);
                map.put("authkey", password);
                Log.d("TAG", ""+email+" "+password);
                return map;
            }
        };

        mQueue.add(stringRequest);
        return true;
    }
}
