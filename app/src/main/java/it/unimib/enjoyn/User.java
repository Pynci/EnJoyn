package it.unimib.enjoyn;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {

    private String email;
    private String password;
    private String username;
    private String name;
    private String surname;
    private String description;

    /**TODO ggiungere immagine*/

    public User(String email, String password, String username, String name, String surname, String description) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.description = description;
    }

    protected User(Parcel in) {
        email = in.readString();
        password = in.readString();
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

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDescription() {
        return description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(description);
    }
}
