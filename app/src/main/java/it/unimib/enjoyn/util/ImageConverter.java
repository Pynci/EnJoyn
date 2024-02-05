package it.unimib.enjoyn.util;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import it.unimib.enjoyn.R;

public class ImageConverter {

    public void setCategoryImage(ImageView imageView, String nomeCategory){
        switch (nomeCategory){
            case "Passeggiata":
                imageView.setBackgroundResource(R.drawable.passeggiata);
                break;
            case "Viaggi":
                imageView.setBackgroundResource(R.drawable.viaggi);
                break;
            case "Cibo":
                imageView.setBackgroundResource(R.drawable.cibo);
                break;
            case "Giochi":
                imageView.setBackgroundResource(R.drawable.giochi_da_tavolo);
                break;
            case "Shopping":
                imageView.setBackgroundResource(R.drawable.shopping);
                break;
            case "Cinema":
                imageView.setBackgroundResource(R.drawable.cinema);
                break;
            case "Sport":
                imageView.setBackgroundResource(R.drawable.sport);
                break;
            case "Party":
                imageView.setBackgroundResource(R.drawable.party);
                break;
        }
    }

    public void setWeatherIcon(ImageView weatherIcon, int code){
        if (code == 0){
            weatherIcon.setBackgroundResource(R.drawable.drawable_sun);
        } else if (code >= 1 && code <= 3){
            weatherIcon.setBackgroundResource(R.drawable.drawable_partlycloudy);
        } else if (code == 45 || code == 48){
            weatherIcon.setBackgroundResource(R.drawable.drawable_fog);
        } else if (code == 51 || code == 53 || code == 55 || code == 56 || code == 57) {
            weatherIcon.setBackgroundResource(R.drawable.drawable_drizzle);
        } else if (code == 61 || code == 63 || code == 65 || code == 66 || code == 67 || code == 80 || code == 81 || code == 82){
            weatherIcon.setBackgroundResource(R.drawable.drawable_rain);
        } else if (code == 71 || code == 73 || code == 75 || code == 77){
            weatherIcon.setBackgroundResource(R.drawable.drawable_snowlight);
        } else if (code == 85 || code == 86){
            weatherIcon.setBackgroundResource(R.drawable.drawable_snow);
        } else if (code == 95 || code == 96 || code == 99){
            weatherIcon.setBackgroundResource(R.drawable.drawable_thunderstorm);
        }
    }

    public void setColorEvent(TextView textViewPlace, ImageView backgroundImage, Button joinButton, ColorObject color, View itemView){

        switch (color.getHex()){
            case "#805D93":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Pomp_and_Power));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Pomp_and_Power));


                break;
            case "#CE6A85":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Blush));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Blush));
                break;
            case "#FF8C61":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Coral));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Coral));
                break;
            case "#9EBD6E":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Olivine));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Olivine));
                break;
            case "#169873":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Shamrock_green));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Shamrock_green));
                break;
            case "#348AA7":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Light_Blue));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Light_Blue));
                break;
            case "#355691":
                backgroundImage.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Blue));

                joinButton.setBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.Blue));

                break;
        }

    }
}
