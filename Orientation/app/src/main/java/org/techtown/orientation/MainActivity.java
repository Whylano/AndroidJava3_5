package org.techtown.orientation;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import org.techtown.orientation.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    // 가속도 센서로 측정한 값을 담을 배열
    float[] accValue = new float[3];
    // 자기장 센서로 측정한 값을 담을 배열
    float[] magValue = new float[3];

    // 센서로 부터 값이 측정되었는지...
    boolean isGetAcc = false;
    boolean isGetMag = false;


    // 각 센서의 리스너
    AccelerometerSensorListener accelerometerSensorListener;
    MagneticSensorListener magneticSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Button1CLickListener button1CLickListener = new Button1CLickListener();
        activityMainBinding.button.setOnClickListener(button1CLickListener);

        Buttton2ClickListener buttton2ClickListener = new Buttton2ClickListener();
        activityMainBinding.button2.setOnClickListener(buttton2ClickListener);
    }

    // 가속도 센서의 리스너
    class AccelerometerSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // 가속도 센서로 측정된 값을 배열에 담는다.
            accValue[0] = sensorEvent.values[0];
            accValue[1] = sensorEvent.values[1];
            accValue[2] = sensorEvent.values[2];

            isGetAcc = true;

            getAzimuth();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    // 자기장 센서의 리스너
    class MagneticSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // 자기장 센서로 측정된 값을 배열에 담는다.
            magValue[0] = sensorEvent.values[0];
            magValue[1] = sensorEvent.values[1];
            magValue[2] = sensorEvent.values[2];

            isGetMag = true;

            getAzimuth();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    }

    // 방위값을 계산하는 함수
    public void getAzimuth() {
        // 두 센서 모두 값이 측정된 적이 없다면...
        if (isGetAcc == true && isGetMag == true) {
            // 방위값 등을 계산하기 위한 계산 행렬
            float[] R = new float[9];
            float[] I = new float[9];
            //계산 행렬 값을 구한다.
            SensorManager.getRotationMatrix(R, I, accValue, magValue);
            //계산 행렬을 이용하여 방위값을 추출한다.
            float[] values = new float[3];
            SensorManager.getOrientation(R, values);

            // 결과가 라디언으로 나오기 떄문에 각도 값으로 변환한다.
            double azimuth = Math.toDegrees(values[0]);
            double pitch = Math.toDegrees(values[1]);
            double roll = Math.toDegrees(values[2]);
            // 만약 방위값이 음수로 나온다면 360을 더해준다
            if (azimuth < 0) {
                azimuth = azimuth + 360;
            }
            activityMainBinding.textView.setText("방위값 : " + azimuth);
            activityMainBinding.textView2.setText("좌우 기울기 값 : " + pitch);
            activityMainBinding.textView3.setText("앞뒤 기울기 값 : " + roll);

            // 이미지 뷰의 회전값
            float rotationValue = (float) (360 - azimuth);
            activityMainBinding.imageView.setRotation(rotationValue);
        }
    }

    class Button1CLickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

            // 가속도 센서와 자기장 센서 객체를 가져온다.
            Sensor accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Sensor magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

            //리스너 생성
            accelerometerSensorListener = new AccelerometerSensorListener();
            magneticSensorListener = new MagneticSensorListener();

            //센서에 리스너를 연결한다.
            sensorManager.registerListener(accelerometerSensorListener, accSensor, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(magneticSensorListener, magSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    class Buttton2ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

            sensorManager.unregisterListener(accelerometerSensorListener);
            sensorManager.unregisterListener(magneticSensorListener);
        }
    }
}