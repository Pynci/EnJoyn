package it.unimib.enjoyn.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import it.unimib.enjoyn.repository.IMeteoRepository;

public class EventViewModelFactory implements ViewModelProvider.Factory {

    private final IMeteoRepository iWeatherRepository;


    public EventViewModelFactory(IMeteoRepository iWeatherRepository) {
        this.iWeatherRepository = iWeatherRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EventViewModel(iWeatherRepository);
    }
}
