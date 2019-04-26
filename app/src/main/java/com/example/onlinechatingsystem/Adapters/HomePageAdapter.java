package com.example.onlinechatingsystem.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlinechatingsystem.ChatActivity;
import com.example.onlinechatingsystem.Helper.C_Threads;
import com.example.onlinechatingsystem.R;

import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.HPViewHolder> {


    private Context context;
    private List<C_Threads> list;
    private final int[] colorId = {R.color.one,R.color.two,R.color.three,R.color.four,R.color.five,
            R.color.six,R.color.seven,R.color.eight,R.color.nine,R.color.ten};

    public HomePageAdapter(Context context, List<C_Threads> list) {
        this.context = context;
        this.list = list;
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
                Intent i = new Intent(context, ChatActivity.class);
                Log.d("Rid", String.valueOf(item.getId()));
                i.putExtra("id",String.valueOf(item.getId()));
                i.putExtra("name",item.getName());
                i.putExtra("email",item.getEmail());
                i.putExtra("msg_table",item.getMsg_table());
                i.putExtra("chat_table",item.getChat_table());
                context.startActivity(i);
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
