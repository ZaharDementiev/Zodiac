package com.example.zodiac.Chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zodiac.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ChatItemViewHolder> {
    private ArrayList<ChatItem> chats;
    private ChatItemAdapter.OnUserClickListener listener;

    public ChatItemAdapter(ArrayList<ChatItem> users) {
        this.chats = users;
    }

    public interface OnUserClickListener {
        void onUserClickListener(int position);
    }

    public void setOnUserClickListener(ChatItemAdapter.OnUserClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatItemAdapter.ChatItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatItemAdapter.ChatItemViewHolder(view, listener);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemAdapter.ChatItemViewHolder holder, int position) {
        ChatItem chatItem = chats.get(position);
        if (!chatItem.getImage().equals(""))
            Picasso.get().load("http://" + chatItem.getImage()).into(holder.avatarImageView);
        holder.userNameTextView.setText(chatItem.getChatName());
        holder.lastMessage.setText(chatItem.getLastMessage());
    }

    public static class ChatItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView avatarImageView;
        public TextView userNameTextView;
        public TextView lastMessage;

        public ChatItemViewHolder(View itemView, ChatItemAdapter.OnUserClickListener listener) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            lastMessage = itemView.findViewById(R.id.last_message);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onUserClickListener(position);
                    }
                }
            });
        }
    }
}