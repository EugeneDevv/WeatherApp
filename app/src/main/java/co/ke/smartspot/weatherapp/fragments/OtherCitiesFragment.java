package co.ke.smartspot.weatherapp.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import co.ke.smartspot.weatherapp.R;
import co.ke.smartspot.weatherapp.models.WeatherModel;
import co.ke.smartspot.weatherapp.utils.Helper;
import co.ke.smartspot.weatherapp.viewmodels.CityViewModel;

public class OtherCitiesFragment extends Helper {

    //Initialize variables
    ImageView weatherIconIV;
    TextView lisbonTV, madridTV, berlinTV, parisTV, copenhagenTV, romeTV, londonTV, dublinTV,
            pragueTV, viennaTV, temperatureTV, conditionTV, cityNameTV, pressureTV, humidityTV,
            windTV, visibilityTV, dateTimeTV;
    Button refreshBtn;
//    RelativeLayout relativeLayoutNoNet;
    RelativeLayout progressBarCurrent;

    ColorStateList defaultColor, selectedColor;

    private String API_KEY;
    private String city;
    private CityViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_other_cities, container, false);

        lisbonTV = view.findViewById(R.id.lisbon_tv);
        madridTV = view.findViewById(R.id.madrid_tv);
        berlinTV = view.findViewById(R.id.berlin_tv);
        parisTV = view.findViewById(R.id.paris_tv);
        copenhagenTV = view.findViewById(R.id.copenhagen_tv);
        romeTV = view.findViewById(R.id.rome_tv);
        londonTV = view.findViewById(R.id.london_tv);
        dublinTV = view.findViewById(R.id.dublin_tv);
        pragueTV = view.findViewById(R.id.prague_tv);
        viennaTV = view.findViewById(R.id.vienna_tv);

        temperatureTV = view.findViewById(R.id.temp_tv);
        conditionTV = view.findViewById(R.id.desc_tv);
        cityNameTV = view.findViewById(R.id.city);
        weatherIconIV = view.findViewById(R.id.weatherIcon_iv);
        refreshBtn = view.findViewById(R.id.refresh_btn);
        progressBarCurrent = view.findViewById(R.id.progress_bar_current);
        pressureTV = view.findViewById(R.id.pressure_tv);
        humidityTV = view.findViewById(R.id.humidity_tv);
        windTV = view.findViewById(R.id.wind_tv);
        visibilityTV = view.findViewById(R.id.visibility_tv);
        dateTimeTV = view.findViewById(R.id.date_time_tv);

        variableHooks();


        //Method to implement OnClick Listeners
        onClicks();
        doTheNetworking(city);
        //Return view
        return view;
    }

    private void variableHooks() {
        //Get the two different text colors when fragment is created
        API_KEY = "68ea9cffda56da62a2f7abfa4f88fd14";
        defaultColor = madridTV.getTextColors();
        selectedColor = lisbonTV.getTextColors();
        viewModel = new ViewModelProvider(this).get(CityViewModel.class);
        city = "Lisbon";
    }

    private void doTheNetworking(String city) {
        viewModel.getCityWeatherData(city,API_KEY).observe(this, weatherResponse -> {
            List<WeatherModel> weatherModel = weatherResponse.getWeatherModel();
            String drawable;
            String cityName;
            String condition;
            String temperature;
            String pressure;
            String humidity;
            String windSpeed;
            String visibility;
            String dateTime;

            cityName = ""+weatherResponse.getCity();

            condition = ""+weatherModel.get(0).getDescription();

            double tempDouble = Double.valueOf(weatherResponse.getMainDetailsModel().getTemp())-273.15;
            int roundedTemp=(int)Math.rint(tempDouble);
            temperature = roundedTemp +"°C";

            double humidityDouble = Double.valueOf(weatherResponse.getMainDetailsModel().getHumidity());
            int roundedHumidity = (int)Math.rint(humidityDouble);
            humidity = ""+ roundedHumidity;

            double pressureDouble = Double.valueOf(weatherResponse.getMainDetailsModel().getPressure());
            int roundedPressure = (int)Math.rint(pressureDouble);
            pressure = ""+roundedPressure;

            int id = weatherModel.get(0).getId();
            drawable = setImageIcon(id);

            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.HALF_UP);
            windSpeed = df.format(Double.valueOf(weatherResponse.getWindModel().getSpeed()) * 1.60934);

            int visibilityInt = weatherResponse.getVisibility() / 100;
            visibility = Integer.toString(visibilityInt);

            //Convert timestamp to proper format
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTimeInMillis(Long.parseLong(""+System.currentTimeMillis()));
            } catch (Exception e){

            }
            dateTime = DateFormat.format("EEE, d MMM yyyy", cal).toString();

            updateUI(drawable,cityName,condition,temperature,pressure,humidity,windSpeed,visibility,dateTime);
        });
    }
    private void updateUI(String drawable, String city, String condition, String temperature, String pressure, String humidity, String windSpeed, String visibility, String dateTime) {
        cityNameTV.setText(city);
        conditionTV.setText(condition);
        temperatureTV.setText(temperature);
        pressureTV.setText(pressure);
        humidityTV.setText(humidity);
        windTV.setText(windSpeed);
        visibilityTV.setText(visibility);
        dateTimeTV.setText(dateTime);

        int resourceID = getResources().getIdentifier(drawable,"drawable",getActivity().getPackageName());
        weatherIconIV.setImageResource(resourceID);
        progressBarCurrent.setVisibility(View.GONE);
    }
    private void onClicks() {
        lisbonTV.setOnClickListener(v -> {
            doTheNetworking(lisbonTV.getText().toString().trim());
            lisbonTV.setTextColor(selectedColor);
            lisbonTV.setBackgroundResource(R.drawable.shape_selected_city);
            madridTV.setTextColor(defaultColor);
            madridTV.setBackgroundResource(0);
            berlinTV.setTextColor(defaultColor);
            berlinTV.setBackgroundResource(0);
            parisTV.setTextColor(defaultColor);
            parisTV.setBackgroundResource(0);
          copRomLonDubPraVienDefaultColor();
        });
        madridTV.setOnClickListener(v -> {
            doTheNetworking(madridTV.getText().toString().trim());
            lisbonTV.setTextColor(defaultColor);
            lisbonTV.setBackgroundResource(0);
            madridTV.setTextColor(selectedColor);
            madridTV.setBackgroundResource(R.drawable.shape_selected_city);
            berlinTV.setTextColor(defaultColor);
            berlinTV.setBackgroundResource(0);
            parisTV.setTextColor(defaultColor);
            parisTV.setBackgroundResource(0);
            copRomLonDubPraVienDefaultColor();
        });
        berlinTV.setOnClickListener(v -> {
            doTheNetworking(berlinTV.getText().toString().trim());
            lisbonTV.setTextColor(defaultColor);
            lisbonTV.setBackgroundResource(0);
            madridTV.setTextColor(defaultColor);
            madridTV.setBackgroundResource(0);
            berlinTV.setTextColor(selectedColor);
            berlinTV.setBackgroundResource(R.drawable.shape_selected_city);
            parisTV.setTextColor(defaultColor);
            parisTV.setBackgroundResource(0);
            copRomLonDubPraVienDefaultColor();
        });
        parisTV.setOnClickListener(v -> {
            doTheNetworking(parisTV.getText().toString().trim());
            lisbonTV.setTextColor(defaultColor);
            lisbonTV.setBackgroundResource(0);
            madridTV.setTextColor(defaultColor);
            madridTV.setBackgroundResource(0);
            berlinTV.setTextColor(defaultColor);
            berlinTV.setBackgroundResource(0);
            parisTV.setTextColor(selectedColor);
            parisTV.setBackgroundResource(R.drawable.shape_selected_city);
            copRomLonDubPraVienDefaultColor();
        });
        copenhagenTV.setOnClickListener(v -> {
            doTheNetworking(copenhagenTV.getText().toString().trim());
            lisMadberParDefaultColor();
            copenhagenTV.setTextColor(selectedColor);
            copenhagenTV.setBackgroundResource(R.drawable.shape_selected_city);
            romeTV.setTextColor(defaultColor);
            romeTV.setBackgroundResource(0);
            londonTV.setTextColor(defaultColor);
            londonTV.setBackgroundResource(0);
            dublinTV.setTextColor(defaultColor);
            dublinTV.setBackgroundResource(0);
            pragueTV.setTextColor(defaultColor);
            pragueTV.setBackgroundResource(0);
            viennaTV.setTextColor(defaultColor);
            viennaTV.setBackgroundResource(0);
        });
        romeTV.setOnClickListener(v -> {
            doTheNetworking(romeTV.getText().toString().trim());
            lisMadberParDefaultColor();
            copenhagenTV.setTextColor(defaultColor);
            copenhagenTV.setBackgroundResource(0);
            romeTV.setTextColor(selectedColor);
            romeTV.setBackgroundResource(R.drawable.shape_selected_city);
            londonTV.setTextColor(defaultColor);
            londonTV.setBackgroundResource(0);
            dublinTV.setTextColor(defaultColor);
            dublinTV.setBackgroundResource(0);
            pragueTV.setTextColor(defaultColor);
            pragueTV.setBackgroundResource(0);
            viennaTV.setTextColor(defaultColor);
            viennaTV.setBackgroundResource(0);
        });
        londonTV.setOnClickListener(v -> {
            doTheNetworking(londonTV.getText().toString().trim());
            lisMadberParDefaultColor();
            copenhagenTV.setTextColor(defaultColor);
            copenhagenTV.setBackgroundResource(0);
            romeTV.setTextColor(defaultColor);
            romeTV.setBackgroundResource(0);
            londonTV.setTextColor(selectedColor);
            londonTV.setBackgroundResource(R.drawable.shape_selected_city);
            dublinTV.setTextColor(defaultColor);
            dublinTV.setBackgroundResource(0);
            pragueTV.setTextColor(defaultColor);
            pragueTV.setBackgroundResource(0);
            viennaTV.setTextColor(defaultColor);
            viennaTV.setBackgroundResource(0);
        });
        dublinTV.setOnClickListener(v -> {
            doTheNetworking(dublinTV.getText().toString().trim());
            lisMadberParDefaultColor();
            copenhagenTV.setTextColor(defaultColor);
            copenhagenTV.setBackgroundResource(0);
            romeTV.setTextColor(defaultColor);
            romeTV.setBackgroundResource(0);
            londonTV.setTextColor(defaultColor);
            londonTV.setBackgroundResource(0);
            dublinTV.setTextColor(selectedColor);
            dublinTV.setBackgroundResource(R.drawable.shape_selected_city);
            pragueTV.setTextColor(defaultColor);
            pragueTV.setBackgroundResource(0);
            viennaTV.setTextColor(defaultColor);
            viennaTV.setBackgroundResource(0);
        });
        pragueTV.setOnClickListener(v -> {
            doTheNetworking(pragueTV.getText().toString().trim());
            lisMadberParDefaultColor();
            copenhagenTV.setTextColor(defaultColor);
            copenhagenTV.setBackgroundResource(0);
            romeTV.setTextColor(defaultColor);
            romeTV.setBackgroundResource(0);
            londonTV.setTextColor(defaultColor);
            londonTV.setBackgroundResource(0);
            dublinTV.setTextColor(defaultColor);
            dublinTV.setBackgroundResource(0);
            pragueTV.setTextColor(selectedColor);
            pragueTV.setBackgroundResource(R.drawable.shape_selected_city);
            viennaTV.setTextColor(defaultColor);
            viennaTV.setBackgroundResource(0);
        });
        viennaTV.setOnClickListener(v -> {
            doTheNetworking(viennaTV.getText().toString().trim());
            lisMadberParDefaultColor();
            copenhagenTV.setTextColor(defaultColor);
            copenhagenTV.setBackgroundResource(0);
            romeTV.setTextColor(defaultColor);
            romeTV.setBackgroundResource(0);
            londonTV.setTextColor(defaultColor);
            londonTV.setBackgroundResource(0);
            dublinTV.setTextColor(defaultColor);
            dublinTV.setBackgroundResource(0);
            pragueTV.setTextColor(defaultColor);
            pragueTV.setBackgroundResource(0);
            viennaTV.setTextColor(selectedColor);
            viennaTV.setBackgroundResource(R.drawable.shape_selected_city);
        });
    }

    private void lisMadberParDefaultColor() {
        lisbonTV.setTextColor(defaultColor);
        lisbonTV.setBackgroundResource(0);
        madridTV.setTextColor(defaultColor);
        madridTV.setBackgroundResource(0);
        berlinTV.setTextColor(defaultColor);
        berlinTV.setBackgroundResource(0);
        parisTV.setTextColor(defaultColor);
        parisTV.setBackgroundResource(0);
    }

    private void copRomLonDubPraVienDefaultColor() {
        copenhagenTV.setTextColor(defaultColor);
        copenhagenTV.setBackgroundResource(0);
        romeTV.setTextColor(defaultColor);
        romeTV.setBackgroundResource(0);
        londonTV.setTextColor(defaultColor);
        londonTV.setBackgroundResource(0);
        dublinTV.setTextColor(defaultColor);
        dublinTV.setBackgroundResource(0);
        pragueTV.setTextColor(defaultColor);
        pragueTV.setBackgroundResource(0);
        viennaTV.setTextColor(defaultColor);
        viennaTV.setBackgroundResource(0);
    }
    @Override
    public void onResume() {
        super.onResume();
            doTheNetworking(city);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here


        //set onclick listeners
    }
}
