package it.unimib.enjoyn.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Entity
public class User implements Parcelable {

    @PrimaryKey @NonNull
    private String uid;
    private String username;
    private String email;
    private String name;
    private String surname;
    private String description;
    private Boolean isEmailVerified;
    private Boolean isProfileConfigured;

    /* TODO aggiungere immagine*/

    public User(){
        this.uid = "";
    }

    public User(@NonNull String uid) {
        this.uid = uid;
    }

    public User(@NonNull String uid, String email){
        this.uid = uid;
        this.email = email;
        this.isEmailVerified = false;
        this.isProfileConfigured = false;
    }

    public User(@NonNull String uid, String username, String email){
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.isEmailVerified = false;
        this.isProfileConfigured = false;
    }

    public User(@NonNull String uid, String username, String email,
                String name, String surname, String description) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.description = description;
        this.isEmailVerified = false;
        this.isProfileConfigured = false;
    }

    protected User(Parcel in) {
        username = in.readString();
        name = in.readString();
        surname = in.readString();
        description = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(description);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public Boolean getProfileConfigured() {
        return isProfileConfigured;
    }

    public void setProfileConfigured(Boolean profileConfigured) {
        isProfileConfigured = profileConfigured;
    }
}
