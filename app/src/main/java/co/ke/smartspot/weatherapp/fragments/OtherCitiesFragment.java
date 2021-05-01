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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import co.ke.smartspot.weatherapp.R;
import co.ke.smartspot.weatherapp.models.WeatherModel;
import co.ke.smartspot.weatherapp.utils.Helper;
import co.ke.smartspot.weatherapp.viewmodels.CityViewModel;
import co.ke.smartspot.weatherapp.viewmodels.CurrentLocationViewModel;

public class OtherCitiesFragment extends Helper {

    //Initialize variables
    ImageView weatherIconIV;
    TextView lisbonTV, madridTV, berlinTV, parisTV, copenhagenTV, romeTV, londonTV, dublinTV,
            pragueTV, viennaTV, temperatureTV, conditionTV, cityNameTV;
    Button refreshBtn;
//    RelativeLayout relativeLayoutNoNet;
    ProgressBar progressBarCurrent;

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

//        relativeLayoutNoNet = view.findViewById(R.id.no_net_rl_c);
        refreshBtn = view.findViewById(R.id.refresh_btn_c);
        progressBarCurrent = view.findViewById(R.id.progress_bar_current_c);

        variableHooks();


        //Method to implement OnClick Listeners
        onClicks();
        if (isConnected(getContext())){
            checkLocationPermission();
        }
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
    private boolean isConnected(Context homeActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) homeActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())){
//            relativeLayoutNoNet.setVisibility(View.GONE);
            return true;
        } else {
//            showInternetDialog();
//            relativeLayoutNoNet.setVisibility(View.VISIBLE);
            return false;
        }
    }
    private void showInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Please connect to the internet to proceed")
                .setCancelable(false)
                .setPositiveButton("Connect", (dialogInterface, i) -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)))
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
            //When permission granted,
            progressBarCurrent.setVisibility(View.VISIBLE);
            doTheNetworking(city);
        } else{
            //When permission is not granted
            //Request permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }

    private void doTheNetworking(String city) {
        viewModel.getCityWeatherData(city,API_KEY).observe(this, weatherResponse -> {
            progressBarCurrent.setVisibility(View.GONE);
            cityNameTV.setText("" + weatherResponse.getCity());
            List<WeatherModel> hhlist = weatherResponse.getWeatherModel();
            conditionTV.setText(""+hhlist.get(0).getDescription());
            double temperature = Double.valueOf(weatherResponse.getTempModel().getTemp())-273.15;
            int roundedValue=(int)Math.rint(temperature);
            String converted = Integer.toString(roundedValue);
            temperatureTV.setText(converted+"°C");

            int id = hhlist.get(0).getId();
            String drawable = setImageIcon(id);
            updateUI(drawable);
        });
    }
    private  void updateUI(String cond){

        int resourceID = getResources().getIdentifier(cond,"drawable",getActivity().getPackageName());
        weatherIconIV.setImageResource(resourceID);


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
        if (isConnected(getContext())){
            checkLocationPermission();
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here


        //set onclick listeners
    }
}
