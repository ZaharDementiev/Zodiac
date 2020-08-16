package com.example.zodiac.Users;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {
    private int id;
    private String name;
    private String phoneNumber;
    private String mail;
    private String password;
    private String birthday;
    private String mainPhoto;
    private double latitude;
    private double longitude;
    private String bio;
    private String city;
    private List<String> photos;

    public User(int id, String name, String phoneNumber, String mail,
                String password, String birthday, String mainPhoto,
                double latitude, double longitude, String bio, String city) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
        this.password = password;
        this.birthday = birthday;
        this.mainPhoto = mainPhoto;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bio = bio;
        this.city = city;
        this.photos = new ArrayList<>();
    }

    public User() {
        this.id = 0;
        this.name = "";
        this.phoneNumber = "";
        this.mail = "";
        this.password = "";
        this.birthday = "";
        this.mainPhoto = "";
        this.latitude = -10000.0;
        this.longitude = -10000.0;
        this.bio = "";
        this.city = "";
    }

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phoneNumber = in.readString();
        mail = in.readString();
        password = in.readString();
        birthday = in.readString();
        mainPhoto = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        bio = in.readString();
        city = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(mail);
        dest.writeString(password);
        dest.writeString(birthday);
        dest.writeString(mainPhoto);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(bio);
        dest.writeString(city);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMainPhoto() {
        return mainPhoto;
    }
    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getPhotos() {
        return photos;
    }
    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
