package org.techtown.deviceinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;

import org.techtown.deviceinfo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    // 확인할 권한들
    String[] permission_list = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_NUMBERS

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        requestPermissions(permission_list, 0);

        Button1ClickListener button1ClickListener = new Button1ClickListener();
        activityMainBinding.button.setOnClickListener(button1ClickListener);

        Button2ClickListener button2ClickListener = new Button2ClickListener();
        activityMainBinding.button2.setOnClickListener(button2ClickListener);

        Button3ClickListener button3ClickListener = new Button3ClickListener();
        activityMainBinding.button3.setOnClickListener(button3ClickListener);
    }

    class Button1ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                activityMainBinding.textView.setText("권한을 허용해주세요");
                return;
            }

            //전화번호
            String a1 = telephonyManager.getLine1Number();
            //SIM국가 코드
            String a2 = telephonyManager.getSimCountryIso();
            // 모바일 국가 코드 + 모바일 네트워크 코드
            String a3 = telephonyManager.getSimOperator();
            // 서비스 이름
            String a4 = telephonyManager.getSimOperatorName();
            // SIM 상태(통신 가능여부, PIN LOCK 여부)
            int a5 = telephonyManager.getSimState();
            //음성 메일 번호
            String a6 = telephonyManager.getVoiceMailNumber();

            activityMainBinding.textView.setText("전화번호 : " + a1 + "\n");
            activityMainBinding.textView.append("SIM 국가 코드 : " + a2 + "\n");
            activityMainBinding.textView.append("모바일 국가 코드 + 모바일 네트워크 코드 : " + a3 + "\n");
            activityMainBinding.textView.append("서비스 이름 : " + a4 + "\n");
            activityMainBinding.textView.append("SIM 상태(통신 가능여부, PIN Lock 여부 등) : " + a5 + "\n");
            activityMainBinding.textView.append("음성 메일 번호 : " + a5 + "\n");

        }
    }

    class Button2ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            activityMainBinding.textView.setText("보드 이름 : " + Build.BOARD + "\n");
            activityMainBinding.textView.append("소프트웨어를 커스터마이징한 회사 : " + Build.BRAND + "\n");
            activityMainBinding.textView.append("제조사 디자인 명 : " + Build.DEVICE + "\n");
            activityMainBinding.textView.append("사용자에게 표시되는 빌드 ID : " + Build.DISPLAY + "\n");
            activityMainBinding.textView.append("빌드 고유 ID : " + Build.FINGERPRINT + "\n");
            activityMainBinding.textView.append("ChangeList 번호 : " + Build.ID + "\n");
            activityMainBinding.textView.append("제품/하드웨어 제조업체 : " + Build.MANUFACTURER + "\n");
            activityMainBinding.textView.append("제품 모델명 : " + Build.MODEL + "\n");
            activityMainBinding.textView.append("제품명 : " + Build.PRODUCT + "\n");
            activityMainBinding.textView.append("빌드 구분 : " + Build.TAGS + "\n");
            activityMainBinding.textView.append("빌드 타입 : " + Build.TYPE + "\n");
            activityMainBinding.textView.append("안드로이드 버전 : " + Build.VERSION.RELEASE);
        }
    }

    class Button3ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // 해상도 정보를 가지고 있는 객체를 추출한다.
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            // 안드로이드 버전으로 분기한다.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // 단말기 해상도 정보를 추출한다.
                WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();

                // 해상도 정보를 가지고 있는 객체를 추출한다.
                Rect rect = windowMetrics.getBounds();
                // 가로길이를 추출한다.
                int width = rect.width();
                // 세로길이를 추출한다.
                int height = rect.height();

                activityMainBinding.textView.setText("width : " + width + "\n");
                activityMainBinding.textView.append("height : " + height + "\n");
            } else {
                // 메인화면 관련 객체를 가져온다.
                Display display = windowManager.getDefaultDisplay();

                // 해상도 정보를 담을 객체를 추출한다.
                Point point = new Point();
                // 가로 세로 길이를 담는다.
                display.getSize(point);

                activityMainBinding.textView.setText("width : " + point.x + "\n");
                activityMainBinding.textView.append("height : " + point.y + "\n");
            }
        }
    }
}