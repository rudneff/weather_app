package ru.tp.rudi.weather_app;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.Weather;
import ru.mail.weather.lib.WeatherStorage;
import ru.mail.weather.lib.WeatherUtils;

public class WeatherIntentService extends IntentService {
    public static final String WEATHER_ERROR_ACTION = "ru.tp.rudi.weather_app.WEATHER_ERROR_ACTION";
    public static final String WEATHER_CHANGED_ACTION = "ru.tp.rudi.weather_app.WEATHER_CHANGED_ACTION";
    public static final String WEATHER_LOAD_ACTION = "ru.tp.rudi.weather_app.WEATHER_LOAD_ACTION";

    public WeatherIntentService() {
        super("WeatherIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("!@!", "onHandleIntent");
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        if (intent != null && intent.getAction().equals(WEATHER_LOAD_ACTION)) {
            try {
                City currentCity = WeatherStorage.getInstance(this).getCurrentCity();
                Weather weather = WeatherUtils.getInstance().loadWeather(currentCity);
                WeatherStorage.getInstance(this).saveWeather(currentCity, weather);
                broadcastManager.sendBroadcast(new Intent(WEATHER_CHANGED_ACTION));
            } catch (IOException e) {
                e.printStackTrace();
                broadcastManager.sendBroadcast(new Intent(WEATHER_ERROR_ACTION));
            }
        }


    }
}
