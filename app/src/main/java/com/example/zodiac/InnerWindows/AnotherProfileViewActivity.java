package com.example.zodiac.InnerWindows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.zodiac.Chat.ChatItem;
import com.example.zodiac.R;
import com.example.zodiac.Swipes.CardStackAdapter;
import com.example.zodiac.Swipes.ItemCard;
import com.example.zodiac.Users.User;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class AnotherProfileViewActivity extends AppCompatActivity {

    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    private User mainUser;
    private int currentPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_profile_view);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent givenIntent = getIntent();
        mainUser = givenIntent.getParcelableExtra("user");
        mainUser.setPhotos(givenIntent.getStringArrayListExtra("list"));

        CardStackView cardStackView = findViewById(R.id.another_card_edit_view);
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
            }

            @Override
            public void onCardSwiped(Direction direction) {

            }

            @Override
            public void onCardRewound() {
            }

            @Override
            public void onCardCanceled() {
            }

            @Override
            public void onCardAppeared(View view, int position) {

            }

            @Override
            public void onCardDisappeared(View view, int position) {
            }
        });

        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(1);
        manager.setSwipeableMethod(SwipeableMethod.None);
        manager.setCanScrollHorizontal(false);
        manager.setCanScrollVertical(false);
        manager.setOverlayInterpolator(new LinearInterpolator());

        List<ItemCard> user = new ArrayList<>();
        adapter = new CardStackAdapter(user);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
        adapter.addItem(new ItemCard(mainUser.getMainPhoto(), mainUser.getName(), mainUser.getBirthday(), mainUser.getBio(), mainUser.getId(), mainUser.getPhotos()));

        adapter.setOnUserClickListener(view -> {
            if (mainUser.getPhotos().size() > 1) {
                if (currentPosition >= mainUser.getPhotos().size())
                    currentPosition = 0;
                Picasso.get().load("http://" + mainUser.getPhotos().get(currentPosition)).fit().centerCrop().into(view);
                currentPosition++;
            }
        });
    }
}