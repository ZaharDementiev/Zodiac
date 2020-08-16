package com.example.zodiac.Chat;

public class Message {
    private long id;
    private long chatId;
    private String text;
    private String imageUrl;
    private long senderId;
    private String senderName;

    public Message() {}

    public Message(long chatId, String text, String imageUrl, long senderId, String senderName) {
        this.chatId = chatId;
        this.text = text;
        this.imageUrl = imageUrl;
        this.senderId = senderId;
        this.senderName = senderName;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }
    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUtl) {
        this.imageUrl = imageUtl;
    }

    public long getSenderId() {
        return senderId;
    }
    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
