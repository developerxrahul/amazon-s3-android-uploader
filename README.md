# rc-s3-uploader
A library which helps developer to upload image and video in one line of code just by implementing rc-s3-uploader in your any android project.  

# Gradle

Step 1. Add the JitPack repository to your build file
```groovy
allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```
Step 2. Add the dependency
```groovy
dependencies {
  implementation 'com.github.developerxrahul:rc-s3-uploader:1.1'
}
 ```
# Usage

For Image Upload
```groovy
public class MainActivity extends AppCompatActivity {

    RcS3Uploader rcS3Uploader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcS3Uploader.uploadImageTos3(MainActivity.this, imageUri); /*Uri of image which you wish to upload*/
    }
}
 ```
 For Video Upload

```groovy
public class MainActivity extends AppCompatActivity {

    RcS3Uploader rcS3Uploader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcS3Uploader.uploadVideoTos3(MainActivity.this, videoUri); /*Uri of video which you wish to upload*/
    }
}
 ```
