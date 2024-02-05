package it.unimib.enjoyn.util;

import android.os.Parcel;
import android.os.Parcelable;

public class ColorObject implements Parcelable {
    private String name;
    private String hex;
    private String contrastHex;

    public ColorObject( ) {

    }

    public ColorObject(String name, String hex, String contrastHex) {
        this.name = name;
        this.hex = hex;
        this.contrastHex = contrastHex;
    }


    public String getHexHash() {
        return "#" + hex;
    }

    public String getContrastHexHash() {
        return "#" + contrastHex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getContrastHex() {
        return contrastHex;
    }

    public void setContrastHex(String contrastHex) {
        this.contrastHex = contrastHex;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.hex);
        dest.writeString(this.contrastHex);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.hex = source.readString();
        this.contrastHex = source.readString();
    }

    protected ColorObject(Parcel in) {
        this.name = in.readString();
        this.hex = in.readString();
        this.contrastHex = in.readString();
    }

    public static final Creator<ColorObject> CREATOR = new Creator<ColorObject>() {
        @Override
        public ColorObject createFromParcel(Parcel source) {
            return new ColorObject(source);
        }

        @Override
        public ColorObject[] newArray(int size) {
            return new ColorObject[size];
        }
    };
}