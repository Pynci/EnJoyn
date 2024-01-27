package it.unimib.enjoyn.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import it.unimib.enjoyn.repository.IWeatherRepository;
import it.unimib.enjoyn.repository.IEventRepository;
import it.unimib.enjoyn.repository.MapRepository;

public class EventViewModelFactory implements ViewModelProvider.Factory {

    private final IEventRepository iEventRepository;
    private final IWeatherRepository iWeatherRepository;

    private final MapRepository mapRepository;

    public EventViewModelFactory(IEventRepository iEventRepository, IWeatherRepository iWeatherRepository, MapRepository mapRepository) {
        this.iEventRepository = iEventRepository;
        this.iWeatherRepository = iWeatherRepository;
        this.mapRepository = mapRepository;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EventViewModel(iEventRepository, iWeatherRepository, mapRepository);
    }


}
