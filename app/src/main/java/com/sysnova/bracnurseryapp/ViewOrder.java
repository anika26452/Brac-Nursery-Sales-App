package com.sysnova.bracnurseryapp;

import static com.sysnova.bracnurseryapp.MainActivity.ad_user_id;
import static com.sysnova.bracnurseryapp.MainActivity.employeeid;
import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedOrganizationName;
import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedWarehouseName;
import static com.sysnova.bracnurseryapp.PostgreSQL.apiUrl;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;


public class ViewOrder extends AppCompatActivity {
    Button showdate;
    String cdate,cdate1,dateOrdered="";
    RecyclerView orderListRecyclerView;
    DatabaseHelper mDatabaseHelper;
    TextView emptyListText;
    ArrayList<ViewOrderListModel> orderListModalArrayList = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener dateSetListener1;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        getSupportActionBar().setTitle("View Sales Order");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#97c740")));

        showdate = (Button) findViewById(R.id.date);
        orderListRecyclerView = findViewById(R.id.showorderinlist);
        emptyListText = findViewById(R.id.no_list_id);
        mDatabaseHelper = new DatabaseHelper(this);
        dateOrdered=getIntent().getStringExtra("dateOrdered");

        Date dateSql=new Date();

        if(dateOrdered==null || dateOrdered.equals("") || dateOrdered.equals("null")){
            cdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            cdate1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        } else{
            try {
                dateSql = dateFormat.parse(dateOrdered);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            cdate = dateOrdered;
            cdate1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(dateSql);
        }

        showdate.setText("" + cdate1 + "");

        showdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ViewOrder.this,
                        dateSetListener1,
                        year, month, day);
                datePickerDialog.show();
            }
        });

        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String sdate = dayOfMonth+"/"+month+"/"+year;
                Date date = new Date(year-1900, month-1,dayOfMonth);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy");
                cdate = formatter.format(date);
                cdate1 = formatter1.format(date);
                showdate.setText(cdate1);
                Showallorder();
            }
        };

        Showallorder();
    }

    private void Showallorder() {
        AsyncViewer task = new AsyncViewer();
        task.execute();
    }

    private final class AsyncViewer extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... strings) {
            String result = "", error = "";
            try {
                result = DBSYNCVIEW();
            } catch (Exception e) {
                error = e.getMessage();
                e.printStackTrace();
                return error;
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(ViewOrder.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Order is Fetching ........");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result.equals("Insert Successfully into View Order Table")) {
                initRecyclerView();
            } else {
                initRecyclerView();
                Toast.makeText(ViewOrder.this, result, Toast.LENGTH_LONG).show();
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
            while ((line=bufferedReader.readLine())!=null) {
                a=line;
            }
            return_text=sb.toString();
        } catch (Exception e) {

        }
        return a;
    }

    private String DBSYNCVIEW() {

        String res = "";

        res=fetchAllOrderDateWise();

        try {
            return res;
        } catch (Exception e){
            res=e.getMessage();
        }
        return res;
    }

    private String  fetchAllOrderDateWise() {
        String t_qty="", t_total="", res = "", t_sales_order_parent_ID="", orderno ="",dateordered ="", grandtotal ="", serial="", bpname ="",docstatus="", mobile="", userid = "";
        boolean result = false;

        if(!orderListModalArrayList.isEmpty()){
            orderListModalArrayList.clear();
        }
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "ViewOrder"));

            list.add(new BasicNameValuePair("date", cdate));
            list.add(new BasicNameValuePair("ad_user_id", employeeid));
            list.add(new BasicNameValuePair("orgName", selectedOrganizationName));

            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);

            if (s.isEmpty() || !s.startsWith("[")){
                res="There are no Order";
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
                serial = (String) line.get(0);
                orderno = (String) line.get(1);
                dateordered = (String) line.get(2);
                mobile = (String) line.get(3);
                userid = (String) line.get(4);
                t_sales_order_parent_ID = (String) line.get(5);
                grandtotal = (String) line.get(6);
                docstatus = (String) line.get(7);
                t_qty = (String) line.get(8);
                t_total = (String) line.get(9);

                ViewOrderListModel orderListModal = new ViewOrderListModel(serial,orderno,dateordered,mobile,t_sales_order_parent_ID, grandtotal, docstatus, t_qty, t_total);
                orderListModalArrayList.add(orderListModal);
                result =true;
            }

            if (result) {
                res = "Insert Successfully into View Order Table";
            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        orderListRecyclerView.setLayoutManager(linearLayoutManager);
        ShoworderDateWise adapter = new ShoworderDateWise(orderListModalArrayList,ViewOrder.this);
        orderListRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(ViewOrder.this, Menu.class);
        startActivity(i);
    }

}