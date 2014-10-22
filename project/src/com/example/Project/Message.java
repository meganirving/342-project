package com.example.Project;

/**
 * Created by Megan on 22/10/2014.
 */
public class Message {
    private int ID;
    private String message;
    private double latitude;
    private double longitude;
    private int score;

    // set/get
    public int getID() { return ID; }
    public double getLat() { return latitude; }
    public double getLong() { return longitude; }
    public int getScore() { return score; }
    public String getMessage() { return message; }
    public void setID(int newID) { ID = newID; }
    public void setLat(double newLat) { latitude = newLat; }
    public void setLong(double newLong) { longitude = newLong; }
    public void setScore(int newScore) { score = newScore; }
    public void setMsg(String newMsg) { message = newMsg; }

    @Override
    public String toString() { return message; }

    // update score
    public void updateScore(boolean positive) {
        if (positive) {
            score++;
        } else {
            score--;
        }
    }
}
