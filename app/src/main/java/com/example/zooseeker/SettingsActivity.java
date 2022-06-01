package com.example.zooseeker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

public class SettingsActivity extends AppCompatActivity {
    public static boolean checked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Switch settings = findViewById(R.id.settings_switch);
        SharedPreferences sharedPrefs = getSharedPreferences("settings", MODE_PRIVATE);
        settings.setChecked(sharedPrefs.getBoolean("ddirections", false));

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    public void onLaunchSettingsClicked(View view) {
        Switch settings = findViewById(R.id.settings_switch);

        if (settings.isChecked()) //if (tgpref) may be enough, not sure
        {
            SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
            editor.putBoolean("ddirections", true);
            editor.commit();
            checked = true;
        }

        else
        {
            SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
            editor.putBoolean("ddirections", false);
            editor.commit();
            checked = false;
        }

    }
}
