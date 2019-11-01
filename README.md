# rc-s3-uploader
A library which helps developer to upload image and video in one line of code just by implementing rc-s3-uploader in your any android project.  

# Manifest

Go to your manifest and paste it.
```groovy
<service
     android:name="com.amazonaws.mobileconnectors.s3.transferutility.TransferService"
     android:enabled="true" />
```

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
    implementation 'com.github.developerxrahul:amazon-s3-android-uploader:v1.4'
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
        rcS3Uploader.uploadImageTos3(MainActivity.this, /*Context*/
        poolId, /*Replace with your client pool Id*/
        Region.ap-south-1, /*Replace with your s3 bucket Region*/
        bucketName, /*Replace with your s3 bucket name*/
        imageUri); /*Uri of image which you wish to upload*/
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

        rcS3Uploader.uploadImageTos3(MainActivity.this, /*Context*/
        poolId, /*Replace with your client pool Id*/
        Region.ap-south-1, /*Replace with your s3 bucket Region*/
        bucketName, /*Replace with your s3 bucket name*/
        imageUri); /*Uri of image which you wish to upload*/
    }
}
 ```
 
 # Licence
```
Copyright (c) 2019 Rahul Chourasia

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
