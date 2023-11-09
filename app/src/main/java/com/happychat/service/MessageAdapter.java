package com.happychat.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.happychat.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private List<Message> messages;
    //构造器
    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }


    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.getFromMe() == 1 ? 1 : 2;
    }

    @NonNull
    @Override
    //为每个列表项创建一个新的视图，并将它添加到RecyclerView中
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_from_me, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_from_other, parent, false);
        }
        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageText.setText(message.getText());
        holder.messageTime.setText(message.getTime());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView messageTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            messageTime = itemView.findViewById(R.id.messageTime);
        }
    }
}
