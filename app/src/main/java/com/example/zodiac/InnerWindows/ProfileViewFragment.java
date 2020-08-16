package com.example.zodiac.InnerWindows;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.Helpers.ModelsFiller;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.MainWindows.CardWindowFragment;
import com.example.zodiac.NotificationWindows.GeoDataActivity;
import com.example.zodiac.NotificationWindows.SendNotificationsActivity;
import com.example.zodiac.R;
import com.example.zodiac.SearchingData.SearchingSettings;
import com.example.zodiac.Swipes.CardStackAdapter;
import com.example.zodiac.Swipes.ItemCard;
import com.example.zodiac.Swipes.Swipe;
import com.example.zodiac.Users.User;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewFragment extends Fragment {

    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    private User mainUser;
    private int currentPosition = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainUser = ((ProfileEditActivity)getActivity()).getMainUser();
        return inflater.inflate(R.layout.activity_profile_view_fragment, container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        CardStackView cardStackView = getActivity().findViewById(R.id.card_edit_view);
        manager = new CardStackLayoutManager(this.getContext(), new CardStackListener() {
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