package com.example.s3example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.s3example.aws.S3Uploader;
import com.example.s3example.aws.S3Utils;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Button bt_upload, bt_select;
    ImageView image;
    S3Uploader s3uploaderObj;
    String urlFromS3 = null;
    Uri imageUri;
    private String TAG = MainActivity.class.getCanonicalName();
    private int SELECT_PICTURE = 1;
    ProgressDialog progressDialog;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        s3uploaderObj = new S3Uploader(MainActivity.this);
        progressDialog = new ProgressDialog(MainActivity.this);

        bt_upload = findViewById(R.id.bt_upload);
        bt_select = findViewById(R.id.bt_select);
        image = findViewById(R.id.image);

        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStoragePermissionGranted();
            }
        });

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 0) {
                    uploadImageTos3(imageUri);
                }else{
                    Toast.makeText(MainActivity.this, "Choose image first", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                chooseImage();
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
            Log.v(TAG, "Permission is granted");
            chooseImage();
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            chooseImage();
            Log.e(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        } else {
            Log.e(TAG, "Please click again and select allow to choose profile picture");
        }
    }

    public void OnPictureSelect(Intent data) {
        imageUri = data.getData();
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (imageStream != null) {
            count++;
            image.setImageBitmap(BitmapFactory.decodeStream(imageStream));
        }
    }

    private String getFilePathFromURI(Uri selectedImageUri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(selectedImageUri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        assert cursor != null;
        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;

    }

    private void uploadImageTos3(Uri imageUri) {
        final String path = getFilePathFromURI(imageUri);
        if (path != null) {
            showLoading();
            s3uploaderObj.initUpload(path);
            s3uploaderObj.setOns3UploadDone(new S3Uploader.S3UploadInterface() {
                @Override
                public void onUploadSuccess(String response) {
                    if (response.equalsIgnoreCase("Success")) {
                        hideLoading();
                        urlFromS3 = S3Utils.generates3ShareUrl(getApplicationContext(), path);
                        if(!TextUtils.isEmpty(urlFromS3)) {
                            Toast.makeText(MainActivity.this, "Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onUploadError(String response) {
                    hideLoading();
                    Log.e(TAG, "Error Uploading");

                }
            });
        }else{
            Toast.makeText(this, "Null Path", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage("Uploading Image !!");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                OnPictureSelect(data);
            }
        }
    }
}
