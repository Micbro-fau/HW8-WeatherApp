package com.example.hw8.ui.dashboard;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hw8.R;
import com.example.hw8.WeatherData;

import java.util.Locale;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    //apart of Text to Speech
    TextToSpeech t1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        WeatherData weather = WeatherData.getInstance();

        t1=new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    t1.setLanguage(Locale.UK);
                    WeatherData weather = WeatherData.getInstance();
                    String toSpeak = "The Temperature is currently " + weather.getTemperature() + " but it feels like " + weather.getFeelsLike() + ". The Humidity is currently" + weather.getHumidity() + ". The Wind Speed is " + weather.getWindSpeed() + "While the Wind Direction is" + weather.getWindDegrees() + ". The Pressure is " + weather.getPressure() + "They are " + weather.getCloudType() + "and the Sunrise will be at" + weather.getSunrise() + "while the Sunset is at" + weather.getSunset();
                    Toast.makeText(getActivity(), toSpeak,Toast.LENGTH_SHORT).show();
                    t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });



        final TextView textView = root.findViewById(R.id.row1name);
        final TextView textView2 = root.findViewById(R.id.row1value);
        final TextView textView3 = root.findViewById(R.id.row2name);
        final TextView textView4 = root.findViewById(R.id.row2value);
        final TextView textView5 = root.findViewById(R.id.row3name);
        final TextView textView6 = root.findViewById(R.id.row3value);
        final TextView textView7 = root.findViewById(R.id.row4name);
        final TextView textView8 = root.findViewById(R.id.row4value);
        final TextView textView9 = root.findViewById(R.id.row5name);
        final TextView textView10 = root.findViewById(R.id.row5value);
        final TextView textView11 = root.findViewById(R.id.row6name);
        final TextView textView12 = root.findViewById(R.id.row6value);
        final TextView textView13 = root.findViewById(R.id.row7name);
        final TextView textView14 = root.findViewById(R.id.row7value);
        final TextView textView15 = root.findViewById(R.id.row8name);
        final TextView textView16 = root.findViewById(R.id.row8value);
        final TextView textView17 = root.findViewById(R.id.row9name);
        final TextView textView18 = root.findViewById(R.id.row9value);
        final TextView textView19 = root.findViewById(R.id.row10name);
        final TextView textView20 = root.findViewById(R.id.row10value);

        textView.setText("Temperature");


        textView2.setText(weather.getTemperature());

        textView3.setText("Feels Like");

        textView4.setText(weather.getFeelsLike());

        textView5.setText("Humidity");

        textView6.setText(weather.getHumidity());

        textView7.setText("Wind Speed");

        textView8.setText(weather.getWindSpeed());

        textView9.setText("Wind Direction");

        textView10.setText(weather.getWindDegrees());

        textView11.setText("Pressure");

        textView12.setText(weather.getPressure());

        textView13.setText("Cloud Type");

        textView14.setText(weather.getCloudType());

        textView15.setText("Country");

        textView16.setText(weather.getCountry());

        textView17.setText("Sunrise");

        textView18.setText(weather.getSunrise());

        textView19.setText("Sunset");

        textView20.setText(weather.getSunset());

        //final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}