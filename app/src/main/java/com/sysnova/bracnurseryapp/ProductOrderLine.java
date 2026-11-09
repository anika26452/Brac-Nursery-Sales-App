package com.sysnova.bracnurseryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.sysnova.bracnurseryapp.MainActivity.ad_user_id;
import static com.sysnova.bracnurseryapp.MainActivity.employeeid;
import static com.sysnova.bracnurseryapp.MainActivity.selected_org_name;
import static com.sysnova.bracnurseryapp.MainActivity.selected_warehouse_name;

import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedOrganizationName;
import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedWarehouseName;
import static com.sysnova.bracnurseryapp.PostgreSQL.apiUrl;
import static com.sysnova.bracnurseryapp.SegmentActivity.selectedsegment;

public class ProductOrderLine extends AppCompatActivity {
    RecyclerView recyclerView;
    OrderLineAdapater adapter;
    List<OrderLineModel> model;
    Dialog dialog;
    EditText numberET;
    Button okButton,noButton;
    String num="";
    String oldserial="",olddate="",finalserial="";
    DatabaseHelper databaseHelper;
    TextView qtySum,toatlSum;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String newdate = sdf.format(System.currentTimeMillis());
    public static final String myNumPref2 = "number2" ;
    SharedPreferences NumPreference2;
    long now = System.currentTimeMillis();
    String strLong = Long.toString(now);
  //  String documentNo=employeeid+strLong;
    String documentNo=employeeid.replace("_", "!@")+strLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_order_line);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#2f5712")));
        getSupportActionBar().setTitle("Product Order Line");

        if (Build.VERSION.SDK_INT >= 23) {
            Window window = ProductOrderLine.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(ProductOrderLine.this,R.color.grey));
        }

        databaseHelper=new DatabaseHelper(ProductOrderLine.this);
        model=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductOrderLine.this);
        recyclerView.setLayoutManager(layoutManager);

        dialog = new Dialog(ProductOrderLine.this);
        dialog.setContentView(R.layout.confirm_order_dialog_box_layout);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);
        noButton = (Button)dialog.findViewById(R.id.no);
        okButton=(Button)dialog.findViewById(R.id.ok);
        dialog.setCancelable(false);

        numberET=(EditText)findViewById(R.id.numET);
        qtySum=(TextView)findViewById(R.id.listQtySum);
        toatlSum=(TextView)findViewById(R.id.listTotalSum);
        toatlSum.setText("");

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
        loadProductOrderLine();

        NumPreference2 = getSharedPreferences(myNumPref2, Context.MODE_PRIVATE);
    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double ItemName = intent.getDoubleExtra("TOTAL",0.0);
            int itemName2=intent.getIntExtra("TOTALQTY",0);
            toatlSum.setText(String.valueOf(ItemName));
            qtySum.setText(String.valueOf(itemName2));
        }
    };
    private void loadProductOrderLine(){
        Cursor data = databaseHelper.getProductOrderLine();
        if(data.moveToFirst()){
            do{
                int id= data.getInt(0);
                String code=data.getString(1);
                String name=data.getString(2);
                int qty=data.getInt(3);
                double price=data.getDouble(4);
                double total=data.getDouble(5);
                OrderLineModel orderListModal = new OrderLineModel(id,code,name,qty,price,total);
                model.add(orderListModal);
            }while(data.moveToNext());
        }
        if(model == null || model.isEmpty()){
            toast("No Order Found");
        }else{
            initRecyclerView();
            double t=0.0;
            t=getTotal();
            int Tqty=0;
            Tqty=getTotalQty();
            qtySum.setText(String.valueOf(Tqty));
            toatlSum.setText(String.valueOf(t));
        }
    }
    void toast(String msg)
    {
        Toast.makeText(ProductOrderLine.this,msg,Toast.LENGTH_SHORT).show();
    }

    private void initRecyclerView(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductOrderLine.this);
        recyclerView.setLayoutManager(layoutManager);
        OrderLineAdapater adapter = new OrderLineAdapater(model,ProductOrderLine.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public double getTotal() {
        double total=0.0;
        for(int i=0;i<model.size();i++) {
            total=total+model.get(i).getTotal();
        }
        return total;
    }
    public int getTotalQty() {
        int total=0;
        for(int i=0;i<model.size();i++) {
            total=total+model.get(i).getQty();
        }
        return total;
    }
    public void callConfirm(View view) {
        if(numberET.getText().toString().isEmpty()) {
            numberET.requestFocus();
            numberET.setError("Enter Number");
            return;
        }
        orderConfirmationDialog();
    }
    public void callAddMoreProducts(View view) {

        Intent i=new Intent(ProductOrderLine.this, HowToOrder.class);
        startActivity(i);
    }
    public void callCancel(View view) {
        databaseHelper.deleteAllProductOrderLine();
        Intent i=new Intent(ProductOrderLine.this, HowToOrder.class);
        startActivity(i);
    }
    public void orderConfirmationDialog() {
        dialog.show();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AysncSerial asyncSendToServer=new AysncSerial();
                asyncSendToServer.execute();
                dialog.dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(ProductOrderLine.this, HowToOrder.class);
        startActivity(i);
    }
    private final class AsyncSendToServer extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            boolean isActive=true;

            Cursor check = databaseHelper .getProductOrderLine();
            if(check.moveToNext()) {
            } else {
                return "noOrderLine";
            }
            try {
                ArrayList salesHeader=new ArrayList();

                Vector headerLines=new Vector();

                headerLines.add(selectedOrganizationName);
                headerLines.add(selectedWarehouseName);
                headerLines.add(selectedsegment);
                headerLines.add("10/4/2021");
                headerLines.add("Sundry Partner");
                headerLines.add(finalserial);
                headerLines.add(numberET.getText().toString().trim());
                headerLines.add(employeeid.replace("_", "!@"));
                headerLines.add("Gate Sales");
                headerLines.add(documentNo);
                headerLines.add(ad_user_id);
                salesHeader.add(headerLines);

                String headerString = "";
                for (Object s : salesHeader) {
                    if (headerString.isEmpty()) {
                        headerString =headerString+s;
                    } else {
                        headerString =headerString+"_" + s;
                    }
                }

                Cursor dataorderLine = databaseHelper.getProductOrderLine();

                ArrayList salesOrder=new ArrayList();

                while (dataorderLine.moveToNext()) {
                    String productName = dataorderLine.getString(2);
                    String productQTY = String.valueOf(dataorderLine.getInt(3));
                    String unit_price  = String.valueOf(dataorderLine.getDouble(4));
                    String total=String.valueOf(dataorderLine.getDouble(5));

                    Vector orderLines=new Vector();
                    orderLines.add(selected_org_name);
                    orderLines.add(selected_warehouse_name);
                    orderLines.add("product category");
                    orderLines.add(productName.replace("_", "!@"));
                    orderLines.add("PIECE");
                    orderLines.add(productQTY);
                    orderLines.add(unit_price);
                    orderLines.add(total);

                    salesOrder.add(orderLines);
                }

                String lineString = "";
                for (Object s : salesOrder) {
                    if (lineString.isEmpty()) {
                        lineString =lineString+s;
                    } else {
                        lineString =lineString+"_" + s;
                    }
                }

                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/SalesOrder")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/SalesOrder");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("type", "salesorder"));
                list.add(new BasicNameValuePair("header", headerString));
                list.add(new BasicNameValuePair("lines",lineString));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);



                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                System.out.println(headerString);
                System.out.println(lineString);

                if (s.isEmpty() || !s.equals("Success")){
                    result=s;
                    return result;
                } else if(s.equals("Success")){
                    return "Success";
                }

            } catch (Exception e){
                result = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                if(s.equals("Success")){


                    toast("Order Placed Successfully");
                    toast("Your order serial : "+finalserial);
                    databaseHelper.deleteAllProductOrderLine();
                    //insertintoDate(num);
                    Intent i=new Intent(ProductOrderLine.this, Menu.class);
                    startActivity(i);

                    finish();
                }else if(s.equals("noOrderLine")){
                    toastMessage("No order line exits");
                } else{
                    Log.d("DEBUG","error: "+s);

                    toast("Server Connection Failed Please Send Again"+s);

                    System.out.println("Server Connection Failed Please Send Agai. Why? = "+s);
                }
            }catch (NullPointerException ex){

            }
        }
    }
    public String readResponse(HttpResponse res) {
        InputStream is=null;
        String return_text="",a="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                a=line;
            }
            return_text=sb.toString();
        } catch (Exception e)
        {
        }
        return a;
    }
    void toastMessage(String str)
    {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    public void generateSerial() {
        if(oldserial.isEmpty()|| oldserial.equals("")) {
            finalserial=newdate+"-"+"1";
            System.out.println("finalserial="+finalserial);
        } else{
            int pos=oldserial.indexOf("-");
            olddate=oldserial.substring(0,pos);
            if(olddate.equals(newdate)) {
                int olddateserial=Integer.parseInt(oldserial.substring(pos+1,oldserial.length()));
                System.out.println("olddateserialnumber="+olddateserial);
                int incrementedNum=olddateserial+1;
                finalserial=newdate+"-"+incrementedNum;
                System.out.println("finalserial="+finalserial);
            }
            else
            {
                finalserial=newdate+"-"+"1";
                System.out.println("finalserial="+finalserial);
            }
        }
        AsyncSendToServer sendToServer=new AsyncSendToServer();
        sendToServer.execute();
    }
    private final class AysncSerial extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected String doInBackground(String... params) {

            String result="",error="";boolean r=false;

            try {
                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", "serial"));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                System.out.println("serialresponse"+ " "+s);
                int y = s.length();
                if (s.isEmpty() || !s.startsWith("[")){
                    result="";
                } else{
                    result=s.substring(1,y-1);
                }
            } catch (ClientProtocolException e) {
                error=e.getMessage();
                return  error;
            } catch (UnsupportedEncodingException e) {
                error=e.getMessage();
                return  error;
            } catch (IOException e) {
                error="Failed To Connect";
                return  error;
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ProductOrderLine.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("generating serial ........");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            oldserial=result;
            generateSerial();
            System.out.println("fetched Serial="+oldserial);
        }
    }
}