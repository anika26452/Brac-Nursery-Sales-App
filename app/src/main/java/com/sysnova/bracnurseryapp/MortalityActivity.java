package com.sysnova.bracnurseryapp;

import static com.sysnova.bracnurseryapp.MainActivity.employeeid;
import static com.sysnova.bracnurseryapp.MainActivity.selected_org_name;
import static com.sysnova.bracnurseryapp.MainActivity.selected_warehouse_name;
import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedOrganizationId;
import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedOrganizationName;
import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedWarehouseId;
import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedWarehouseName;
import static com.sysnova.bracnurseryapp.PostgreSQL.apiUrl;
import static com.sysnova.bracnurseryapp.SegmentActivity.selectedsegment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class MortalityActivity extends AppCompatActivity {
    TextView orgvw, datevw, wrevw;
    Spinner blockspinner,  plotspinner,  parentcatspinner, productcatspinner, productspinner, doctypespinner,
            reasontypespinner, reasonspinner;
    private DatePickerDialog.OnDateSetListener dateListener;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    List<String> ProjectArrayList = new ArrayList<String>();
    HashMap<String,String> hashProjectId  = new HashMap<>();
    List<String> BlockArrayList = new ArrayList<String>();
    HashMap<String,String> hashBlockId  = new HashMap<>();
    List<String> PlotArrayList = new ArrayList<String>();
    HashMap<String,String> hashPlotId  = new HashMap<>();
    String selectedparent="", selectedProject="", selectedprojectid="", selectedBlock="", selectedblockid="",
            selectedPlot="", selectedplotid="", selectedproductcat="", selectedproductcatid="", selectedProduct="",
            selectedProductID="", documentno="", c_doctype_id, selectedmortalitytype="", selectedmortalityname="", report_date,
            remarks="", mortqty="", selectedproductcode="", oldserial="",finalserial="",olddate="", doctype="";
    DatabaseHelper mDatabaseHelper;
    List<String> ProductCatArrayList = new ArrayList<String>();
    HashMap<String,String> hashProductCatId  = new HashMap<>();
    List<String> productArrayList = new ArrayList<String>();
    HashMap<String,String> hashProductID  = new HashMap<>();
    HashMap<String,String> hashProductCode  = new HashMap<>();
    EditText remarksedittext, search_editText, morqtyeditvw, documentnoeditvw;
    CheckBox searchcodebox;
    Button addproductbtn, checkhistorybtn,browsebtn, sendtoserverbtn;
    Uri uri;
    InputStream iStream = null;
    Bitmap bitmap;
    ImageView viewimage;
    byte[] inputData;
    int checkphoto = 0, cmort = 0;
    RecyclerView productrecyclerview;
    HashMap<String,String> hashDocTypeId  = new HashMap<>();
    List<String> reasontypeArrayList = new ArrayList<String>();
    List<String> reasonnameArrayList = new ArrayList<String>();
    ArrayList<ViewMortalityListModel> productmortArrayList = new ArrayList<>();
    Date selectedDate;
    LinearLayout imagelayoutvw, proviewlayoutvw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mortality);

        getSupportActionBar().setTitle("Mortality Entry");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#97c740")));

        orgvw = findViewById(R.id.org_textvw);
        datevw = findViewById(R.id.date_textvw);
        blockspinner = findViewById(R.id.block_spinner);
 //       projectspinner = findViewById(R.id.project_spinner);
        plotspinner = findViewById(R.id.plot_spinner);
        wrevw = findViewById(R.id.warehouse_textvw);
        parentcatspinner = findViewById(R.id.parentcat_spinner);
        productcatspinner = findViewById(R.id.productcat_spinner);
        productspinner = findViewById(R.id.product_spinner);
        search_editText = findViewById(R.id.search_customer_editText);
        remarksedittext = findViewById(R.id.remarks_edittext);
        searchcodebox = findViewById(R.id.search_code_checkbox);
        addproductbtn = findViewById(R.id.addproduct_button);
        checkhistorybtn = findViewById(R.id.chkhistory_button);
        doctypespinner = findViewById(R.id.documenttype_spinner);
        morqtyeditvw = findViewById(R.id.morqty_edittext);
        browsebtn = findViewById(R.id.browsebtn);
        viewimage = findViewById(R.id.imageview);
        productrecyclerview = findViewById(R.id.showmortalityrecyclervw);
        documentnoeditvw = findViewById(R.id.documentno_editText);
        reasontypespinner = findViewById(R.id.reasontype_spinner);
        reasonspinner = findViewById(R.id.reason_spinner);
        sendtoserverbtn = findViewById(R.id.sendtoserver_button);
        imagelayoutvw = findViewById(R.id.imagelayout);
        proviewlayoutvw = findViewById(R.id.proviewlayout);
        mDatabaseHelper = new DatabaseHelper(MortalityActivity.this);

        orgvw.setText(selectedOrganizationName);
        wrevw.setText(selectedWarehouseName);
        datevw.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        selectedDate = new Date();
        report_date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate);

        FetchDocType();
        FetchBlockSpinner();
        ViewProductLayout();

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                // Format the selected date.
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month - 1, dayOfMonth);
                selectedDate = selectedCalendar.getTime();

                // Get the current date.
                Calendar currentCalendar = Calendar.getInstance();
                Date currentDate = currentCalendar.getTime();

                // Define the start of the current month.
                Calendar startOfMonth = Calendar.getInstance();
                startOfMonth.set(Calendar.DAY_OF_MONTH, 1);
                startOfMonth.set(Calendar.HOUR_OF_DAY, 0);
                startOfMonth.set(Calendar.MINUTE, 0);
                startOfMonth.set(Calendar.SECOND, 0);
                startOfMonth.set(Calendar.MILLISECOND, 0);

                // Check if the selected date is valid.
                if (selectedDate.before(startOfMonth.getTime()) || selectedDate.after(currentDate)) {
                    showAlert("Warning", "You can only select dates from the current month and not in the future.");
                } else {
                    report_date = formatter.format(selectedDate);
                    datevw.setText(formatter.format(selectedDate));
                }
            }
        };

        addproductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remarks = remarksedittext.getText().toString();
                mortqty = morqtyeditvw.getText().toString();

                if(checkphoto!=1){
                    toastMessage("Please upload an image of the product.");
                    return;
                }

                if(mortqty.equals("")){
                    morqtyeditvw.setError("Please enter mortality QTY");
                    return;
                }

                if(selectedProductID.equals("")){
                    toastMessage("Please select a product.");
                }

                if(selectedProject.equals("--Select Doctype--")){
                    doctype = "";
                }else{
                    doctype  =selectedProject;
                }

                if(selectedmortalitytype.equals("--Select Reason--")){
                    toastMessage("Please select Mortality Reason Type.");
                    return;
                }

                if(selectedmortalityname.equals("")){
                    toastMessage("Please select Mortality Reason.");
                    return;
                }

                Boolean res = false, resline = false;
                int countmort = 0;

                Cursor cursor = mDatabaseHelper.getcountmortality(employeeid);

                if(cursor.moveToFirst()){
                    do{
                        countmort = Integer.parseInt(cursor.getString(0));
                    }while(cursor.moveToNext());
                }

                if(countmort<3){
                    res = mDatabaseHelper.insertIntomortality(selectedOrganizationId, selectedWarehouseId, report_date, employeeid);

                    if(res){
                        resline = mDatabaseHelper.insertIntomortalityline( doctype, documentno,inputData,mortqty,selectedProductID,selectedproductcode, selectedProduct,
                                selectedmortalitytype,selectedmortalityname ,selectedblockid, selectedplotid, employeeid, remarks);

                        if(resline){
                            toastMessage("Done successfully");
                            morqtyeditvw.setText("");
                            loadMortalityReasonSpinner();
                            reasonnameArrayList.clear();
                            loadMortalityReasonnameSpinner();
                            imagelayoutvw.setVisibility(View.GONE);
                            loadBlockSpinner();
                            loadPlotSpinner();
                        }
                    }else{
                        toastMessage("Something is wrong");
                        loadMortalityReasonSpinner();
                        reasonnameArrayList.clear();
                        loadMortalityReasonnameSpinner();
                        loadBlockSpinner();
                        loadPlotSpinner();
                    }
                }else{
                    toastMessage("You cannot add more than two products.");
                }
                ViewProductLayout();
            }
        });

        checkhistorybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MortalityActivity.this, CheckHistory.class);
                intent.putExtra("selectedProductID", selectedProductID);
                startActivity(intent);
            }
        });

        browsebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(MortalityActivity.this)
                        .crop()
                        .compress(300)
                        // .cameraOnly()
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        sendtoserverbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AysncSerial asyncSendToServer=new AysncSerial();
                asyncSendToServer.execute();
            }
        });
    }

    private void ViewProductLayout(){
        Cursor cursor = mDatabaseHelper.getcountmortality(employeeid);

        if(cursor.moveToFirst()){
            do{
                cmort = Integer.parseInt(cursor.getString(0));
            }while(cursor.moveToNext());
        }

        if(cmort<1){
            proviewlayoutvw.setVisibility(View.GONE);
        }else{
            proviewlayoutvw.setVisibility(View.VISIBLE);
        }
        GetMortality();
    }

    private void GetMortality(){
        if(!productmortArrayList.isEmpty()) {
            productmortArrayList.clear();
        }

        Cursor cursor = mDatabaseHelper.getmortalityline(employeeid);

        if(cursor.moveToFirst()){
            do{
                String pro_code = cursor.getString(11);
                String pro_name = cursor.getString(10);
                String m_product_id = cursor.getString(5);
                String mor_qty = cursor.getString(6);

                ViewMortalityListModel orderListModal = new ViewMortalityListModel(m_product_id, pro_code,pro_name,mor_qty);
                productmortArrayList.add(orderListModal);

           //     Bitmap bit = convertByteArrayToBitmap(imageBytes);
          //      viewimage.setImageBitmap(bit);
            }while(cursor.moveToNext());
        }
        initRecyclerView();
    }

    public void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        productrecyclerview.setLayoutManager(linearLayoutManager);
        ShowMortality adapter = new ShowMortality(productmortArrayList, MortalityActivity.this);
        productrecyclerview.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MortalityActivity.RESULT_OK) {
            uri = data.getData();
            try {
                iStream = getContentResolver().openInputStream(uri);
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                viewimage.setImageBitmap(bitmap);
                imagelayoutvw.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputData = getBytes(iStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkphoto = 1;
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void FetchDocType(){
        AsyncDoctype asyncDoctype=new AsyncDoctype();
        asyncDoctype.execute();
    }

    public void FetchBlockSpinner(){
        AsyncBlock asyncBlock=new AsyncBlock();
        asyncBlock.execute();
    }

    public void onClickDocumentNo(View view){
        documentno = documentnoeditvw.getText().toString();
        if(documentno.equals("")){
            documentnoeditvw.setError("Please enter a document no");
            return;
        }

        if(selectedProject.equals("--Select Doctype--")){
            toastMessage("Please select a document type");
            return;
        }

        AsyncDocNum asyncDocNum=new AsyncDocNum();
        asyncDocNum.execute();
    }

    public void onClickCalendarPicker(View view) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MortalityActivity.this,
                dateListener,
                year, month, day);
        datePickerDialog.show();
    }
    public void showAlert(String title,String message){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(MortalityActivity.this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private final class AsyncDocNum extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            String res = "", error = "";

            try {
                HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
                con.setRequestMethod("POST");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

                List<NameValuePair> list=new ArrayList<NameValuePair>();

                list.add(new BasicNameValuePair("name", "DocNumValidation"));
                list.add(new BasicNameValuePair("docbasetype",selectedprojectid));
                list.add(new BasicNameValuePair("doctype",selectedProject));
                list.add(new BasicNameValuePair("docnum",documentno));
                list.add(new BasicNameValuePair("c_doctype_id", c_doctype_id));

                httpPost.setEntity(new UrlEncodedFormEntity(list));
                HttpResponse httpResponse=	httpClient.execute(httpPost);

                HttpEntity httpEntity=httpResponse.getEntity();
                String s= readResponse(httpResponse);
                if (s.isEmpty() || !s.startsWith("[")){
                    res="Document No Not Found";
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

                    String doc = (String) line.get(0);

                    if((documentno.toUpperCase()).equals(doc.toUpperCase())){
                        res = "Document is correct";
                    }
                }
            } catch (Exception e) {
                res = e.getMessage();
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MortalityActivity.this);
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

            if (result.equals("Document is correct")) {
                toastMessage("Document Number is correct");
            } else {
                toastMessage("Wrong Document Number");
                documentnoeditvw.setText("");
            }
        }
    }

    private final class AsyncDoctype extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            String result = "", error = "";
            try {
                result = SYNCProject();
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

            pd = new ProgressDialog(MortalityActivity.this);
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

            if (result.equals("Project Sync Successful")) {
                loadProjectSpinner();
                loadParentSpinner();
                getreasonSpinner();
            } else {
                toastMessage(result);
            }
        }
    }

    private final class AsyncBlock extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            String result = "", error = "";
            try {
                result = SYNCBLock();
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

            pd = new ProgressDialog(MortalityActivity.this);
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

            if (result.equals("Block Sync Successful")) {
                loadBlockSpinner();
            } else {
                toastMessage(result);
            }
        }
    }

    private final class AsyncPlot extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            String result = "", error = "";
            try {
                result = SYNCPlot();
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

            pd = new ProgressDialog(MortalityActivity.this);
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

            if (result.equals("Plot Sync Successful")) {
                loadPlotSpinner();
            } else {
                toastMessage(result);
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
        } catch (Exception e)
        {
            a = "Error" + e.getMessage();
            return a;
        }
        return a;

    }

    public  String SYNCProject() {
        String res = "";

        res = FetchAllProject();

        try {
            return res;
        } catch (Exception e){
            res=e.getMessage();
        }
        return res;
    }

    public  String SYNCBLock() {
        String res = "";

        res = FetchAllBlocks();

        try {
            return res;
        } catch (Exception e){
            res=e.getMessage();
        }
        return res;
    }

    public  String SYNCPlot() {
        String res = "";

        res = FetchAllPlots();

        try {
            return res;
        } catch (Exception e){
            res=e.getMessage();
        }
        return res;
    }

    private String FetchAllProject() {

        String res = "", doctype="", docbasetype="", doc_id;
        boolean result = false;

        if(!ProjectArrayList.isEmpty()){
            ProjectArrayList.clear();
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

            list.add(new BasicNameValuePair("name", "Doctype"));

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
                doctype=(String) line.get(0);
                docbasetype =(String) line.get(1);
                doc_id = (String) line.get(2);

                ProjectArrayList.add(doctype);
                hashProjectId.put(doctype,docbasetype);
                hashDocTypeId.put(doctype, doc_id);

                result = true;
            }

            if (result) {
                res = "Project Sync Successful";

            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }

    private String FetchAllBlocks() {

        String res = "", c_projectphase_id="", block_name="";
        boolean result = false;

        if(!BlockArrayList.isEmpty()){
            BlockArrayList.clear();
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

            list.add(new BasicNameValuePair("name", "blocks"));
            list.add(new BasicNameValuePair("ad_org_id",selectedOrganizationId));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Blocks are Not Found";
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
                c_projectphase_id=(String) line.get(0);
                block_name =(String) line.get(1);

                BlockArrayList.add(block_name);
                hashBlockId.put(block_name,c_projectphase_id);

                result = true;
            }

            if (result) {
                res = "Block Sync Successful";

            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }

    private String FetchAllPlots() {

        String res = "", t_plot_id="", plot_name="";
        boolean result = false;

        if(!PlotArrayList.isEmpty()){
            PlotArrayList.clear();
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

            list.add(new BasicNameValuePair("name", "plots"));
            list.add(new BasicNameValuePair("t_block_id",selectedblockid));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Plots are Not Found";
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
                t_plot_id=(String) line.get(0);
                plot_name =(String) line.get(1);

                PlotArrayList.add(plot_name);
                hashPlotId.put(plot_name,t_plot_id);

                result = true;
            }

            if (result) {
                res = "Plot Sync Successful";

            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }

    private void loadProjectSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ProjectArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        doctypespinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        selectProjectSpinner();
    }

    private void selectProjectSpinner() {
        doctypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedProject = parent.getSelectedItem().toString();
                    selectedprojectid = hashProjectId.get(selectedProject);
                    c_doctype_id = hashDocTypeId.get(selectedProject);
                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void loadBlockSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, BlockArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        blockspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        selectBlockSpinner();
    }

    private void selectBlockSpinner() {
        blockspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedBlock = parent.getSelectedItem().toString();
                    selectedblockid = hashBlockId.get(selectedBlock);

                    if(!selectedBlock.equals("--Select Block--")){
                        AsyncPlot asyncPlot=new AsyncPlot();
                        asyncPlot.execute();
                    }
                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void loadPlotSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, PlotArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        plotspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();

        selectPlotSpinner();
    }

    private void selectPlotSpinner() {
        plotspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedPlot = parent.getSelectedItem().toString();
                    selectedplotid = hashPlotId.get(selectedPlot);

                    if(!selectedplotid.equals("1")){

                    }
                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadParentSpinner(){
        List<String> ParentList=new ArrayList<>();
        ParentList.add("FINISHED GOODS");
        ParentList.add("RAW MATERIALS");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ParentList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parentcatspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectParentSpinner();
    }
    private void selectParentSpinner() {
        parentcatspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedparent = parent.getSelectedItem().toString();

                    mDatabaseHelper.deleteProductCatagory();
                    mDatabaseHelper.deleteProducts();
                    AsyncProductcat asyncProductcat=new AsyncProductcat();
                    asyncProductcat.execute();

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private final class AsyncProductcat extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            String result = "", error = "";
            try {
                result = syncproductcat();
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

            pd = new ProgressDialog(MortalityActivity.this);
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

            if (result.equals("Product Catagory Sync Successful")) {
                toastMessage("SYNC Successful");
                getProductCat();
            } else {
                toastMessage(result);
            }
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public  String syncproductcat() {
        String res = "";
        try {
            mDatabaseHelper.deleteProductCatagory();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }
        try {
            mDatabaseHelper.deleteProducts();
        } catch (Exception e) {
            res = e.getMessage();
            return res;
        }
        res = FetchAllOrganization();
        try {
            return res;

        } catch (Exception e){
            res=e.getMessage();
        }
        return res;
    }

    private String FetchAllOrganization() {
        String parent_cat_id="";
        if(selectedparent.equals("RAW MATERIALS")) {
            parent_cat_id="1000001";
        }
        else if(selectedparent.equals("FINISHED GOODS")) {
            parent_cat_id="1000002";
        }

        String res = "";

        boolean result = false;
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();

            list.add(new BasicNameValuePair("name", "product_catagory"));
            list.add(new BasicNameValuePair("parent_cat_id",parent_cat_id));
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse httpResponse=	httpClient.execute(httpPost);

            HttpEntity httpEntity=httpResponse.getEntity();
            String s= readResponse(httpResponse);
            if (s.isEmpty() || !s.startsWith("[")){
                res="Product Catagory Not Foundd";
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
                int catagoryId=(Integer.parseInt((String) line.get(0)));
                String catagoryName=(String) line.get(1);

                result = mDatabaseHelper.insertIntoProductCatagory(catagoryId, catagoryName);
            }

            if (result) {
                res = "Product Catagory Sync Successful";
            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }

    private void getreasonSpinner(){
        if(!reasontypeArrayList.isEmpty()) {
            reasontypeArrayList.clear();
        }

        Cursor cursor = mDatabaseHelper.getMortalitype();

        if(cursor.moveToFirst()){
            do{
                String reason_type   = cursor.getString(0);
                reasontypeArrayList.add(reason_type);
            }while(cursor.moveToNext());
        }
        loadMortalityReasonSpinner();
    }
    private void getProductCat() {
        if(!ProductCatArrayList.isEmpty()) {
            ProductCatArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getProductCategoryCursor();

        if(cursor.moveToFirst()){
            do{
                String   id   = cursor.getString(0);
                String   name   = cursor.getString(1);
                ProductCatArrayList.add(name);
                hashProductCatId.put(name,id);
            }while(cursor.moveToNext());
        }
        loadProductCatSpinner();
    }

    private void loadMortalityReasonSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, reasontypeArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasontypespinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectMortalityReasonSpinner();
    }

    private void loadMortalityReasonnameSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, reasonnameArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectMortalityReasonnameSpinner();
    }

    private void selectMortalityReasonnameSpinner(){
        reasonspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedmortalityname = parent.getSelectedItem().toString();

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void selectMortalityReasonSpinner(){
        reasontypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedmortalitytype = parent.getSelectedItem().toString();

                    if(!selectedmortalitytype.equals("--Select Reason--")){
                        if(!reasonnameArrayList.isEmpty()){
                            reasonnameArrayList.clear();
                        }

                        Cursor cursor = mDatabaseHelper.getMortalityname(selectedmortalitytype);

                        if(cursor.moveToFirst()){
                            do{
                                String reason_name   = cursor.getString(0);
                                reasonnameArrayList.add(reason_name);
                            }while(cursor.moveToNext());
                        }
                        loadMortalityReasonnameSpinner();
                    }
                    loadMortalityReasonnameSpinner();

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadProductCatSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ProductCatArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productcatspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectProductCatSpinner();
    }

    private void selectProductCatSpinner(){
        productcatspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedproductcat = parent.getSelectedItem().toString();
                    selectedproductcatid = hashProductCatId.get(selectedproductcat);
                    AsyncProduct asyncProduct=new AsyncProduct();
                    asyncProduct.execute();

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
                e.printStackTrace();
                return error;
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MortalityActivity.this);
            pd.setTitle("Please Wait ");
            pd.setMessage("Loading Product ...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.dismiss();

            if (result.equals("Product Sync Successful")) {
             //   toastMessage("Product Sync Successful");
                getProduct();
            } else {
             //   toastMessage(result);
                mDatabaseHelper.deleteProducts();
                getProduct();
            }
        }
    }

    private void getProduct() {
        if(!productArrayList.isEmpty()) {
            productArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getProductCursor(selectedproductcatid);

        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(1);
                String proid=cursor.getString(0);
                String uom=cursor.getString(4);
                String value = cursor.getString(5);

                hashProductID.put(name,proid);
                hashProductCode.put(name, value);
                productArrayList.add(name);
            }while(cursor.moveToNext());
        }
        loadProductSpinner();
    }

    private void loadProductSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, productArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productspinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        searchCustomer();

        selectProductSpinner();
    }

    private String  FetchProductsFromServer2() {
        mDatabaseHelper.deleteProducts();
        boolean result = false;
        hashProductID.clear();

        String name, productID, price, res = "";
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(""+apiUrl+"/DBSync")).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            HttpClient httpClient=new DefaultHttpClient();
            HttpPost httpPost=new HttpPost(""+apiUrl+"/DBSync");

            List<NameValuePair> list=new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("name", "allproduct"));
            list.add(new BasicNameValuePair("pro_cat_id", selectedproductcatid));
            list.add(new BasicNameValuePair("ad_org_id", selectedOrganizationId));
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

            String[] parts = s.split("]_");
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
                productID=((String) line.get(0));
                name=(String) line.get(1);
                String unitprice=(String) line.get(2);
                String uom=(String) line.get(3);
                String searchkey=(String) line.get(4);
                String avlqty = (String) line.get(5);

                result = mDatabaseHelper.insertIntoProduct(productID, name, Integer.parseInt(selectedproductcatid), unitprice,uom,searchkey,avlqty);
                hashProductID.put( name, productID);
            }

            if (result) {
                res = "Product Sync Successful";
            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }

    private void searchCustomer(){
        search_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(searchcodebox.isChecked()) {
                    String searchKey = search_editText.getText().toString();

                    List<String> customerList = (List<String>) mDatabaseHelper.searchCustomerByCode(searchKey);

                    if (!customerList.isEmpty()) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MortalityActivity.this, android.R.layout.simple_spinner_item, customerList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        productspinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                    } else {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MortalityActivity.this, android.R.layout.simple_spinner_item, productArrayList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        productspinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                    }
                }else{
                    String searchKey = search_editText.getText().toString();

                    List<String> customerList = (List<String>) mDatabaseHelper.searchCustomerByName(searchKey);

                    if(!customerList.isEmpty()){
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MortalityActivity.this, android.R.layout.simple_spinner_item, customerList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        productspinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                    }else{
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MortalityActivity.this, android.R.layout.simple_spinner_item, productArrayList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        productspinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void selectProductSpinner(){
        productspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    selectedProduct = parent.getSelectedItem().toString();
                    selectedProductID = hashProductID.get(selectedProduct);
                    selectedproductcode = hashProductCode.get(selectedProduct);

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    public Bitmap convertByteArrayToBitmap(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
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
                list.add(new BasicNameValuePair("name", "mortalityserial"));

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

            pd = new ProgressDialog(MortalityActivity.this);
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

    public void generateSerial() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String newdate = sdf.format(selectedDate);

        String[] words = selectedOrganizationName.split(" ");

        String org = words[2].substring(0, 3).toUpperCase();

        if(oldserial.isEmpty()|| oldserial.equals("")) {
            finalserial=org+"-"+"MOR"+"-"+newdate+"-"+"1";
        } else{
            String[] parts = oldserial.split("-");

            olddate=parts[2];

            if(olddate.equals(newdate)) {
                int olddateserial=Integer.parseInt(parts[3]);

                int incrementedNum=olddateserial+1;

                finalserial=org+"-"+"MOR"+"-"+newdate+"-"+incrementedNum;
            } else {
                finalserial=org+"-"+"MOR"+"-"+newdate+"-"+"1";
            }
        }
        AsyncSendToServer sendToServer=new AsyncSendToServer();
        sendToServer.execute();
    }

    private final class AsyncSendToServer extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            try {

                Cursor mortality = mDatabaseHelper.getmortality(employeeid);

                ArrayList salesOrder=new ArrayList();
                while (mortality.moveToNext()) {
                    String ad_org_id = mortality.getString(1);
                    String m_warehouse_id = mortality.getString(2);
                    String report_date = mortality.getString(3);

                    Vector headerLines = new Vector();

                    headerLines.add(ad_org_id);
                    headerLines.add(m_warehouse_id);
                    headerLines.add(report_date);
                    headerLines.add(finalserial);
                    headerLines.add(employeeid.replace("_", "!@"));

                    salesOrder.add(headerLines);

                    String headerString = "";
                    for (Object s : salesOrder) {
                        if (headerString.isEmpty()) {
                            headerString = headerString + s;
                        } else {
                            headerString = headerString + "_" + s;
                        }
                    }

                    Cursor mortalityLine = mDatabaseHelper.getmortalityline(employeeid);

                    ArrayList salesOrderline = new ArrayList();

                    while (mortalityLine.moveToNext()) {
                        String doctype = mortalityLine.getString(1);
                        String docno = mortalityLine.getString(2);
                        String t_block_id = mortalityLine.getString(3);
                        String t_plot_id = mortalityLine.getString(4);
                        String m_product_id = mortalityLine.getString(5);
                        String qty = mortalityLine.getString(6);
                        byte[] image = mortalityLine.getBlob(7);
                        String reasontype = mortalityLine.getString(8);
                        String reasonname = mortalityLine.getString(9);
                        String pro_name = mortalityLine.getString(10);
                        String pro_code = mortalityLine.getString(11);
                        String remark = mortalityLine.getString(13);

                        if(t_plot_id.equals("")){
                            t_plot_id = "1";
                        }

                        Vector orderLines = new Vector();

                        orderLines.add(doctype);
                        orderLines.add(docno);
                        orderLines.add(t_block_id);
                        orderLines.add(t_plot_id);
                        orderLines.add(m_product_id);
                        orderLines.add(qty);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            orderLines.add(Base64.getEncoder().encodeToString(image));
                        }
                        orderLines.add(reasontype);
                        orderLines.add(reasonname);
                        orderLines.add(pro_name);
                        orderLines.add(pro_code);
                        orderLines.add(remark);

                        salesOrderline.add(orderLines);
                    }

                    String lineString = "";
                    for (Object s : salesOrderline) {
                        if (lineString.isEmpty()) {
                            lineString = lineString + s;
                        } else {
                            lineString = lineString + "_" + s;
                        }
                    }

                    HttpURLConnection con = (HttpURLConnection) (new URL("" + apiUrl + "/Mortlity")).openConnection();
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.connect();

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("" + apiUrl + "/Mortlity");

                    List<NameValuePair> list = new ArrayList<NameValuePair>();
                    list.add(new BasicNameValuePair("type", "Mortlity"));
                    list.add(new BasicNameValuePair("header", headerString));
                    list.add(new BasicNameValuePair("lines", lineString));
                    httpPost.setEntity(new UrlEncodedFormEntity(list));
                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();
                    String s = readResponse(httpResponse);

                    if (s.isEmpty() || !s.equals("Success")) {
                        result = s;
                        return result;
                    } else if (s.equals("Success")) {
                        return "Success";
                    }
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
                    toastMessage("Saved to server Successfully");
                    toastMessage("Your serial : "+finalserial);
                    mDatabaseHelper.deletemortality();
                    mDatabaseHelper.deletemortalityline();

                    Intent i=new Intent(MortalityActivity.this, Menu.class);
                    startActivity(i);

                    finish();
                }else if(s.equals("noOrderLine")){
                    toastMessage("No mortality exits");
                } else{
                    toastMessage("Server Connection Failed Please Send Again"+s);
                }
            }catch (NullPointerException ex){}
        }
    }
}