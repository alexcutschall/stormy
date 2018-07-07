package com.example.alexcutschall.stormy.Weather;

import com.example.alexcutschall.stormy.CurrentWeather;

public class Forecast {

    private CurrentWeather currentWeather;
    private Hour[] hourlyForecast;

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public Hour[] getHourlyForecast() {
        return hourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
    }
}
