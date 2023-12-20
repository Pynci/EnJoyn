package it.unimib.enjoyn.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class EventsDatabaseResponse implements Parcelable {

    private List<Event> events;

    public EventsDatabaseResponse(List<Event> events){
        this.events = events;
    }

    public static final Parcelable.Creator<EventsDatabaseResponse> CREATOR = new Parcelable.Creator<EventsDatabaseResponse>() {
        @Override
        public EventsDatabaseResponse createFromParcel(Parcel in) {
            return new EventsDatabaseResponse(in);
        }

        @Override
        public EventsDatabaseResponse[] newArray(int size) {
            return new EventsDatabaseResponse[size];
        }
    };

    protected EventsDatabaseResponse(Parcel in) {
        this.events = in.createTypedArrayList(Event.CREATOR);
    }



    public List<Event> getEventList(){
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(this.events);
    }

    public void readFromParcel(Parcel source){
        this.events = source.createTypedArrayList(Event.CREATOR);
    }

}
