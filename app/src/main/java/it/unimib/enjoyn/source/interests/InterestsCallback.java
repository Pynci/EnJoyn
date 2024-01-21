package it.unimib.enjoyn.source.interests;

public interface InterestsCallback {

    void onSuccessCreateUsers();
    void onFailureCreateUsers(Exception e);
}
