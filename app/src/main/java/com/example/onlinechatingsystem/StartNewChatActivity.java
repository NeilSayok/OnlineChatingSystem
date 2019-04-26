package com.example.onlinechatingsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinechatingsystem.Adapters.SearchUserAdapter;
import com.example.onlinechatingsystem.Helper.C_Threads;
import com.example.onlinechatingsystem.Helper.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StartNewChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText searchET;
    View loading;
    StringRequest stringRequest;
    List<C_Threads> c_threads;
    SearchUserAdapter adapter;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_chat);
        setTitle("Search Users");

        setViews();
    }

    private void setViews() {

        recyclerView = findViewById(R.id.home_page_rec_view);
        searchET = findViewById(R.id.searchUser);
        loading = findViewById(R.id.loadingView);
        searchET.addTextChangedListener(textWatcher);

        c_threads = new ArrayList<>();

        adapter = new SearchUserAdapter(StartNewChatActivity.this,c_threads);

        sp = getSharedPreferences("LOGIN",MODE_PRIVATE);


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count){

            String url = "https://chatterboxunlimited.000webhostapp.com/getusers.php?key="+s.toString()+"&id="+sp.getString("id","0");
            Log.d("url",sp.getString("name","0"));

            stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                            obj.setMsg_table("");

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

                }
            });

            VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}
