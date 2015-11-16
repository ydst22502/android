package com.turtle920.ohmycto;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        SharedPreferences sharedPreferences = getSharedPreferences("login", Activity.MODE_PRIVATE);
        String userid = sharedPreferences.getString("userid", "");
        String token = sharedPreferences.getString("token", "");

        Log.d("TAG", userid + " " + token);

        if (token.equals("")) {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("userid", userid);
            bundle.putString("token", token);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }
}
