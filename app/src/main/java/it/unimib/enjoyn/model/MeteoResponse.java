package it.unimib.enjoyn.model;

import static it.unimib.enjoyn.util.Constants.METEO_INTERVAL_PARAMETER;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MeteoResponse implements Parcelable {
    @SerializedName(METEO_INTERVAL_PARAMETER)
    Meteo weather;

    public MeteoResponse(Meteo weather) {
        this.weather = weather;
    }

    protected MeteoResponse(Parcel in) {
        weather = in.readParcelable(Meteo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(weather, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MeteoResponse> CREATOR = new Creator<MeteoResponse>() {
        @Override
        public MeteoResponse createFromParcel(Parcel in) {
            return new MeteoResponse(in);
        }

        @Override
        public MeteoResponse[] newArray(int size) {
            return new MeteoResponse[size];
        }
    };

    public Meteo getWeather() {
        return weather;
    }

    public void setWeather(Meteo weather) {
        this.weather = weather;
    }
}
