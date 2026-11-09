package com.sysnova.bracnurseryapp;

import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedOrganizationId;
import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedWarehouseId;
import static com.sysnova.bracnurseryapp.PostgreSQL.apiUrl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CheckHistory extends AppCompatActivity {

    RecyclerView orderListRecyclerView;
    ArrayList<ViewHistoryListModel> productmortArrayList = new ArrayList<>();
    String m_product_id="";
    Button ok_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_history);

        getSupportActionBar().setTitle("Mortality History");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#97c740")));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle!=null) {
            m_product_id   = getIntent().getStringExtra("selectedProductID");
        }

        orderListRecyclerView = findViewById(R.id.showdelchallaninlist);
        ok_btn = findViewById(R.id.ok_btn);

        AsyncDoctype asyncDoctype=new AsyncDoctype();
        asyncDoctype.execute();

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private final class AsyncDoctype extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            String res = "", error = "", reporting_date="", serial="", mortality_qty="";
            Boolean result=false;

            if(!productmortArrayList.isEmpty()){
                productmortArrayList.clear();
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

                list.add(new BasicNameValuePair("name", "checkhistory"));
                list.add(new BasicNameValuePair("m_product_id",m_product_id));
                list.add(new BasicNameValuePair("ad_org_id", selectedOrganizationId));
                list.add(new BasicNameValuePair("m_warehouse_id", selectedWarehouseId));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                if (s.isEmpty() || !s.startsWith("[")){
                    res="Document Type Not Found";
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

                    reporting_date=(String) line.get(0);
                    serial =(String) line.get(1);
                    mortality_qty = (String) line.get(2);

                    ViewHistoryListModel orderListModal = new ViewHistoryListModel(reporting_date,serial,mortality_qty);
                    productmortArrayList.add(orderListModal);

                    result = true;
                }

                if (result) {
                    res = "Project Sync Successful";
                }
            } catch (Exception e) {
                error = e.getMessage();
                e.printStackTrace();
                return error;
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(CheckHistory.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Sync On Progress ........");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            initRecyclerView();
        }
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        orderListRecyclerView.setLayoutManager(linearLayoutManager);
        ShowHistory adapter = new ShowHistory(productmortArrayList, CheckHistory.this);
        orderListRecyclerView.setAdapter(adapter);
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
}