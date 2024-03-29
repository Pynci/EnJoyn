package it.unimib.enjoyn.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class InterestViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;

    public InterestViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new InterestsViewModel(application);
    }
}
