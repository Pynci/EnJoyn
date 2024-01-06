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

import it.unimib.enjoyn.model.Weather;
import it.unimib.enjoyn.model.EventsDatabaseResponse;
import it.unimib.enjoyn.model.WeatherApiResponse;
import it.unimib.enjoyn.model.WeatherDatabaseResponse;
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

    public WeatherDatabaseResponse parseJSONMeteoFileWithGSon(BufferedReader bufferedReader) throws IOException{
        return new Gson().fromJson(bufferedReader, WeatherDatabaseResponse.class);
    }


    public WeatherApiResponse parseJSONFileAPIMeteo(String fileName) throws IOException {
        InputStream inputStream = context.getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, WeatherApiResponse.class);
    }
    public WeatherDatabaseResponse parseJSONFileWithJSONObjectArray(String fileName)
            throws IOException, JSONException {

        InputStream inputStream = context.getAssets().open(fileName);
        String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject rootJSONObject = new JSONObject(content);

        WeatherDatabaseResponse weatherDataBaseResponse = new WeatherDatabaseResponse();


        JSONObject meteoJSONObject = rootJSONObject.getJSONObject("weather");

        List<Weather> newsList = null;
        int articlesCount = meteoJSONObject.length();


            newsList = new ArrayList<>();
            Weather weather;
            //for (int i = 0; i < articlesCount; i++) {

                JSONArray timeJSONArray = meteoJSONObject.getJSONArray("time");
                JSONArray temperatureJSONArray = meteoJSONObject.getJSONArray( "temperature_2m");
                JSONArray weatherCodeJSONArray = meteoJSONObject.getJSONArray("weather_code");

                weather = new Weather();
                String[] meteoHour = new String[timeJSONArray.length()];
                for (int j = 0; j < meteoHour.length; j++) {
                    meteoHour[j] = timeJSONArray.getString(j);
                }
                weather.setHour(meteoHour);
                double[] meteoTemperature= new double[temperatureJSONArray.length()];
                for (int j = 0; j < meteoTemperature.length; j++) {
                    meteoTemperature[j] = temperatureJSONArray.getDouble(j);
                }
                weather.setTemperature(meteoTemperature);
                int[] meteoWeatherCode = new int[weatherCodeJSONArray.length()];
                for (int j = 0; j < meteoWeatherCode.length; j++) {
                    meteoWeatherCode[j] = weatherCodeJSONArray.getInt(j);
                }
                weather.setWeather_code(meteoWeatherCode);

                        newsList.add(weather);
                   // }

            weatherDataBaseResponse.setWeatherList(newsList);


        return weatherDataBaseResponse;
    }










    }

