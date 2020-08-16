package com.example.zodiac.InnerWindows;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.zodiac.Chat.ChatItem;
import com.example.zodiac.Chat.Message;
import com.example.zodiac.Chat.MessageAdapter;
import com.example.zodiac.DB.Constants;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.DB.RequestHandler;
import com.example.zodiac.DB.SharedPrefManager;
import com.example.zodiac.Helpers.ModelsFiller;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.MainWindows.MainWindowActivity;
import com.example.zodiac.R;
import com.example.zodiac.SignIn.EmailSignInActivity;
import com.example.zodiac.Users.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class ChatActivity extends AppCompatActivity {

    private ChatItem chatItem;
    private ListView messageListView;
    private MessageAdapter adapter;
    private ImageButton sendImageButton;
    private ImageButton sendMessageButton;
    private EditText messageEditText;

    private static final int RC_IMAGE_PICKER = 17;
    private TextView userNameChat;
    private CircleImageView avatarChat;

    private Message message;
    private User mainUser;

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sendImageButton = findViewById(R.id.sendPhotoChat);
        sendMessageButton = findViewById(R.id.sendMessageChat);
        messageEditText = findViewById(R.id.chatTextView);
        userNameChat = findViewById(R.id.userNameChat);
        avatarChat = findViewById(R.id.avatarChat);
        sendMessageButton.setEnabled(false);

        Intent givenIntent = getIntent();
        ChatItem givenItem = givenIntent.getParcelableExtra("chat");
        ChatItem ids = givenIntent.getParcelableExtra("ids");
        mainUser = givenIntent.getParcelableExtra("user");
        chatItemAction(givenItem, ids, mainUser);

        messageListView = findViewById(R.id.messagesListView);
        List<Message> messages = new ArrayList<>();
        adapter = new MessageAdapter(this, R.layout.item_message, messages, mainUser);

        DBHandler.getMessages(ChatActivity.this, chatItem.getId(), new DBHandler.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Message local = ModelsFiller.fillMessage(jsonArray.getJSONObject(i));
                        adapter.add(local);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

        messageListView.setAdapter(adapter);
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0)
                    sendMessageButton.setEnabled(true);
                else
                    sendMessageButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        messageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
        sendMessageButton.setOnClickListener(view -> {
            message = new Message(chatItem.getId(), messageEditText.getText().toString().trim(), null,
                    chatItem.getSenderId(), chatItem.getSender());

            //Создание сообщения для БД
            adapter.add(message);
            messageEditText.setText("");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CREATE_MESSAGE, response -> {}, error -> {}
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("chatId", String.valueOf(chatItem.getId()));
                    params.put("text", message.getText());
                    params.put("imageUrl", "");
                    params.put("senderId", String.valueOf(mainUser.getId()));

                    return params;
                }
            };
            RequestHandler.getInstance(ChatActivity.this).addToRequestQueue(stringRequest);
        });
        sendImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(Intent.createChooser(intent, "Choose image"), RC_IMAGE_PICKER);
        });

        try {
            Socket socket = IO.socket("http://134.0.116.112:6001");

            socket.on(Socket.EVENT_CONNECT, args -> socket.emit("foo", "hi")).on("chat-" + chatItem.getId(), args ->  {
                JSONObject jsonObject = (JSONObject)args[0];
                try {
                    if (jsonObject.getLong("senderId") != mainUser.getId()) {
                        message = ModelsFiller.fillMessage(jsonObject);
                        runOnUiThread(() -> {
                            adapter.add(message);
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).on(Socket.EVENT_DISCONNECT, args -> {});
            socket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void chatItemAction(ChatItem givenItem, ChatItem ids, User user) {
        if (givenItem != null && ids == null) {
            chatItem = new ChatItem(givenItem.getId(), givenItem.getChatName(), givenItem.getImage(), null,
                    user.getName(), user.getMainPhoto(), user.getId(), givenItem.getReceiverId());
            userNameChat.setText(chatItem.getChatName());
            Picasso.get().load("http://" + chatItem.getImage()).into(avatarChat);
        }
        else if (givenItem == null && ids != null) {
            DBHandler.getUserById(ChatActivity.this, ids.getReceiverId(), new DBHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        User localUser = ModelsFiller.fillUser(obj);
                        chatItem = new ChatItem(ids.getId(), localUser.getName(), localUser.getMainPhoto(), null,
                                user.getName(), user.getMainPhoto(), user.getId(), ids.getReceiverId());

                        userNameChat.setText(chatItem.getChatName());
                        Picasso.get().load("http://" + chatItem.getImage()).into(avatarChat);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 74, outputStream);
                byte[] imageBytes = outputStream.toByteArray();

                String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
                String imageData = encodedImage;

                DBHandler.uploadPhotoMessage(ChatActivity.this, imageData, chatItem.getId(), mainUser.getId(), new DBHandler.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String path = jsonObject.getString("path");
                            Message photoMessage = new Message(chatItem.getId(), null, uri.toString(), chatItem.getSenderId(), chatItem.getSender());
                            DBHandler.deleteLastPhotoMessage(ChatActivity.this, path, new DBHandler.VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CREATE_MESSAGE, response -> {}, error -> {}) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<>();
                                            params.put("chatId", String.valueOf(chatItem.getId()));
                                            params.put("text", "");
                                            params.put("imageUrl", path);
                                            params.put("senderId", String.valueOf(photoMessage.getSenderId()));
                                            return params;
                                        }
                                    };
                                    RequestHandler.getInstance(ChatActivity.this).addToRequestQueue(stringRequest);
                                    adapter.add(photoMessage);
                                }
                                @Override
                                public void onError(VolleyError error) { }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
