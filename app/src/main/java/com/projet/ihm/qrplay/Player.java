package com.projet.ihm.qrplay;

import android.util.Log;

import java.util.ArrayList;

public class Player extends Thread{

    private static final String TAG = "Player";
    CameraView view;

    ArrayList<String> listeQR = new ArrayList<>();
    ArrayList<String> updateList = new ArrayList<>();
    String instrument = "piano";
    ArrayList<String> listePiano = new ArrayList<>();

    Player(CameraView cv)
    {
        this.view = cv;
    }

    public void init()
    {
        listePiano.add("1");
        listePiano.add("2");
        listePiano.add("3");
        listePiano.add("4");
    }

    public void run(){
        try {
                init();
                while(true)
                {
                    ArrayList<String> absents = new ArrayList<String>();
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
                                    case "1":
                                        view.play("do");
                                        break;
                                    case "2":
                                        view.play("re");
                                        break;
                                    case "3":
                                        view.play("mi");
                                        break;
                                    case "4":
                                        view.play("fa");
                                        break;
                            }
                        }
                    }
                    sleep(1000);
                    //Log.d(TAG,"--empty list");
                    listeQR.clear();
                }
        }
        catch (Exception e)
        {
            Log.d(TAG,"--ERROR : " + e.getMessage());
        }
    }

    public String showList()
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
