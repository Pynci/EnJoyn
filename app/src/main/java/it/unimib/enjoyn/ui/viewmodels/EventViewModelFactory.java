package it.unimib.enjoyn.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import it.unimib.enjoyn.repository.IWeatherRepository;
import it.unimib.enjoyn.repository.IEventRepositoryWithLiveData;
import it.unimib.enjoyn.repository.MapRepository;

public class EventViewModelFactory implements ViewModelProvider.Factory {

    private final IEventRepositoryWithLiveData iEventRepositoryWithLiveData;
    private final IWeatherRepository iWeatherRepository;

    private final MapRepository mapRepository;

    public EventViewModelFactory(IEventRepositoryWithLiveData iEventRepositoryWithLiveData, IWeatherRepository iWeatherRepository, MapRepository mapRepository) {
        this.iEventRepositoryWithLiveData = iEventRepositoryWithLiveData;
        this.iWeatherRepository = iWeatherRepository;
        this.mapRepository = mapRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EventViewModel(iEventRepositoryWithLiveData, iWeatherRepository, mapRepository);
    }


}
