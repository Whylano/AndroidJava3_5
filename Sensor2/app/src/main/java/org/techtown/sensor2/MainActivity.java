package org.techtown.sensor2;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import org.techtown.sensor2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    //가속도 센서 리스너
    AccelerometersensorListener accelerometersensorListener;
    //자기장 센서 리스너
    MagneticSensorListener magneticSensorListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        Button1ClickListener button1ClickListener = new Button1ClickListener();
        activityMainBinding.button.setOnClickListener(button1ClickListener);

        Button2ClickListener button2ClickListener = new Button2ClickListener();
        activityMainBinding.button2.setOnClickListener(button2ClickListener);

        Button3ClickListener button3ClickListener = new Button3ClickListener();
        activityMainBinding.button3.setOnClickListener(button3ClickListener);

        Button4ClickListener button4ClickListener = new Button4ClickListener();
        activityMainBinding.button4.setOnClickListener(button4ClickListener);
    }


    // 가속도 센서에 연결할 리스너
    class AccelerometersensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // X축 기울기
            float a1 = sensorEvent.values[0];
            // Y축 기울기
            float a2 = sensorEvent.values[1];
            // Z축 기울기
            float a3 = sensorEvent.values[2];

            activityMainBinding.textView.setText("X축 기울기 : " + a1 + "\n");
            activityMainBinding.textView.append("Y축 기울기 : " + a2 + '\n');
            activityMainBinding.textView.append("Z축 기울기 : " + a3 + "\n");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    class Button1ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (accelerometersensorListener == null) {
                accelerometersensorListener = new AccelerometersensorListener();

                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

                //가속도 센서를 가져온다.
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

                //가속도 센서와 리스너를 연결한다.
                boolean chk = sensorManager.registerListener(accelerometersensorListener, sensor, SensorManager.SENSOR_DELAY_UI);

                if (chk == false) {
                    accelerometersensorListener = null;
                    activityMainBinding.textView.setText("가속도 센서를 지원하지 않습니다");
                }
            }
        }
    }

    class Button2ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (accelerometersensorListener != null) {
                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                sensorManager.unregisterListener(accelerometersensorListener);
            }
        }
    }

    // 자기장 센서와 연결될 리스너
    class MagneticSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // X축 주변 자기장
            float a1 = sensorEvent.values[0];

            // Y축 주변 자기장
            float a2 = sensorEvent.values[1];
            // Z축 주변 자기장
            float a3 = sensorEvent.values[2];

            activityMainBinding.textView.setText("X축 주변 자기장 : " + a1 + "" + "\n");
            activityMainBinding.textView.append("Y축 주변 자기장 : " + a2 + "\n");
            activityMainBinding.textView.append("Z축 주변 자기장 : " + a3);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    class Button3ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (magneticSensorListener == null) {
                magneticSensorListener = new MagneticSensorListener();

                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                // 자기장 센서 객체를 가져온다.
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                // 자기장 센서에 리스너를 연결한다.
                boolean chk = sensorManager.registerListener(magneticSensorListener, sensor, SensorManager.SENSOR_DELAY_UI);

                if (chk == false) {
                    magneticSensorListener = null;
                    activityMainBinding.textView.setText("자기장 센서를 지원하지 않습니다");
                }
            }
        }
    }

    class Button4ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (magneticSensorListener != null) {
                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                sensorManager.unregisterListener(magneticSensorListener);
            }
        }
    }
}