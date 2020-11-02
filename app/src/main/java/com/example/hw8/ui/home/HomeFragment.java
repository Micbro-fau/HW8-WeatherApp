package com.example.hw8.ui.home;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hw8.MainActivity;
import com.example.hw8.R;
import com.example.hw8.WeatherData;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private final int REQ_CODE = 100;


    Button btnWeatherAndMapGPS;
    Button btnWeatherAndMapSearch;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView speak = root.findViewById(R.id.speak);

        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    /*
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();

                     */
                }
            }
        });

        btnWeatherAndMapSearch = (Button) root.findViewById(R.id.search);

        final EditText searchText = (EditText) root.findViewById(R.id.searchText);

        btnWeatherAndMapSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String value = searchText.getText().toString(); //gets the data from the text bar

                MainActivity main = (MainActivity) getActivity();
                main.HomeSearch(value); //sends text field string to HomeSearch located in the main activity

            }
        });

        btnWeatherAndMapGPS = (Button) root.findViewById(R.id.GPS);

        btnWeatherAndMapGPS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                MainActivity main = (MainActivity) getActivity();
                main.HomeGPS(); //calls the HomeGPS function which updates the WeatherData Class with data from the GPS location

            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);



            }
        });
        return root;




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //for the voice code
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    MainActivity main = (MainActivity) getActivity();
                    main.HomeSearch(result.get(0));
                }
                break;
            }
        }
    }
}