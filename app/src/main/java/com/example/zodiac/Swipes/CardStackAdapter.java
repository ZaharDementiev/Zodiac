package com.example.zodiac.Swipes;

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
import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.CardViewHolder> {

    private CardStackAdapter.OnUserClickListener listener;
    private List<ItemCard> items;

    public CardStackAdapter(List<ItemCard> items) {
        this.items = items;
    }

    public void addItem(ItemCard item) {
        items.add(item);
    }

    public void removeItem(int position) {
        items.remove(position);
    }

    public ItemCard getItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, age, about;
        long userId;
        CardViewHolder(@NonNull View itemView, OnUserClickListener listener) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image_card);
            name = itemView.findViewById(R.id.item_name_card);
            age = itemView.findViewById(R.id.item_age_card);
            about = itemView.findViewById(R.id.item_about_card);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUserClickListener(image);
                }
            });
        }

        void setData(ItemCard data) {
            if (!data.getImage().equals(""))
                Picasso.get().load("http://" + data.getImage()).fit().centerCrop().into(image);
            name.setText(data.getName());
            age.setText(data.getAge());
            about.setText(data.getAbout());
            userId = data.getUserId();
        }
    }

    public void setOnUserClickListener(CardStackAdapter.OnUserClickListener listener) {
        this.listener = listener;
    }

    public interface OnUserClickListener {
        void onUserClickListener(ImageView view);
    }
}
