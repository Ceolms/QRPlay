package com.projet.ihm.qrplay;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

public class CameraView extends AppCompatActivity implements BarcodeRetriever {

    private static final String TAG = "CameraView";
    private static Context mContext;
    private Player player;
    MediaPlayer doPlayer;

   // SurfaceView cameraPreview
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

        final BarcodeCapture barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setRetrieval(this);

        player = new Player(this);
        doPlayer = MediaPlayer.create(this.getContext(),R.raw.sound_do);
        player.start();

    }

    public void play(String note)
    {
        Log.d(TAG,"playing sound");
        switch (note)
        {

            case"do":
                doPlayer.start();
                break;
            case"re":
                //    rePlayer.start();
                break;
            case"mi":
                //   miPlayer.start();
                break;
            case"fa":
                //  faPlayer.start();
                break;
            case"sol":
                //   solPlayer.start();
                break;
            case"la":
                //    laPlayer.start();
                break;
        }
    }


    @Override
    public void onPermissionRequestDenied() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CameraView.this)
                .setTitle("Erreur")
                .setMessage("La camera est néccéssaire!");
        builder.show();
    }

    // for one time scan
    @Override
    public void onRetrieved(final Barcode barcode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                player.addQR(barcode.displayValue);
                //Log.d(TAG,barcode.displayValue);
            }
        });

    }
    // for multiple callback
    @Override
    public void onRetrievedMultiple(final Barcode closetToClick, final List<BarcodeGraphic> barcodeGraphics) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                for (int index = 0; index < barcodeGraphics.size(); index++) {
                    Barcode barcode = barcodeGraphics.get(index).getBarcode();
                    Log.d(TAG,barcode.displayValue);
                }
                Log.d(TAG,"----------------");
            }
        });

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
        // when image is scanned and processed
    }

    @Override
    public void onRetrievedFailed(String reason) {
        // in case of failure
    }

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }
}
