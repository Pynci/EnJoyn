package it.unimib.enjoyn.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Event implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;

    private String description;

    private String date;

    private String place;

    private String time;

    private boolean confidential;
    @Embedded(prefix = "location_")
    private EventLocation location;
    @Embedded(prefix = "category_")
    private Category category;

    private int peopleNumber;
    @Nullable
    private double distance;

    private boolean isTODO;
    @Nullable
    private boolean isFavorite;
    @Embedded(prefix = "weather_")
    private Weather weather;

    @Embedded
    private Uri imageUrl;

    public Event(){

    }

    public Event(long id, String title, String description, String date, String place, String time, boolean confidential, EventLocation location, Category category, int peopleNumber, double distance, boolean isTODO, boolean isFavorite, Weather weather, Uri imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.place = place;
        this.time = time;
        this.confidential = confidential;
        this.location = location;
        this.category = category;
        this.peopleNumber = peopleNumber;
        this.distance = distance;
        this.isTODO = isTODO;
        this.isFavorite = isFavorite;
        this.weather = weather;
        this.imageUrl = imageUrl;
    }

    public long getId() {
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

    public EventLocation getLocation() {
        return location;
    }

    public String getPlace() {
        return place;
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
        return (distance)+" km";
    }

    public boolean isTODO() {
        return isTODO;
    }

    public boolean isFavorite() {
        return isFavorite;
    }


    public Weather getWeather() {
        return weather;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }

    public void setId(long id) {
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

    public void setLocation(EventLocation location) {
        this.location = location;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public void setTODO(boolean TODO) {
        isTODO = TODO;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void incrementPeopleNumber(){

            peopleNumber++;

        this.peopleNumber = peopleNumber;
    }

    public void decrementPeopleNumber(){

            peopleNumber--;
            this.peopleNumber=peopleNumber;
    }

    //TODO aggiungere parcel di EventLocation


    /*TODO
    quando prendiamo da db Firebase aggiungere peopleNumber*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return  confidential == event.confidential && Double.compare(event.distance, distance) == 0  && Objects.equals(title, event.title) && Objects.equals(description, event.description) && Objects.equals(date, event.date) && Objects.equals(time, event.time) && Objects.equals(location, event.location) && Objects.equals(category, event.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash( title, description, date, time, confidential, location, category, distance);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.date);
        dest.writeString(this.place);
        dest.writeString(this.time);
        dest.writeByte(this.confidential ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.location, flags);
        dest.writeParcelable(this.category, flags);
        dest.writeInt(this.peopleNumber);
        dest.writeDouble(this.distance);
        dest.writeByte(this.isTODO ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.weather, flags);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.title = source.readString();
        this.description = source.readString();
        this.date = source.readString();
        this.place = source.readString();
        this.time = source.readString();
        this.confidential = source.readByte() != 0;
        this.location = source.readParcelable(EventLocation.class.getClassLoader());
        this.category = source.readParcelable(Category.class.getClassLoader());
        this.peopleNumber = source.readInt();
        this.distance = source.readDouble();
        this.isTODO = source.readByte() != 0;
        this.isFavorite = source.readByte() != 0;
        this.weather = source.readParcelable(Weather.class.getClassLoader());
    }

    protected Event(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
        this.place = in.readString();
        this.time = in.readString();
        this.confidential = in.readByte() != 0;
        this.location = in.readParcelable(EventLocation.class.getClassLoader());
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.peopleNumber = in.readInt();
        this.distance = in.readDouble();
        this.isTODO = in.readByte() != 0;
        this.isFavorite = in.readByte() != 0;
        this.weather = in.readParcelable(Weather.class.getClassLoader());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}