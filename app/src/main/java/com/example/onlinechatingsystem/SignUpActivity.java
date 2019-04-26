package com.example.onlinechatingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
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

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText nameEt ,emailET, passwordET, rePasswordEt;
    Button signUp;
    View loading;
    SharedPreferences sp;
    String name,email,pass,repass;
    final String url = "https://chatterboxunlimited.000webhostapp.com/signup.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setViews();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                sign_up_clicked();
            }
        });



    }

    private void sign_up_clicked() {

        name = nameEt.getText().toString();
        email = emailET.getText().toString();
        pass = passwordET.getText().toString();
        repass = rePasswordEt.getText().toString();

        if (name.equals("") || email.equals("") || pass.equals("") || repass.equals("")){
            Toast.makeText(getApplicationContext(),"Please fill all the above feilds",Toast.LENGTH_LONG).show();
            loading.setVisibility(View.GONE);
        }else {
            if (!pass.equals(repass)){
                Toast.makeText(getApplicationContext(),"Passwords donot match",Toast.LENGTH_LONG).show();
                rePasswordEt.requestFocus();
            }else {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.setVisibility(View.GONE);
                        Log.d("Response",response);
                        try {
                            JSONObject jObject = new JSONObject(response);
                            String status = null;
                            try {
                                status = jObject.getString("status");
                            } catch (JSONException e) {
                                loading.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"11"+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                            }

                            if ("done".equalsIgnoreCase(status)){
                                sp.edit().putString("email",email).apply();
                                sp.edit().putString("password",pass).apply();
                                sp.edit().putString("name",name).apply();
                                sp.edit().putString("chat_table",jObject.getString("chat_table")).apply();
                                sp.edit().putString("id",jObject.getString("id")).apply();

                                finish();
                                startActivity(new Intent(SignUpActivity.this,HomePageActivity.class));
                            }else {
                                emailET.requestFocus();
                                Toast.makeText(getApplicationContext(),"This user is already present in DataBase",Toast.LENGTH_LONG).show();
                                loading.setVisibility(View.GONE);
                            }




                        } catch (JSONException e) {
                            loading.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"12"+e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"13"+error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("name",name);
                        params.put("email",email);
                        params.put("password",pass);
                        return params;
                    }
                };
                VolleySingleton.getmInstance(getApplicationContext()).addToRequestQue(stringRequest);
            }
        }



    }

    private void setViews(){
        sp = getSharedPreferences("LOGIN",MODE_PRIVATE);

        nameEt = findViewById(R.id.signup_name);
        emailET = findViewById(R.id.signup_email);
        passwordET = findViewById(R.id.signup_password);
        rePasswordEt = findViewById(R.id.signup_repassword);

        signUp = findViewById(R.id.signup_signup_btn);
        loading = findViewById(R.id.loading);

    }
}
