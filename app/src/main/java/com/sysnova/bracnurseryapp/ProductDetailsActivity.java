
package com.sysnova.bracnurseryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.sysnova.bracnurseryapp.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedOrganizationId;
import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedWarehouseId;
import static com.sysnova.bracnurseryapp.PostgreSQL.apiUrl;


public class ProductDetailsActivity extends AppCompatActivity {
    TextView product_code_TV,product_name_TV,price_per_unit_TV,total_cost_TV, availqtytv;
    EditText product_qty_TV;
    String qrcode="",productcodeV,productnameV, name="",unitprice="0.0",uom="", availableqty="";
    int productqtyV;
    double priceperunitV=100.0,totalCostV=0;
    DatabaseHelper databaseHelper;
    boolean flag =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("==============onCreate called===========");
        setContentView(R.layout.activity_product_details);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#2f5712")));

        if (Build.VERSION.SDK_INT >= 23) {
            Window window = ProductDetailsActivity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(ProductDetailsActivity.this,R.color.grey));
        }

        databaseHelper=new DatabaseHelper(ProductDetailsActivity.this);
        product_code_TV=(TextView)findViewById(R.id.product_code_tv);
        product_name_TV=(TextView)findViewById(R.id.product_name_tv);
        product_qty_TV=(EditText) findViewById(R.id.product_quantity_tv);
        price_per_unit_TV=(TextView)findViewById(R.id.product_price_tv);
        total_cost_TV=(TextView)findViewById(R.id.total_cost_tv);
        availqtytv = findViewById(R.id.avail_qty_tv);
        qrcode= getIntent().getStringExtra("CODE");
        product_code_TV.setText(qrcode);

        if(!qrcode.isEmpty()) {
            AsyncProduct asyncProduct=new AsyncProduct();
            asyncProduct.execute();
        }

        product_qty_TV.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int qtyval;
                if (product_qty_TV.getText().toString().equals("") || product_qty_TV.getText().toString().equals(null)) {
                    Toast.makeText(ProductDetailsActivity.this, "please insert a value", Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(product_qty_TV.getText().toString()) <= 0 || product_qty_TV.getText().toString().equals(".")) {
                    Toast.makeText(ProductDetailsActivity.this, "quantity cannot be zero", Toast.LENGTH_LONG).show();
                } else {
                    qtyval = Integer.parseInt(product_qty_TV.getText().toString());
                    totalCost(qtyval, Double.parseDouble(unitprice.trim()));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(ProductDetailsActivity.this, QRScannerActivity.class);
        startActivity(i);
        finish();
    }

    public void callCancel(View view) {
        Intent i=new Intent(ProductDetailsActivity.this, QRScannerActivity.class);
        startActivity(i);
        finish();
    }

    public void callAddProduct(View view) {
        productcodeV=product_code_TV.getText().toString();
        productnameV=product_name_TV.getText().toString();
        productqtyV=Integer.parseInt(product_qty_TV.getText().toString());

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        int id =sh.getInt("NUM",0);
        int id1=id+1;

        boolean result=false;

        //getProductbyCode added by anika on 2025-06-24
        Cursor cursor = databaseHelper.getProductbyCode(productnameV);

        if (cursor.moveToNext()) {
            CreateAlert();
        }else{
            result=databaseHelper.insertIntoProductOrderLine(id1,productcodeV,
                    productnameV,productqtyV,Double.parseDouble(unitprice),totalCostV);

            if(result) {
                toast(" Product Inserted");
                System.out.println(id+" "+productcodeV+" "+productnameV+" "+productqtyV+" "+priceperunitV+" "+totalCostV);
                Log.d("ERROR",id1+" "+productcodeV+" "+productnameV+" "+productqtyV+" "+priceperunitV+" "+totalCostV);
                SharedPreferences.Editor editor = sh.edit();

                editor.putInt("NUM",id1);
                editor.commit();
                Intent i=new Intent(ProductDetailsActivity.this, ProductOrderLine.class);

                startActivity(i);
            } else {
                toast("could not insert into internal database");
            }
        }
    }

    public void callAdd(View view) {
        int q=Integer.parseInt(product_qty_TV.getText().toString());
        q++;
        product_qty_TV.setText(String.valueOf(q));
        totalCost(q,Double.parseDouble(unitprice.trim()));

    }
    public void callMinus(View view) {
        int q1=Integer.parseInt(product_qty_TV.getText().toString());
        if(q1==1)
        {
            toast("Minimum 1 unit should be ordered");
        }
        else
        {
            q1--;
            product_qty_TV.setText(String.valueOf(q1));
            totalCost(q1,Double.parseDouble(unitprice.trim()));
        }
    }
    void toast(String msg)
    {
        Toast.makeText(ProductDetailsActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
    void totalCost(int qty,double price)
    {
        double total=(double)(qty*price);
        totalCostV=total;
        total_cost_TV.setText(qty+" X "+price+" = "+String.valueOf(total)+" taka ");
    }
    private final class AsyncProduct extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            String result = "", error = "";
            try {
                result =FetchProductsFromServer2();
            } catch (Exception e) {
                error = e.getMessage();
                Log.d("DEBUG",error);
                e.printStackTrace();
                return error;
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ProductDetailsActivity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Loading Product Details...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("=============AsyncTask executed==========");
            pd.dismiss();

            if (result.equals("Product Sync Successful")) {
                toastMessage("Product Sync Successful");
                product_name_TV.setText(name);
                price_per_unit_TV.setText(unitprice);
                availqtytv.setText(availableqty);
                totalCost(Integer.parseInt(product_qty_TV.getText().toString()),Double.parseDouble(unitprice.trim()));

            } else {
                toastMessage(result);

                Log.d("DEBUG",result);
            }
        }
    }
    private String  FetchProductsFromServer2() {
        boolean result = false;
        String res = "";

        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "getProducPriceByID"));
            list.add(new BasicNameValuePair("pro_id", qrcode));
            list.add(new BasicNameValuePair("org_id", selectedOrganizationId));
            list.add(new BasicNameValuePair("m_warehouse_id", selectedWarehouseId));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);

            if (s.isEmpty() || !s.startsWith("[")){
                res="Product Not Foundd";
                return res;
            }

            ArrayList list4=new ArrayList();

            String[] parts = s.split("_");
            for(int i=0;i<parts.length;i++){
                Vector line=new Vector();
                String[] parts1 = parts[i].split(",");
                for(int j=0;j<parts1.length;j++){

                    String value=parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                    value=value.replaceAll("[\\[\\]]","");
                    line.add(value);
                }
                list4.add(line);
            }

            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);

                name=(String) line.get(0);
                unitprice=(String) line.get(1);
                uom=(String) line.get(2);
                availableqty = (String) line.get(3);

                result = true;
            }

            if (result) {
                res = "Product Sync Successful";
            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }

    public String readResponse(HttpResponse res) {
        InputStream is=null;
        String return_text="",a="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null) {

                a=line;
            }
            return_text=sb.toString();
        } catch (Exception e) {
            a = "Error" + e.getMessage();
            return a;
        }
        return a;
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void CreateAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);

        builder.setTitle("Alert!");
        builder.setMessage("This Product Is Already Added!!!");
        builder.setCancelable(false);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        Intent i = new Intent(ProductDetailsActivity.this, ProductOrderLine.class);
                        startActivity(i);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}