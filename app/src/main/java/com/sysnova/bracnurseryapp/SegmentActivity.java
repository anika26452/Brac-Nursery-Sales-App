package com.sysnova.bracnurseryapp;

import static com.sysnova.bracnurseryapp.PostgreSQL.apiUrl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class SegmentActivity extends AppCompatActivity {

    Spinner segmentspinner;
    Button nextbtn;
    DatabaseHelper mDatabaseHelper;
    ArrayList<String> productArraylist = new ArrayList<>();
    HashMap<String, String> hashproductmap = new HashMap<>();
    public static String selectedsegment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segment);

        getSupportActionBar().setTitle("Brac Nursery App");

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#97c740")));

        if (Build.VERSION.SDK_INT >= 23) {
            Window window = SegmentActivity.this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(SegmentActivity.this,R.color.white));
        }

        segmentspinner = findViewById(R.id.segment_spinner);
        nextbtn = findViewById(R.id.next_button);

        mDatabaseHelper = new DatabaseHelper(this);

        getallsegment();

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedsegment.equals("")){
                    toastMessage("Please select a segment!");
                }else{
                    Intent activityintent = new Intent(SegmentActivity.this, HowToOrder.class);
                    startActivity(activityintent);
                }
            }
        });
    }

    private void getallsegment() {
        AsyncSegment task = new AsyncSegment();
        task.execute();
    }
    private final class AsyncSegment extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected String doInBackground(String... params) {
            String result = "", error = "";
            try {
                result = warehouse();
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
            pd = new ProgressDialog(SegmentActivity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Segments are loading ........");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result.equals("Segments Sync Successful")) {
                loadwarehouselist();
            } else {
                toastMessage("Segments are not found");
            }
        }
    }
    private String warehouse() {

        String res = "";

        try {
            mDatabaseHelper.deletesalesType();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }

        res = FetchAllWarehouse();

        try {
            return res;
        } catch (Exception e){
            res=e.getMessage();
        }

        return res;
    }
    private void toastMessage(String message) {
        Toast.makeText(SegmentActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public String FetchAllWarehouse() {
        if(!productArraylist.isEmpty()){
            productArraylist.clear();
        }

        boolean result = false;
        String  salesType ="" , c_doctype_id ="", res = "";
        try {
            HttpURLConnection con = (HttpURLConnection) (new URL("" + apiUrl + "/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("" + apiUrl + "/DBSync");

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "salesType"));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse = httpClient.execute(httpPost);

            HttpEntity httpEntity = httpResponse.getEntity();
            String s = readResponse(httpResponse);

            if (s.isEmpty() || !s.startsWith("[")) {
                res = "salesType Not Foundd";
                return res;
            }

            ArrayList list4 = new ArrayList();

            String[] parts = s.split("_");
            for (int i = 0; i < parts.length; i++) {
                Vector line = new Vector();
                String[] parts1 = parts[i].split(",");
                for (int j = 0; j < parts1.length; j++) {
                    String value = parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]", "");
                    value = value.replaceAll("[\\[\\]]", "");
                    line.add(value);
                }
                list4.add(line);
            }

            for (int i1 = 0; i1 < list4.size(); i1++) {
                Vector line = (Vector) list4.get(i1);
                salesType = ((String) line.get(0));
                c_doctype_id = (String) line.get(1);

                result = mDatabaseHelper.insertIntoSalesType(salesType, c_doctype_id);
                productArraylist.add(salesType);
                hashproductmap.put(salesType, c_doctype_id);
            }

            if (result) {
                res = "Segments Sync Successful";
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
            while ((line=bufferedReader.readLine())!=null)
            {

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

    private void loadwarehouselist() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, productArraylist);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        segmentspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        selectProductSpinner();
    }

    private void selectProductSpinner() {
        segmentspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedsegment = parent.getSelectedItem().toString();
                } catch (NullPointerException ex) {
                } catch (ArrayIndexOutOfBoundsException ex) {}
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent activityintent = new Intent(SegmentActivity.this, Menu.class);
        startActivity(activityintent);
    }
}