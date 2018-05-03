package com.projet.ihm.qrplay;

import java.io.*;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class PlayerView extends AppCompatActivity {

    PlayerView view = this;

    int duree = 0;
    ArrayList<Note> listeNotes = new ArrayList<>();

    private Playback playback = new Playback(this, new ArrayList<Note>(), duree, 0);
    MediaPlayer doPlayer ;
    MediaPlayer rePlayer;
    MediaPlayer miPlayer;
    MediaPlayer faPlayer;
    MediaPlayer solPlayer;
    MediaPlayer laPlayer;

    TextView playerLabel;
    SeekBar playerBar;

    private static final int FILE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_view);
        playerLabel = findViewById(R.id.playerLabel);
        playerBar = findViewById(R.id.playerBar);
        playerBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playback.stop = true;
                try {
                    playback.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                playback = new Playback(view, listeNotes, duree, seekBar.getProgress());
                playback.start();
            }
        });

        doPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound_do);
        rePlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound_re);
        miPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound_mi);
        faPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound_fa);
        solPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound_sol);
        laPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sound_la);

        //ouvre le selecteur de fichiers
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/plain");
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        playback.stop = true;
        super.onDestroy();
    }

    /**
     * Appele quand l'utilisateur selectionne un fichier
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            playFile(data.getData());
        }
    }

    /**
     * decode le fichier et lance la lecture
     * @param uri lien vers le fichier a lire
     */
    public void playFile(Uri uri){
        try {
            playback.stop = true;
            //lire le fichier
            String path = uri.getPath();
            InputStream is = getContentResolver().openInputStream(uri);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            duree = Integer.parseInt(br.readLine());

            listeNotes.clear();
            String[] note;
            while(br.ready()){
                note = br.readLine().split(" ");
                if (note.length == 2){
                    listeNotes.add(new Note(note[1], Integer.parseInt(note[0])));
                }
            }

            br.close();
            is.close();

            //lancer la lecture
            try {
                playback.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int fileNamePos = path.lastIndexOf('/');
            if (fileNamePos != -1) path = path.substring(fileNamePos + 1);
            playerLabel.setText(path);
            playerBar.setProgress(0);
            playerBar.setMax(duree);

            playback = new Playback(view, listeNotes, duree, 0);
            playback.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(this, getString(R.string.read_error), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Joue une note
     * @param note Le nom de la note a jouer
     */
    public void playSound(String note)
    {
        Log.d("PlayerView","--playing sound : " + note);
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

    /**
     * met a jour la progression de la lecture
     * @param p progression actuelle
     */
    public void progress(int p){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            playerBar.setProgress(p, true);
        }else{
            playerBar.setProgress(p);
        }
    }
}
