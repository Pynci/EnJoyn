package it.unimib.enjoyn.model;

import android.os.Parcel;
import android.os.Parcelable;

public class EventLocation implements Parcelable {
    String id, name;
    double longitude, latitude;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }
    public String getLongitudeToString(){
        return Double.toString(longitude);
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getLatitudeToString(){
        return Double.toString(latitude);
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
        this.longitude = source.readDouble();
        this.latitude = source.readDouble();
    }

    public EventLocation() {
    }

    protected EventLocation(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
    }

    public static final Parcelable.Creator<EventLocation> CREATOR = new Parcelable.Creator<EventLocation>() {
        @Override
        public EventLocation createFromParcel(Parcel source) {
            return new EventLocation(source);
        }

        @Override
        public EventLocation[] newArray(int size) {
            return new EventLocation[size];
        }
    };
}
