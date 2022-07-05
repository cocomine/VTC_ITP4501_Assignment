package com.cocomine.itp4501_as;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class rank extends AppCompatActivity {

    MyTask task = null;
    private ListView list_rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        list_rank = findViewById(R.id.list_rank);

        //get data
        if (task == null || task.getStatus().equals(AsyncTask.Status.FINISHED)) {
            task = new MyTask();
            task.execute("http://10.0.2.2/ranking_api.php");
        }
    }

    private class MyTask extends AsyncTask<String, Integer, String>{

        //progress result
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray json = new JSONArray(s); //turn to json obj
                String[] list = new String[json.length()]; //create list

                //turn to List
                List<JSONObject> jsonList = new ArrayList<JSONObject>();
                for (int i=0;i<json.length();i++) {
                    jsonList.add(json.getJSONObject(i));
                }

                //sort
                jsonList.sort(new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject a, JSONObject b) {
                        int valB = 0, valA = 0;

                        //get Duration
                        try {
                            valA = a.getInt("Duration");
                            valB = b.getInt("Duration");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return valA - valB; //valA < valB
                    }
                });

                //read data
                for (int i = 0;i<jsonList.size();i++) {
                    list[i] = "Rank "+i+", "+jsonList.get(i).getString("Name")+", "+jsonList.get(i).getInt("Duration")+" sec";
                }

                //update ui
                list_rank.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //get data on server
        @Override
        protected String doInBackground(String... strings) {
            InputStream inputStream = null;
            String result = "";
            URL url = null;

            try {
                url = new URL(strings[0]); //set request url

                HttpURLConnection con = (HttpURLConnection) url.openConnection(); //open connect

                con.setRequestMethod("GET"); //set get mouthed
                con.connect(); //start connect

                inputStream = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                //read result
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                Log.d("doInBackground", "get data complete");
                inputStream.close(); //close connect

            } catch (Exception e) {
                e.printStackTrace();
                result = e.getMessage();
            }
            return result;
        }
    }
}