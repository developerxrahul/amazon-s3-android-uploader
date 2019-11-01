package com.example.aws_s3_android_uploader_library.aws;

import android.content.Context;
import android.util.Log;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;

import java.io.File;
import java.net.URL;


public class S3Utils {

    /**
     * Method to generate a presignedurl for the image
     * @param applicationContext context
     * @param path image path
     * @return presignedurl
     */
    public static String generates3ShareUrl(Context applicationContext, String path) {

        File f = new File(path);
        AmazonS3 s3client = AmazonUtil.getS3Client(applicationContext);

        ResponseHeaderOverrides overrideHeader = new ResponseHeaderOverrides();
        String mediaUrl = f.getName();
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(AWSKeys.BUCKET_NAME, mediaUrl);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET); // Default.
        generatePresignedUrlRequest.setResponseHeaders(overrideHeader);

        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
        Log.e("s", url.toString());
        return url.toString();
    }
}
