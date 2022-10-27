package com.example.asteroides;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Map;

public class PreferencesActivity extends AppCompatActivity {

  private static final String DEFAULT_MAX_PLAYERS = "1";
  private static final String DEFAULT_MAX_FRAGMENTS = "3";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings_activity);
    if (savedInstanceState == null) {
      getSupportFragmentManager()
              .beginTransaction()
              .replace(R.id.settings, new SettingsFragment())
              .commit();
    }
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }

  public boolean onOptionsItemSelected(@NonNull MenuItem item){
    if(item.getItemId() == android.R.id.home){
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }
  public static class SettingsFragment extends PreferenceFragmentCompat {
    private SharedPreferences sharedPrefs;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
      setPreferencesFromResource(R.xml.preferences, rootKey);
      PreferencesValidation();
    }

    private void PreferencesValidation(){
      sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
      SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
          try{
            Map<String, ?> map = prefs.getAll();
            switch (key){
              case "fragments":
                ValidateFragments(map.get(key), key);
                break;
              case "max_players":
                ValidateMaxPlayers(map.get(key), key);
                break;
            }
          }catch (Exception ex){
            Toast.makeText(getActivity().getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
          }
        };
      };

      sharedPrefs.registerOnSharedPreferenceChangeListener(listener);
    }

    private void ValidateFragments(Object value, String key){
      if (!value.toString().matches("^[0-9]+$")) {
        Toast.makeText(getActivity().getApplicationContext(), String.format("El valor %s no és válido", value), Toast.LENGTH_SHORT).show();
        ((EditTextPreference)findPreference(key)).setText(DEFAULT_MAX_FRAGMENTS);
      } else {
        int normalizedValue = Integer.parseInt(value.toString());
        if (normalizedValue < 3 || normalizedValue > 9){
          Toast.makeText(getActivity().getApplicationContext(), String.format("El valor %s no és válido", value), Toast.LENGTH_SHORT).show();
          ((EditTextPreference)findPreference(key)).setText(DEFAULT_MAX_FRAGMENTS);
        }
      }
    }

    private void ValidateMaxPlayers(Object value, String key){
      if (!value.toString().matches("^[0-9]+$")) {
        Toast.makeText(getActivity().getApplicationContext(), String.format("El valor %s no és válido", value), Toast.LENGTH_SHORT).show();
        ((EditTextPreference)findPreference(key)).setText(DEFAULT_MAX_PLAYERS);
      } else {
        int normalizedValue = Integer.parseInt(value.toString());
        if (normalizedValue > 4){
          Toast.makeText(getActivity().getApplicationContext(), String.format("El valor %s no és válido", value), Toast.LENGTH_SHORT).show();
          ((EditTextPreference)findPreference(key)).setText(DEFAULT_MAX_PLAYERS);
        }
      }
    }

  }
}