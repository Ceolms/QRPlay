package com.projet.ihm.qrplay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class PrintView extends AppCompatActivity{
    private static final String TAG = "PrintView";
    ImageView image;
    boolean imageIsQR = false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_view);

        image = (ImageView) findViewById(R.id.PrintPreview);
        image.setOnTouchListener(new OnSwipeTouchListener(PrintView.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                if(imageIsQR)
                {
                    image.setImageResource(R.drawable.barcodes);
                    imageIsQR = false;
                }
                else{
                    image.setImageResource(R.drawable.qrcodes);
                    imageIsQR = true;
                }
            }
            public void onSwipeLeft() {
                if(imageIsQR)
                {
                    image.setImageResource(R.drawable.barcodes);
                    imageIsQR = false;
                }
                else{
                    image.setImageResource(R.drawable.qrcodes);
                    imageIsQR = true;
                }
            }
            public void onSwipeBottom() {
            }

        });
    }

    /**
     * imprimer les codes directement
     */
    public void printCodes(View view){
        Log.d(TAG,"--Impression du fichier");
        PrintHelper p = new PrintHelper(this);
        p.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = null;
        if(imageIsQR){bitmap = BitmapFactory.decodeResource(getResources(), R.raw.qrcodes);}
        else {bitmap = BitmapFactory.decodeResource(getResources(), R.raw.barcodes);}
        p.printBitmap("QR Codes", bitmap);
    }

    /**
     * envoyer les codes vers une autre application
     */
    public void sendCodes(View view){
        //préparer le fichier
        try {
        File dir = new File(getApplicationContext().getCacheDir(), "qr_print");
        if(!dir.isDirectory()) {
            if(dir.exists()) //noinspection ResultOfMethodCallIgnored
                dir.delete();
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        File image = new File(dir, "qr_codes.png");
        if(!image.exists()){
            //copier le fichier depuis les ressources
            InputStream in = null;

            if(imageIsQR){in = getResources().openRawResource(R.raw.qrcodes);}
            else {in = getResources().openRawResource(R.raw.barcodes);}
            OutputStream out = new FileOutputStream(image);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
            out.close();
            in.close();
        }
        Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.projet.ihm.qrplay.fileprovider", image);

        //envoyer le fichier
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
