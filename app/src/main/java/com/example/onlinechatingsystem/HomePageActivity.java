package com.example.onlinechatingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinechatingsystem.Adapters.HomePageAdapter;
import com.example.onlinechatingsystem.Helper.C_Threads;
import com.example.onlinechatingsystem.Helper.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomePageActivity extends AppCompatActivity {

    final String url = "https://chatterboxunlimited.000webhostapp.com/home_page.php";
    RecyclerView allChats;
    FloatingActionButton btnNewThread;
    View loading;
    SharedPreferences sp;
    List<C_Threads> c_threads;
    HomePageAdapter adapter;
    Handler h;
    int delay = 500;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        h = new Handler();

        setViews();

        getData();




    }

    private void getData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                Log.d("Response",response);
                c_threads.clear();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        C_Threads obj = new C_Threads();

                        obj.setId(jsonObject.getInt("id"));
                        obj.setEmail(jsonObject.getString("email"));
                        obj.setName(jsonObject.getString("name"));
                        obj.setChat_table(jsonObject.getString("chat_table"));
                        obj.setMsg_table(jsonObject.getString("msg_table"));

                        c_threads.add(obj);


                    }
                    Log.d("Json Arr", jsonArray.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("chat_table", Objects.requireNonNull(sp.getString("chat_table", "")));
                return params;
            }
        };

        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);


    }

    private void setViews() {

        allChats = findViewById(R.id.home_page_rec_view);
        btnNewThread = findViewById(R.id.create_new_thread);
        loading = findViewById(R.id.loadingView);
        loading.setVisibility(View.VISIBLE);

        sp = getSharedPreferences("LOGIN",MODE_PRIVATE);
        c_threads = new ArrayList<>();

        btnNewThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(HomePageActivity.this,StartNewChatActivity.class));
            }
        });

        adapter = new HomePageAdapter(HomePageActivity.this,c_threads);
        allChats.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        allChats.setAdapter(adapter);

    }

    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    @Override
    protected void onResume() {
        h.postDelayed( runnable = new Runnable() {
            public void run() {
                getData();
                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.Logout:
                final Snackbar snackbar = Snackbar.make(findViewById(R.id.base),"Do you want to logout?",Snackbar.LENGTH_LONG);
                snackbar.setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        sp.edit().remove("email").apply();
                        sp.edit().remove("password").apply();
                        sp.edit().remove("chat_table").apply();
                        sp.edit().remove("name").apply();
                        sp.edit().remove("id").apply();
                        finish();
                        startActivity(new Intent(HomePageActivity.this,MainActivity.class));
                    }
                });
                snackbar.show();
                return true;

        }
        return false;
    }





}
