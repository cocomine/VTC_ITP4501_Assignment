package com.cocomine.itp4501_as;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        //close app
        if(view.getId() == R.id.close){
            finish();
        }

        //rank page
        if(view.getId() == R.id.rank){
            Intent intent = new Intent(this, rank.class);
            startActivity(intent);
        }

        //record page
        if(view.getId() == R.id.record){
            Intent intent = new Intent(this, record.class);
            startActivity(intent);
        }

        //play page
        if(view.getId() == R.id.play){
            Intent intent = new Intent(this, play.class);
            startActivity(intent);
        }
    }
}