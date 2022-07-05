package com.cocomine.itp4501_as;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class record extends AppCompatActivity {

    SQLiteDatabase db;
    private ListView list_record;
    float tableV[] = new float[3];

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        list_record = findViewById(R.id.list_record);

        int win = 0, lose = 0, draw = 0;
        //sql
        try {
            Log.d("SQL Path", getDatabasePath("record.db").getPath());
            db = SQLiteDatabase.openDatabase(getDatabasePath("record.db").getPath(), null, SQLiteDatabase.CREATE_IF_NECESSARY); //open db
            //db.execSQL("DROP TABLE IF EXISTS GamesLog"); //debug
            db.execSQL("CREATE TABLE IF NOT EXISTS GamesLog (gameID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, playDate varchar(10) NOT NULL, playTime varchar(10) NOT NULL, duration int NOT NULL, winningStatus int NOT NULL)"); //create table

            //get data
            Cursor cursor = db.rawQuery("SELECT * FROM GamesLog", null);
            String[] list = new String[cursor.getCount()];
            int i = 0;
            while (cursor.moveToNext()) {
                int winStatus = cursor.getInt(cursor.getColumnIndex("winningStatus"));

                //get proportion
                if(winStatus == 0) draw++;
                if(winStatus == 1) win++;
                if(winStatus == 2) lose++;

                //set list
                String winningStatus = winStatus == 0 ? "Draw" : (winStatus == 1 ? "Win" : "Lose");
                list[i] = cursor.getString(cursor.getColumnIndex("playDate")) + ", "+cursor.getString(cursor.getColumnIndex("playTime"))+", "+winningStatus+", "+cursor.getString(cursor.getColumnIndex("duration"))+" sec";
                i++;
            }

            //update ui
            list_record.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list));
            db.close(); //close db
        }catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //Calculate the proportion
        float total = win+lose+draw;
        tableV[0] = win/total; //win
        tableV[1] = lose/total; //lose
        tableV[2] = draw/total; //draw
    }

    //jump to chart page
    public void show_chart(View view){
        Intent intent = new Intent(this, chart.class);
        intent.putExtra("tableV", tableV);
        startActivity(intent);
    }
}