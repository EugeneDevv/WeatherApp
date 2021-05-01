package co.ke.smartspot.weatherapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.text.CollationKey;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import co.ke.smartspot.weatherapp.R;
import co.ke.smartspot.weatherapp.adapters.PagerAdapter;

public class HomeActivity extends AppCompatActivity {

    //Initialize variables
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView themeToggleIV;

    SharedPreferences sharedPreferences = null;
    Boolean bool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        widgetHooks();
        onClicks();
        pagerAdapterInstance();
    }

    private void widgetHooks() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        themeToggleIV = findViewById(R.id.theme_toggle_iv);

        sharedPreferences = getSharedPreferences("night", 0);
        bool = sharedPreferences.getBoolean("night_mode", false);
        if (bool){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);;
            themeToggleIV.setImageResource(R.drawable.ic_sunny);
        }
    }
    private void onClicks() {
        themeToggleIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bool) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode", false);
                    editor.commit();
                    themeToggleIV.setImageResource(R.drawable.ic_night);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("night_mode", true);
                    editor.commit();
                    themeToggleIV.setImageResource(R.drawable.ic_sunny);
                }
            }
        });
    }
    private void pagerAdapterInstance() {
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}