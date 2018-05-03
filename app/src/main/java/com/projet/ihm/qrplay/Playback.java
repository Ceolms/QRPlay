package com.projet.ihm.qrplay;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;


public class Playback extends Thread{

    PlayerView view;
    public boolean stop = false;

    private int temps;
    private int duree;
    private ArrayList<Note> listeNotes;

    Playback(PlayerView view, ArrayList<Note> listeNotes, int duree, int temps){
        this.view = view;
        this.temps = temps;
        this.duree = duree;
        this.listeNotes = listeNotes;
    }

    @Override
    public void run() {
        try {
            Iterator <Note> i = listeNotes.iterator();
            if (i.hasNext()) {
                Note note = i.next();
                while(i.hasNext() && note.temps < temps){
                    note = i.next();
                }
                while (temps <= duree && !stop) {
                    view.progress(temps);
                    while(note.temps <= temps && !stop){
                        view.playSound(note.valeur);
                        if (i.hasNext()){
                            note = i.next();
                        }else{
                            stop = true;
                            view.progress(duree);
                        }
                    }
                    sleep(500);
                    temps++;
                }
            }
        }catch (Exception e){
            Log.d("Playback","--ERROR : " + e.getMessage());
        }
    }
}
