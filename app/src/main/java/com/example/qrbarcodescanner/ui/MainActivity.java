package com.example.qrbarcodescanner.ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.qrbarcodescanner.R;
import com.example.qrbarcodescanner.db.DBManager;
import com.example.qrbarcodescanner.model.Code;
import com.example.qrbarcodescanner.utils.ActivityUtils;
import com.example.qrbarcodescanner.utils.DialogCustom;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.Result;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_HISTORY = 3;
    private static final int REQUEST_CODE_GENERATE = 4;
    private static final int REQUEST_CODE_SCANPHOTO = 5;
    private static final int REQUEST_CODE_SAVED = 6;
    private static final int REQUEST_CODE_SCANPHOTO1 = 7;
    DrawerLayout drawer;
    private boolean mPermissionGranted;
    NavigationView navigationView;
    DialogCustom dialog;
    private boolean isFlashOn = false;
    private boolean hasFlash;
    private Camera camera;
    private Parameters params;
    private final int SPLASH_DELAY = 3000;
    public static int ACTIVITY_CODE_RESULT = 1;
    ImageView opennav, flash, logo, closenav;
    LinearLayout generate, saved, scanphoto, history;
    CodeScanner codeScanner;
    CodeScannerView scannerView;
    DBManager dbManager;
    AdView ad1;
    private static final int RC_PERMISSION = 10;
    public static final String RESULT = "RESULT";
    LinearLayout historymain, upgrade, feedback, checkforupdate, policy, otherapps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
        ButterKnife.bind(this);
        Init();
        Log.e("sssss", "onCreate:requestPermissions " );

        dbManager = new DBManager(this);

        ObjectAnimator flip = ObjectAnimator.ofFloat(logo, "rotationY", 0f, 360f); // or rotationX
        flip.setDuration(3000); // 3 seconds
        flip.setRepeatCount(ValueAnimator.INFINITE);
        flip.start();

        codeScanner.startPreview();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                mPermissionGranted = false;
                requestPermissions(new String[]{Manifest.permission.CAMERA}, RC_PERMISSION);
            } else {
                mPermissionGranted = true;
            }
        } else {
            mPermissionGranted = true;
        }
        codeScanner.setAutoFocusInterval(200);
        codeScanner.setDecodeCallback(new DecodeCallback() {

            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                        String s = result.getText();
                        Date date = new Date();
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                        String time = formatter.format(date);
                        dbManager.addQrCodeHistory(new Code(time, "Phone", s));
                        Log.d("fdsdf", s);
                        ActivityUtils.startActivityForResult(MainActivity.this, ResultActivity.class, s, ACTIVITY_CODE_RESULT);
                    }
                });
            }
        });

    }

    public void Init() {
        scannerView = findViewById(R.id.scan);
        codeScanner = new CodeScanner(this, scannerView);
        codeScanner.setFlashEnabled(true);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        opennav = findViewById(R.id.opennav);
        flash = findViewById(R.id.flash);
        logo = findViewById(R.id.logo);
        generate = findViewById(R.id.generate);
        saved = findViewById(R.id.saved);
        scanphoto = findViewById(R.id.scanphoto);
        history = findViewById(R.id.history);
        historymain = navigationView.findViewById(R.id.historynav);
        upgrade = navigationView.findViewById(R.id.upgrade);
        feedback = navigationView.findViewById(R.id.feedback);
        checkforupdate = navigationView.findViewById(R.id.checkforupdate);
        policy = navigationView.findViewById(R.id.policy);
        otherapps = navigationView.findViewById(R.id.otherapps);
        closenav = navigationView.findViewById(R.id.closenav);
        //ad1 = findViewById(R.id.ad1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SCANPHOTO:
                if (data == null || data.getData() == null) {
                    Log.e("TAG", "The uri is null, probably the user cancelled the image selection process using the back button.");
                    return;
                }
                else {
                    Intent intent = new Intent(MainActivity.this, ScanphotoActivity.class);
                    Uri uri = data.getData();
                    intent.putExtra("Image",uri);
                    startActivityForResult(intent, REQUEST_CODE_SCANPHOTO1);
                }
                break;
            case REQUEST_CODE_GENERATE:
                ObjectAnimator flip = ObjectAnimator.ofFloat(logo, "rotationY", 0f, 360f); // or rotationX
                flip.setDuration(3000); // 3 seconds
                flip.setRepeatCount(10);
                flip.start();
        }


        setResult(RESULT_OK);
    }

    @SuppressLint("LongLogTag")
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }

    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            camera = Camera.open();
            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

            // changing button/switch image
            toggleButtonImage();
        }
    }


    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }
            camera = Camera.open();
            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

            // changing button/switch image
            toggleButtonImage();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        codeScanner.releaseResources();
    }

    // thay doi image flash
    private void toggleButtonImage() {
        if (isFlashOn == true) {
            flash.setImageResource(R.mipmap.ic_flash_on);
        } else {
            flash.setImageResource(R.mipmap.flashlight);
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.opennav:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case R.id.historynav:
                ActivityUtils.startActivity(MainActivity.this, HistoryActivity.class, "");
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @OnClick({R.id.opennav, R.id.flash, R.id.logo, R.id.generate, R.id.saved, R.id.scanphoto, R.id.history, R.id.closenav, R.id.historynav, R.id.policy, R.id.feedback, R.id.otherapps, R.id.upgrade,  R.id.checkforupdate
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.opennav:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case R.id.flash:
                if (!isFlashOn) {
                    Log.d("fdfdf", "dfd");
                    turnOnFlash();
                } else {
                    Log.d("fdfdf", "dfd1");
                    turnOffFlash();
                }
                break;
            case R.id.logo:
            case R.id.upgrade:
                ActivityUtils.startActivity(MainActivity.this, AdvActivity.class, "");
                break;
            case R.id.generate:
//                Intent intent1 = new Intent(MainActivity.this, GenerateActivity.class);
//                startActivityForResult(intent1, REQUEST_CODE_GENERATE);
                ActivityUtils.startActivity(MainActivity.this, GenerateActivity.class, "");
                break;
            case R.id.saved:
//                Intent intent2 = new Intent(MainActivity.this, SavedActivity.class);
//                startActivityForResult(intent2, REQUEST_CODE_SAVED);
                ActivityUtils.startActivity(MainActivity.this, SavedActivity.class, "");
                break;
            case R.id.scanphoto:
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI , "image/*");
                startActivityForResult(pickIntent, REQUEST_CODE_SCANPHOTO);
                break;
            case R.id.history:
            case R.id.historynav:
//                Intent intent3 = new Intent(MainActivity.this, HistoryActivity.class);
//                startActivityForResult(intent3, REQUEST_CODE_HISTORY);
                ActivityUtils.startActivity(MainActivity.this, HistoryActivity.class, "");
                break;
            case R.id.closenav:
                drawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.policy:
                String urlString = "https://policy.ecomobile.vn/privacy-policy/qr-code";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                startActivity(intent);
                break;
            case R.id.feedback:

                break;
            case R.id.otherapps:
                ActivityUtils.startActivity(MainActivity.this, OtherAppsActivity.class, "");
                break;
            case R.id.checkforupdate:
                //startActivity(getPackageManager().getLaunchIntentForPackage("https://play.google.com/store/apps/details?id=com.vtool.qrcodereader.barcodescanner&fbclid=IwAR1A6uUoiAhIFiG-CQW5ckg80_UIK3O_eWYNqVQvrYECoKQ0_3pP8TqFcDM"));
//                String urlString1 = "https://play.google.com/store/apps/details?id=com.vtool.qrcodereader.barcodescanner&fbclid=IwAR1A6uUoiAhIFiG-CQW5ckg80_UIK3O_eWYNqVQvrYECoKQ0_3pP8TqFcDM";
//                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString1));
//                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent1.setPackage("com.android.chrome");
//                startActivity(intent1);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.vtool.qrcodereader.barcodescanner&fbclid=IwAR1A6uUoiAhIFiG-CQW5ckg80_UIK3O_eWYNqVQvrYECoKQ0_3pP8TqFcDM")));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.e("ssss", "onRequestPermissionsResult: PERMISSION_GRANTED" );
                } else {

                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            default:
                Log.e("ssss", "onRequestPermissionsResult: default " + requestCode);

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}