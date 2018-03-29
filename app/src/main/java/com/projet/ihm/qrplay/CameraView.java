package com.projet.ihm.qrplay;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.util.List;

public class CameraView extends Activity {

    private static final String TAG = CameraView.class.getSimpleName();
   // private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
           // barcodeView.setStatusText(result.getText());

            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
           // ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
          // imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera_view);
/*
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.decodeContinuous(callback);
*/
        beepManager = new BeepManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

      //  barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    //    barcodeView.pause();
    }

    public void pause(View view) {
     //   barcodeView.pause();
    }

    public void resume(View view) {
      //  barcodeView.resume();
    }

    public void triggerScan(View view) {
      //  barcodeView.decodeSingle(callback);
    }
}
