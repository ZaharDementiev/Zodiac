package com.example.zodiac.Chat;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatItem implements Parcelable {
    private long id;
    private long senderId;
    private long receiverId;
    private String image;
    private String chatName;
    private String sender;
    private String senderImage;
    private String lastMessage;

    private long firstUserId;
    private long secondUserId;

    public ChatItem() { }

    //Конструктор для формирования списка переписок
    public ChatItem(long id, String chatName, String image, String lastMessage) {
        this.id = id;
        this.chatName = chatName;
        this.image = image;
        this.lastMessage = lastMessage;
    }

    //Конструктор для самого чата
    public ChatItem(long id, String chatName, String image, String lastMessage, String sender, String senderImage, long senderId, long receiverId) {
        this(id, chatName, image, lastMessage);
        this.sender = sender;
        this.senderImage = senderImage;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    protected ChatItem(Parcel in) {
        id = in.readLong();
        senderId = in.readLong();
        receiverId = in.readLong();
        image = in.readString();
        chatName = in.readString();
        sender = in.readString();
        senderImage = in.readString();
        firstUserId = in.readLong();
        secondUserId = in.readLong();
        lastMessage = in.readString();
    }

    public static final Creator<ChatItem> CREATOR = new Creator<ChatItem>() {
        @Override
        public ChatItem createFromParcel(Parcel in) {
            return new ChatItem(in);
        }

        @Override
        public ChatItem[] newArray(int size) {
            return new ChatItem[size];
        }
    };

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getChatName() {
        return chatName;
    }
    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderImage() {
        return senderImage;
    }
    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public long getSenderId() {
        return senderId;
    }
    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public long getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(long firstUserId) {
        this.firstUserId = firstUserId;
    }

    public long getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(long secondUserId) {
        this.secondUserId = secondUserId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(senderId);
        parcel.writeLong(receiverId);
        parcel.writeString(image);
        parcel.writeString(chatName);
        parcel.writeString(sender);
        parcel.writeString(senderImage);
        parcel.writeLong(firstUserId);
        parcel.writeLong(secondUserId);
        parcel.writeString(lastMessage);
    }
}
