package com.projet.ihm.qrplay;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class CameraView extends Activity {

    private static final String TAG = "CameraView";
    private static Context mContext;
    private Player player;
    MediaPlayer doPlayer ;
    MediaPlayer rePlayer;
    MediaPlayer miPlayer;
    MediaPlayer faPlayer;
    MediaPlayer solPlayer;
    MediaPlayer laPlayer;

    SurfaceView cameraPreview;
    BarcodeDetector barcodeDetector;
    /*
    final MediaPlayer rePlayer = MediaPlayer.create(view,R.raw.sound_re);
    final MediaPlayer miPlayer = MediaPlayer.create(view,R.raw.sound_mi);
    final MediaPlayer faPlayer = MediaPlayer.create(view,R.raw.sound_fa);
    final MediaPlayer solPlayer = MediaPlayer.create(view,R.raw.sound_sol);
    final MediaPlayer laPlayer = MediaPlayer.create(view,R.raw.sound_la);
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        mContext = getApplicationContext();
        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);

        createCameraSource();

        player = new Player(this);
        doPlayer = MediaPlayer.create(this.getContext(),R.raw.sound_do);
        rePlayer = MediaPlayer.create(this.getContext(),R.raw.sound_re);
        miPlayer = MediaPlayer.create(this.getContext(),R.raw.sound_mi);
        faPlayer = MediaPlayer.create(this.getContext(),R.raw.sound_fa);
        solPlayer = MediaPlayer.create(this.getContext(),R.raw.sound_sol);
        laPlayer = MediaPlayer.create(this.getContext(),R.raw.sound_la);

        player.start();

    }

    public void createCameraSource()
    {
        barcodeDetector = new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600,1024)
                .build();
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    return;
                }

                try {
                    cameraSource.start(cameraPreview.getHolder());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> listeCodes = detections.getDetectedItems();
                if(listeCodes.size() >0)
                {
                    for(int i =0 ; i< listeCodes.size();i++)
                    {
                        player.addQR(listeCodes.valueAt(i).displayValue);
                    }
                }
            }
        });
    }

    public void play(String note)
    {
        Log.d(TAG,"--playing sound : " + note);
        switch (note)
        {

            case"Do":
                doPlayer.start();
                break;
            case"Re":
                rePlayer.start();
                break;
            case"Mi":
                miPlayer.start();
                break;
            case"Fa":
                faPlayer.start();
                break;
            case"Sol":
                solPlayer.start();
                break;
            case"La":
                laPlayer.start();
                break;
        }
    }


    public void onPermissionRequestDenied() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CameraView.this)
                .setTitle("Erreur")
                .setMessage("La camera est néccéssaire!");
        builder.show();
    }

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }
}
