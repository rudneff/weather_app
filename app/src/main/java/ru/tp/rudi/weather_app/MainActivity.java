package ru.tp.rudi.weather_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.Weather;
import ru.mail.weather.lib.WeatherStorage;
import ru.mail.weather.lib.WeatherUtils;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button backgroundUpdateOnBtn = (Button) findViewById(R.id.background_update_on_btn);

        backgroundUpdateOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherIntentService.class);
                intent.setAction(WeatherIntentService.WEATHER_LOAD_ACTION);
                WeatherUtils.getInstance().schedule(MainActivity.this, intent);
            }
        });

        Button backgroundUpdateOffBtn = (Button) findViewById(R.id.background_update_off_btn);

        backgroundUpdateOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherIntentService.class);
                intent.setAction(WeatherIntentService.WEATHER_LOAD_ACTION);
                WeatherUtils.getInstance().unschedule(MainActivity.this, intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        WeatherStorage weatherStorage = WeatherStorage.getInstance(this);
        Button cityBtn = (Button)findViewById(R.id.choose_city_btn);
        cityBtn.setText(weatherStorage.getCurrentCity().name());
        cityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseCityActivity();
            }
        });

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction()
                        .equals(WeatherIntentService.WEATHER_CHANGED_ACTION)) {
                    MainActivity.this.updateWeather();
                }
            }
        };

        IntentFilter filter = new IntentFilter(WeatherIntentService.WEATHER_CHANGED_ACTION);

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

        Intent weatherLoadIntent = new Intent(MainActivity.this, WeatherIntentService.class);
        weatherLoadIntent.setAction(WeatherIntentService.WEATHER_LOAD_ACTION);
        startService(weatherLoadIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
            receiver = null;
        }

    }

    public void updateWeather() {
        WeatherStorage weatherStorage = WeatherStorage.getInstance(this);
        City currentCity = weatherStorage.getCurrentCity();
        Weather weather = weatherStorage.getLastSavedWeather(currentCity);
        TextView weatherTextView = (TextView) findViewById(R.id.weather_text);
        if (weather != null) {
            weatherTextView.setText(String.format("%s - %d (%s)", new Object[]{currentCity.name(),
                    Integer.valueOf(weather.getTemperature()),
                    weather.getDescription()}));
        }
        else {
            weatherTextView.setText(R.string.error_message);
        }
    }


    private void startChooseCityActivity() {
        Intent intent = new Intent(this, ChooseCityActivity.class);
        startActivity(intent);
    }


    public void onWeatherChangeResult(final boolean success) {
        TextView weatherText = (TextView) findViewById(R.id.weather_text);
        WeatherStorage weatherStorage = WeatherStorage.getInstance(this);
        weatherText.setText(weatherStorage.getLastSavedWeather(weatherStorage.getCurrentCity())
                .toString()
        );
    }



}
