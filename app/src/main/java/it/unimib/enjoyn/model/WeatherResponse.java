package it.unimib.enjoyn.model;

import static it.unimib.enjoyn.util.Constants.WEATHER_INTERVAL_PARAMETER;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse implements Parcelable {
    @SerializedName(WEATHER_INTERVAL_PARAMETER)
    Weather weather;

    public WeatherResponse(Weather weather) {
        this.weather = weather;
    }

    protected WeatherResponse(Parcel in) {
        weather = in.readParcelable(Weather.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(weather, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherResponse> CREATOR = new Creator<WeatherResponse>() {
        @Override
        public WeatherResponse createFromParcel(Parcel in) {
            return new WeatherResponse(in);
        }

        @Override
        public WeatherResponse[] newArray(int size) {
            return new WeatherResponse[size];
        }
    };

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}
