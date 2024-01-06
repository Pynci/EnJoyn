package it.unimib.enjoyn.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class WeatherDatabaseResponse implements Parcelable {

    private List<Weather> weatherList;

    public WeatherDatabaseResponse(){};
    public WeatherDatabaseResponse(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<WeatherDatabaseResponse> CREATOR = new Parcelable.Creator<WeatherDatabaseResponse>() {
        @Override
        public WeatherDatabaseResponse createFromParcel(Parcel in) {
            return new WeatherDatabaseResponse(in);
        }

        @Override
        public WeatherDatabaseResponse[] newArray(int size) {
            return new WeatherDatabaseResponse[size];
        }
    };

    protected WeatherDatabaseResponse(Parcel in) {
        this.weatherList = in.createTypedArrayList(Weather.CREATOR);
    }


    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(this.weatherList);
    }

    public void readFromParcel(Parcel source){
        this.weatherList = source.createTypedArrayList(Weather.CREATOR);
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }


}
