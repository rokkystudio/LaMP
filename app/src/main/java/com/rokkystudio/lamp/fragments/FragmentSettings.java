package com.rokkystudio.lamp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.rokkystudio.lamp.R;
import com.rokkystudio.lamp.service.LampClient;
import com.rokkystudio.lamp.service.LampService;

public class FragmentSettings extends PreferenceFragment implements
    SharedPreferences.OnSharedPreferenceChangeListener
{
    private boolean mNeedSync = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onDestroy()
    {
        if (mNeedSync) {
            Intent intent = new Intent(getActivity(), LampService.class);
            intent.putExtra("COMMAND", "SYNC_MEDIA");
            getActivity().startService(intent);
        }

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
            .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
            .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if ( key.equals(getString(R.string.media_type_aiff)) ||
             key.equals(getString(R.string.media_type_ape)) ||
             key.equals(getString(R.string.media_type_mp2)) ||
             key.equals(getString(R.string.media_type_mp3)) ||
             key.equals(getString(R.string.media_type_ogg)) ||
             key.equals(getString(R.string.media_type_flac)) ) {
            mNeedSync = true;
        }
    }
}