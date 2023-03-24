package org.techtown.httpmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.techtown.httpmedia.databinding.ActivityMainBinding;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    //서버 주소 및 포트 번호
    String serverAddress = "http://172.20.10.4:8080";

    // 사운드 재생을 위한 객체
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        Button1ClickListener button1ClickListener = new Button1ClickListener();
        activityMainBinding.button.setOnClickListener(button1ClickListener);

        Button2ClickListener button2ClickListener = new Button2ClickListener();
        activityMainBinding.button2.setOnClickListener(button2ClickListener);

        Button3ClcikListener button3ClcikListener = new Button3ClcikListener();
        activityMainBinding.button3.setOnClickListener(button3ClcikListener);

        Button4ClickListener button4ClickListener = new Button4ClickListener();
        activityMainBinding.button4.setOnClickListener(button4ClickListener);

        Button5ClcikListener button5ClcikListener = new Button5ClcikListener();
        activityMainBinding.button5.setOnClickListener(button5ClcikListener);
    }

    // 이미지 데이터를 받아오는 쓰레드
    class ImageNetworkThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                URL url = new URL(serverAddress + "/img.android.jpg");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();

                // Bitmap 객체를 생성한다.
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityMainBinding.imageView.setImageBitmap(bitmap);
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
            ImageNetworkThread imageNetworkThread = new ImageNetworkThread();
            imageNetworkThread.start();
        }
    }

    class Button2ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mediaPlayer == null) {
                // 사운드 파일의 주소
                Uri uri = Uri.parse(serverAddress + "/song.mp3");
                // MediaPlayer에 사운드 파일 주소를 설정한다.
                mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
                // 사운드 재생
                mediaPlayer.start();
            }
        }
    }

    class Button3ClcikListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mediaPlayer != null) {
                // 사운드 재생을 정지한다.
                mediaPlayer.stop();
                mediaPlayer = null;
            }
        }
    }

    class Button4ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // 영상 재생중이 아니라면..
            if (activityMainBinding.videoView.isPlaying() == false) {
                // 재생할 영상의 주소
                Uri uri = Uri.parse(serverAddress + "/video.mp4");
                // VideoView에 재생할 영상의 주소를 셋팅한다.
                activityMainBinding.videoView.setVideoURI(uri);
                // 재생한다
                activityMainBinding.videoView.start();
            }
        }
    }

    class Button5ClcikListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // 재생 중이라면...
            if (activityMainBinding.videoView.isPlaying() == false) {
                //영상 재생을 중지한다.
                activityMainBinding.videoView.stopPlayback();
            }
        }
    }
}