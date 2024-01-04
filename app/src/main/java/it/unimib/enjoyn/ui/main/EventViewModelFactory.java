package it.unimib.enjoyn.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import it.unimib.enjoyn.repository.IMeteoRepository;
import it.unimib.enjoyn.repository.IEventRepositoryWithLiveData;

public class EventViewModelFactory implements ViewModelProvider.Factory {

    private final IEventRepositoryWithLiveData iEventRepositoryWithLiveData;
    private final IMeteoRepository iWeatherRepository;

    public EventViewModelFactory(IEventRepositoryWithLiveData iEventRepositoryWithLiveData, IMeteoRepository iWeatherRepository) {
        this.iEventRepositoryWithLiveData = iEventRepositoryWithLiveData;
        this.iWeatherRepository = iWeatherRepository;
    }

    public EventViewModelFactory(IEventRepositoryWithLiveData iEventRepositoryWithLiveData) {
        this.iEventRepositoryWithLiveData = iEventRepositoryWithLiveData;
        iWeatherRepository = null;
    }

    public EventViewModelFactory(IMeteoRepository iWeatherRepository) {
        this.iWeatherRepository = iWeatherRepository;
        iEventRepositoryWithLiveData = null;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EventViewModel(iEventRepositoryWithLiveData, iWeatherRepository);
    }


}
