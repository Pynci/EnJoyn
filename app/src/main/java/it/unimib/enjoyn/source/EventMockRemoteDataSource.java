package it.unimib.enjoyn.source;

import java.io.IOException;

import it.unimib.enjoyn.model.Event;
import it.unimib.enjoyn.model.EventsDatabaseResponse;
import it.unimib.enjoyn.source.events.BaseEventRemoteDataSource;
import it.unimib.enjoyn.util.JSONParserUtil;

public class EventMockRemoteDataSource extends BaseEventRemoteDataSource {

    private final JSONParserUtil jsonParserUtil;

    public EventMockRemoteDataSource(JSONParserUtil jsonParserUtil) {
        this.jsonParserUtil = jsonParserUtil;
    }

    @Override
    public void getEvent(String category) {
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

    @Override
    public void createEvent(Event event) {

    }
}
