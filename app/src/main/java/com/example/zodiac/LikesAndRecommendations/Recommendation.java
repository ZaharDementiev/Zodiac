package com.example.zodiac.LikesAndRecommendations;

public class Recommendation {
    private long id;
    private String nameAge;
    private String imageUrl;
    private long userId;

    public Recommendation() { }

    public Recommendation(long id, String nameAge, String imageUrl, long userId) {
        this.id = id;
        this.nameAge = nameAge;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    public String getNameAge() {
        return nameAge;
    }
    public void setNameAge(String nameAge) {
        this.nameAge = nameAge;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
