package com.example.zodiac.SearchingData;

public class SearchingSettings {
    private long id;
    private long userId;
    private int findingRadius;
    private int ageStart;
    private int ageEnd;

    public SearchingSettings() { }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getFindingRadius() {
        return findingRadius;
    }
    public void setFindingRadius(int findingRadius) {
        this.findingRadius = findingRadius;
    }

    public int getAgeStart() {
        return ageStart;
    }
    public void setAgeStart(int ageStart) {
        this.ageStart = ageStart;
    }

    public int getAgeEnd() {
        return ageEnd;
    }
    public void setAgeEnd(int ageEnd) {
        this.ageEnd = ageEnd;
    }
}
