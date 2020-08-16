package com.example.zodiac.Chat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private User user;

    public MessageAdapter(@NonNull Context context, int resource, List<Message> messages, User user) {
        super(context, resource, messages);
        this.user = user;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        ImageView photoImageViewSender = convertView.findViewById(R.id.sendMessagePhoto);
        TextView textTextViewSender = convertView.findViewById(R.id.sendMessage);

        ImageView photoImageViewReceiver = convertView.findViewById(R.id.receivePhoto);
        TextView textTextViewReceiver = convertView.findViewById(R.id.receiveMessage);

        Message message = getItem(position);
        boolean isText = message.getImageUrl() == null;

        if (message.getSenderId() == user.getId()) {
            if (isText) {
                photoImageViewSender.setVisibility(View.GONE);
                textTextViewSender.setVisibility(View.VISIBLE);
                textTextViewSender.setText(message.getText());

            } else {
                photoImageViewSender.setVisibility(View.VISIBLE);
                textTextViewSender.setVisibility(View.GONE);
                Picasso.get().load("http://" + message.getImageUrl()).into(photoImageViewSender);
            }
            photoImageViewReceiver.setVisibility(View.GONE);
            textTextViewReceiver.setVisibility(View.GONE);
        } else {
            if (isText) {
                photoImageViewSender.setVisibility(View.GONE);
                textTextViewReceiver.setVisibility(View.VISIBLE);
                textTextViewReceiver.setText(message.getText());
            } else {
                photoImageViewReceiver.setVisibility(View.VISIBLE);
                textTextViewReceiver.setVisibility(View.GONE);
                Picasso.get().load("http://" + message.getImageUrl()).into(photoImageViewReceiver);
                photoImageViewSender.setVisibility(View.GONE);
                textTextViewSender.setVisibility(View.GONE);
            }
            photoImageViewSender.setVisibility(View.GONE);
            textTextViewSender.setVisibility(View.GONE);
        }

        return convertView;
    }
}
