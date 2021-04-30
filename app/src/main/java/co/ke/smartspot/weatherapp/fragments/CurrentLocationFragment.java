package co.ke.smartspot.weatherapp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Looper;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.ke.smartspot.weatherapp.R;
import co.ke.smartspot.weatherapp.models.WeatherData;
import co.ke.smartspot.weatherapp.models.WeatherModel;
import co.ke.smartspot.weatherapp.utils.Helper;
import co.ke.smartspot.weatherapp.viewmodels.CurrentLocationViewModel;
import cz.msebera.android.httpclient.Header;

public class CurrentLocationFragment extends Helper {

    //Initialize variables
    LocationManager locationManager;
    FusedLocationProviderClient client;

    private String API_KEY;
    private CurrentLocationViewModel viewModel;
    ImageView weatherIconIV;
    TextView temperatureTV, conditionTV, cityNameTV;
    Button refreshBtn;
    RelativeLayout relativeLayoutNoNet;
    ProgressBar progressBarCurrent;

    private String latitude;
    private String longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view
        View view = inflater.inflate(R.layout.fragment_current_location, container, false);

        temperatureTV = view.findViewById(R.id.temperature_tv);
        conditionTV = view.findViewById(R.id.condition_tv);
        cityNameTV = view.findViewById(R.id.cityName_tv);
        weatherIconIV = view.findViewById(R.id.weatherIcon_iv);
        relativeLayoutNoNet = view.findViewById(R.id.no_net_rl);
        refreshBtn = view.findViewById(R.id.refresh_btn);
        progressBarCurrent = view.findViewById(R.id.progress_bar_current);


//
        variableHooks();
        onClicks();
        if (isConnected(getContext())){
            checkLocationPermission();
        }
        //Return view
        return view;
    }

    private void variableHooks() {
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        API_KEY = "68ea9cffda56da62a2f7abfa4f88fd14";
        viewModel = new ViewModelProvider(this).get(CurrentLocationViewModel.class);
    }
    private boolean isConnected(Context homeActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) homeActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())){
            relativeLayoutNoNet.setVisibility(View.GONE);
            return true;
        } else {
//            showInternetDialog();
            relativeLayoutNoNet.setVisibility(View.VISIBLE);
            return false;
        }
    }
    private void onClicks() {
        refreshBtn.setOnClickListener(v -> {
            if (!isConnected(getContext())){
                showInternetDialog();
            }
            else {
                checkLocationPermission();
            }
        });
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
            getCurrentLocation();
        } else{
            //When permission is not granted
            //Request permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //when location service is enabled
            //Get last location
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Initialize location
                    Location location = task.getResult();
                    //check condition
                    if (location != null){
                        //when location result is not null
                        //set latitude
                        latitude = String.valueOf(location.getLatitude());
                        //set longitude
                        longitude = String.valueOf(location.getLongitude());
                        doTheNetworking();
                    } else{
                        //when location result is null
                        //Initialize location request
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(100)
                                .setNumUpdates(1);

                        //Initialize location call back
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                //Initialize location
                                Location location1 = locationResult.getLastLocation();
                                //set latitude
                                latitude = String.valueOf(location1.getLatitude());
                                //set longitude
                                longitude = String.valueOf(location1.getLongitude());
                                doTheNetworking();
                            }
                        };
                        //Request Location updates
                        client.requestLocationUpdates(locationRequest,
                                locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            //when location service is not enabled
            //open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Check condition
        if (requestCode == 100 && (grantResults.length > 0) &&
                (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)){
            //When permissions are granted
            //Call method
//            getCurrentLocation();
            makeToast("Permission granted for location");
        } else {
            //When permission are denied
            //Display toast
//            makeToast("Permission denied for location");
//            getActivity().finish();
        }
    }

    private void doTheNetworking() {
        viewModel.getCurrentWeatherData(latitude,longitude,API_KEY).observe(this, weatherResponse -> {
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