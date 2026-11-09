package com.sysnova.bracnurseryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sysnova.bracnurseryapp.*;
// implements onClickListener for the onclick behaviour of button
public class QRScannerActivity extends AppCompatActivity  {
    Button scanBtn;
    TextView messageText, messageFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scan a barcode or QR Code");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();

    }

   /** @Override
    public void onClick(View v) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library

    }**/
   @Override
   public void onBackPressed() {
       super.onBackPressed();
       Intent i=new Intent(QRScannerActivity.this, HowToOrder.class);
       startActivity(i);
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Intent i=new Intent(QRScannerActivity.this, HowToOrder.class);
                startActivity(i);
              //  Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
               // messageText.setText(intentResult.getContents());
               // messageFormat.setText(intentResult.getFormatName());
                Intent i=new Intent(QRScannerActivity.this, ProductDetailsActivity.class);
                i.putExtra("CODE",intentResult.getContents());
              //  i.putExtra("ORG_ID", s_orginazation);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);
            }
        } else {
        //    super.onActivityResult(requestCode, resultCode, data);
         //   Toast.makeText(getBaseContext(), "Nothing Found", Toast.LENGTH_SHORT).show();
        }
    }
}
