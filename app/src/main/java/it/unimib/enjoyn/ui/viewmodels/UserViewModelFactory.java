package it.unimib.enjoyn.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.enjoyn.repository.user.IUserRepository;

public class UserViewModelFactory implements ViewModelProvider.Factory {
    private final IUserRepository userRepository;

    public UserViewModelFactory(IUserRepository userRepository){
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new UserViewModel(userRepository);
    }
}
