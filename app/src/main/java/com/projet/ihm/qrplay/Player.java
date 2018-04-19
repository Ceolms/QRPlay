package com.projet.ihm.qrplay;

import android.util.Log;

import java.util.ArrayList;

public class Player extends Thread{

    private static final String TAG = "Player";
    CameraView view;

    ArrayList<String> listeQR = new ArrayList<>();
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
                ArrayList<String> absents = new ArrayList<String>();
                while(true)
                {

                    for(int i = 0 ; i<listeQR.size();i++)
                    {
                        String qr = listeQR.get(i);
                        boolean exist = false;

                        for(int j = 0 ;j < listePiano.size();j++)
                        {
                            if(qr == listePiano.get(j)) exist = true;

                        }
                        if(!exist) absents.add(qr);
                    }

                    if(instrument == "piano" && listeQR.size() >0);
                    {
                        for(int i = 0; i < absents.size();i++)
                        {
                            Log.d(TAG,"NOT FOUND : " + absents.get(i));
                            String val = absents.get(i);
                            switch (val)
                            {

                                    case "1":
                                        view.play("do");
                                        break;
                                    case "2":
                                        break;
                                    case "3":
                                        break;
                                    case "4":
                                        break;
                            }
                        }
                    }
                    sleep(2000);
                    Log.d(TAG,"empty list");
                    listeQR.clear();
                }
        }
        catch (Exception e)
        {
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
        if(!listeQR.contains(qr))
        {
            listeQR.add(qr);
        }
        Log.d(TAG,"adding: " + qr + " , " + showList());
    }

}
