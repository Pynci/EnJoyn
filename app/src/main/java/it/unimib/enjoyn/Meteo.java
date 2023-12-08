package it.unimib.enjoyn;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Meteo implements Parcelable {

    @SerializedName("weather_code")
    int[] weatherCode;
    @SerializedName("temperature_2m")
    int[] temperature;
    @SerializedName("time")
    String[] hour;

    public Meteo(int[] weterCode, int[] temperature, String[] hour) {
        this.weatherCode = weterCode;
        this.temperature = temperature;
        this.hour = hour;
    }

    public int[] getWeterCode() {
        return weatherCode;
    }

    public void setWeterCode(int[] weterCode) {
        this.weatherCode = weterCode;
    }

    public int[] getTemperature() {
        return temperature;
    }

    public void setTemperature(int[] temperature) {
        this.temperature = temperature;
    }

    public String[] getHour() {
        return hour;
    }

    public void setHour(String[] hour) {
        this.hour = hour;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(this.weatherCode);
        dest.writeIntArray(this.temperature);
        dest.writeStringArray(this.hour);
    }

    public void readFromParcel(Parcel source) {
        this.weatherCode = source.createIntArray();
        this.temperature = source.createIntArray();
        this.hour = source.createStringArray();
    }

    protected Meteo(Parcel in) {
        this.weatherCode = in.createIntArray();
        this.temperature = in.createIntArray();
        this.hour = in.createStringArray();
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
