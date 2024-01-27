package it.unimib.enjoyn.model;

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

    @Embedded(prefix = "location_")
    private EventLocation location;
    @Embedded(prefix = "category_")
    private Category category;

    private int participants;
    @Nullable
    private double distance;
    private int weatherCode;
    private double weatherTemperature;

    public Event(){

    }

    public Event(long id, String title, String description, String date, String place, String time,
                 EventLocation location, Category category, int participants,
                 double distance, int weatherCode, double weatherTemperature) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.place = place;
        this.time = time;
        this.location = location;
        this.category = category;
        this.participants = participants;
        this.distance = distance;
        this.weatherCode = weatherCode;
        this.weatherTemperature = weatherTemperature;
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

    public EventLocation getLocation() {
        return location;
    }

    public String getPlace() {
        return place;
    }

    public Category getCategory() {
        return category;
    }

    public int getParticipants() {
        return participants;
    }

    public String getPeopleNumberString(){
        return Integer.toString(participants);
    }

    public double getDistance() {
        return distance;
    }

    public String getDistanceString(){
        return Double.toString(distance)+" km";
    }


    public int getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public double getWeatherTemperature() {
        return weatherTemperature;
    }

    public void setWeatherTemperature(double weatherTemperature) {
        this.weatherTemperature = weatherTemperature;
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

    public void setLocation(EventLocation location) {
        this.location = location;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
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
    }

    public void setFavorite(boolean favorite) {
    }


    public void incrementPeopleNumber(){

            participants++;

        this.participants = participants;
    }

    public void decrementPeopleNumber(){

            participants--;
            this.participants = participants;
    }

    //TODO aggiungere parcel di EventLocation


    /*TODO
    quando prendiamo da db Firebase aggiungere peopleNumber*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return  Double.compare(event.distance, distance) == 0  && Objects.equals(title, event.title) && Objects.equals(description, event.description) && Objects.equals(date, event.date) && Objects.equals(time, event.time) && Objects.equals(location, event.location) && Objects.equals(category, event.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash( title, description, date, time, location, category, distance);
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
        dest.writeParcelable(this.location, flags);
        dest.writeParcelable(this.category, flags);
        dest.writeInt(this.participants);
        dest.writeDouble(this.distance);
        dest.writeInt(this.weatherCode);
        dest.writeDouble(this.weatherTemperature);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.title = source.readString();
        this.description = source.readString();
        this.date = source.readString();
        this.place = source.readString();
        this.time = source.readString();
        this.location = source.readParcelable(EventLocation.class.getClassLoader());
        this.category = source.readParcelable(Category.class.getClassLoader());
        this.participants = source.readInt();
        this.distance = source.readDouble();
        this.weatherCode = source.readInt();
        this.weatherTemperature = source.readDouble();
    }

    protected Event(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
        this.place = in.readString();
        this.time = in.readString();
        this.location = in.readParcelable(EventLocation.class.getClassLoader());
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.participants = in.readInt();
        this.distance = in.readDouble();
        this.weatherCode = in.readInt();
        this.weatherTemperature = in.readDouble();
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