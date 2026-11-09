package com.sysnova.bracnurseryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedOrganizationId;
import static com.sysnova.bracnurseryapp.OrgBranchSelection.selectedWarehouseId;

import static com.sysnova.bracnurseryapp.PostgreSQL.apiUrl;

public class ChooseProductActivity extends AppCompatActivity {
    Spinner orgSpinner,branchSpinner,parentSpinner;
    DatabaseHelper mDatabaseHelper;
    boolean doubleBackToExitPressedOnce=false;
    public static String selectedOrgId="",selectedWHId="", selectedproductID = "";
    String selectedProductPrice="", availableqtyonhand;
    CheckBox codeCheckbox;
    EditText search_editText;
    List<String> orgNameArrayList = new ArrayList<String>();
    List<String> branchNameArrayList = new ArrayList<String>();
    ArrayList<String> productArrayList = new ArrayList<String>();
    HashMap<String,String> hashOrgId  = new HashMap<>();
    HashMap<String,String> hashBranchId  = new HashMap<>();
    HashMap<String,String> hasProductPrice  = new HashMap<>();
    HashMap<String,String> hasProductid  = new HashMap<>();
    String logged_user_id="",selectedProduct="",selectedparent="";
    TextView avlqtyvw;
    LinearLayout avllayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_product);

        getSupportActionBar().setTitle("Select Product");
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#97c740")));

        search_editText = findViewById(R.id.search_customer_editText);
        codeCheckbox    = findViewById(R.id.search_code_checkbox);
        orgSpinner    = findViewById(R.id.org_spinner);
        branchSpinner = findViewById(R.id.branch_spinner);
        parentSpinner=findViewById(R.id.parent_cat_spinner);
        avlqtyvw = findViewById(R.id.avlqty_vw);
        avllayout = findViewById(R.id.avlqtylayout);

        mDatabaseHelper=new DatabaseHelper(ChooseProductActivity.this);
        loadParentSpinner();
    }
    public void callConfirm(View view) {
       callAddProduct();
    }

    public void callCancel(View view) {
        Intent i=new Intent(ChooseProductActivity.this,HowToOrder.class);
        startActivity(i);
    }

    public void callAddProduct( ) {

        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        int id =sh.getInt("NUM",0);
        int id1=id+1;

        boolean result=false;

        //getProductbyCode added by anika on 2025-06-24
        Cursor cursor = mDatabaseHelper.getProductbyCode(selectedProduct);

        if (cursor.moveToNext()) {
            CreateAlert();
        }else {
            result = mDatabaseHelper.insertIntoProductOrderLine(id1, "1000911",
                    selectedProduct, 1, Double.parseDouble(selectedProductPrice), Double.parseDouble(selectedProductPrice));

            if (result) {
                toast("Product Inserted");
                System.out.println(id + " " + "productcodeV" + " " + selectedProduct + " " + "productqtyV" + " " + "priceperunitV" + " " + "totalCostV");
                Log.d("ERROR", id1 + " " + "productcodeV" + " " + selectedProduct + " " + "productqtyV" + " " + "priceperunitV" + " " + "totalCostV");
                SharedPreferences.Editor editor = sh.edit();

                editor.putInt("NUM", id1);
                editor.commit();
                // toast(String.valueOf(sh.getInt("NUM",0)));//not showing this toast ! daya pata lagao !
                Intent i = new Intent(ChooseProductActivity.this, ProductOrderLine.class);

                startActivity(i);

            } else {
                toast("Could not insert into internal database");
            }
        }
    }
    void toast(String msg)
    {
        Toast.makeText(ChooseProductActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
    private void loadOrganizationSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, orgNameArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orgSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectOrgSpinner();
    }
    private void loadWarehouseSpinner(){

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, branchNameArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }
    private void loadParentSpinner(){
        List<String> ParentList=new ArrayList<>();
        ParentList.add("FINISHED GOODS");
        ParentList.add("RAW MATERIALS");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, ParentList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        parentSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        selectParentSpinner();

    }
    private void getOrg() {
        if(!orgNameArrayList.isEmpty()) {
            orgNameArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getProductCategoryCursor();

        if(cursor.moveToFirst()){
            do{
                String   id   = cursor.getString(0);
                String   name   = cursor.getString(1);
                orgNameArrayList.add(name);
                hashOrgId.put(name,id);
            }while(cursor.moveToNext());
        }
        loadOrganizationSpinner();
    }



    private void getWH()
    {
        if(!branchNameArrayList.isEmpty())
        {
            branchNameArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getProductCursor(selectedOrgId);

        if(cursor.moveToFirst()){
            do{
                String   name   = cursor.getString(1);
                String price=cursor.getString(3);
                Log.d("DEBUG","price from db"+price);
                String uom=cursor.getString(4);
                branchNameArrayList.add(name);
              //  hashBranchId.put(name,warehouseID);
                hasProductPrice.put(name,price);
            }while(cursor.moveToNext());
        }
        loadWarehouseSpinner();
    }
    private void selectOrgSpinner(){
        orgSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedOrg = parent.getSelectedItem().toString();
                    selectedOrgId = hashOrgId.get(selectedOrg);
                    AsyncProduct asyncProduct=new AsyncProduct();
                    asyncProduct.execute();

                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void selectParentSpinner() {
        parentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedBranch = parent.getSelectedItem().toString();
                    selectedparent=selectedBranch;
               //     toastMessage("selectedparent="+selectedparent);
                    mDatabaseHelper.deleteProductCatagory();
                    mDatabaseHelper.deleteProducts();
                    AsyncSender asyncSender=new AsyncSender();
                    asyncSender.execute();


                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void selectWHSpinner(){
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String selectedBranch = parent.getSelectedItem().toString();
                    selectedProduct=selectedBranch;
                    selectedProductPrice=hasProductPrice.get(selectedProduct);
                    selectedWHId = hashBranchId.get(selectedBranch);
                    selectedproductID = hasProductid.get(selectedProduct);
                    Cursor cursor = mDatabaseHelper.getProductbyID(selectedproductID);

                    if(cursor.moveToFirst()){
                        do{
                            availableqtyonhand = cursor.getString(6);
                        }while(cursor.moveToNext());
                        avlqtyvw.setText(availableqtyonhand);
                        avllayout.setVisibility(View.VISIBLE);
                    }


            //        toastMessage("Branch="+selectedBranch+" id="+selectedWHId);
          //          toastMessage("Selected Product price"+selectedProductPrice);


                }catch (NullPointerException ex){

                }catch (ArrayIndexOutOfBoundsException ex){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public  String SYNCORGBRANCH()
    {
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
        if(selectedparent.equals("RAW MATERIALS"))
        {
            parent_cat_id="1000001";
        }
        else if(selectedparent.equals("FINISHED GOODS"))
        {
            parent_cat_id="1000002";
        }


        String catagoryname, res = "";

        int id;
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


            String ad_user_id="",m_warehouse_id="";
            for(int i1=0;i1<list4.size();i1++){
                Vector line= (Vector) list4.get(i1);
                int catagoryId=(Integer.parseInt((String) line.get(0)));
                String catagoryName=(String) line.get(1);

                result = mDatabaseHelper.insertIntoProductCatagory(catagoryId, catagoryName);

            }

            if (result) {

                //toastMessage("Insert  Successful into Product Catagory Table : ");
                res = "Product Catagory Sync Successful";


            }


        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Failed to Data fetch",Toast.LENGTH_SHORT).show();
            res = e.getMessage();


        }
        return res;
    }

    private final class AsyncSender extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected String doInBackground(String... params) {
            String result = "", error = "";
            try {
                result = SYNCORGBRANCH();
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

            pd = new ProgressDialog(ChooseProductActivity.this);
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
                getOrg();
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

    private String  FetchProductsFromServer2() {
        mDatabaseHelper.deleteProducts();
        int catagoryID;
        boolean result = false;
        hasProductid.clear();

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
            list.add(new BasicNameValuePair("pro_cat_id", selectedOrgId));
        //    list.add(new BasicNameValuePair("ad_org_id", s_ad_org_id));
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

                result = mDatabaseHelper.insertIntoProduct(productID, name, Integer.parseInt(selectedOrgId), unitprice,uom,searchkey,avlqty);
                hasProductid.put( name, productID);
            }

            if (result) {
                res = "Product Sync Successful";
            }
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
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

            pd = new ProgressDialog(ChooseProductActivity.this);
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
                toastMessage("Product Sync Successful");
                getProduct();
            } else {
                toastMessage(result);
                mDatabaseHelper.deleteProducts();
                loadWarehouseSpinner();
                avllayout.setVisibility(View.GONE);
            }
        }
    }
    private void getProduct() {
        if(!productArrayList.isEmpty()) {
            productArrayList.clear();
        }
        Cursor cursor = mDatabaseHelper.getProductCursor(selectedOrgId);

        if(cursor.moveToFirst()){
            do{
                String name = cursor.getString(1);
                String price=cursor.getString(3);
                String uom=cursor.getString(4);
                String value = cursor.getString(5);

                hasProductPrice.put(name,price);
                productArrayList.add(name);
            }while(cursor.moveToNext());
        }
        loadProductSpinner();

    }
    private void loadProductSpinner() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, productArrayList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        branchSpinner.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
        searchCustomer();

        selectWHSpinner();
    }





    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {


      Intent i   =new Intent(ChooseProductActivity.this,HowToOrder.class);
      startActivity(i);
    }

    public void ShowAlert(String title,String msg){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(ChooseProductActivity.this);

        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(title);
        dlgAlert.setIcon(R.drawable.braclogo);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void CreateAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseProductActivity.this);

        builder.setTitle("Alert!");
        builder.setMessage("This Product Is Already Added!!!");
        builder.setCancelable(false);

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        Intent i = new Intent(ChooseProductActivity.this, ProductOrderLine.class);
                        startActivity(i);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void searchCustomer(){
        search_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(codeCheckbox.isChecked()) {
                    String searchKey = search_editText.getText().toString();

                    List<String> customerList = (List<String>) mDatabaseHelper.searchCustomerByCode(searchKey);

                    if (!customerList.isEmpty()) {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChooseProductActivity.this, android.R.layout.simple_spinner_item, customerList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        branchSpinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                    } else {
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChooseProductActivity.this, android.R.layout.simple_spinner_item, branchNameArrayList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        branchSpinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                    }
                }else{
                    String searchKey = search_editText.getText().toString();

                    List<String> customerList = (List<String>) mDatabaseHelper.searchCustomerByName(searchKey);

                    if(!customerList.isEmpty()){
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChooseProductActivity.this, android.R.layout.simple_spinner_item, customerList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        branchSpinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                    }else{
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChooseProductActivity.this, android.R.layout.simple_spinner_item, branchNameArrayList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        branchSpinner.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



}