package org.techtown.getalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import org.techtown.getalbum.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    ActivityResultLauncher<Intent> albumActivityLauncher;

    String[] permissionList = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_MEDIA_LOCATION

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        requestPermissions(permissionList, 0);

        AlbumActivityCallback callback1 = new AlbumActivityCallback();
        ActivityResultContracts.StartActivityForResult contract1;
        contract1 = new ActivityResultContracts.StartActivityForResult();
        albumActivityLauncher = registerForActivityResult(contract1, callback1);

        Button1ClickListener button1ClickListener = new Button1ClickListener();
        activityMainBinding.button.setOnClickListener(button1ClickListener);
    }

    class AlbumActivityCallback implements ActivityResultCallback<ActivityResult> {

        @Override
        public void onActivityResult(ActivityResult result) {
            int resultCode = result.getResultCode();

            if (resultCode == RESULT_OK) {
                try {
                    // 선택할 이미지의 경로데이터를 관리하는 URI객체를 추출한다.
                    Intent data = result.getData();
                    Uri uri = data.getData();

                    // 컨텐츠 리졸버를 가져온다
                    ContentResolver contentResolver = getContentResolver();

                    if (uri != null) {
                        // 안드로이드 Q(10)버전 이상이라면
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            // 이미지 객체를 생성할 수 있는 디코더를 생성한다.
                            ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);

                            //Bitmap객체를 생성한다
                            Bitmap bitmap = ImageDecoder.decodeBitmap(source);

                            activityMainBinding.imageView.setImageBitmap(bitmap);
                        } else {
                            // 컨텐츠 프로바이더를 통해 이미지 데이터 정보를 가져온다
                            Cursor cursor = contentResolver.query(uri, null, null, null, null);
                            if (cursor != null) {
                                cursor.moveToNext();

                                // 이미지의 경로를 가져온다.
                                int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                                String source = cursor.getString(index);

                                // 이미지를 생성한다.
                                Bitmap bitmap = BitmapFactory.decodeFile(source);
                                activityMainBinding.imageView.setImageBitmap(bitmap);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Button1ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // 앨범에서 사진을 선택할 수 있도록 셋팅된 인텐트를 생성한다.
            Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // 실행할 액티비티의 타입을 설정(이미지를 선택할 수 있는 것)
            albumIntent.setType("image/*");
            // 선택할 파일의 타입을 지정(안드로이드 OS가 사전 작업을 할 수 있도록)
            String[] mimeType = {"image/*"};
            albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);

            // 액티비티 실행한다.
            albumActivityLauncher.launch(albumIntent);
        }
    }
}