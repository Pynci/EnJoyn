package it.unimib.enjoyn.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.enjoyn.repository.IEventRepositoryWithLiveData;

public class EventViewModelFactory implements ViewModelProvider.Factory {

    private final IEventRepositoryWithLiveData iEventRepositoryWithLiveData;

    public EventViewModelFactory(IEventRepositoryWithLiveData iEventRepositoryWithLiveData) {
        this.iEventRepositoryWithLiveData = iEventRepositoryWithLiveData;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EventViewModel(iEventRepositoryWithLiveData);
    }
}
