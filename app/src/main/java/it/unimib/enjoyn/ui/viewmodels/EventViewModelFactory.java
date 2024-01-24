package it.unimib.enjoyn.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import it.unimib.enjoyn.repository.IWeatherRepository;
import it.unimib.enjoyn.repository.IEventRepositoryWithLiveData;

public class EventViewModelFactory implements ViewModelProvider.Factory {

    private final IEventRepositoryWithLiveData iEventRepositoryWithLiveData;
    private final IWeatherRepository iWeatherRepository;

    public EventViewModelFactory(IEventRepositoryWithLiveData iEventRepositoryWithLiveData, IWeatherRepository iWeatherRepository) {
        this.iEventRepositoryWithLiveData = iEventRepositoryWithLiveData;
        this.iWeatherRepository = iWeatherRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EventViewModel(iEventRepositoryWithLiveData, iWeatherRepository);
    }


}
