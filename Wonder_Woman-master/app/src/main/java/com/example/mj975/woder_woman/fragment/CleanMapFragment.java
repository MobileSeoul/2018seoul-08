package com.example.mj975.woder_woman.fragment;

import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mj975.woder_woman.R;
import com.example.mj975.woder_woman.data.Toilet;
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

import java.util.ArrayList;

public class CleanMapFragment extends Fragment implements OnMapReadyCallback {

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();   //위도
            longitude = location.getLongitude(); //경도
            System.out.println("test lng " + longitude);
            System.out.println("test lat " + latitude);
            myPosition = new LatLng(latitude, longitude);

            dialog.dismiss();
            gpsButton.setEnabled(true);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private double longitude;
    private double latitude;

    ClusterManager<Toilet> clusterManager;
    private MapView mapView;
    private GoogleMap googleMap;
    private LatLng myPosition;

    private FloatingActionButton gpsButton;

    private ArrayList<Toilet> toilets;
    private Bundle bundle;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clean_map, container, false);
        bundle = getArguments();

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("데이터를 읽어오는 중입니다.");
        dialog.show();

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


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
//        LatLng SEOUL = new LatLng(latitude,longitude);
        GPSUtil.ENABLE_GPS_INFO(getActivity(), locationListener);
        this.googleMap = googleMap;

        clusterManager = new ClusterManager<>(getActivity().getBaseContext(), this.googleMap);
        clusterManager.setIconGenerator(new SafePlaceIconGenerator<>(getContext(),null, BitmapDescriptorFactory.fromResource(R.drawable.safe_place)));
        LatLng SEOUL = new LatLng(37.56, 126.97);

        this.googleMap.setOnCameraIdleListener(clusterManager);
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        if (bundle != null && bundle.getSerializable("TOILETS") != null) {
            toilets = (ArrayList<Toilet>) getArguments().getSerializable("TOILETS");

            clusterManager.setItems(toilets);
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }
}