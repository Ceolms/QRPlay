package com.projet.ihm.qrplay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public void PrintCodes(View view) {
        PrintHelper p = new PrintHelper(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.qr_codes);
        p.printBitmap("QR Codes", bitmap);
    }
}
