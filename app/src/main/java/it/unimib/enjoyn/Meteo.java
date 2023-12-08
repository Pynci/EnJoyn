package it.unimib.enjoyn;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Meteo implements Parcelable {

    @SerializedName("weather_code")
    int weatherCode;
    @SerializedName("temperature_2m")
    int temperature;
    @SerializedName("time")
    String hour;

    public Meteo(int weterCode, int temperature, String hour) {
        this.weatherCode = weterCode;
        this.temperature = temperature;
        this.hour = hour;
    }

    public int getWeatherCode(int i) {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public int getTemperature(int i) {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.weatherCode);
        dest.writeInt(this.temperature);
        dest.writeString(this.hour);
    }

    public void readFromParcel(Parcel source) {
        this.weatherCode = source.readInt();
        this.temperature = source.readInt();
        this.hour = source.readString();
    }

    protected Meteo(Parcel in) {
        this.weatherCode = in.readInt();
        this.temperature = in.readInt();
        this.hour = in.readString();
    }

    public static final Creator<Meteo> CREATOR = new Creator<Meteo>() {
        @Override
        public Meteo createFromParcel(Parcel source) {
            return new Meteo(source);
        }

        @Override
        public Meteo[] newArray(int size) {
            return new Meteo[size];
        }
    };
}
