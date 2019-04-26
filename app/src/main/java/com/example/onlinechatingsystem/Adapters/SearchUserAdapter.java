package com.example.onlinechatingsystem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.onlinechatingsystem.ChatActivity;
import com.example.onlinechatingsystem.Helper.C_Threads;
import com.example.onlinechatingsystem.Helper.VolleySingleton;
import com.example.onlinechatingsystem.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.HPViewHolder> {


    private Context context;
    private List<C_Threads> list;
    private final int[] colorId = {R.color.one,R.color.two,R.color.three,R.color.four,R.color.five,
            R.color.six,R.color.seven,R.color.eight,R.color.nine,R.color.ten};

    String url = "https://chatterboxunlimited.000webhostapp.com/startchat.php";
    SharedPreferences sp;

    StringRequest stringRequest;

    public SearchUserAdapter(Context context, List<C_Threads> list) {
        this.context = context;
        this.list = list;
        sp = context.getSharedPreferences("LOGIN",MODE_PRIVATE);
    }

    @NonNull
    @Override
    public HPViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_home_page, viewGroup, false);

        return new HPViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HPViewHolder holder, int i) {

        final C_Threads item = list.get(i);

        Log.d("Color" , String.valueOf(i%10));

        holder.startAlphabet.setText(Character.toString(item.getName().toUpperCase().charAt(0)));
        holder.senderEmail.setText(item.getName());
        holder.btnBack.setCardBackgroundColor(ContextCompat.getColor(context,colorId[(int) (Math.random() * ((10 - 1) + 1))]));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("OnClick Resp" ,response);

                        if (response.equalsIgnoreCase("ChatPresent")){
                            Toast.makeText(context,"You are already connected to this user",Toast.LENGTH_LONG).show();
                        }else if (response.equalsIgnoreCase("usrErr")){
                            Toast.makeText(context,"User is not defined in our Database",Toast.LENGTH_LONG).show();
                        }else if (response.equalsIgnoreCase("dataEntryErr") || response.equalsIgnoreCase("TableCreateErr")){
                            Toast.makeText(context,"We are facing some problems please try again.",Toast.LENGTH_LONG).show();

                        }else if(response.contains("msgt_")){
                            Intent i = new Intent(context, ChatActivity.class);
                            ((Activity)context).finish();
                            i.putExtra("id",String.valueOf(item.getId()));
                            i.putExtra("name",item.getName());
                            i.putExtra("email",item.getEmail());
                            i.putExtra("msg_table",response);
                            i.putExtra("chat_table",item.getChat_table());
                            context.startActivity(i);
                        }else {
                            Toast.makeText(context,"We are facing some problems please try again.",Toast.LENGTH_LONG).show();

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
                        params.put("sId",sp.getString("id",""));
                        params.put("rId", String.valueOf(item.getId()));
                        return params;
                    }
                };
                VolleySingleton.getmInstance(context).addToRequestQue(stringRequest);
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class HPViewHolder extends RecyclerView.ViewHolder{

        TextView startAlphabet, senderEmail;
        CardView btnBack;

        public HPViewHolder(@NonNull View itemView) {
            super(itemView);

            startAlphabet = itemView.findViewById(R.id.starting_char);
            senderEmail = itemView.findViewById(R.id.item_email);
            btnBack = itemView.findViewById(R.id.btn_text_background);



        }
    }
}
