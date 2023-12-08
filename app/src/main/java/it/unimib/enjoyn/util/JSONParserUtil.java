package it.unimib.enjoyn.util;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;

import it.unimib.enjoyn.Meteo;
import it.unimib.enjoyn.model.EventsDatabaseResponse;
import it.unimib.enjoyn.model.MeteoDatabaseResponse;
import it.unimib.enjoyn.model.UsersDatabaseResponse;

public class JSONParserUtil {

    private final Context context;

    public JSONParserUtil(Application application) { //passo l'application per avere
        // il contesto generale dell'applicazione, non specifico a qualcosa
        this.context = application.getApplicationContext();
    }

    public EventsDatabaseResponse parseJSONEventFileWithGSon(BufferedReader bufferedReader) throws IOException{

        return new Gson().fromJson(bufferedReader, EventsDatabaseResponse.class);
    }

    public UsersDatabaseResponse parseJSONUserFileWithGSon(BufferedReader bufferedReader) throws IOException{
        return new Gson().fromJson(bufferedReader, UsersDatabaseResponse.class);
    }

    public MeteoDatabaseResponse parseJSONMeteoFileWithGSon(BufferedReader bufferedReader) throws IOException{
        return new Gson().fromJson(bufferedReader, MeteoDatabaseResponse.class);
    }
}