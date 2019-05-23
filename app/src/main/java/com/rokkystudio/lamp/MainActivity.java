package com.rokkystudio.lamp;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.rokkystudio.lamp.fragments.FragmentPlayer;

// TODO ========================================
// - Хранить информацию о плейлистах и треках в SQLITE.
// Это позволит загружать обложки из сети, вести статистику популярных песен,
// хранить название песни и имя исполнителя и др. Сравнивать песни по MD5
// TODO ========================================

public class MainActivity extends Activity
{
    private static final int CALLBACK_PERMISSIONS_READ_STORAGE  = 1;
    private static final int CALLBACK_PERMISSIONS_WRITE_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) return;

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        if (CheckPermissions()) showPlayerFragment();
    }

    private void showPlayerFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main_frame, new FragmentPlayer());
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        if (requestCode == CALLBACK_PERMISSIONS_READ_STORAGE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (CheckPermissions()) showPlayerFragment();
            } else {
                Toast.makeText(this, "Нет прав на чтение внешней SD карты", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        else if (requestCode == CALLBACK_PERMISSIONS_WRITE_STORAGE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (CheckPermissions()) showPlayerFragment();
            } else {
                Toast.makeText(this, "Нет прав на запись на внешнюю SD карту", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean CheckPermissions()
    {
        // if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))

        // В версии Android 22 и ниже разрешения предоставляются без запроса
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CALLBACK_PERMISSIONS_READ_STORAGE);
            return false;
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CALLBACK_PERMISSIONS_WRITE_STORAGE);
            return false;
        }
        return true;
    }
}
