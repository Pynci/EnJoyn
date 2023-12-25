package it.unimib.enjoyn.source;

import java.io.IOException;

import it.unimib.enjoyn.model.EventsDatabaseResponse;
import it.unimib.enjoyn.util.JSONParserUtil;

public class EventRemoteDataSource extends BaseEventRemoteDataSource{
    private final JSONParserUtil jsonParserUtil;
    private EventsDatabaseResponse eventsDatabaseResponse;
    public EventRemoteDataSource(JSONParserUtil jsonParserUtil) {
        //this.eventsDatabaseResponse = ServiceLocator.getInstance().
        this.jsonParserUtil = jsonParserUtil;
    }

    @Override
    public void getEvent(String category) {

    }

    @Override
    public void getEvent() {
        EventsDatabaseResponse eventDBResponse = null;
        try {
            eventDBResponse = jsonParserUtil.parseJSONEventFileWithGSon("prova.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (eventDBResponse != null){
            eventCallback.onSuccessFromRemote(eventDBResponse, System.currentTimeMillis());
        } else{
            //TODO onFailure
        }
    }

    }

