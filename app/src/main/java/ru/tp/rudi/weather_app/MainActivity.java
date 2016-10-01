package ru.tp.rudi.weather_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ru.mail.weather.lib.WeatherStorage;

public class MainActivity extends AppCompatActivity implements ServiceHelper.WeatherChangeResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cityBtn = (Button)findViewById(R.id.choose_city_btn);
        WeatherStorage weatherStorage = WeatherStorage.getInstance(this);
        cityBtn.setText(weatherStorage.getCurrentCity().name());

        cityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChooseCityActivity();
            }
        });

        TextView weatherText = (TextView) findViewById(R.id.weather_text);

        Button backgroundUpdateOnBtn = (Button) findViewById(R.id.background_update_on_btn);

        backgroundUpdateOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        Button backgroundUpdateOffBtn = (Button) findViewById(R.id.background_update_off_btn);

        backgroundUpdateOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
