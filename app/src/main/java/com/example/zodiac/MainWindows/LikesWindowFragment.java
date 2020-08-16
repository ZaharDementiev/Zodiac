package com.example.zodiac.MainWindows;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.example.zodiac.Chat.ChatItem;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.Helpers.ModelsFiller;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.InnerWindows.AnotherProfileViewActivity;
import com.example.zodiac.InnerWindows.ChatActivity;
import com.example.zodiac.LikesAndRecommendations.Recommendation;
import com.example.zodiac.LikesAndRecommendations.RecommendationAdapter;
import com.example.zodiac.R;
import com.example.zodiac.Swipes.Swipe;
import com.example.zodiac.Users.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LikesWindowFragment extends Fragment {

    private User mainUser;

    private List<Recommendation[]> recommendationList;
    private RecyclerView recyclerView;
    private RecommendationAdapter adapter;
    private RecyclerView.LayoutManager manager;

    private TextView amountLikes;
    private TextView topSimilar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null)
            mainUser = (User) getArguments().get("user");
        return inflater.inflate(R.layout.activity_likes_window_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        amountLikes = getActivity().findViewById(R.id.amountLikes);
        topSimilar = getActivity().findViewById(R.id.topSimilar);

        onLikes();

        amountLikes.setOnClickListener(view -> onLikes());
        topSimilar.setOnClickListener(view -> onRecommendations());
    }

    private void onLikes() {
        amountLikes.setTextColor(getResources().getColor(R.color.white));
        topSimilar.setTextColor(getResources().getColor(R.color.gray_light));

        recommendationList = new ArrayList<>();
        recyclerView = getActivity().findViewById(R.id.likeGoldRecyclerView);

        buildRecyclerView();
        attachItemsLikes();
    }

    private void onRecommendations() {
        topSimilar.setTextColor(getResources().getColor(R.color.white));
        amountLikes.setTextColor(getResources().getColor(R.color.gray_light));

        recommendationList = new ArrayList<>();
        recyclerView = getActivity().findViewById(R.id.likeGoldRecyclerView);

        buildRecyclerView();
        attachItemsRecommendations();
    }

    private void buildRecyclerView() {
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(getActivity());
        adapter = new RecommendationAdapter(recommendationList, position -> {
            if (position < 0) {
                position++;
                position *= -1;
                makeLike(adapter.getRecommendationRow(position)[0].getUserId());
            } else {
                makeLike(adapter.getRecommendationRow(position)[1].getUserId());
            }
        });

        adapter.setOnUserCardListener(position -> {
            long userId = 0;
            if (position < 0) {
                position++;
                position *= -1;
                userId = adapter.getRecommendationRow(position)[0].getUserId();
            } else {
                userId = adapter.getRecommendationRow(position)[1].getUserId();
            }
            DBHandler.getUserById(getContext(), userId, new DBHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        User localUser = ModelsFiller.fillUser(obj);
                        DBHandler.getPhotos(getContext(), localUser.getMail(), new DBHandler.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                List<String> images = new ArrayList<>();
                                ModelsFiller.fillImagesList(images, result);
                                localUser.setPhotos(images);
                                Intent intent = Utils.goToNextActivity(localUser, new Intent(getContext(), AnotherProfileViewActivity.class));
                                intent.putExtra("list", (Serializable)localUser.getPhotos());
                                startActivity(intent);
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
        });

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void makeLike(long id) {
        Swipe swipe = new Swipe();
        swipe.setWasVisitedBy(mainUser.getId());
        swipe.setWhoWasVisited(id);
        swipe.setRightSwipe(true);
        DBHandler.createSympathy(this.getContext(), swipe.getWasVisitedBy(), swipe.getWhoWasVisited());
        DBHandler.createChat(this.getContext(), swipe.getWasVisitedBy(), swipe.getWhoWasVisited(), new DBHandler.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    long chatId = Integer.parseInt(new JSONObject(result).getString("id"));
                    DBHandler.getChatsById(LikesWindowFragment.this.getContext(), chatId, new DBHandler.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject obj = new JSONObject(result);
                                ChatItem chatItem = ModelsFiller.chatItemFiller(obj, mainUser.getId());
                                Intent intent = Utils.goToNextActivity(mainUser, new Intent(getActivity(),ChatActivity.class));
                                intent.putExtra("ids", chatItem);
                                startActivity(intent);
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
        DBHandler.createSwipe(this.getContext(), swipe);
    }

    private void attachItemsRecommendations() {
        DBHandler.getRecommendations(LikesWindowFragment.this.getActivity(), mainUser, 10, new DBHandler.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                onAttachingSuccess(result);
            }
            @Override
            public void onError(VolleyError error) { }
        });
    }

    private void attachItemsLikes() {
        DBHandler.getWhoLikesMe(LikesWindowFragment.this.getActivity(), mainUser.getId(), new DBHandler.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                onAttachingSuccess(result);
            }

            @Override
            public void onError(VolleyError error) { }
        });
    }

    private void onAttachingSuccess(String result) {
        try {
            List<Recommendation> recommendations = new ArrayList<>();
            JSONArray jsonArray = new JSONObject(result).getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                Recommendation recommendation = new Recommendation();
                recommendation.setId(jsonArray.getJSONObject(i).getLong("id"));
                DBHandler.getUserById(LikesWindowFragment.this.getActivity(), recommendation.getId(), new DBHandler.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject obj = new JSONObject(result);
                            User localUser = ModelsFiller.fillUser(obj);
                            recommendation.setImageUrl(localUser.getMainPhoto());
                            String nameAge = localUser.getName() + ", " + Utils.howOld(localUser.getBirthday());
                            recommendation.setNameAge(nameAge);
                            recommendation.setUserId(localUser.getId());
                            recommendations.add(recommendation);

                            if (recommendations.size() == jsonArray.length()) {
                                if (recommendations.size() % 2 != 0) {
                                    for (int i = 0; i < recommendations.size() - 1; i += 2) {
                                        Recommendation[] arr = new Recommendation[2];
                                        arr[0] = recommendations.get(i);
                                        arr[1] = recommendations.get(i + 1);

                                        recommendationList.add(arr);
                                    }
                                    Recommendation[] arr = new Recommendation[1];
                                    arr[0] = recommendations.get(recommendations.size() - 1);
                                    recommendationList.add(arr);
                                } else {
                                    for (int i = 0; i < recommendations.size(); i += 2) {
                                        Recommendation[] arr = new Recommendation[2];
                                        arr[0] = recommendations.get(i);
                                        arr[1] = recommendations.get(i + 1);

                                        recommendationList.add(arr);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) { }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}