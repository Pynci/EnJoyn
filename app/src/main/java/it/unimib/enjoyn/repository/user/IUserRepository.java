package it.unimib.enjoyn.repository.user;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.User;

public interface IUserRepository {

    MutableLiveData<Result> createUser(String email, String password, String username);

    MutableLiveData<Result> signIn(String email, String password);

    MutableLiveData<Result> signOut();

    MutableLiveData<Result> sendEmailVerification();

    MutableLiveData<Result> getUserByUsername(String username);

    MutableLiveData<Result> getUserByEmail(String email);

    User getCurrentUser();

    MutableLiveData<Result> updatePropic(Uri uri);

    MutableLiveData<Result> updateNameAndSurname(String nome, String cognome);

    MutableLiveData<Result> updateDescription(String description);

    MutableLiveData<Result> sendResetPasswordEmail(String email);
}
