package com.sysnova.bracnurseryapp;

import static com.sysnova.bracnurseryapp.PostgreSQL.apiUrl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class FullViewOrderActivity extends AppCompatActivity {

    String product, price, qty, total, uom;
    String wordsAmtBags;
    TextView total_bagsTextView,total_moneyTextView,qty_wordsTextView;
    RecyclerView recyclerViewList;
    double totalTakaAmt = 0;
    ArrayList<ViewProductDetailsModal> productDetailsModals = new ArrayList<>();
    String t_sales_order_id = "", grandtotal = "", t_qty="", t_total = "", dateOrdered = "";
    DatabaseHelper dbHelper;
    private static final DecimalFormat decfor = new DecimalFormat("0.00");
    Button cancelbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_vieworder);

        recyclerViewList = findViewById(R.id.order_product_list_recyclerview);
        total_bagsTextView    = findViewById(R.id.total_bags);
        total_moneyTextView   = findViewById(R.id.total_price_amt);
        qty_wordsTextView      = findViewById(R.id.quantity_words_bags);
        cancelbtn = findViewById(R.id.cancel_button);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null) {
            t_sales_order_id = getIntent().getStringExtra("sales_order_id");
            grandtotal = getIntent().getStringExtra("grandtotal");
            t_qty = getIntent().getStringExtra("t_qty");
            t_total = getIntent().getStringExtra("t_total");
            dateOrdered = getIntent().getStringExtra("dateOrdered");
        }

        getSupportActionBar().setTitle("Order View Activity");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#97c740")));

        dbHelper = new DatabaseHelper(this);
        total_moneyTextView.setText(t_total);
        total_bagsTextView.setText(t_qty);

        AysncOrderlineData AysncOrderlineData = new AysncOrderlineData();
        AysncOrderlineData.execute();

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(FullViewOrderActivity.this, ViewOrder.class);
                i.putExtra("dateOrdered", dateOrdered);
                startActivity(i);
            }
        });
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        ViewProductsListRecyclerViewAdapter adapter = new ViewProductsListRecyclerViewAdapter(productDetailsModals, FullViewOrderActivity.this);
        recyclerViewList.setAdapter(adapter);
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
        } catch (Exception e)
        {
            a = "Error" + e.getMessage();
            return a;
        }
        return a;
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        Intent intent=new Intent(FullViewOrderActivity.this, ViewOrder.class);
        intent.putExtra("dateOrdered", dateOrdered);
        startActivity(intent);
    }

    private final class AysncOrderlineData extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {

            String result1="",error="";boolean r=false;
            boolean result = false;
            HttpURLConnection con=null;
            try {
                con = (HttpURLConnection) (new URL("" + apiUrl + "/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("" + apiUrl + "/DBSync");

                List<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "salesorderlineList"));
                list.add(new BasicNameValuePair("t_sales_order_id", t_sales_order_id));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                String s = readResponse(httpResponse);
                if (s.isEmpty() || !s.startsWith("[")) {
                }

                ArrayList list4 = new ArrayList();

                String[] parts = s.split("_");
                for (int i = 0; i < parts.length; i++) {
                    Vector line = new Vector();
                    String test = parts[i];
                    String[] parts1 = parts[i].split(",", 9);
                    for (int j = 0; j < parts1.length; j++) {

                        String value = parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]", "");
                        value = value.replaceAll("[\\[\\]]", "");
                        line.add(value);

                    }
                    list4.add(line);
                }

                for (int i1 = 0; i1 < list4.size(); i1++) {
                    Vector line = (Vector) list4.get(i1);

                    product = ((String) line.get(0));
                    price = (String) line.get(1);
                    qty = ((String) line.get(2));
                    total = ((String) line.get(3));
                    uom = ((String) line.get(4));

                    ViewProductDetailsModal modal = new ViewProductDetailsModal(product, price, qty, total, uom);
                    productDetailsModals.add(modal);
                    result = true;
                }
                if (result) {
                    result1 = "Success";
                }
            }
            catch (ClientProtocolException e) {
                error=e.getMessage();
                return  error;
            } catch (UnsupportedEncodingException e) {
                error=e.getMessage();
                return  error;
            } catch (IOException e) {
                error="Failed To Connect";
                return  error;
            }
            finally {
                try {
                    if (con != null)
                        con.disconnect();
                    con = null;
                } catch (Exception e) {
                }
            }
            return result1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(FullViewOrderActivity.this);
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try{
                if(s.equals("Success")){
                    long intTotalPrice = (new Double(totalTakaAmt)).longValue();
                    wordsAmtBags =   NumberToWords2.convert(intTotalPrice);
                    initRecyclerView();
                }else{
                    toastMessage(s);
                }
            }catch (NullPointerException ex){
                System.out.println(ex.getMessage().toString());
            }
        }
    }

    public void toast(String msg) {
        Toast.makeText(FullViewOrderActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

}
