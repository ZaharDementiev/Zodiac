package com.example.zodiac.LikesAndRecommendations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zodiac.R;
import com.squareup.picasso.Picasso;
import java.lang.ref.WeakReference;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder> {
    private List<Recommendation[]> recommendations;
    private RecommendationAdapter.OnCardClickListener listener;
    private RecommendationAdapter.OnLikeClickListener onLikeClickListener;

    public void setOnUserCardListener(RecommendationAdapter.OnCardClickListener listener) {
        this.listener = listener;
    }

    public RecommendationAdapter(List<Recommendation[]> recommendations, OnLikeClickListener onLikeClickListener) {
        this.recommendations = recommendations;
        this.onLikeClickListener = onLikeClickListener;
    }

    public Recommendation[] getRecommendationRow(int position) {
        return recommendations.get(position);
    }


    @NonNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_propositions, parent, false);
        return new RecommendationViewHolder(view, listener, onLikeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
        Recommendation[] innerRecommendations = recommendations.get(position);

        if (innerRecommendations.length == 2) {
            holder.secondItemInRow.setVisibility(View.VISIBLE);
            if (!innerRecommendations[0].getImageUrl().equals(""))
                Picasso.get().load("http://" + innerRecommendations[0].getImageUrl()).into(holder.likedPhoto1);
            holder.likedName1.setText(innerRecommendations[0].getNameAge());
            holder.userId1 = innerRecommendations[0].getUserId();
            if (!innerRecommendations[1].getImageUrl().equals(""))
                Picasso.get().load("http://" + innerRecommendations[1].getImageUrl()).into(holder.likedPhoto2);
            holder.likedName2.setText(innerRecommendations[1].getNameAge());
            holder.userId2 = innerRecommendations[1].getUserId();
        } else if (innerRecommendations.length == 1) {
            if (!innerRecommendations[0].getImageUrl().equals(""))
                Picasso.get().load("http://" + innerRecommendations[0].getImageUrl()).into(holder.likedPhoto1);
            holder.likedName1.setText(innerRecommendations[0].getNameAge());
            holder.secondItemInRow.setVisibility(View.INVISIBLE);
            holder.userId1 = innerRecommendations[0].getUserId();
        }
    }

    @Override
    public int getItemCount() {
        return recommendations.size();
    }

    public static class RecommendationViewHolder extends RecyclerView.ViewHolder {
        public long userId1;
        public long userId2;

        public TextView likedName1;
        public TextView likedName2;

        public ImageView likedPhoto1;
        public ImageView likedPhoto2;

        public CircleImageView likeFirst;
        public CircleImageView likeSecond;

        public RelativeLayout secondItemInRow;
        private WeakReference<OnLikeClickListener> listenerRef;

        public RecommendationViewHolder(View itemView, RecommendationAdapter.OnCardClickListener listener,
                                        RecommendationAdapter.OnLikeClickListener onLikeClickListener) {
            super(itemView);
            listenerRef = new WeakReference<>(onLikeClickListener);
            likedName1 = itemView.findViewById(R.id.likedName1);
            likedName2 = itemView.findViewById(R.id.likedName2);
            likedPhoto1 = itemView.findViewById(R.id.likedPhoto1);
            likedPhoto2 = itemView.findViewById(R.id.likedPhoto2);
            likeFirst = itemView.findViewById(R.id.likeFirst);
            likeSecond = itemView.findViewById(R.id.likeSecond);
            secondItemInRow = itemView.findViewById(R.id.secondItemInRow);

            likedPhoto1.setOnClickListener(view -> listener.onCardClickListener((getAdapterPosition() + 1) * -1));
            likedPhoto2.setOnClickListener(view -> listener.onCardClickListener(getAdapterPosition()));

            likeFirst.setOnClickListener(view -> {
                listenerRef.get().onLikeClickListener((getAdapterPosition() + 1) * -1);
            });
            likeSecond.setOnClickListener(view -> {
                listenerRef.get().onLikeClickListener(getAdapterPosition());
            });
        }
    }

    public interface OnCardClickListener {
        void onCardClickListener(int position);
    }

    public interface OnLikeClickListener {
        void onLikeClickListener(int position);
    }
}
