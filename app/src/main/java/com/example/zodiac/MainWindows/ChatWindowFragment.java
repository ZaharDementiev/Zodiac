package com.example.zodiac.MainWindows;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.example.zodiac.Chat.ChatItem;
import com.example.zodiac.Chat.ChatItemAdapter;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.Helpers.ModelsFiller;
import com.example.zodiac.InnerWindows.ChatActivity;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ChatWindowFragment extends Fragment {

    private User mainUser;
    private TextView starterChatText;
    private TextView messagesIntro;

    private ArrayList<ChatItem> chatItemArrayList;
    private RecyclerView userRecyclerView;
    private ChatItemAdapter chatItemAdapter;
    private RecyclerView.LayoutManager userLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null)
            mainUser = (User) getArguments().get("user");
        return inflater.inflate(R.layout.activity_chat_window_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        starterChatText = getActivity().findViewById(R.id.starterChatText);
        messagesIntro = getActivity().findViewById(R.id.messagesIntro);
        chatItemArrayList = new ArrayList<>();
        userRecyclerView = getActivity().findViewById(R.id.chatListRecyclerView);

        buildRecyclerView();
        attachItems();
    }

    //получение чата из бд
    private void attachItems() {
        DBHandler.getChats(ChatWindowFragment.this.getContext(), mainUser.getId(), new DBHandler.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray jsonArray = new JSONObject(result).getJSONArray("result");
                    if (result.equals("{\"result\":[]}")) {
                        starterChatText.setVisibility(View.VISIBLE);
                        messagesIntro.setVisibility(View.VISIBLE);
                        userRecyclerView.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ChatItem item = ModelsFiller.chatItemFillerId(jsonArray.getJSONObject(i));
                        long receiver;
                        if (item.getFirstUserId() == mainUser.getId())
                            receiver = item.getSecondUserId();
                        else
                            receiver = item.getFirstUserId();

                        DBHandler.getUserById(ChatWindowFragment.this.getContext(), receiver, new DBHandler.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject obj = new JSONObject(result);
                                    User localUser = ModelsFiller.fillUser(obj);
                                    ChatItem chatItem = ModelsFiller.chatItemFillerOtherFieldsByUser(item, localUser, obj);
                                    DBHandler.getLastMessage(ChatWindowFragment.this.getContext(), chatItem.getId(), new DBHandler.VolleyCallback() {
                                        @Override
                                        public void onSuccess(String newResult) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(newResult);
                                                if (!jsonObject.getString("text").equals(""))
                                                    chatItem.setLastMessage(jsonObject.getString("text"));
                                                else if (!jsonObject.getString("imageUrl").equals(""))
                                                    chatItem.setLastMessage("image");
                                            } catch (JSONException e) {
                                                if (newResult.equals("null"))
                                                    chatItem.setLastMessage("No messages");
                                            }
                                            chatItemArrayList.add(chatItem);
                                            chatItemAdapter.notifyDataSetChanged();
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
                            public void onError(VolleyError error) { }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) { }
        });
    }

    private void buildRecyclerView() {
        userRecyclerView.setHasFixedSize(true);
        userLayoutManager = new LinearLayoutManager(getActivity());
        chatItemAdapter = new ChatItemAdapter(chatItemArrayList);

        userRecyclerView.setLayoutManager(userLayoutManager);
        userRecyclerView.setAdapter(chatItemAdapter);

        chatItemAdapter.setOnUserClickListener(this::goToChat);
    }

    private void goToChat(int position) {
        Intent intent = new Intent(ChatWindowFragment.this.getContext(), ChatActivity.class);
        intent.putExtra("user", mainUser);
        intent.putExtra("chat", chatItemArrayList.get(position));
        startActivity(intent);
    }
}