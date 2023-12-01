package it.unimib.enjoyn.util;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;

import it.unimib.enjoyn.model.EventsDatabaseResponse;

public class JSONParserUtil {

    private final Context context;

    public JSONParserUtil(Application application) { //passo l'application per avere
        // il contesto generale dell'applicazione, non specifico a qualcosa
        this.context = application.getApplicationContext();
    }

    public EventsDatabaseResponse parseJSONFileWithGSon(BufferedReader bufferedReader) throws IOException{

        return new Gson().fromJson(bufferedReader, EventsDatabaseResponse.class);
    }
}
