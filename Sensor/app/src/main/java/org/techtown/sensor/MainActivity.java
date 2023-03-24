package org.techtown.sensor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import org.techtown.sensor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    // 조도 센서의 리스너
    LightSensorListener lightSensorListener;

    // 기압 센서의 리스너
    PressureSensorListener pressureSensorListener;
    // 근접 센서의 리스너 Proximation
    ProximitySensorListener proximitySensorListener;
    //자이로스코프 센서의 리스너
    GyroscopeSensorListener gyroscopeSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Button1ClcikListener button1ClcikListener = new Button1ClcikListener();
        activityMainBinding.button.setOnClickListener(button1ClcikListener);

        Button2ClickListener button2ClickListener = new Button2ClickListener();
        activityMainBinding.button2.setOnClickListener(button2ClickListener);

        Button3ClickListener button3ClickListener = new Button3ClickListener();
        activityMainBinding.button3.setOnClickListener(button3ClickListener);

        Button4ClickListener button4ClickListener = new Button4ClickListener();
        activityMainBinding.button4.setOnClickListener(button4ClickListener);

        Button5ClcikListener button5ClcikListener = new Button5ClcikListener();
        activityMainBinding.button5.setOnClickListener(button5ClcikListener);

        Button6ClickListener button6ClickListener = new Button6ClickListener();
        activityMainBinding.button6.setOnClickListener(button6ClickListener);

        Button7ClcikListener button7ClcikListener = new Button7ClcikListener();
        activityMainBinding.button7.setOnClickListener(button7ClcikListener);

        BUtton8ClickListener button8ClcikListener = new BUtton8ClickListener();
        activityMainBinding.button8.setOnClickListener(button8ClcikListener);
    }

    //조도 센서에 연결할 리스너
    class LightSensorListener implements SensorEventListener {
        // 센서로 부터 가져온 값이 변경되었을 때
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // 조도 값을 가지고 온다.
            float a1 = sensorEvent.values[0];
            activityMainBinding.textView.setText("주변 밝기 : " + a1 + " lux");

        }

        // 센서의 감도가 변화 되었을 떄 호출
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    class Button1ClcikListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            if (lightSensorListener == null) {
                // 리스너 객체를 생성한다.
                lightSensorListener = new LightSensorListener();
                // 센서들을 관리하는 객체를 추출한다
                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                //조도 센서에 연결할 수 있는 객체를 가져온다.
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                // 조도 센서와 리스너를 연결한다
                boolean chk = sensorManager.registerListener(lightSensorListener, sensor, SensorManager.SENSOR_DELAY_UI);
                activityMainBinding.textView.setText("조도 센서를 지원하지 않습니다");
            }
        }
    }

    class Button2ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (lightSensorListener != null) {
                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                // 센서에 등록된 리스너를 해제한다
                sensorManager.unregisterListener(lightSensorListener);

            }
        }
    }

    // 기압 센서에 연결할 리스너
    class PressureSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //현재 기압 값을 가지고 온다.
            float a1 = sensorEvent.values[0];

            activityMainBinding.textView.setText("현재 기압 : " + a1 + "millibar");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    class Button3ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (pressureSensorListener == null) {
                pressureSensorListener = new PressureSensorListener();

                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                // 기압 센서를 추출한다.
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
                //기압 센서와 리스너를 연결한다.
                boolean chk = sensorManager.registerListener(pressureSensorListener, sensor, SensorManager.SENSOR_DELAY_UI);

                if (chk == false) {
                    pressureSensorListener = null;
                    activityMainBinding.textView.setText("기압 센서를 지원하지 않습니다");
                }
            }
        }
    }

    class Button4ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (pressureSensorListener != null) {
                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                sensorManager.unregisterListener(pressureSensorListener);
            }
        }
    }

    // 근접 센서에 연결할 리스너
    class ProximitySensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // 물체와의 거리를 가져온다.
            float a1 = sensorEvent.values[0];
            activityMainBinding.textView.setText("물체와의 거리 " + a1 + "cm");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    class Button5ClcikListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (proximitySensorListener == null) {
                proximitySensorListener = new ProximitySensorListener();

                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                //근접 센서 객체를 가져온다.
                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                //근접 센서와 리스너를 연결한다.
                boolean chk = sensorManager.registerListener(proximitySensorListener, sensor, SensorManager.SENSOR_DELAY_UI);

                if (chk == false) {
                    proximitySensorListener = null;
                    activityMainBinding.textView.setText("근접 센서를 지원하지 않습니다");
                }
            }
        }
    }

    class Button6ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (proximitySensorListener != null) {
                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                sensorManager.unregisterListener(proximitySensorListener);
            }
        }
    }

    // 자이로 스코프 센서와 연결될 리스너
    class GyroscopeSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // X 축의 각속도
            float a1 = sensorEvent.values[0];
            // Y 축의 각속도
            float a2 = sensorEvent.values[1];
            // Z 축의 각속도
            float a3 = sensorEvent.values[2];

            activityMainBinding.textView.setText("X축의 각속도 : " + a1 + "\n");
            activityMainBinding.textView.append("Y축의 각속도 : " + a2 + "\n");
            activityMainBinding.textView.append("Z축의 각속도 : " + a3);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    class Button7ClcikListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (gyroscopeSensorListener == null) {
                gyroscopeSensorListener = new GyroscopeSensorListener();

                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

                Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

                //자이로스코프 센서 객체를 추출한다.
                boolean chk = sensorManager.registerListener(gyroscopeSensorListener, sensor, SensorManager.SENSOR_DELAY_UI);

                if (chk == false) {
                    gyroscopeSensorListener = null;
                    activityMainBinding.textView.setText("자이로스코프 센서를 지원하지 않습니다");
                }
                ;
            }
        }
    }

    class BUtton8ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (gyroscopeSensorListener != null) {
                SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                sensorManager.unregisterListener(gyroscopeSensorListener);
            }
        }
    }
}