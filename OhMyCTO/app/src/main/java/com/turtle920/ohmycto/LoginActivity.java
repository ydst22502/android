package com.turtle920.ohmycto;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public String email;
    public String password;
    public HashMap<String, String> response = new HashMap<>();
    public ServerCommunication server = new ServerCommunication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button button1 = (Button) findViewById(R.id.button_loginActivity_login);
        button1.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        EditText editText1 = (EditText) findViewById(R.id.editText_loginActivity_email);
        this.email = editText1.getText().toString();
        EditText editText2 = (EditText) findViewById(R.id.editText_loginActivity_password);
        this.password = editText2.getText().toString();
        Log.d("TAG", "" + this.email + " " + this.password);
        if (server.login(this.email, this.password, this.response, this)) {//这里这个参数this就是传一个context给volley，此处传this也就是LoginActivity
            /*int flag = Integer.parseInt(response.get("flag"));
            String userid = response.get("userid");
            String token = response.get("token");*/

            /******
             *userid 和 token 存在本地
             */

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            TextView textView1 = (TextView)findViewById(R.id.textView_loginActivity_error);
            textView1.setText("邮箱或密码错误，请重试");

        }
    }
}
