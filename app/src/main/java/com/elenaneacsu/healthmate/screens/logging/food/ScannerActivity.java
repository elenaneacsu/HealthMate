package com.elenaneacsu.healthmate.screens.logging.food;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.elenaneacsu.healthmate.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ScannerActivity extends AppCompatActivity {
    SurfaceView surfaceQRScanner;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    String scanResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        initStuff(); /* Calling this function to initialize components */

    }

    /* Function used to initialize components of activity */
    public void initStuff() {

        /* Initializing objects */
        surfaceQRScanner = findViewById(R.id.surface_view);
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setRequestedPreviewSize(1024, 768)
                .setAutoFocusEnabled(true)
                .build();

        /* Adding Callback method to SurfaceView */
        surfaceQRScanner.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    /* Asking user to allow access of camera */
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PERMISSION_GRANTED) {
                        cameraSource.start(surfaceQRScanner.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannerActivity.this, new
                                String[]{Manifest.permission.CAMERA}, 1024);
                    }
                } catch (IOException e) {
                    Log.e("Camera start error-->> ", e.getMessage().toString());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        /* Adding Processor to Barcode detector */
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems(); /* Retrieving QR Code */
                if (barcodes.size() > 0) {

                    barcodeDetector.release(); /* Releasing barcodeDetector */

                    ToneGenerator toneNotification = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100); /* Setting beep sound */
                    toneNotification.startTone(ToneGenerator.TONE_PROP_BEEP, 150);

                    scanResult = barcodes.valueAt(0).displayValue.toString(); /* Retrieving text from QR Code */
                    Log.d("tag", "receiveDetections: scan " + scanResult);

                    Intent intent = new Intent();
                    intent.putExtra("barcode", scanResult);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1024:
                if (permissions.length > 0 && grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            cameraSource.start(surfaceQRScanner.getHolder());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    /* Initialize components again */
    @Override
    public void onResume() {
        super.onResume();
        initStuff();
    }
}
