package com.sysnova.bracnurseryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class HowToOrder extends AppCompatActivity {
DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_order);
        databaseHelper=new DatabaseHelper(HowToOrder.this);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#97c740")));
        if (Build.VERSION.SDK_INT >= 23) {
            Window window = HowToOrder.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(HowToOrder.this,R.color.white));
        }
    }
    public void callSeachProduct(View view)
    {
        Intent i=new Intent(HowToOrder.this,ChooseProductActivity.class);

          startActivity(i);
    }
    public void callQRScanner(View view)
    {
       Intent i=new Intent(HowToOrder.this,QRScannerActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseHelper.deleteProducts();

        Intent i=new Intent(HowToOrder.this,SegmentActivity.class);
        startActivity(i);

    }
}