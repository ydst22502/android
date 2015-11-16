package com.turtle920.ohmycto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public String email;
    public String password;
    public HashMap<String, String> response = new HashMap<>();
    public Config server = new Config();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button1 = (Button) findViewById(R.id.button_loginActivity_login);
        button1.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        /***
         * 点击了登陆按钮
         */
        if (v.getId() == R.id.button_loginActivity_login) {
            EditText editText1 = (EditText) findViewById(R.id.editText_loginActivity_email);
            email = editText1.getText().toString();
            EditText editText2 = (EditText) findViewById(R.id.editText_loginActivity_password);
            password = editText2.getText().toString();
            Log.d("TAG", "editText: " + this.email + " " + this.password);

            RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.BASE_URL + "tb-userinfo/login",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("TAG", "Json from server: " + response);//response为服务器返回的Json
                            Gson gson = new Gson();
                            VolleyLogin loginResponse = gson.fromJson(response, VolleyLogin.class);
                            /****
                             * userid和token需要存在本地
                             */
                            Log.d("TAG", "token decode from Json: "+loginResponse.token);
                            if (loginResponse.flag == 1) {
                                TextView textView = (TextView) findViewById(R.id.textView_loginActivity_error);
                                textView.setText("正在登陆...");
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                TextView textView = (TextView) findViewById(R.id.textView_loginActivity_error);
                                textView.setText("邮箱或密码错误，请重试");
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
                    map.put("email", email);
                    map.put("authkey", password);
                    return map;
                }
            };

            mQueue.add(stringRequest);
        }

    }

}
