package it.unimib.enjoyn.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

import it.unimib.enjoyn.Event;
import it.unimib.enjoyn.User;

public class UsersDatabaseResponse implements Parcelable {

    private List<User> users;

    public UsersDatabaseResponse(List<User> users) {
        this.users = users;
    }

    public static final Parcelable.Creator<UsersDatabaseResponse> CREATOR = new Parcelable.Creator<UsersDatabaseResponse>() {
        @Override
        public UsersDatabaseResponse createFromParcel(Parcel in) {
            return new UsersDatabaseResponse(in);
        }

        @Override
        public UsersDatabaseResponse[] newArray(int size) {
            return new UsersDatabaseResponse[size];
        }
    };

    protected UsersDatabaseResponse(Parcel in) {
        this.users = in.createTypedArrayList(User.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(this.users);
    }

    public void readFromParcel(Parcel source){
        this.users = source.createTypedArrayList(User.CREATOR);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
