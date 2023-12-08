package it.unimib.enjoyn;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Calendar;

public class Event implements Parcelable {

    private int id;
    private String title;

    private String description;

    private String date;

    private String time;

    private boolean confidential;

    private String place;

    private String placeName;

    private Category category;

    private int peopleNumber;

    private double distance;

    private Meteo meteo;

    public Event(int id, String title, String description, String date, String time, boolean confidential,
                 String place, String placeName, Category category, int peopleNumber, double distance) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setDate(date);
        setTime(time);
        setConfidential(confidential);
        setPlace(place);
        setPlaceName(placeName);
        setCategory(category);
        setPeopleNumber(peopleNumber);
        setDistance(distance);
    }

    protected Event(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        date = in.readString();
        time = in.readString();
        confidential = in.readByte() != 0;
        place = in.readString();
        placeName = in.readString();
        peopleNumber = in.readInt();
        distance = in.readDouble();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isConfidential() {
        return confidential;
    }

    public String getPlace() {
        return place;
    }

    public String getPlaceName() {
        return placeName;
    }

    public Category getCategory() {
        return category;
    }

    public int getPeopleNumber() {
        return peopleNumber;
    }

    public String getPeopleNumberString(){
        return Integer.toString(peopleNumber);
    }

    public double getDistance() {
        return distance;
    }

    public String getDistanceString(){
        return Double.toString(distance);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setConfidential(boolean confidential) {
        this.confidential = confidential;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeByte((byte) (confidential ? 1 : 0));
        dest.writeString(place);
        dest.writeString(placeName);
        dest.writeInt(peopleNumber);
        dest.writeDouble(distance);
    }
}