package com.projet.ihm.qrplay;

import android.content.Intent;
import android.util.Log;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.util.ArrayList;
import java.io.*;

public class Player extends Thread{

    private static final String TAG = "Player";
    CameraView view;
    public boolean activityDestroyed = false;

    private ArrayList<String> listeQR = new ArrayList<>();
    private ArrayList<String> updateList = new ArrayList<>();
    private String instrument = "piano";
    private ArrayList<String> listePiano = new ArrayList<>();

    //variables d'enregistrement
    public boolean commandeEnregistrement = false;
    public boolean statutEnregistrement = false;
    private int tempsEnregistrement = 0;
    private ArrayList<Note> listeEnregistrement = new ArrayList<>();

    Player(CameraView cv)
    {
        this.view = cv;
    }

    private void init()
    {
        listePiano.add("Do");
        listePiano.add("Re");
        listePiano.add("Mi");
        listePiano.add("Fa");
    }

    public void run(){
        try {
                init();
                while(!activityDestroyed)
                {
                    //lancement et arret de l'enregistrement
                    if(commandeEnregistrement){
                        commandeEnregistrement = false;
                        statutEnregistrement = !statutEnregistrement;
                        if(!statutEnregistrement && tempsEnregistrement > 0){

                            //enregistrement
                            int n = 0;
                            File file = new File(view.getApplicationContext().getExternalFilesDir(null), n + ".txt");
                            while(file.exists()){
                                n++;
                                file = new File(view.getApplicationContext().getExternalFilesDir(null), n + ".txt");
                            }
                            //noinspection ResultOfMethodCallIgnored
                            file.createNewFile();
                            OutputStream out = new FileOutputStream(file);
                            OutputStreamWriter writer = new OutputStreamWriter(out);
                            writer.write(tempsEnregistrement + "\n");
                            for (Note note : listeEnregistrement) {
                                writer.write(note.temps + " " + note.valeur + "\n");
                            }
                            writer.close();
                            out.close();

                            //propose de partager le fichier
                            Uri fileUri = FileProvider.getUriForFile(view.getApplicationContext(), "com.projet.ihm.qrplay.fileprovider", file);

                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                            shareIntent.setType("text/plain");
                            view.startActivity(Intent.createChooser(shareIntent, view.getString(R.string.share)));

                            //reinitialisation
                            tempsEnregistrement = 0;
                            listeEnregistrement.clear();
                        }
                    }

                    if(statutEnregistrement) tempsEnregistrement++;

                    ArrayList<String> absents = new ArrayList<>();
                    listeQR.addAll(updateList);
                    updateList.clear();
                    absents.clear();
                    //Log.d(TAG,"--" + listeQR.size());

                    for(int i = 0 ; i<listeQR.size();i++)
                    {
                        for(int j = 0 ; j < listePiano.size();j++)
                        {
                            // si une note est dans le piano mais son qr code n'est pas visible , on l'ajoute
                            // a la liste des notes qui seront joués.
                            if(!listeQR.contains(listePiano.get(i)) && !absents.contains(listeQR.get(i)))
                            {
                                absents.add(listeQR.get(i));
                            }
                        }
                    }

                    Log.d(TAG,"--ListeQR : " + showList());
                    Log.d(TAG,"--abscents : " + absents.size());

                    if(instrument.equals("piano") && listeQR.size() >0)
                    {
                        for(int i = 0; i < absents.size();i++)
                        {
                            Log.d(TAG,"--NOT FOUND : " + absents.get(i));
                            String val = absents.get(i);
                            switch (val)
                            {
                                    case "Do":
                                        view.play("Do");
                                        if(statutEnregistrement) listeEnregistrement.add(new Note("Do", tempsEnregistrement));
                                        break;
                                    case "Re":
                                        view.play("Re");
                                        if(statutEnregistrement) listeEnregistrement.add(new Note("Re", tempsEnregistrement));
                                        break;
                                    case "Mi":
                                        view.play("Mi");
                                        if(statutEnregistrement) listeEnregistrement.add(new Note("Mi", tempsEnregistrement));
                                        break;
                                    case "Fa":
                                        view.play("Fa");
                                        if(statutEnregistrement) listeEnregistrement.add(new Note("Fa", tempsEnregistrement));
                                        break;
                            }
                        }
                    }
                    sleep(500);
                    //Log.d(TAG,"--empty list");
                    listeQR.clear();
                }
        }
        catch (Exception e)
        {
            Log.d(TAG,"--ERROR : " + e.getMessage());
        }
    }

    private String showList()
    {
        String s = "{";
        for(int j = 0 ;j < listeQR.size();j++)
        {
            s += listeQR.get(j);
            s+=",";
        }
        s+="}";
        return s;
    }

    public void addQR(String qr)
    {
        if(!updateList.contains(qr))
        {
            updateList.add(qr);
           // Log.d(TAG,"--adding: " + qr + " , " + showList());
        }
    }
}
