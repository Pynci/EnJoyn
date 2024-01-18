package it.unimib.enjoyn.repository.user;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Result;

public interface IUserRepository {

    MutableLiveData<Result> createUser(String email, String password, String username);

    Result isCurrentUserEmailVerified();

    MutableLiveData<Result> signIn(String email, String password);

    void signOut();

    MutableLiveData<Result> sendEmailVerification();

    MutableLiveData<Result> getUserByUsername(String username);

    MutableLiveData<Result> getUserByEmail(String email);

    MutableLiveData<Result> getCurrentUser();

    MutableLiveData<Result> createPropic(Uri uri);

    MutableLiveData<Result> updateNameAndSurname(String nome, String cognome);

    MutableLiveData<Result> updateDescription(String description);

    MutableLiveData<Result> sendResetPasswordEmail(String email);
}
