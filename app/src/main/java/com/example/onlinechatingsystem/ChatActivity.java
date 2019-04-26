package com.example.onlinechatingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinechatingsystem.Adapters.MessageAdapter;
import com.example.onlinechatingsystem.Helper.Message_item;
import com.example.onlinechatingsystem.Helper.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    Intent i;
    RecyclerView chatRecycler;
    FloatingActionButton sendBtn;
    EditText msgInput;
    SharedPreferences sp;
    List<Message_item> messages;
    MessageAdapter mAdapter;
    Handler h;
    int delay = 500;
    Runnable runnable;
    ProgressBar p;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        i = getIntent();
        setTitle("Connected to "+i.getStringExtra("name"));

        h = new Handler();

        setViews();

        getMessages();

    }
    private void setViews() {
        chatRecycler = findViewById(R.id.chatRecycler);
        sendBtn = findViewById(R.id.sendBtn);
        msgInput = findViewById(R.id.msgInp);

        sp = getSharedPreferences("LOGIN",MODE_PRIVATE);

        messages = new ArrayList<>();
        mAdapter = new MessageAdapter(ChatActivity.this,messages);

        p = findViewById(R.id.sendingProgress);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        chatRecycler.setLayoutManager(linearLayoutManager);
        chatRecycler.setAdapter(mAdapter);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
                p.setVisibility(View.VISIBLE);
            }
        });


    }

    private void sendMsg() {

        String sendUrl = "https://chatterboxunlimited.000webhostapp.com/sendMessage.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, sendUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                p.setVisibility(View.GONE);
                Log.d("sent",response);
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                msgInput.setText("");
                getMessages();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                p.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("sId",String.valueOf(sp.getString("id","")));
                params.put("rId",i.getStringExtra("id"));
                params.put("msg_table",i.getStringExtra("msg_table"));
                params.put("msg",msgInput.getText().toString());
                return params;
            }
        };

        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);


    }

    private void getMessages() {
        String getMsgUrl = "https://chatterboxunlimited.000webhostapp.com/getMessages.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getMsgUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

               // Log.d("Resp" , response);
                messages.clear();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Message_item item = new Message_item();

                        item.setFrom(jsonObject.getInt("from"));
                        item.setMessage(jsonObject.getString("message"));
                        item.setTime(jsonObject.getString("time"));

                        messages.add(item);

                    }

                    mAdapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("msg_table",i.getStringExtra("msg_table"));
                return params;
            }
        };

        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChatActivity.this,HomePageActivity.class));
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
                getMessages();
                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }
}
