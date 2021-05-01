package co.ke.smartspot.weatherapp.utils;

import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class Helper extends Fragment {

    public static String setImageIcon(int condition) {
        if(condition>=0 && condition<=300)
        {
            return "thunderstorm";
        }
        else if(condition>=300 && condition<=500)
        {
            return "lightrain";
        }
        else if(condition>=500 && condition<=600)
        {
            return "shower";
        }
        else  if(condition>=600 && condition<=700)
        {
            return "snow2";
        }
        else if(condition>=701 && condition<=771)
        {
            return "fog";
        }

        else if(condition>=772 && condition<=800)
        {
            return "overcast";
        }
        else if(condition==800)
        {
            return "sunny";
        }
        else if(condition>=801 && condition<=804)
        {
            return "cloudy";
        }
        else  if(condition>=900 && condition<=902)
        {
            return "thunderstorm";
        }
        if(condition==903)
        {
            return "snow1";
        }
        if(condition==904)
        {
            return "sunny";
        }
        if(condition>=905 && condition<=1000)
        {
            return "thunderstorm";
        }

        return null;

    }
    public void makeToast(String message){
        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
    }
}
