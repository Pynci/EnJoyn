package it.unimib.enjoyn.model;

import static it.unimib.enjoyn.util.Constants.METEO_INTERVAL_PARAMETER;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MeteoResponse implements Parcelable {
    @SerializedName(METEO_INTERVAL_PARAMETER)
    List<Meteo> meteoList;

    public MeteoResponse(List<Meteo> meteoList) {
        this.meteoList = meteoList;
    }

    protected MeteoResponse(Parcel in) {
        meteoList = in.createTypedArrayList(Meteo.CREATOR);
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

    public List<Meteo> getMeteoList() {
        return meteoList;
    }

    public void setMeteoList(List<Meteo> meteoList) {
        this.meteoList = meteoList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(meteoList);
    }
}
