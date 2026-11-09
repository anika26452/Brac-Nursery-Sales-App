package com.sysnova.bracnurseryapp;


import static com.sysnova.bracnurseryapp.PostgreSQL.apiUrl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    EditText employeeIdEditText, passwordEditText;
    public static String employeeid="",ad_user_id = "";String password="",selectedUserName="";
    public static String selected_org_name="",selected_warehouse_name="",m_warehouse_id="";
    String logged_user_id="";
    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
    TextView versiontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);}
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DatabaseHelper(this);

        employeeIdEditText = (EditText) findViewById(R.id.useredit);
        passwordEditText = (EditText) findViewById(R.id.passedit);
        versiontext = (TextView) findViewById(R.id.version_no);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mDatabaseHelper.deleteProducts();

        versiontext.setText("App Version : "+getResources().getString(R.string.app_version));
    }
    public void callLogin(View view) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        employeeid = employeeIdEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        if(employeeid.isEmpty()) {
            employeeIdEditText.requestFocus();
            employeeIdEditText.setError("Enter UserName");
            return ;
        }
        if(password.isEmpty()) {
            passwordEditText.requestFocus();
            passwordEditText.setError("Enter Password");
            return ;
        }

        AysncLogin task = new AysncLogin();
        task.execute();
    }

    private final class AysncLogin extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected String doInBackground(String... params) {

            String result="",error="";boolean r=false;

            try {
                mDatabaseHelper.deleteOrganization();
                mDatabaseHelper.deleteEmployee();
                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/Login")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/Login");

                List<NameValuePair> list=new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("name", employeeid));
                list.add(new BasicNameValuePair("pass",password));
                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                int y = s.length();
                if (s.isEmpty() || !s.startsWith("[")){
                    result=s;
                    Log.d("DEBUG","Result "+result);
                    return result;
                }
                ArrayList list4=new ArrayList();

                String[] parts = s.split("_");
                for(int i=0;i<parts.length;i++){
                    Vector line1=new Vector();

                    String[] parts1 = parts[i].split(",");
                    for(int j=0;j<parts1.length;j++){
                        String value = parts1[j].replaceFirst("[\\[\\]\\\" \"\\^:,]","");
                        value=value.replaceAll("[\\[\\]]","");
                        line1.add(value);

                    }
                    list4.add(line1);
                }
                String org_name="", warehouse_name ="";

                for(int i1=0;i1<list4.size();i1++){
                    Vector line= (Vector) list4.get(i1);
                    ad_user_id=(String) line.get(0);
//                    org_name=(String) line.get(2);
//                    selected_org_name=org_name;
//                    s_ad_org_id=(String)line.get(3);
//                    warehouse_name=(String)line.get(4);
//                    selected_warehouse_name=warehouse_name;
//                    String warehouse_id=(String)line.get(5);
//                    m_warehouse_id=warehouse_id;
                    logged_user_id=ad_user_id;

                    r= mDatabaseHelper.insertIntoEmployee(ad_user_id,org_name,warehouse_name);
                }

                if (r) {
                    result="Success";
                } else {
                    error="Failed to insert into Employee Table";
                    return error;
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

            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Login ...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result.equals("Success")) {
                toastMessage("Successfully Logged in!");
                selectedUserName=employeeid;
                finish();
              //  Intent activityintent = new Intent(MainActivity.this, HowToOrder.class);
             //   Intent activityintent = new Intent(MainActivity.this, SegmentActivity.class);
                Intent activityintent = new Intent(MainActivity.this, OrgBranchSelection.class);
                activityintent.putExtra("LOGGED_USER_ID",logged_user_id);
                startActivity(activityintent);
            } else {
                if(result.isEmpty()) {
                    ShowAlert("ERROR","No organization is assigned to this user!");
                } else {
                    ShowAlert("ERROR",result);
                }
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

                //sb.append(line);
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
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void ShowAlert(String title, String msg){


        androidx.appcompat.app.AlertDialog.Builder dlgAlert  = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.braclogo);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }




}
