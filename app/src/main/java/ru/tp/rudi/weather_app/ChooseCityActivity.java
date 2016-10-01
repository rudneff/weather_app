package ru.tp.rudi.weather_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.mail.weather.lib.City;
import ru.mail.weather.lib.WeatherStorage;

public class ChooseCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);

        ViewGroup choose_city_activity = (ViewGroup) findViewById(R.id.activity_choose_city);

        for (final City city : City.values()) {
            Button btn = new Button(this);

            btn.setText(city.name());

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WeatherStorage.getInstance(ChooseCityActivity.this).setCurrentCity(city);
                    finish();
                }
            });

            choose_city_activity.addView(btn);

        }
    }
}
