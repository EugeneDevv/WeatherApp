package co.ke.smartspot.weatherapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import co.ke.smartspot.weatherapp.R;
import co.ke.smartspot.weatherapp.activities.HomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Function to delay the splash screen with a few seconds
        splashDelay();
    }

    private void splashDelay() {
        int DELAY_TIME_SECONDS = 5 * 1000;  //5 seconds
        new Handler().postDelayed(() -> {
            // This method will kill the launcher activity and launch the home once the timer is over
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            onBackPressed();
        }, DELAY_TIME_SECONDS);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}