package ru.tp.rudi.weather_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.StringDef;
import android.support.v4.content.LocalBroadcastManager;


/**
 * Created by rudi on 30.09.16.
 */

class ServiceHelper {
    private static ServiceHelper instance;

    private WeatherChangeResultListener weatherChangeResultListener = null;

    private ServiceHelper() {
    }

    synchronized static ServiceHelper getInstance(final Context context) {
        if (instance == null) {
            instance = new ServiceHelper();
            instance.initBroadcastReceiver(context);
        }
        return instance;
    }

    private void initBroadcastReceiver(Context context) {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(WeatherIntentService.WEATHER_CHANGE_ACTION);
        filter.addAction(WeatherIntentService.WEATHER_ERROR_ACTION);

        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (weatherChangeResultListener != null) {
                    final boolean success = intent.getAction()
                            .equals(WeatherIntentService.WEATHER_CHANGE_ACTION);
                    weatherChangeResultListener.onWeatherChangeResult(success);
                }
            }
        }, filter);
    }

    void weatherUpdate(final Context context, final WeatherChangeResultListener listener) {
        weatherChangeResultListener = listener;

        Intent intent = new Intent(context, WeatherIntentService.class);
        intent.setAction(WeatherIntentService.WEATHER_LOAD_ACTION);
        context.startService(intent);

    }

    void removeListener() {
        weatherChangeResultListener = null;
    }

    interface WeatherChangeResultListener {
        void onWeatherChangeResult(final boolean success);
    }
}
}
