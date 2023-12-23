package it.unimib.enjoyn.source;

import it.unimib.enjoyn.model.EventsDatabaseResponse;
import it.unimib.enjoyn.util.ServiceLocator;

public class EventRemoteDataSource extends BaseEventRemoteDataSource{

    private EventsDatabaseResponse eventsDatabaseResponse;
    public EventRemoteDataSource() {
        //this.eventsDatabaseResponse = ServiceLocator.getInstance().
    }

    @Override
    public void getEvent(String category) {

    }

    @Override
    public void getEvent() {

    }
}
