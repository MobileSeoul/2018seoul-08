package com.example.mj975.woder_woman.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.data.Event;
import com.example.mj975.woder_woman.data.Toilet;
import com.example.mj975.woder_woman.fragment.CleanMapFragment;
import com.example.mj975.woder_woman.fragment.MainFragment;
import com.example.mj975.woder_woman.fragment.PersonFragment;
import com.example.mj975.woder_woman.fragment.ReportFragment;
import com.example.mj975.woder_woman.fragment.ServiceFragment;
import com.example.mj975.woder_woman.service.DatabaseClient;
import com.example.mj975.woder_woman.util.GPSUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {
    private LocationListener locationListener;
    private double longitude;
    private double latitude;

    private FragmentManager fm;
    private long pressedTime;
    private Fragment fragment;

    private ArrayList<Event> events;
    private ArrayList<Toilet> toilets;
    private ArrayList<Toilet> nearToilets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude(); //경도
                latitude = location.getLatitude();   //위도
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };

        GPSUtil.ENABLE_GPS_INFO(this, locationListener);

        new ToiletTask().execute();
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (getIntent().getParcelableArrayListExtra("EVENTS") != null)
            events = (ArrayList<Event>) getIntent().getSerializableExtra("EVENTS");

        if (getIntent().getParcelableArrayListExtra("NEAR") != null)
            nearToilets = (ArrayList<Toilet>) getIntent().getSerializableExtra("NEAR");


        if (f == null) {
            f = new MainFragment();
            Bundle bundle = new Bundle();
            if (events != null)
                bundle.putSerializable("EVENTS", events);
            if (nearToilets != null)
                bundle.putSerializable("NEAR", nearToilets);
            f.setArguments(bundle);
            fm.beginTransaction().add(R.id.fragment_container, f).commit();
        }

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = new Bundle();
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = new MainFragment();
                        bundle.putSerializable("EVENTS", events);
                        bundle.putSerializable("NEAR", nearToilets);
                        break;
                    case R.id.action_clean:
                        fragment = new CleanMapFragment();
                        bundle.putSerializable("TOILETS", toilets);
                        break;
                    case R.id.action_report:
                        fragment = new ReportFragment();
                        bundle.putDouble("LNG", longitude);
                        bundle.putDouble("LAT", latitude);
                        break;
                    case R.id.action_relieved:
                        fragment = new ServiceFragment();
                        break;
                    case R.id.action_person:
                        fragment = new PersonFragment();
                        break;
                }

                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
            return;
        }
        if (pressedTime == 0) {
            Toast.makeText(MainActivity.this, getString(R.string.string_close_warning), Toast.LENGTH_LONG).show();
            pressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if (seconds > 2000) {
                Toast.makeText(MainActivity.this, getString(R.string.string_close_warning), Toast.LENGTH_LONG).show();
                pressedTime = 0;
            } else {
                super.onBackPressed();
            }
        }
    }

    private class ToiletTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                toilets = DatabaseClient.getInstance().getAllToilets().execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }

    @Override
    public void onDestroy() {
        GPSUtil.DISABLE_GPS(locationListener);
        super.onDestroy();
    }
}
