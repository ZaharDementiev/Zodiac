package com.example.zodiac.Swipes;

import java.util.List;

public class ItemCard {
    private int userId;
    private String image;
    private String name, age, about;
    private List<String> photos;

    public ItemCard() { }

    public ItemCard(String image, String name, String age, String about, int userId, List<String> photos) {
        this.image = image;
        this.name = name;
        this.age = age;
        this.about = about;
        this.userId = userId;
        this.photos = photos;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
