package org.techtown.httpjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;
import org.techtown.httpjson.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    String serverAddress = "http://192.168.1.111:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Button1ClickListener button1ClickListener = new Button1ClickListener();
        activityMainBinding.button.setOnClickListener(button1ClickListener);
    }

    class JsonNetworkThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                // 서버에 접속해 input stream을 추출한다.
                String site = serverAddress + "/json.jsp";
                URL url = new URL(site);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();

                // 라인단위 입력을 통해 json 양식의 문자열 데이터를 모두 읽어온다.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String str = null;
                StringBuffer stringBuffer = new StringBuffer();

                do {
                    str = bufferedReader.readLine();
                    if (str != null) ;
                    {
                        stringBuffer.append(str);
                    }
                } while (str != null);

                String data = stringBuffer.toString();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityMainBinding.textView.setText("");
                    }
                });


                // 읽어온 문자열을 가지고, Json 배열을 생성한다.
                JSONArray root = new JSONArray(data);

                // 배열에 있는 객체의 수 만큼 반복한다
                for (int i = 0; i < root.length(); i++) {
                    JSONObject jsonObject = root.getJSONObject(i);

                    //데이터를 추출한다.
                    String data1 = jsonObject.getString("data1");
                    int data2 = jsonObject.getInt("data2");
                    double data3 = jsonObject.getDouble("data3");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activityMainBinding.textView.append("data1 : " + data1 + "\n");
                            activityMainBinding.textView.append("data2 : " + data2 + "\n");
                            activityMainBinding.textView.append("data3 : " + data3 + "\n\n");
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Button1ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            JsonNetworkThread jsonNetworkThread = new JsonNetworkThread();
            jsonNetworkThread.start();
        }
    }
}