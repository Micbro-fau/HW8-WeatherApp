package com.example.hw8;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.security.auth.callback.Callback;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;



public class MainActivity extends AppCompatActivity {

    private static JSONObject jsonObj;


    private String TAG = MainActivity.class.getSimpleName();

    String urlSearch;

    // GPSTracker class
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_forecast)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }

    public void HomeSearch(String search){

        //Start of Volley Code
        RequestQueue queue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Making a request to url and getting response
        queue = new RequestQueue(cache,network);

        boolean valueResult;
        //checks if the string is numeric or not
        try{
            Integer.parseInt(search);
            valueResult = true;
        }catch(Exception e){
            valueResult = false;
        }

        //if the value was numeric (AKA a Zip), then make the urlSearch go to ZIP. Otherwise, make it go to Q
        if(valueResult == true){
            urlSearch = "https://api.openweathermap.org/data/2.5/weather?zip=" + search + "&appid=886fe0f06bc7abce6ea11e881327ebd3";
        }else{
            urlSearch = "https://api.openweathermap.org/data/2.5/weather?q=" + search + "&appid=886fe0f06bc7abce6ea11e881327ebd3";
        }

        queue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSearch,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //textView.setText("Response is: "+ response.substring(0,500));
                        try {

                            jsonObj = new JSONObject(response);
                            WeatherData weather = WeatherData.getInstance();

                            JSONObject coord = jsonObj.getJSONObject("coord");
                            String lat = coord.getString("lat");
                            final double latitude = parseDouble(lat);

                            String lon = coord.getString("lon");
                            final double longitude = parseDouble(lon);

                            weather.setLongitude(longitude);
                            weather.setLatitude(latitude);

                            JSONObject main = jsonObj.getJSONObject("main");

                            double currentTemp = parseDouble(main.getString("temp"));
                            currentTemp = ((currentTemp - 273.25) * 1.8 ) + 32;

                            weather.setTemperature(format("%.0f",currentTemp));

                            currentTemp = parseDouble(main.getString("feels_like"));
                            currentTemp = ((currentTemp - 273.25) * 1.8 ) + 32;

                            weather.setFeelsLike(format("%.0f",currentTemp));

                            weather.setHumidity(main.getString("humidity") + "m/s");

                            JSONObject wind = jsonObj.getJSONObject("wind");

                            weather.setWindSpeed(wind.getString("speed") + "%");

                            if(parseInt(wind.getString("deg")) > 0 & parseInt(wind.getString("deg")) < 90){
                                String windDirection = "NE";
                                weather.setWindDegrees(windDirection);
                            } else if (parseInt(wind.getString("deg")) > 90 & parseInt(wind.getString("deg")) < 180) {
                                String windDirection = "SE";
                                weather.setWindDegrees(windDirection);
                            } else if (parseInt(wind.getString("deg")) > 180 & parseInt(wind.getString("deg")) < 270) {
                                String windDirection = "SW";
                                weather.setWindDegrees(windDirection);
                            } else if (parseInt(wind.getString("deg")) > 270 & parseInt(wind.getString("deg")) < 360) {
                                String windDirection = "NW";
                                weather.setWindDegrees(windDirection);
                            } else if (parseInt(wind.getString("deg")) == 0) {
                                String windDirection = "N";
                                weather.setWindDegrees(windDirection);
                            } else if (parseInt(wind.getString("deg")) == 90) {
                                String windDirection = "E";
                                weather.setWindDegrees(windDirection);
                            }else if (parseInt(wind.getString("deg")) == 180) {
                                String windDirection = "S";
                                weather.setWindDegrees(windDirection);
                            }else if (parseInt(wind.getString("deg")) == 270) {
                                String windDirection = "W";
                                weather.setWindDegrees(windDirection);
                            }

                            weather.setPressure(main.getString("pressure") + " hPa");

                            JSONArray weatherJSON = jsonObj.getJSONArray("weather");
                            JSONObject w = weatherJSON.getJSONObject(0);

                            weather.setCloudType(w.getString("description"));

                            JSONObject sys = jsonObj.getJSONObject("sys");

                            weather.setCountry(sys.getString("country"));

                            Date date = new Date(parseInt(sys.getString("sunrise"))*1000L);
                            SimpleDateFormat jdf = new SimpleDateFormat("hh:mm aa");
                            jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                            String java_date = jdf.format(date);

                            weather.setSunrise(java_date);

                            date = new Date(parseInt(sys.getString("sunset"))*1000L);
                            jdf = new SimpleDateFormat("hh:mm aa");
                            jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                            java_date = jdf.format(date);

                            weather.setSunset(java_date);


                        }catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),
                                            "Json parsing error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //textView.setText("That didn't work!");
            }
        });

        queue.add(stringRequest);

    }

    public void HomeGPS(){

        // create class object
        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            final double latitude = gps.getLatitude();
            final double longitude = gps.getLongitude();

            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            //Start of Volley Code
            RequestQueue queue;
            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());
            // Making a request to url and getting response
            queue = new RequestQueue(cache,network);

            queue.start();
            //URL which sends the lat and lon data from GPS to Open Weather API
            String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=886fe0f06bc7abce6ea11e881327ebd3";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //textView.setText("Response is: "+ response.substring(0,500));
                            try {

                                jsonObj = new JSONObject(response);
                                WeatherData weather = WeatherData.getInstance();

                                weather.setLongitude(longitude);
                                weather.setLatitude(latitude);

                                JSONObject main = jsonObj.getJSONObject("main");

                                double currentTemp = parseDouble(main.getString("temp"));
                                currentTemp = ((currentTemp - 273.25) * 1.8 ) + 32;

                                weather.setTemperature(format("%.0f",currentTemp));

                                currentTemp = parseDouble(main.getString("feels_like"));
                                currentTemp = ((currentTemp - 273.25) * 1.8 ) + 32;

                                weather.setFeelsLike(format("%.0f",currentTemp));

                                weather.setHumidity(main.getString("humidity") + "m/s");

                                JSONObject wind = jsonObj.getJSONObject("wind");

                                weather.setWindSpeed(wind.getString("speed") + "%");

                                if(parseInt(wind.getString("deg")) > 0 & parseInt(wind.getString("deg")) < 90){
                                    String windDirection = "NE";
                                    weather.setWindDegrees(windDirection);
                                } else if (parseInt(wind.getString("deg")) > 90 & parseInt(wind.getString("deg")) < 180) {
                                    String windDirection = "SE";
                                    weather.setWindDegrees(windDirection);
                                } else if (parseInt(wind.getString("deg")) > 180 & parseInt(wind.getString("deg")) < 270) {
                                    String windDirection = "SW";
                                    weather.setWindDegrees(windDirection);
                                } else if (parseInt(wind.getString("deg")) > 270 & parseInt(wind.getString("deg")) < 360) {
                                    String windDirection = "NW";
                                    weather.setWindDegrees(windDirection);
                                } else if (parseInt(wind.getString("deg")) == 0) {
                                    String windDirection = "N";
                                    weather.setWindDegrees(windDirection);
                                } else if (parseInt(wind.getString("deg")) == 90) {
                                    String windDirection = "E";
                                    weather.setWindDegrees(windDirection);
                                }else if (parseInt(wind.getString("deg")) == 180) {
                                    String windDirection = "S";
                                    weather.setWindDegrees(windDirection);
                                }else if (parseInt(wind.getString("deg")) == 270) {
                                    String windDirection = "W";
                                    weather.setWindDegrees(windDirection);
                                }

                                weather.setPressure(main.getString("pressure") + " hPa");

                                JSONArray weatherJSON = jsonObj.getJSONArray("weather");
                                JSONObject w = weatherJSON.getJSONObject(0);

                                weather.setCloudType(w.getString("description"));

                                JSONObject sys = jsonObj.getJSONObject("sys");

                                weather.setCountry(sys.getString("country"));

                                Date date = new Date(parseInt(sys.getString("sunrise"))*1000L);
                                SimpleDateFormat jdf = new SimpleDateFormat("hh:mm aa");
                                jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                                String java_date = jdf.format(date);

                                weather.setSunrise(java_date);

                                date = new Date(parseInt(sys.getString("sunset"))*1000L);
                                jdf = new SimpleDateFormat("hh:mm aa");
                                jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                                java_date = jdf.format(date);

                                weather.setSunset(java_date);


                            }catch (final JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),
                                                "Json parsing error: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //textView.setText("That didn't work!");
                }
            });

            queue.add(stringRequest);

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }


}