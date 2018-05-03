package com.projet.ihm.qrplay;

import java.io.*;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class PrintView extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_view);
    }

    /**
     * imprimer les codes directement
     */
    public void printCodes(View view){
        PrintHelper p = new PrintHelper(this);
        p.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.raw.qr_codes);
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
            InputStream in = getResources().openRawResource(R.raw.qr_codes);
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
