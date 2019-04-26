package com.example.onlinechatingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinechatingsystem.Helper.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextInputEditText emailET, passwordET;
    Button signIn,signUp;
    SharedPreferences sp;
    String em,ps,ct,nm,id;
    View loading;
    final String loginUrl = "https://chatterboxunlimited.000webhostapp.com/login.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Welcome To Chatter Box");

        sp = getSharedPreferences("LOGIN",MODE_PRIVATE);
        emailET = findViewById(R.id.signin_email);
        passwordET = findViewById(R.id.signin_password);
        signIn = findViewById(R.id.signin_signin_btn);
        signUp = findViewById(R.id.signin_signup_btn);
        loading = findViewById(R.id.loadingView);

        em = sp.getString("email","");
        ps = sp.getString("password","");
        ct = sp.getString("chat_table","");
        nm = sp.getString("name","");
        id = sp.getString("id","");

        Log.d("SP" ,em );
        Log.d("SP" , ps);
        Log.d("SP" , ct);
        Log.d("SP" , nm);
        Log.d("SP" ,id );



        if (em.equals("") || ps.equals("") || ct.equals("") || nm.equals("")){

            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loading.setVisibility(View.VISIBLE);
                    if (emailET.getText().length() > 0 ||  passwordET.getText().length() > 0)
                        make_login_request();
                    else
                        Toast.makeText(getApplicationContext(),"Email id/ Password is empty",Toast.LENGTH_SHORT).show();
                }
            });

            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,SignUpActivity.class));
                }
            });




        }else{

                load_homepage_activity();

        }








    }

    private void make_login_request() {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.setVisibility(View.GONE);
                Log.d("Response",response);

                try{
                    JSONObject jObject = new JSONObject(response);
                    String status = null;
                    try {
                        status = jObject.getString("status");
                    } catch (JSONException e) {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }

                    if ("OK".equalsIgnoreCase(status)){
                        sp.edit().putString("email",emailET.getText().toString()).apply();
                        sp.edit().putString("password",passwordET.getText().toString()).apply();
                        sp.edit().putString("chat_table",jObject.getString("chat_table")).apply();
                        sp.edit().putString("name",jObject.getString("name")).apply();
                        sp.edit().putString("id",jObject.getString("id")).apply();
                        finish();
                        startActivity(new Intent(MainActivity.this,HomePageActivity.class));
                    }else if (status.equalsIgnoreCase("passMiss")){
                        Toast.makeText(getApplicationContext(),"Password is incorrect",Toast.LENGTH_LONG).show();
                        passwordET.setText("");
                        passwordET.requestFocus();

                    }else {
                        Toast.makeText(getApplicationContext(),"No such user present in our Database",Toast.LENGTH_LONG).show();
                        loading.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    loading.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }








            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email",emailET.getText().toString());
                params.put("password",passwordET.getText().toString());
                return params;
            }
        };

        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);

    }

    private void load_homepage_activity() {
        finish();
        startActivity(new Intent(MainActivity.this,HomePageActivity.class));
    }


}
