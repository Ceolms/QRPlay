package com.projet.ihm.qrplay;

import java.util.ArrayList;

public class Player extends Thread{

    ArrayList<String> listeQR = new ArrayList<String>();
    String instrument = "piano";
    ArrayList<String> listePiano = new ArrayList<String>();
    //final MediaPlayer soundPlayer = MediaPlayer.create(this,R.raw.do);

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
                while(true){

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
                    if(instrument == "piano");
                    {
                        for(int i = 0; i < absents.size();i++)
                        {
                            String val = absents.get(i);
                            switch (val)
                            {

                                    case "1":
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

                    sleep(200);
                }
        }
        catch (Exception e)
        {

        }


    }



}
