package it.unimib.enjoyn.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Weather implements Parcelable {
    //StringConverter converter = new StringConverter();
    @SerializedName("weather_code")
    int[] weather_code;
    @SerializedName("temperature_2m")
    double[] temperature;
    @SerializedName("time")
    String[] hour;


    public Weather(int[] weatherCode, double[] temperature, String[] hour) {
        this.weather_code = weatherCode;
        this.temperature = temperature;
        this.hour = hour;
    }



    public int[] getWeather_code() {
        return weather_code;
    }
    public String getWeather_codeString(int i) {
        return Integer.toString(weather_code[i]);
    }
    public void setWeather_code(int[] weather_code) {
        this.weather_code = weather_code;
    }

    public double[] getTemperature() {
        return temperature;
    }
    public String getTemperatureString(int i) {
        return Double.toString(temperature[i]);
    }
    public void setTemperature(double[] temperature) {
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
        dest.writeIntArray(this.weather_code);
        dest.writeDoubleArray(this.temperature);
        dest.writeStringArray(this.hour);
    }

    public void readFromParcel(Parcel source) {
        this.weather_code = source.createIntArray();
        this.temperature = source.createDoubleArray();
        this.hour = source.createStringArray();
    }

    public Weather() {
    }

    protected Weather(Parcel in) {
        this.weather_code = in.createIntArray();
        this.temperature = in.createDoubleArray();
        this.hour = in.createStringArray();
    }
    public static final Creator<Weather> CREATOR = new Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel source) {
            return new Weather(source);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
}
