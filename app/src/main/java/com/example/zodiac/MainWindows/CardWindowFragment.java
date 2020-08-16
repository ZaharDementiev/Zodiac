package com.example.zodiac.MainWindows;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.Helpers.ModelsFiller;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.R;
import com.example.zodiac.SearchingData.SearchingSettings;
import com.example.zodiac.Swipes.CardStackAdapter;
import com.example.zodiac.Swipes.ItemCard;
import com.example.zodiac.Swipes.Swipe;
import com.example.zodiac.Users.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CardWindowFragment extends Fragment {

    private User mainUser;
    private CardStackLayoutManager manager;
    private CardStackAdapter adapter;
    private int currentPosition;

    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private CircleImageView dislikeCircleImageView;
    private CircleImageView likeCircleImageView;

    // Константы
    private static final int VISIBLE_COUNT = 3;
    private static final int COUNT_FOR_NEW_CARDS = VISIBLE_COUNT - 1;

    public CardWindowFragment(User user) {
        mainUser = user;
    }

    public CardWindowFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null)
            mainUser = (User) getArguments().get("user");
        return inflater.inflate(R.layout.activity_card_window_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        dislikeCircleImageView = getActivity().findViewById(R.id.dislikeCircleImageView);
        likeCircleImageView = getActivity().findViewById(R.id.likeCircleImageView);

        int geoPermission = ContextCompat.checkSelfPermission(CardWindowFragment.this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);

        if (geoPermission == -1)
            Toast.makeText(CardWindowFragment.this.getActivity(), "Вы должны передавать свои геоданные для отображения людей по близости", Toast.LENGTH_SHORT).show();
        else if (geoPermission == 0) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());

            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                Toast.makeText(CardWindowFragment.this.getContext(), "Enable GPS and update this page", Toast.LENGTH_LONG).show();
            else {
                if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                        Location location = task.getResult();
                        if (location != null) {
                            try {
                                Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                mainUser.setLongitude(addresses.get(0).getLongitude());
                                mainUser.setLatitude(addresses.get(0).getLatitude());

                                DBHandler.updateUser(CardWindowFragment.this.getContext(), mainUser, new DBHandler.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String result) {
                                        getCards();
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                    }
                                });

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else
                    ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }
    }

    private void getCards() {
        CardStackView cardStackView = getActivity().findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(this.getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Swipe swipe = new Swipe();
                if (direction == Direction.Right) {
                    swipe.setWasVisitedBy(mainUser.getId());
                    swipe.setWhoWasVisited(adapter.getItem(currentPosition).getUserId());
                    swipe.setRightSwipe(true);
                    DBHandler.createSympathy(CardWindowFragment.this.getContext(), swipe.getWasVisitedBy(), swipe.getWhoWasVisited());
                    DBHandler.createChat(CardWindowFragment.this.getContext(), swipe.getWasVisitedBy(), swipe.getWhoWasVisited(), new DBHandler.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {

                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });
                } else if (direction == Direction.Left) {
                    swipe.setWasVisitedBy(mainUser.getId());
                    swipe.setWhoWasVisited(adapter.getItem(currentPosition).getUserId());
                    swipe.setRightSwipe(false);
                }
                DBHandler.createSwipe(CardWindowFragment.this.getContext(), swipe);

                deleteCard(manager.getTopPosition() - 1);
                if (adapter.getItemCount() - manager.getTopPosition() == COUNT_FOR_NEW_CARDS)
                    if (adapter.getItemCount() == 0)
                        Toast.makeText(CardWindowFragment.this.getContext(), "Вы просмотрели всех людей", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCardRewound() {
            }

            @Override
            public void onCardCanceled() {
            }

            @Override
            public void onCardAppeared(View view, int position) {
                currentPosition = position;
            }

            @Override
            public void onCardDisappeared(View view, int position) {
            }
        });

        dislikeCircleImageView.setOnClickListener(view -> {
            if (adapter.getItemCount() > 0)
                bottomButtonsSet(new Swipe(), false);
            else
                Toast.makeText(CardWindowFragment.this.getContext(), "Вы просмотрели всех людей", Toast.LENGTH_LONG).show();
        });
        likeCircleImageView.setOnClickListener(view -> {
            if (adapter.getItemCount() > 0) {
                Swipe swipe = new Swipe();
                bottomButtonsSet(swipe, true);
                DBHandler.createSympathy(CardWindowFragment.this.getContext(), swipe.getWasVisitedBy(), swipe.getWhoWasVisited());
                DBHandler.createChat(CardWindowFragment.this.getContext(), swipe.getWasVisitedBy(), swipe.getWhoWasVisited(), new DBHandler.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            } else
                Toast.makeText(CardWindowFragment.this.getContext(), "Вы просмотрели всех людей", Toast.LENGTH_LONG).show();
        });
        setCardsSettings(cardStackView);
    }

    private void bottomButtonsSet(Swipe swipe, boolean isRight) {
        swipe.setWasVisitedBy(mainUser.getId());
        swipe.setWhoWasVisited(adapter.getItem(currentPosition).getUserId());
        swipe.setRightSwipe(isRight);
        DBHandler.createSwipe(CardWindowFragment.this.getContext(), swipe);
        if (adapter.getItemCount() > 0) {
            deleteCard(0);
            if (adapter.getItemCount() == 0)
                Toast.makeText(CardWindowFragment.this.getContext(), "Вы просмотрели всех людей", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteCard(int position) {
        adapter.removeItem(position);
        adapter.notifyDataSetChanged();
    }

    private void setCardsSettings(CardStackView cardStackView) {
        manager.setStackFrom(StackFrom.Top);
        manager.setVisibleCount(VISIBLE_COUNT);
        manager.setTranslationInterval(4.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.45f);
        manager.setMaxDegree(45.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(false);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        firstGettingCards(cardStackView);
    }

    private void firstGettingCards(CardStackView cardStackView) {
        DBHandler.getSearchingSettings(CardWindowFragment.this.getContext(), mainUser.getId(), new DBHandler.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    SearchingSettings searchingSettings = ModelsFiller.fillSearchingSettings(new JSONObject(result));
                    DBHandler.getUserByCoordinates(CardWindowFragment.this.getContext(), mainUser, -1, searchingSettings.getFindingRadius(), new DBHandler.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                List<ItemCard> local = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                adapter = new CardStackAdapter(local);
                                cardStackView.setLayoutManager(manager);
                                cardStackView.setAdapter(adapter);
                                cardStackView.setItemAnimator(new DefaultItemAnimator());

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    User user = ModelsFiller.fillUser(jsonArray.getJSONObject(i));
                                    int age = Integer.parseInt(Utils.howOld(user.getBirthday()));
                                    if (age >= searchingSettings.getAgeStart() && age <= searchingSettings.getAgeEnd())
                                        adapter.addItem(new ItemCard(user.getMainPhoto(), user.getName(), user.getBirthday(), user.getBio(), user.getId(), null));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        });
    }
}