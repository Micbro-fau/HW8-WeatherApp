package com.example.hw8;

public class WeatherData {

    private String temp = "No weather data yet";

    private static WeatherData wd = new WeatherData();

    public static WeatherData getInstance() {
        return WeatherData.wd;
    }

    private String temperature = "10";

    public String getTemperature(){
        return this.temperature;
    }

    public void setTemperature(String tempNew){
        this.temperature = tempNew;
    }

    private String feels_like = "10";

    public String getFeelsLike(){
        return this.feels_like;
    }

    public void setFeelsLike(String tempNew){
        this.feels_like = tempNew;
    }

    private String humidity = "10";

    public String getHumidity(){
        return this.humidity;
    }

    public void setHumidity(String tempNew){
        this.humidity = tempNew;
    }

    private String windSpeed = "10";

    public String getWindSpeed(){
        return this.windSpeed;
    }

    public void setWindSpeed(String tempNew){
        this.windSpeed = tempNew;
    }

    private String windDegrees = "10";

    public String getWindDegrees(){
        return this.windDegrees;
    }

    public void setWindDegrees(String tempNew){
        this.windDegrees = tempNew;
    }

    private String pressure = "10";

    public String getPressure(){
        return this.pressure;
    }

    public void setPressure(String tempNew){
        this.pressure = tempNew;
    }

    private String cloudType = "10";

    public String getCloudType(){
        return this.cloudType;
    }

    public void setCloudType(String tempNew){
        this.cloudType = tempNew;
    }

    private String country = "10";

    public String getCountry(){
        return this.country;
    }

    public void setCountry(String tempNew){
        this.country = tempNew;
    }

    private String sunrise = "10";

    public String getSunrise(){
        return this.sunrise;
    }

    public void setSunrise(String tempNew){
        this.sunrise = tempNew;
    }

    private String sunset = "10";

    public String getSunset(){
        return this.sunset;
    }

    public void setSunset(String tempNew){
        this.sunset = tempNew;
    }

    private double latitude = -34;

    public double getLatitude(){
        return this.latitude;
    }

    public void setLatitude(double tempNew){
        this.latitude = tempNew;
    }

    private double longitude = 151;

    public double getLongitude(){
        return this.longitude;
    }

    public void setLongitude(double tempNew){
        this.longitude = tempNew;
    }

}
