package it.unimib.enjoyn.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class MeteoDatabaseResponse implements Parcelable {

    private List<Meteo> meteoList;

    public MeteoDatabaseResponse(){};
    public MeteoDatabaseResponse(List<Meteo> meteoList) {
        this.meteoList = meteoList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MeteoDatabaseResponse> CREATOR = new Parcelable.Creator<MeteoDatabaseResponse>() {
        @Override
        public MeteoDatabaseResponse createFromParcel(Parcel in) {
            return new MeteoDatabaseResponse(in);
        }

        @Override
        public MeteoDatabaseResponse[] newArray(int size) {
            return new MeteoDatabaseResponse[size];
        }
    };

    protected MeteoDatabaseResponse(Parcel in) {
        this.meteoList = in.createTypedArrayList(Meteo.CREATOR);
    }


    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(this.meteoList);
    }

    public void readFromParcel(Parcel source){
        this.meteoList = source.createTypedArrayList(Meteo.CREATOR);
    }

    public List<Meteo> getMeteoList() {
        return meteoList;
    }

    public void setMeteoList(List<Meteo> meteoList) {
        this.meteoList = meteoList;
    }


}
