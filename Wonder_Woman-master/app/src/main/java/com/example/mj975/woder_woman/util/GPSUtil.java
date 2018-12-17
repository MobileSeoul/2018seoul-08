package com.example.mj975.woder_woman.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

public class GPSUtil {

//    private final LocationListener mLocationListener = new LocationListener() {
//        public void onLocationChanged(Location location) {
//            //여기서 위치값이 갱신되면 이벤트가 발생한다.
//            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.
//
//            Log.d("GPS_UTIL", "onLocationChanged, location:" + location);
//            double longitude = location.getLongitude(); //경도
//            double latitude = location.getLatitude();   //위도
//            double altitude = location.getAltitude();   //고도
//            float accuracy = location.getAccuracy();    //정확도
//            String provider = location.getProvider();   //위치제공자
//            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
//            //Network 위치제공자에 의한 위치변화
//            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
//            System.out.println("위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
//                    + "\n고도 : " + altitude + "\n정확도 : " + accuracy);
//        }
//
//        public void onProviderDisabled(String provider) {
//            Log.d("GPS_UTIL", "onProviderDisabled, provider:" + provider);
//        }
//
//        public void onProviderEnabled(String provider) {
//            Log.d("GPS_UTIL", "onProviderEnabled, provider:" + provider);
//        }
//
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            Log.d("GPS_UTIL", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
//        }
//    };

    private static LocationManager lm;

    public static void ENABLE_GPS_INFO(Activity activity, LocationListener listener) {
        // LocationManager 객체를 얻어온다
        if (lm == null)
            lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                100, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                listener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                100, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                listener);
    }

    public static void DISABLE_GPS(LocationListener listener) {
        lm.removeUpdates(listener);
    }
}
