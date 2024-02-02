package it.unimib.enjoyn.util;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ColorObject implements Parcelable {
    private String name;
    private String hex;
    private String contrastHex;

    public ColorObject(String name, String hex, String contrastHex) {
        this.name = name;
        this.hex = hex;
        this.contrastHex = contrastHex;
    }

    protected ColorObject(Parcel in) {
        name = in.readString();
        hex = in.readString();
        contrastHex = in.readString();
    }

    public static final Creator<ColorObject> CREATOR = new Creator<ColorObject>() {
        @Override
        public ColorObject createFromParcel(Parcel in) {
            return new ColorObject(in);
        }

        @Override
        public ColorObject[] newArray(int size) {
            return new ColorObject[size];
        }
    };

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
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(hex);
        dest.writeString(contrastHex);
    }
}