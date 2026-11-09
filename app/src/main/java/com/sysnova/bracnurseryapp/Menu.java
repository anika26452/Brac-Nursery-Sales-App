package com.sysnova.bracnurseryapp;

import static com.sysnova.bracnurseryapp.PostgreSQL.apiUrl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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

public class Menu extends AppCompatActivity {

    Button createorderbtn, todays_orderbtn, mortalitybtn;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#97c740")));
        if (Build.VERSION.SDK_INT >= 23) {
            Window window = Menu.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(Menu.this,R.color.white));
        }

        mDatabaseHelper = new DatabaseHelper(this);

        createorderbtn = findViewById(R.id.create_sales_order);
        todays_orderbtn = findViewById(R.id.todays_order);
        mortalitybtn = findViewById(R.id.mortality_btn);

        createorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Menu.this,SegmentActivity.class);
                startActivity(i);
            }
        });

        mortalitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncDoctype asyncDoctype=new AsyncDoctype();
                asyncDoctype.execute();
            }
        });

        todays_orderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Menu.this,ViewOrder.class);
                startActivity(i);
            }
        });
    }

    private final class AsyncDoctype extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected String doInBackground(String... params) {
            String res = "", error = "", reasontype="", reasonname="";
            HttpURLConnection con = null;
            boolean result = false;

            mDatabaseHelper.deleteMortalityReason();

            try {
                con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

                List<NameValuePair> list=new ArrayList<NameValuePair>();

                list.add(new BasicNameValuePair("name", "reasontype"));

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
                    reasontype=(String) line.get(0);
                    reasonname =(String) line.get(1);

                    result = mDatabaseHelper.insertIntoMortalityReason(reasontype, reasonname);
                }

                if (result) {
                    res = "Reasons Sync Successful";
                }
            } catch (Exception e) {
                res = e.getMessage();
            } finally {
                if(con!= null) {
                    con.disconnect();
                    con = null;
                }
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(Menu.this);
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

            Intent i=new Intent(Menu.this,MortalityActivity.class);
            startActivity(i);
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
        } catch (Exception e)
        {
            a = "Error" + e.getMessage();
            return a;
        }
        return a;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(Menu.this,OrgBranchSelection.class);
        startActivity(i);
    }
}