package com.sysnova.bracnurseryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    Dialog dialog;
    EditText numberET;
    Button okButton;
    SharedPreferences sharedpreferences, NumPreference;
    public static final String MyPREFERENCES = "numOfOrders" ;
    public static final String myNumPref = "number" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#97c740")));

        if (Build.VERSION.SDK_INT >= 23) {
            Window window = HomeActivity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(HomeActivity.this,R.color.white));
        }

        dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.dialog_box_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.getWindow().setAttributes(lp);
        numberET = (EditText) dialog.findViewById(R.id.numberView);
        okButton=(Button)dialog.findViewById(R.id.ok);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("NUM",0);
        editor.commit();

        NumPreference = getSharedPreferences(myNumPref, Context.MODE_PRIVATE);

        SharedPreferences sh = getSharedPreferences("number2", Context.MODE_PRIVATE);
        String givenNumber =sh.getString("NUMBER2","null");
        if(sh.contains("NUMBER2")) {
            numberET.setText(givenNumber);
        }
    }

    public void enterNumber(View view) {
        dialog.show();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberET.getText().toString().isEmpty())
                {
                    numberET.requestFocus();
                    numberET.setError("Enter Number");
                    return;
                }


                String num=numberET.getText().toString();

                SharedPreferences.Editor ed = NumPreference.edit();
                ed.putString("NUMBER", num);
                ed.commit();
                Toast.makeText(HomeActivity.this, "Dialogue clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void callQRScanner(View view) {
        Intent i=new Intent(HomeActivity.this, HowToOrder.class);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(HomeActivity.this, MainActivity.class);
        startActivity(i);
    }
}