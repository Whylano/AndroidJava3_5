package org.techtown.httpxml;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import org.techtown.httpxml.databinding.ActivityMainBinding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    String serverAddress = "http://192.168.1.111:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Button1CLickListener button1CLickListener = new Button1CLickListener();
        activityMainBinding.button.setOnClickListener(button1CLickListener);
    }

    class XmlNetworkThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                // 접속할 주소
                String site = serverAddress + "/xml.jsp";
                URL url = new URL(site);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();

                // DDM 방식으로 XML 문서를 분석할 수 있는 (Parsing)객체를 생성한다
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(inputStream);

                // 전체를 가지고 있는 root 태그 객체를 가져온다
                Element root = document.getDocumentElement();

                // item 태그들을 가져온다.
                NodeList itemList = root.getElementsByTagName("item");

                // 텍스트뷰를 비운다
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityMainBinding.textView.setText("");
                    }
                });
                // item 태그의 수 만큼 반복한다.
                for (int i = 0; i < itemList.getLength(); i++) {
                    // i 번째 item태그 객체를 추출한다.
                    Element itemTag = (Element) itemList.item(i);

                    // item 태그안의 태그들을 가져온다.
                    NodeList dataList = itemTag.getElementsByTagName("data1");
                    NodeList data2List = itemTag.getElementsByTagName("data2");
                    NodeList data3List = itemTag.getElementsByTagName("data3");

                    // 0 번째 태그를 가져온다
                    Element data1Tag = (Element) dataList.item(0);
                    Element data2Tag = (Element) dataList.item(0);
                    Element data3Tag = (Element) dataList.item(0);

                    // 태그 내의 문자열 데이터를 추출한다
                    String data1 = data1Tag.getTextContent();
                    String data2 = data2Tag.getTextContent();
                    String data3 = data3Tag.getTextContent();

                    int data22 = Integer.parseInt(data2);
                    double data33 = Double.parseDouble(data3);

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

    class Button1CLickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            XmlNetworkThread xmlNetworkThread = new XmlNetworkThread();
            xmlNetworkThread.start();
        }
    }
}