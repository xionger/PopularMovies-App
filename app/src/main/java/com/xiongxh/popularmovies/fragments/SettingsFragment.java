package com.xiongxh.popularmovies.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.xiongxh.popularmovies.R;

public class SettingsFragment extends PreferenceFragmentCompat implements OnSharedPreferenceChangeListener {

    public static boolean mPreferenceChanged = false;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_settings);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        for (int i=0; i < count; i++){
            Preference p = prefScreen.getPreference(i);
            //Preference p = prefCategory.getPreference(i);

            if (!(p instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);

            }
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        Activity activity = getActivity();

        if (key.equals(getString(R.string.sorting_key))){

        }

        Preference preference = findPreference(key);
        if (null != preference){
            if (!(preference instanceof CheckBoxPreference)){
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);

                mPreferenceChanged = true;
            }
        }
    }

    private void setPreferenceSummary(Preference preference, String value){
        if (preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0){
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

}
