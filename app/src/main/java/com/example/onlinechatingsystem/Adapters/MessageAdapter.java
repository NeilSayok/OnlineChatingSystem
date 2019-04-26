package com.example.onlinechatingsystem.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlinechatingsystem.Helper.Message_item;
import com.example.onlinechatingsystem.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Message_item> msgList;
    SharedPreferences sp;

    public MessageAdapter(Context context, List<Message_item> msgList) {
        this.context = context;
        this.msgList = msgList;
        sp = context.getSharedPreferences("LOGIN",MODE_PRIVATE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        switch (i){
            case 0:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_text_sent, viewGroup, false);
                return new ViewHolderSent(itemView);
            case 1:
                itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_text_rec, viewGroup, false);
                return new ViewHolderRecieved(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        Message_item msg = msgList.get(i);

        switch (holder.getItemViewType()){
            case 0:
                ViewHolderSent viewHolderSent = (ViewHolderSent)holder;
                viewHolderSent.msg.setText(msg.getMessage());
                viewHolderSent.time.setText(msg.getTime());
                break;
            case 1:
                ViewHolderRecieved viewHolderRecieved = (ViewHolderRecieved)holder;
                viewHolderRecieved.msg.setText(msg.getMessage());
                viewHolderRecieved.time.setText(msg.getTime());
                break;
        }


    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (msgList.get(position).getFrom() == Integer.parseInt(sp.getString("id","-1"))){
            return 0;
        }else
            return 1;
    }

    class ViewHolderSent extends RecyclerView.ViewHolder {
        TextView msg,time;
        public ViewHolderSent(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg);
            time = itemView.findViewById(R.id.time);
        }
    }

    class ViewHolderRecieved extends RecyclerView.ViewHolder {
        TextView msg,time;
        public ViewHolderRecieved(@NonNull View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.msg);
            time = itemView.findViewById(R.id.time);
        }
    }


}
