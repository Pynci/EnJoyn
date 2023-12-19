package it.unimib.enjoyn.model;

import android.os.Parcel;
import android.os.Parcelable;

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

    private String time;

    private boolean confidential;

    private String place;

    private String placeName;
    @Embedded(prefix = "category_")
    private Category category;

    private int peopleNumber;

    private double distance;

    private boolean isTODO;

    private boolean isFavorite;
    @Embedded(prefix = "meteo_")
    private Meteo meteo;



    public Event(long id, String title, String description, String date, String time, boolean confidential, String place, String placeName, Category category, int peopleNumber, double distance, boolean isTODO, boolean isFavorite, Meteo meteo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.confidential = confidential;
        this.place = place;
        this.placeName = placeName;
        this.category = category;
        this.peopleNumber = peopleNumber;
        this.distance = distance;
        this.isTODO = isTODO;
        this.isFavorite = isFavorite;
        this.meteo = meteo;
    }


    /* da errore POJOs
    public Event(long id, String title, String description, String date, String time, boolean confidential,
                 String place, String placeName, Category category, int peopleNumber, double distance, boolean todo, boolean favorite, Meteo meteo) {
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
        setFavorite(favorite);
        setTODO(todo);
        setMeteo(meteo);
    }
     */

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

    public boolean isTODO() {
        return isTODO;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public Meteo getMeteo() {
        return meteo;
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

    public void setTODO(boolean TODO) {
        isTODO = TODO;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setMeteo(Meteo meteo) {
        this.meteo = meteo;
    }

    public void incrementPeopleNumber(){

            peopleNumber++;

        this.peopleNumber=peopleNumber;
    }

    public void decrementPeopleNumber(){

            peopleNumber--;
            this.peopleNumber=peopleNumber;
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
        dest.writeString(this.time);
        dest.writeByte(this.confidential ? (byte) 1 : (byte) 0);
        dest.writeString(this.place);
        dest.writeString(this.placeName);
        dest.writeParcelable(this.category, flags);
        dest.writeInt(this.peopleNumber);
        dest.writeDouble(this.distance);
        dest.writeByte(this.isTODO ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.meteo, flags);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.title = source.readString();
        this.description = source.readString();
        this.date = source.readString();
        this.time = source.readString();
        this.confidential = source.readByte() != 0;
        this.place = source.readString();
        this.placeName = source.readString();
        this.category = source.readParcelable(Category.class.getClassLoader());
        this.peopleNumber = source.readInt();
        this.distance = source.readDouble();
        this.isTODO = source.readByte() != 0;
        this.isFavorite = source.readByte() != 0;
        this.meteo = source.readParcelable(Meteo.class.getClassLoader());
    }

    protected Event(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.confidential = in.readByte() != 0;
        this.place = in.readString();
        this.placeName = in.readString();
        this.category = in.readParcelable(Category.class.getClassLoader());
        this.peopleNumber = in.readInt();
        this.distance = in.readDouble();
        this.isTODO = in.readByte() != 0;
        this.isFavorite = in.readByte() != 0;
        this.meteo = in.readParcelable(Meteo.class.getClassLoader());
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

    /*TODO
    quando prendiamo da db Firebase aggiungere peopleNumber*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return  confidential == event.confidential && Double.compare(event.distance, distance) == 0  && Objects.equals(title, event.title) && Objects.equals(description, event.description) && Objects.equals(date, event.date) && Objects.equals(time, event.time) && Objects.equals(place, event.place) && Objects.equals(placeName, event.placeName) && Objects.equals(category, event.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash( title, description, date, time, confidential, place, placeName, category, distance);
    }
}