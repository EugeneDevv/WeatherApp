package co.ke.smartspot.weatherapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import co.ke.smartspot.weatherapp.fragments.CurrentLocationFragment;
import co.ke.smartspot.weatherapp.fragments.OtherCitiesFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private int numberOfTabs;

    //create constructor
    public PagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CurrentLocationFragment();
            case 1:
                return new OtherCitiesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        //Return fragment array list size
        return numberOfTabs;
    }

}
