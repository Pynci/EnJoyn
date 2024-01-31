package it.unimib.enjoyn.source.users;

import android.net.Uri;

import it.unimib.enjoyn.model.User;

public interface UserCallback {

    void onGetCurrentUserPropicSuccess(Uri uri);
    void onGetCurrentUserPropicFailure(Exception e);
}