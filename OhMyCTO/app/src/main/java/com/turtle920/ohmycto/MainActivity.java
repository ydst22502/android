package com.turtle920.ohmycto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String userid;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        userid = bundle.getString("userid");
        token = bundle.getString("token");

        TextView textView = (TextView)findViewById(R.id.textView_mainActivity_hello);
        textView.setText("userid: "+userid+" token: "+token);

        Button button = (Button)findViewById(R.id.button_mainActivity_logout);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_mainActivity_logout){

        }
    }
}
