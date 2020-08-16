package com.example.zodiac.Helpers;

import com.example.zodiac.Chat.ChatItem;
import com.example.zodiac.Chat.Message;
import com.example.zodiac.Images.Image;
import com.example.zodiac.SearchingData.SearchingSettings;
import com.example.zodiac.Users.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class ModelsFiller {

    public static User fillUser(JSONObject jsonData) throws JSONException {
        User user = new User();
        user.setId(jsonData.getInt("id"));
        user.setName(jsonData.getString("name"));
        user.setMail(jsonData.getString("email"));
        user.setPassword(jsonData.getString("password"));
        user.setPhoneNumber(jsonData.getString("phone"));
        user.setBio(jsonData.getString("bio"));
        user.setCity(jsonData.getString("city"));
        user.setBirthday(jsonData.getString("birthday"));
        user.setMainPhoto(jsonData.getString("main_photo_path"));
        user.setLatitude(Double.parseDouble(jsonData.getString("latitude")));
        user.setLongitude(Double.parseDouble(jsonData.getString("longitude")));
        return user;
    }

    public static void fillImagesList(List<String> images, String jsonData) {
        try {
            JSONObject object = new JSONObject(jsonData);
            JSONArray jsonArray = object.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++)
                images.add(jsonArray.getJSONObject(i).getString("image_path"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ChatItem chatItemFillerId(JSONObject jsonObject) throws JSONException {
        ChatItem item = new ChatItem();
        item.setId(jsonObject.getLong("id"));
        item.setFirstUserId(jsonObject.getLong("participant_1"));
        item.setSecondUserId(jsonObject.getLong("participant_2"));
        return item;
    }

    public static ChatItem chatItemFillerOtherFieldsByUser(ChatItem chatItem, User user, JSONObject jsonObject) throws JSONException {
        ChatItem item = chatItem;
        chatItem.setChatName(user.getName());
        chatItem.setImage(user.getMainPhoto());
        return item;
    }

    public static ChatItem chatItemFiller(JSONObject jsonObject, long userId) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        JSONObject data = jsonArray.getJSONObject(0);
        ChatItem item = new ChatItem();
        item.setId(data.getLong("id"));
        item.setSenderId(userId);

        if (userId == data.getInt("participant_1"))
            item.setReceiverId(data.getInt("participant_2"));
        else if (userId == data.getInt("participant_2"))
            item.setReceiverId(data.getInt("participant_1"));

        return item;
    }

    public static SearchingSettings fillSearchingSettings(JSONObject jsonObject) throws JSONException {
        SearchingSettings searchingSettings = new SearchingSettings();
        searchingSettings.setId(jsonObject.getLong("id"));
        searchingSettings.setUserId(jsonObject.getLong("id_user"));
        searchingSettings.setFindingRadius(jsonObject.getInt("radius"));
        searchingSettings.setAgeStart(jsonObject.getInt("age_start"));
        searchingSettings.setAgeEnd(jsonObject.getInt("age_end"));
        return searchingSettings;
    }

    public static Image fillImage(JSONObject jsonObject) throws JSONException {
        Image image = new Image();
        image.setId(jsonObject.getLong("id"));
        image.setPath(jsonObject.getString("image_path"));
        image.setUserEmail(jsonObject.getString("owner"));
        return image;
    }

    public static Message fillMessage(JSONObject jsonObject) {
        Message message = new Message();
        try {
            message.setChatId(jsonObject.getLong("chatId"));
            if (!jsonObject.getString("text").equals(""))
                message.setText(jsonObject.getString("text"));
            else
                message.setText(null);
            message.setSenderId(jsonObject.getLong("senderId"));
            if (!jsonObject.getString("imageUrl").equals(""))
                message.setImageUrl(jsonObject.getString("imageUrl"));
            else
                message.setImageUrl(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }
}
