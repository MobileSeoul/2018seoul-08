package com.example.mj975.woder_woman.fragment;

import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.data.SafeHouse;
import com.example.mj975.woder_woman.service.DatabaseClient;
import com.example.mj975.woder_woman.service.SafePlaceIconGenerator;
import com.example.mj975.woder_woman.util.GPSUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import net.sharewire.googlemapsclustering.ClusterManager;

import java.io.IOException;
import java.util.List;

public class ServiceSafeHouseFragment extends Fragment implements OnMapReadyCallback {

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            latitude = (float) location.getLatitude();   //위도
            longitude = (float) location.getLongitude(); //경도
            System.out.println("test lng " + longitude);
            System.out.println("test lat " + latitude);
            myPosition = new LatLng(latitude, longitude);

            dialog.dismiss();
            gpsButton.setEnabled(true);
            clusterManager.setItems(list);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private float longitude;
    private float latitude;

    private ClusterManager<SafeHouse> clusterManager;
    private MapView mapView;
    private GoogleMap googleMap;
    private LatLng myPosition;

    private FloatingActionButton gpsButton;


    private ProgressDialog dialog;
    private List<SafeHouse> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_service_safe_house, container, false);
        new BackgroundTask().execute();

        MapsInitializer.initialize(getActivity().getApplicationContext());

        gpsButton = v.findViewById(R.id.find_location_button);
        gpsButton.setEnabled(false);

        mapView = v.findViewById(R.id.map_view);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(this);

        gpsButton.setOnClickListener(view -> {
            if (googleMap != null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
            }
        });


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("데이터를 읽어오는 중입니다.");
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        GPSUtil.DISABLE_GPS(locationListener);
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GPSUtil.ENABLE_GPS_INFO(getActivity(), locationListener);
        this.googleMap = googleMap;

        clusterManager = new ClusterManager<>(getActivity().getBaseContext(), this.googleMap);
        clusterManager.setIconGenerator(new SafePlaceIconGenerator<>(getContext(), null, BitmapDescriptorFactory.fromResource(R.drawable.safe_house)));
        LatLng SEOUL = new LatLng(37.56, 126.97);

        this.googleMap.setOnCameraIdleListener(clusterManager);
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                list = DatabaseClient.getInstance().getAllSafeHouse().execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
