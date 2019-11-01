package com.example.aws_s3_android_uploader_library;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.regions.Regions;
import com.example.aws_s3_android_uploader_library.aws.S3Uploader;
import com.example.aws_s3_android_uploader_library.aws.S3Utils;

public class RcS3Uploader {

    public static S3Uploader s3uploaderObj;

    public static String TAG = RcS3Uploader.class.getCanonicalName();

    public static void uploadImageTos3(final Context context, final String poolId, final Regions regions, final String bucketName, Uri imageUri) {

        s3uploaderObj = new S3Uploader(context, poolId, regions);
        final String path = getFilePathFromURI(imageUri, context);
        if (path != null) {
            s3uploaderObj.initUpload(bucketName, path);
            s3uploaderObj.setOns3UploadDone(new S3Uploader.S3UploadInterface() {
                @Override
                public void onUploadSuccess(String response) {
                    if (response.equalsIgnoreCase("Success")) {
                        String urlFromS3 = S3Utils.generates3ShareUrl(context, poolId, regions, bucketName, path);
                        if(!TextUtils.isEmpty(urlFromS3)) {
                            Log.e(TAG, "Status : File Uploaded :" +urlFromS3);
                        }
                    }
                }

                @Override
                public void onUploadError(String response) {
                    Log.e(TAG, "Status : Error Uploading");

                }
            });
        }else{
            Log.e(TAG, "Status : Invalid file URI");
        }
    }

    public static void uploadVideoTos3(final Context context,final String poolId,final Regions regions,final String bucketName, Uri imageUri) {

        s3uploaderObj = new S3Uploader(context, poolId, regions);
        final String path = getFilePathFromURI(imageUri, context);
        if (path != null) {
            s3uploaderObj.initVideoUpload(bucketName, path);
            s3uploaderObj.setOns3UploadDone(new S3Uploader.S3UploadInterface() {
                @Override
                public void onUploadSuccess(String response) {
                    if (response.equalsIgnoreCase("Success")) {
                        String urlFromS3 = S3Utils.generates3ShareUrl(context, poolId, regions, bucketName, path);
                        if(!TextUtils.isEmpty(urlFromS3)) {
                            Log.e(TAG, "Status : File Uploaded :" +urlFromS3);
                        }
                    }
                }

                @Override
                public void onUploadError(String response) {
                    Log.e(TAG, "Status : Error Uploading");

                }
            });
        }else{
            Log.e(TAG, "Status : Invalid file URI");
        }
    }

    public static String getFilePathFromURI(Uri selectedImageUri, Context context) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(selectedImageUri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        assert cursor != null;
        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;

    }
}
