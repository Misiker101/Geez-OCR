package com.neural.geez_ocr;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {


    private RelativeLayout btn_language, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        loadLocale();
        btn_language = (RelativeLayout) findViewById(R.id.lang);
        about = (RelativeLayout) findViewById(R.id.about_rel);

        btn_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialogue();
            }
        });


    }

    private void showChangeLanguageDialog() {
        // Array of language to display in alert dialog
        final String[] listItems = {"English", "አማርኛ"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

        builder.setTitle("Choose Language...");
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    // English
                    setLocale("en");
                    recreate();
                } else if (i == 1) {
                    // Hebrew
                    setLocale("am");
                    recreate();
                }

                // Dismiss alert dialog when language stored
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();

        // Show alert dialog
        alertDialog.show();
    }



    private void setLocale(String lang) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        configuration.locale = locale;
        super.onConfigurationChanged(configuration);
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        // Save data to shared preference

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("language", lang);
        editor.apply();

    }

    // Load language saved in shared preference
    public void loadLocale() {
        SharedPreferences pref = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = pref.getString("language", "");
        setLocale(language);

    }

    public void showAboutDialogue() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String string = getString(R.string.about);
        alertDialog.setTitle("\t\t\t\t\t\t" + string +"\n\n\n\n\n");
        final TextView result = new TextView(SettingsActivity.this);

        result.setText(R.string.about_description);
        result.setTextSize(17.0f);
        result.setWidth(2);
//        result.setTextColor(Color.LTGRAY);

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(result);

        alertDialog.setView(ll);

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                //ACTION
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

}
