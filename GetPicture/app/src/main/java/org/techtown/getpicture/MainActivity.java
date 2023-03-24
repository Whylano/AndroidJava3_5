package org.techtown.getpicture;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import org.techtown.getpicture.databinding.ActivityMainBinding;

import java.io.File;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    ActivityResultLauncher<Intent> basicCameraLauncher;

    ActivityResultLauncher<Intent> originalCameraLauncher;

    // 파일이 저장될 위치
    String file_path;
    // 저장된 파일에 접근하기 위한 URI
    Uri contentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());


        // 기본 카메라 액티비티 실행 설정
        BasicCameraActivityCallback callback1 = new BasicCameraActivityCallback();
        ActivityResultContracts.StartActivityForResult contract1 = new ActivityResultContracts.StartActivityForResult();
        basicCameraLauncher = registerForActivityResult(contract1, callback1);

        // 원본 카메라 액티비티 실행 설정
        OriginalCameraActivityCallback callback2 = new OriginalCameraActivityCallback();
        ActivityResultContracts.StartActivityForResult contract2 = new ActivityResultContracts.StartActivityForResult();
        originalCameraLauncher = registerForActivityResult(contract2, callback2);


        Button1ClickListener button1ClickListener = new Button1ClickListener();
        activityMainBinding.button.setOnClickListener(button1ClickListener);

        Button2ClickListener button2ClickListener = new Button2ClickListener();
        activityMainBinding.button2.setOnClickListener(button2ClickListener);


        // 애플리케이션을 위한 외부 저장소 경로를 가져온다
        file_path = getExternalFilesDir(null).toString();

    }


    class BasicCameraActivityCallback implements ActivityResultCallback<ActivityResult> {

        @Override
        public void onActivityResult(ActivityResult result) {
            // 액티비티 작업 결과를 가져온다
            int resultCode = result.getResultCode();

            if (resultCode == RESULT_OK) {
                // Intent 객체를 추출한다.
                Intent data = result.getData();
                // 촬영한 이미지 데이터를 가져온다.
                Bitmap bitmap = (Bitmap) data.getParcelableExtra("data");
                activityMainBinding.imageView.setImageBitmap(bitmap);
            }
        }
    }

    class Button1ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            basicCameraLauncher.launch(intent);
        }
    }

    class OriginalCameraActivityCallback implements ActivityResultCallback<ActivityResult> {

        @Override
        public void onActivityResult(ActivityResult result) {
            int resultCode = result.getResultCode();
            if (resultCode == RESULT_OK) {
                // Uri를 이용해 이미지에 접근하여 BItmap객체를 생성한다.
                Bitmap bitmap = BitmapFactory.decodeFile(contentUri.getPath());

                // 현재 촬영한 이미지의 회전 각도를 구한다.
                int degree = getDegree(contentUri);

                //이미지의 사이즈를 조정한다.
                Bitmap bitmap2 = resizeBitmap(1824, bitmap);

                // 이미지를 회전시킨다.
                Bitmap bitmap3 = rotateBitmap(bitmap2, degree);
                activityMainBinding.imageView.setImageBitmap(bitmap3);

                // 사진 파일을 삭제한다.
                File file = new File(contentUri.getPath());
                file.delete();
            }
        }
    }

    class Button2ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // 촬영한 사진이 저장될 파일 이름
            long now = System.currentTimeMillis();
            String file_name = "/temp_" + now + ".jpg";
            // 경로 + 파일이름
            String pic_path = file_path + "/" + file_name;

            File file = new File(pic_path);

            // 사진이 저장될 위치를 관리하는 Uri 객체를 생성
            contentUri = FileProvider.getUriForFile(MainActivity.this, "androidx.core.content.FileProvider", file);

            if (contentUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                originalCameraLauncher.launch(intent);
            }
        }
    }

    // 이미지의 회전 각도를 구하는 메서드
    public int getDegree(Uri uri) {
        // 사진의 정보를 가지고 있는 객체를 담을 변수
        ExifInterface exifInterface = null;
        try {
            // 안드로이드 Q이상 이라면..
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                // 이미지 데이터를 가져올 수 있는 컨텐츠 프로바이더의 Uri를 추출한다
                Uri photoUri = MediaStore.setRequireOriginal(uri);
                // 컨텐츠 프로바이더를 가져온다
                ContentResolver contentResolver = getContentResolver();
                // 스트림을 추출한다.
                InputStream inputStream = contentResolver.openInputStream(photoUri);
                // ExifInterface 정보를 읽어온다.
                exifInterface = new ExifInterface(inputStream);
            } else {
                exifInterface = new ExifInterface(uri.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (exifInterface != null) {
            // 반환할 각도값을 담을 변수
            int degree = 0;
            // ExifInterface에서 회전 각도값을 가져온다
            int ori = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
            return degree;
        } else {
            return 0;
        }
    }

    // 이미지의 사이즈를 조정하는 메서드
    public Bitmap resizeBitmap(int targetWidth, Bitmap source) {
        // 이미지의 축소/확대 배율을 구한다.
        double ratio = (double) targetWidth / (double) source.getWidth();

        //세로 길이를 구한다.
        int targetHeight = (int) (source.getHeight() * ratio);

        //크기를 조정할 Bitmap을 생성한다.
        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);

        return result;
    }

    // 회전
    public Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        // 회전 이미지를 생성하기 위한 변환 행렬
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        // 회전 행렬을 적용하여 회전된 이미지를 생성한다.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        return result;
    }
}