package com.example.maheshpujala.sillymonks.Activities;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maheshpujala.sillymonks.Model.SessionManager;
import com.example.maheshpujala.sillymonks.Model.UserData;
import com.example.maheshpujala.sillymonks.R;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ContestActivity extends AppCompatActivity implements View.OnClickListener {

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "SillyMonks Contest";

    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview;
    private static final int REQUEST_WRITE_STORAGE = 112;
    public static final String KEY_USERID = "userId";
    public static final String KEY_SELFIE = "selfie_image";
    ProgressDialog progress;
    String cat_name="Blank";
    SessionManager session;
    List<UserData> userData;
    LinearLayout previewLayout;
    TextView toolbar_title;
    WebView contest_webview;
    EditText mobileNumber_holder;
    Button uploadPicture;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        contest_webview = (WebView) findViewById(R.id.contest_webview);

        toolbar_title= (TextView)findViewById(R.id.toolbar_title);
        toolbar_title.setText("Selfie Contest");
        previewLayout =(LinearLayout) findViewById(R.id.previewLayout);
        previewLayout.setVisibility(View.GONE);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        Button btnCapturePicture = (Button) findViewById(R.id.cameraButton);
        uploadPicture= (Button) findViewById(R.id.uploadPicture);
        btnCapturePicture.setOnClickListener(this);
        uploadPicture.setOnClickListener(this);
//        String yourFilePath = Environment.getExternalStorageDirectory() + "/selfie_contest/" + "index.html";

        contest_webview.getSettings().setJavaScriptEnabled(true);
        contest_webview.addJavascriptInterface(new WebAppInterface(this, getParent()), "Android");
//        contest_webview.loadUrl("file:///"+yourFilePath);
        contest_webview.loadUrl(getResources().getString(R.string.main_url)+"/selfie_contest.html");

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i("TAG", "Permission has been denied by user");

                } else {

                    Log.i("TAG", "Permission has been granted by user");

                }
            }
        }
    }

    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                contest_webview.setVisibility(View.GONE);
                previewLayout.setVisibility(View.VISIBLE);
                previewCapturedImage();
            }  else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
    /*
   * Display image from a path to ImageView
   */
    private void previewCapturedImage() {
        toolbar_title.setText(cat_name);
        try {

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath());
            Bitmap waterMarkedImage = waterMark(bitmap);
            imgPreview.setImageBitmap(waterMarkedImage);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        Log.e("mediaStorageDir ", "" + mediaStorageDir);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cameraButton:
                // capture picture
                int permissionCheck = ContextCompat.checkSelfPermission(ContestActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    makeRequest();
                }else{
                    captureImage();
                }
                break;
            case R.id.uploadPicture:
                session = new SessionManager(getApplicationContext());
                userData=session.getUserDetails();
                Log.e("userData.get(0).getMobileNo()","="+userData.get(0).getMobileNo());
                if(userData.get(0).getMobileNo() == null || userData.get(0).getMobileNo().length() < 10 ){
                    showDialogMobileNumber();
                }else{
                    uploadSelifeImage(fileUri.getPath());
                    contest_webview.setVisibility(View.VISIBLE);
                    previewLayout.setVisibility(View.GONE);
                }

                break;
        }
    }

    private void showDialogMobileNumber() {
        final Dialog dialog1 = new Dialog(ContestActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog);
        dialog1.setCancelable(false);

        Button yes = (Button) dialog1.findViewById(R.id.submit_button);
        Button no = (Button) dialog1.findViewById(R.id.cancel_button);
        mobileNumber_holder= (EditText) dialog1.findViewById(R.id.mobileNumber_holder);

        yes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mobileNumber_holder.getText().toString().length() == 10) {
                    session.createLoginSession(userData.get(0).getSmonksId(), userData.get(0).getId(),
                            userData.get(0).getFirstName(), userData.get(0).getLastName(),
                            mobileNumber_holder.getText().toString(), userData.get(0).getEmail(),
                            userData.get(0).getGender(), userData.get(0).getLoginType());
                    Log.e("CREATED SESSION","= true");
                    dialog1.dismiss();
                    uploadPicture.performClick();
                } else {
                    Toast.makeText(ContestActivity.this, "Please enter a valid ten digit mobile number", Toast.LENGTH_LONG).show();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    public Bitmap waterMark(Bitmap src) {
        Bitmap waterMarkImage;
        int w = src.getWidth();
        int h = src.getHeight();
        Log.e("width==="+w,"height==="+h);
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
if(w > 1280 || h> 1280){
    waterMarkImage = BitmapFactory.decodeResource(getResources(), R.drawable.watermark_big);
    canvas.drawBitmap(waterMarkImage,35, h-500, null);
    Log.e("waterMarkImage===","watermark_big");

}else{
     waterMarkImage = BitmapFactory.decodeResource(getResources(), R.drawable.watermark_small);
    canvas.drawBitmap(waterMarkImage,25, h-300, null);
    Log.e("waterMarkImage===","watermark_Small");

}
//        Bitmap selfieWaterMark = BitmapFactory.decodeResource(getResources(), R.drawable.selfie_watermark);

//        Bitmap resizedSillyWaterMark =  Bitmap.createScaledBitmap(sillymonksWaterMark,150,150,false);
//        Bitmap resizedSelfieWaterMark =  Bitmap.createScaledBitmap(selfieWaterMark,150,150,false);

//        canvas.drawBitmap(resizedSelfieWaterMark,w-300, h-300, paint);

//        Karbon=====E  /      width===480: height===640  3.2 mp  480 x 800     Karbon 4     kitkat  front width===240: height===320
//        Moto G               width===960: height===1280  5mp   720 x 1280     MOTO G 4.5  marshmallow
//        Emulator             width===640: height===480
//        Moto G3              width===720: height===1280  13mp   720 x 1280    /width===4160: height===3120
//        Moto e              width===1456: height===2592  5mp   540 x 960 p   /width===1944: height===2592   MOTO E  4.3 kitkat
//        Xioami           E/width===4608: height===3456  16mp
//        Xiaomi            width===1944: height===2592    8mp

        File nFile = new File(fileUri.getPath());
        try {
            nFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(nFile);
            result.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    private void uploadSelifeImage(final String picturePath) {
        session = new SessionManager(getApplicationContext());
        userData=session.getUserDetails();
        progress = new ProgressDialog(ContestActivity.this);
        progress.setTitle("Uploading");
        progress.setMessage("Please wait...");
        progress.show();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                File f = new File(picturePath) ;
//                    File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                String content_type  = getMimeType(f.getPath());

                String file_path = f.getAbsolutePath();
                OkHttpClient client = new OkHttpClient();
                RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                RequestBody request_body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("id",userData.get(0).getSmonksId())
                        .addFormDataPart("contest_category_name",cat_name)
                        .addFormDataPart("type",content_type)
                        .addFormDataPart("photo",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                        .addFormDataPart("mobile",userData.get(0).getMobileNo())
                        .build();
                Log.e("upload cat name",cat_name);

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(getResources().getString(R.string.main_url)+getResources().getString(R.string.contest_url))
                        .post(request_body)
                        .build();
Log.e("URLLLL=====",""+getResources().getString(R.string.main_url)+getResources().getString(R.string.contest_url));
                try {
                    okhttp3.Response response = client.newCall(request).execute();

                    if(!response.isSuccessful()){
                        throw new IOException("Error : "+response);
                    }

                    progress.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }
    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            Log.e("onBackPressed", "___________Entered__________");
        }

        return super.onOptionsItemSelected(item);
    }


    public class WebAppInterface {

        Context mContext;
        Activity mActivity;


        /**
         * Instantiate the interface and set the context
         */
        public WebAppInterface(Context c, Activity a) {
            mContext = c;
            mActivity = a;
        }



        @JavascriptInterface
        public void showCamera(String selfieType) {
            Log.e("received cat name",cat_name);
            int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                makeRequest();
            } else {
                cat_name = selfieType;
                Log.e("cat name update ",cat_name);

                captureImage();
            }
        }

    }

}

