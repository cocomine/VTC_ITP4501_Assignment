package com.cocomine.itp4501_as;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Visibility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class play extends AppCompatActivity {

    private final int[][] box = new int[3][3]; //3 = player, 7 = cpu
    private int boxsChecked = 0;
    private Long startTime;
    private TextView tv_title;
    private Button bt_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        startTime = System.currentTimeMillis();
        tv_title = findViewById(R.id.tv_title);
        bt_continue = findViewById(R.id.bt_continue);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        Button bt = (Button) view;
        //check is click?
        if (!bt.getText().toString().equals("") || boxsChecked == 9) {
            return; //is click
        }

        bt.setText("O"); //set click

        //set in to 2D array
        switch (view.getId()) {
            case R.id.bt_top_left:
                box[0][0] = 3;
                break;
            case R.id.bt_top_center:
                box[0][1] = 3;
                break;
            case R.id.bt_top_right:
                box[0][2] = 3;
                break;
            case R.id.bt_center_left:
                box[1][0] = 3;
                break;
            case R.id.bt_center_center:
                box[1][1] = 3;
                break;
            case R.id.bt_center_right:
                box[1][2] = 3;
                break;
            case R.id.bt_bottom_left:
                box[2][0] = 3;
                break;
            case R.id.bt_bottom_center:
                box[2][1] = 3;
                break;
            case R.id.bt_bottom_right:
                box[2][2] = 3;
                break;
        }
        boxsChecked++; //add checked count

        //box Checked count is < 9, go cpu turn
        if (boxsChecked < 9) {
            cpuTurn();
        }

        //check win
        int winStatus = checkWin();
        if (winStatus == 0 && boxsChecked >= 9) { //draw
            writeData(0);
        } else if (winStatus > 0) {
            writeData(winStatus);
            boxsChecked = 9;
        }
    }

    //cpu turn
    private void cpuTurn() {
        //random position
        Random random = new Random();
        int x = random.nextInt(3);
        int y = random.nextInt(3);

        //check is click?
        if (box[y][x] > 0) {
            cpuTurn(); //is click
        } else {
            //is no click
            box[y][x] = 7;
            boxsChecked++;
            Button bt = findButtonWithPosition(x, y);
            if (bt != null) bt.setText("X"); //set cpu click
        }
    }

    //check cpu or player win
    //return: 0 = draw, 1 = player, 2 = cpu
    private int checkWin() {
        //check vertical ||
        for (int x = 0; x < 3; x++) {
            int tmp = 0;
            for (int y = 0; y < 3; y++) {
                tmp += box[y][x];
            }
            if (tmp == 9) return 1; //player
            if (tmp == 21) return 2; //cpu
        }

        //check horizontal --
        for (int y = 0; y < 3; y++) {
            int tmp = 0;
            for (int x = 0; x < 3; x++) {
                tmp += box[y][x];
            }
            if (tmp == 9) return 1; //player
            if (tmp == 21) return 2; //cpu
        }

        //check Left oblique //
        int tmp = 0;
        for (int i = 2; i >= 0; i--) {
            tmp += box[i][2 - i];
        }
        if (tmp == 9) return 1; //player
        if (tmp == 21) return 2; //cpu

        //check Right oblique \\
        tmp = 0;
        for (int i = 0; i < 3; i++) {
            tmp += box[i][i];
        }
        if (tmp == 9) return 1; //player
        if (tmp == 21) return 2; //cpu

        return 0; //draw
    }

    //writeData in sql
    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void writeData(int winStatus) {
        //calculator time
        SimpleDateFormat dateF = new SimpleDateFormat("d-M-yyyy");
        SimpleDateFormat timeF = new SimpleDateFormat("h:mm a");
        Date date = new Date();
        int duration = (int) ((System.currentTimeMillis() - startTime) / 1000);

        try {
            Log.d("SQL Path", getDatabasePath("record.db").getPath());
            SQLiteDatabase db = SQLiteDatabase.openDatabase(getDatabasePath("record.db").getPath(), null, SQLiteDatabase.CREATE_IF_NECESSARY); //open db
            db.execSQL("CREATE TABLE IF NOT EXISTS GamesLog (gameID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, playDate varchar(10) NOT NULL, playTime varchar(10) NOT NULL, duration int NOT NULL, winningStatus int NOT NULL)"); //create table

            //set data
            db.execSQL("INSERT INTO GamesLog (gameID, playDate, playTime, duration, winningStatus) VALUES (null, '" + dateF.format(date) + "', '" + timeF.format(date) + "', " + duration + ", " + winStatus + ")");

            db.close(); //close db
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //update ui
        if (winStatus == 0) {
            tv_title.setText("You Draw! Duration: " + duration + " sec!");
        } else if (winStatus == 1) {
            tv_title.setText("You Win! Duration: " + duration + " sec!");
        } else if (winStatus == 2) {
            tv_title.setText("You Lose! Duration: " + duration + " sec!");
        }
        bt_continue.setVisibility(View.VISIBLE);
    }

    //get button obj with x,y position
    private Button findButtonWithPosition(int x, int y) {
        String bt_ID = "bt_";

        //get y
        switch (y) {
            case 0:
                bt_ID += "top_";
                break;
            case 1:
                bt_ID += "center_";
                break;
            case 2:
                bt_ID += "bottom_";
                break;
            default:
                return null;
        }

        //get x
        switch (x) {
            case 0:
                bt_ID += "left";
                break;
            case 1:
                bt_ID += "center";
                break;
            case 2:
                bt_ID += "right";
                break;
            default:
                return null;
        }

        int resID = getResources().getIdentifier(bt_ID, "id", getPackageName()); //get button id
        return (Button) findViewById(resID);
    }

    //restart
    public void onContinue(View view){
        this.recreate();
    }
}