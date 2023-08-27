package com.neural.geez_ocr;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Language extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
    }
    public void showChangeLanguageDialog() {
        // Array of language to display in alert dialog
        final String[] listItems = {"English", "አማርኛ"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());

        builder.setTitle(getString(R.string.choose_language));
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    // English
                    setLocale("en");
                    recreate();
                } else if (i == 1) {
                    //amharic
                    setLocale("am");
                    recreate();
                }

                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();

        // Show alert dialog
        alertDialog.show();
    }

    public void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.locale = locale;
        super.onConfigurationChanged(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("language", lang);
        editor.apply();

    }

    public void loadLocale() {
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = pref.getString("language", "");
        setLocale(language);

    }
}
