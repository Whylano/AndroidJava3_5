package org.techtown.httpnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import org.techtown.httpnetwork.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    // 서버의 IP
    String serverIP = "172.20.10.4";
    // 서버의 Port 번호
    int serverPort = 8080;//아파치의 기본 포트 번호

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Button1ClickListener button1ClickListener = new Button1ClickListener();
        activityMainBinding.button.setOnClickListener(button1ClickListener);
    }

    // 네트워크 쓰레드
    class NetworkThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                //접속할 서버 주소
                String site = "http://" + serverIP + ":" + serverPort + "/basic.jsp";
                URL url = new URL(site);

                // 접속
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                // 스트림 추출
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String str = null;
                StringBuffer stringBuffer = new StringBuffer();

                //끝까지 읽어와 담아둔다.
                do {
                    str = bufferedReader.readLine();
                    if (str != null) {
                        stringBuffer.append(str + "\n");
                    }
                } while (str != null);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityMainBinding.textView.setText(stringBuffer.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Button1ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            NetworkThread networkThread = new NetworkThread();
            networkThread.start();
        }
    }
}