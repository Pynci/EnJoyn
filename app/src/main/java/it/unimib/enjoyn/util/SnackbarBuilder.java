package it.unimib.enjoyn.util;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import it.unimib.enjoyn.R;

public class SnackbarBuilder {

    public static Snackbar buildErrorSnackbar(int text, View view, Context context, int currentTheme) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);

        if(currentTheme == Configuration.UI_MODE_NIGHT_YES) {
            snackbar.setBackgroundTint(
                    ContextCompat.getColor(
                            context, androidx.cardview.R.color.cardview_dark_background));

            snackbar.setTextColor(ContextCompat.getColor(
                    context, R.color.md_theme_dark_error));
        }
        else{
            snackbar.setBackgroundTint(
                    ContextCompat.getColor(
                            context, androidx.cardview.R.color.cardview_light_background));

            snackbar.setTextColor(ContextCompat.getColor(
                    context, R.color.md_theme_light_error));
        }

        return snackbar;
    }

    public static Snackbar buildOkSnackbar(int text, View view, Context context, int currentTheme) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);

        if(currentTheme == Configuration.UI_MODE_NIGHT_YES) {
            snackbar.setBackgroundTint(
                    ContextCompat.getColor(
                            context, androidx.cardview.R.color.cardview_dark_background));

            snackbar.setTextColor(ContextCompat.getColor(
                    context, R.color.md_theme_dark_primary));
        }
        else{

            snackbar.setBackgroundTint(
                    ContextCompat.getColor(
                            context, androidx.cardview.R.color.cardview_light_background));

            snackbar.setTextColor(ContextCompat.getColor(
                    context, R.color.md_theme_light_primary));
        }

        return snackbar;
    }

}
