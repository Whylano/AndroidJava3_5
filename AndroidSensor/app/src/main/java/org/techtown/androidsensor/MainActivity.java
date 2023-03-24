package org.techtown.androidsensor;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import org.techtown.androidsensor.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        // 센서를 관리하는 매니저를 추출한다.
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // 단말기에 있는 센서 목록을 가져온다.
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        activityMainBinding.textView.setText("");

        // 센서의 수 만큼 반복한다.
        for (Sensor sensor : sensorList) {
            activityMainBinding.textView.append("센서 이름 : " + sensor.getName() + "\n");
            activityMainBinding.textView.append("센서 종류 : " + sensor.getType() + "\n\n");
        }
    }
}