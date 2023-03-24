package org.techtown.gps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import org.techtown.gps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    // 권한 목록
    String[] permissionList = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    // 위치 측정 리스너
    GPSListener gpsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        requestPermissions(permissionList, 0);
    }
    //권한 확인이 완료되면 호출되는 메서드

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int r1 : grantResults) {
            if (r1 == PackageManager.PERMISSION_DENIED) {
                return;
            }
        }
        // 위치 정보를 관리하는 매니저를 추출한다.
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 기존에 측정하여 저장되어 있는 위치 정보를 가져온다.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //추출한 기존에 측정된 위치 정보를 출력한다.
        if (location1 != null) {
            showInfo(location1);
        } else if (location2 != null) {
            showInfo(location2);
        }
        gpsListener = new GPSListener();

        Button1CLickListener button1CLickListener = new Button1CLickListener();
        activityMainBinding.button.setOnClickListener(button1CLickListener);

        Button2ClickListener button2ClickListener = new Button2ClickListener();
        activityMainBinding.button2.setOnClickListener(button2ClickListener);

    }

    // 현재 위치 정보를 출력하는 함수
    public void showInfo(Location location) {
        if (location != null) {
            activityMainBinding.textView.setText("Provider : " + location.getProvider());
            activityMainBinding.textView2.setText("위도 : " + location.getLatitude());
            activityMainBinding.textView3.setText("경도 : " + location.getLatitude());
        }
    }

    // 위치 측정이 성공하면 동작할 리스너
    class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            showInfo(location);
        }
    }

    class Button1CLickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // 위치 측정 매니저 추출
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            //GPS 프로바이더가 사용 가능 하다면 리스너를 연결한다.
            if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER) == true) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, gpsListener);
            }
            // NETWORK 프로바이더가 사용 가능 하다면 리스너를 연결한다.
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, gpsListener);
            }
        }
    }

    class Button2ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // 리스너 연결을 해제 해 준다.
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.removeUpdates(gpsListener);
        }
    }
}