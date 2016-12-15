package com.example.manasa_pt1119.phone;

import android.support.annotation.IdRes;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    List<HashMap<String, String>> mylist=new ArrayList<>();

    String jsonstr;
    OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void run() throws Exception {

        Request request = new Request.Builder().url("https://jsonplaceholder.typicode.com/posts").build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                  if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                jsonstr =response.body().string();

                //Log.d(TAG, response.body().string());

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            JSONArray json = new JSONArray(jsonstr);
                            JSONObject e;
                            for (int i = 0; i < json.length(); i++) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                e = json.getJSONObject(i);
                                map.put("userId", e.getString("userId"));
                                map.put("id", e.getString("id"));
                                map.put("title", e.getString("title"));
                                map.put("body", e.getString("body"));
                                mylist.add(map);
                            }



                            ListAdapter adapter = new SimpleAdapter(MainActivity.this, mylist,
                                    R.layout.list_itemview, new String[]{"userId", "id",
                                    "title", "body"}, new int[]{R.id.userId,
                                    R.id.id, R.id.title, R.id.body});
                            ListView lv =(ListView)findViewById(R.id.list);

                            lv.setAdapter(adapter);
                        } catch (JSONException j) {
                            j.printStackTrace();
                        }


                    }
                });

            }


        });
    }

}


