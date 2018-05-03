package com.projet.ihm.qrplay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //nettoyer le cache
        File printCache = new File(getApplicationContext().getCacheDir(), "qr_print");
        if(printCache.exists()){
            deleteRec(printCache);
        }
    }

    /**
     * Called when the user taps the Send button
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(MainMenu.this, CameraView.class);
        startActivity(intent);
    }

    /**
     * Called when the user taps the Send button
     */
    public void playRecord(View view) {
        Intent intent = new Intent(MainMenu.this, PlayerView.class);
        startActivity(intent);
    }

    /**
     * Called when the user taps the Print button
     */
    public void goPrint(View view) {
        Intent intent = new Intent(MainMenu.this, PrintView.class);
        startActivity(intent);
    }

    /**
     * Deletes recursively
     * @param f file to delete
     */
    private void deleteRec(File f){
        if(f.isDirectory()){
            File[] l = f.listFiles();
            for(File c : l)
                deleteRec(c);
        }
        //noinspection ResultOfMethodCallIgnored
        f.delete();
    }
}
