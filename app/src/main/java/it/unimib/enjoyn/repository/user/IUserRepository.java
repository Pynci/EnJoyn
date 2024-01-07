package it.unimib.enjoyn.repository.user;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import it.unimib.enjoyn.model.Result;

public interface IUserRepository {

    MutableLiveData<Result> createUser(String email, String password, String username);
    MutableLiveData<Result> getUserByUsername(String username);
    MutableLiveData<Result> getUserByEmail(String email);
    MutableLiveData<Result> createUserImage(Uri uri);
    MutableLiveData<Result> updateNameAndSurname(String nome, String cognome);
}
