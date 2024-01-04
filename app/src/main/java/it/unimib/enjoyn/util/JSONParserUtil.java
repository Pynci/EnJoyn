package it.unimib.enjoyn.util;

import android.app.Application;
import android.content.Context;


import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import it.unimib.enjoyn.model.Meteo;
import it.unimib.enjoyn.model.EventsDatabaseResponse;
import it.unimib.enjoyn.model.MeteoApiResponse;
import it.unimib.enjoyn.model.MeteoDatabaseResponse;
import it.unimib.enjoyn.model.Result;
import it.unimib.enjoyn.model.UsersDatabaseResponse;

public class JSONParserUtil {

    private final Context context;

    public JSONParserUtil(Application application) { //passo l'application per avere
        // il contesto generale dell'applicazione, non specifico a qualcosa
        this.context = application.getApplicationContext();
    }

    public EventsDatabaseResponse parseJSONEventFileWithGSon(String fileName) throws IOException{
        InputStream inputStream = context.getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, EventsDatabaseResponse.class);
    }

    public UsersDatabaseResponse parseJSONUserFileWithGSon(BufferedReader bufferedReader) throws IOException{
        return new Gson().fromJson(bufferedReader, UsersDatabaseResponse.class);
    }

    public MeteoDatabaseResponse parseJSONMeteoFileWithGSon(BufferedReader bufferedReader) throws IOException{
        return new Gson().fromJson(bufferedReader, MeteoDatabaseResponse.class);
    }

    /*public MeteoDatabaseResponse parseJSONAPIResultWithJSONObjectArray(Result result){

    }*/

    public MeteoApiResponse parseJSONFileAPIMeteo(String fileName) throws IOException {
        InputStream inputStream = context.getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, MeteoApiResponse.class);
    }
    public MeteoDatabaseResponse parseJSONFileWithJSONObjectArray(String fileName)
            throws IOException, JSONException {

        InputStream inputStream = context.getAssets().open(fileName);
        String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject rootJSONObject = new JSONObject(content);

        MeteoDatabaseResponse meteoDataBaseResponse = new MeteoDatabaseResponse();


        JSONObject meteoJSONObject = rootJSONObject.getJSONObject("meteo");

        List<Meteo> newsList = null;
        int articlesCount = meteoJSONObject.length();


            newsList = new ArrayList<>();
            Meteo meteo;
            //for (int i = 0; i < articlesCount; i++) {

                JSONArray timeJSONArray = meteoJSONObject.getJSONArray("time");
                JSONArray temperatureJSONArray = meteoJSONObject.getJSONArray( "temperature_2m");
                JSONArray weatherCodeJSONArray = meteoJSONObject.getJSONArray("weather_code");

                meteo = new Meteo();
                String[] meteoHour = new String[timeJSONArray.length()];
                for (int j = 0; j < meteoHour.length; j++) {
                    meteoHour[j] = timeJSONArray.getString(j);
                }
                meteo.setHour(meteoHour);
                double[] meteoTemperature= new double[temperatureJSONArray.length()];
                for (int j = 0; j < meteoTemperature.length; j++) {
                    meteoTemperature[j] = temperatureJSONArray.getDouble(j);
                }
                meteo.setTemperature(meteoTemperature);
                int[] meteoWeatherCode = new int[weatherCodeJSONArray.length()];
                for (int j = 0; j < meteoWeatherCode.length; j++) {
                    meteoWeatherCode[j] = weatherCodeJSONArray.getInt(j);
                }
                meteo.setWeather_code(meteoWeatherCode);

                        newsList.add(meteo);
                   // }

            meteoDataBaseResponse.setMeteoList(newsList);


        return meteoDataBaseResponse;
    }










    }

