package com.example.zodiac.Swipes;

public class Swipe {
    private long id;
    private long wasVisitedBy;
    private long whoWasVisited;
    private boolean isRightSwipe;

    public Swipe() { }

    public Swipe(long id, long wasVisitedBy, long whoWasVisited, boolean isRightSwipe) {
        this.id = id;
        this.wasVisitedBy = wasVisitedBy;
        this.whoWasVisited = whoWasVisited;
        this.isRightSwipe = isRightSwipe;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWasVisitedBy() {
        return wasVisitedBy;
    }

    public void setWasVisitedBy(long wasVisitedBy) {
        this.wasVisitedBy = wasVisitedBy;
    }

    public long getWhoWasVisited() {
        return whoWasVisited;
    }

    public void setWhoWasVisited(long whoWasVisited) {
        this.whoWasVisited = whoWasVisited;
    }

    public boolean isRightSwipe() {
        return isRightSwipe;
    }

    public void setRightSwipe(boolean rightSwipe) {
        isRightSwipe = rightSwipe;
    }
}
