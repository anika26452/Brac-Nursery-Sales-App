package com.sysnova.bracnurseryapp;

import static com.sysnova.bracnurseryapp.MainActivity.employeeid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "productorderline";
    private static final String COL0 = "productId";
    private static final String COL1 = "productCode";
    private static final String COL2 = "productName";
    private static final String COL3 = "productQTY";
    private static final String COL4 = "pricePerUnit";
    private static final String COL5 = "totalCost";

    private static final String DATABASE_NAME = "bracnursery";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 9);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createProductOrderLineTable = "CREATE TABLE " + TABLE_NAME +
                " (productId Integer, productCode TEXT , " +
                COL2 +" TEXT, " +
                COL3 +" INTEGER, " +
                COL4 +" REAL,"+COL5+" REAL)";
        String createProductTable = "CREATE TABLE " + "product" + " (productID TEXT Unique, " +
                "productName" +" TEXT, " +
                "catagoryID" +" INTEGER, " +
                "price" +" TEXT,"+"unit"+" TEXT,"+"availableQty"+" TEXT,onhandQty Text)";
        String createProductCatagoryTable="CREATE TABLE product_catagory(catagoryID INTEGER PRIMARY KEY," +
                "catagoryName TEXT)";

        String createSalesOrderTable="CREATE TABLE sales_order(docNo TEXT not null unique," +
                "date TEXT,chemist_code TEXT,status TEXT default 'draft',userid TEXT,delivary_date Text,description Text,promised_amount Text default '0',sales_type Text,billing_location Text)";

        String createSalesOrderLineTable="CREATE TABLE sales_orderline(orderline INTEGER PRIMARY KEY AUTOINCREMENT,docNo TEXT ," +
                "productID TEXT,quantity INTEGER,price Integer,product_name Text,unit_price Text,uom Text)";

        String createChemistProposalTable="CREATE TABLE chemist_proposal(chemistID INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "shopName TEXT,shopAddress TEXT,tradeLicenseNo TEXT,drugLicenseNo TEXT,contactPerson TEXT," +
                "mobile TEXT,email TEXT,nearbyHospital TEXT,estimatedDailySales TEXT,ownerName TEXT,ownerAddress TEXT)";

        String createDrTable="CREATE TABLE dr_information(dr_code TEXT ," +
                "drName TEXT,dr_address TEXT)";

        String createDrInfoTable="CREATE TABLE dr_proposal(ID INTEGER PRIMARY KEY AUTOINCREMENT,Name TEXT,qualification TEXT" +
                ",institution TEXT,specialisation TEXT,designation TEXT,Phone TEXT,EMail TEXT," +
                "mailingAddress TEXT,DOB TEXT,homeDistrict TEXT,spouseName TEXT,marriageDay TEXT,likings TEXT,dislikings TEXT,chamberAddress TEXT,practicingDay TEXT," +
                "MobileNumber TEXT,visitingHour TEXT,visitingTimeMPO TEXT,avgPatientNo_day TEXT,call_month TEXT,dr_status TEXT,productname TEXT)";

        String createEmployeeTable="CREATE TABLE employee(emp_id INTEGER PRIMARY KEY AUTOINCREMENT,ad_user_id TEXT,user_level TEXT,name Text)";

        String createChemistTable="CREATE TABLE chemist(chemist_code TEXT,chemist_name TEXT,chemist_address TEXT,customer_balance TEXT,phone Text)";

        String createPrescriptionTable="CREATE TABLE prescription(dr_code TEXT,product_name TEXT,image_name TEXT)";

        String createDCRTable="CREATE TABLE dcr(code TEXT,promo_tools TEXT,work_station TEXT)";

        String createPromoToolsTable="CREATE TABLE promo_tools(name TEXT,description TEXT)";

        String CreatePromoDetailsTable="CREATE TABLE promo(promo_id INTEGER PRIMARY KEY AUTOINCREMENT,bpcode TEXT,bpname TEXT,product_code TEXT," +
                "product_name TEXT,m_locatorto_id TEXT,m_warehouse_id TEXT,ad_user_id TEXT,movementqty INTEGER)";

        String createPromoOrderTable="CREATE TABLE promo_order(docNo TEXT not null unique," +
                "date TEXT,dr_code TEXT,status TEXT default 'draft',userid TEXT)";

        String createPromoOrderLineTable="CREATE TABLE promo_orderline(promo_orderline INTEGER PRIMARY KEY AUTOINCREMENT,docNo TEXT ," +
                "productID TEXT,quantity INTEGER)";

        String createOrganizationTable = "Create Table organization (id INTEGER PRIMARY KEY AUTOINCREMENT,ad_org_id Text,org_name Text)";
        String createWarehouseTable="Create Table warehouse (id INTEGER PRIMARY KEY AUTOINCREMENT,warehouse_id Text,ad_org_id Text,warehouse_name Text)";

        String createSalesTypeTable = "Create Table salesType(salesType_id INTEGER PRIMARY KEY AUTOINCREMENT,salesType_name Text, c_doctype_id TEXT)";

        String createOrderApprovalTable = "Create Table orderApproval(orderApproval_id INTEGER PRIMARY KEY AUTOINCREMENT,order_doc Text,erp_orderNo Text,grand_total Text,prepared_by text,sales_type Text,sent_date Text,doctype_id Text,orgid Text)";

        String createSalesOrderRecord="CREATE TABLE sales_order_record(docNo TEXT," +
                "date TEXT,customer_name TEXT,grand_total Text,product_name Text,uom Text,ordered_qty Text default '0',pricePerProduct Text default '0',status Text,orgid text)";
        String createSupervisorTable = "Create Table supervisor(userid Text,docid Text,supid Text,supname Text)";
        String createCustomerTable="Create Table customer(id INTEGER PRIMARY KEY AUTOINCREMENT,customer_id Text,customer_name   Text,code Text)";
        String createMortalityReasonTable = "Create Table MortalityReason (reason_type Text,reason_name Text)";
        String createMortalityTable="Create Table t_mortality(id INTEGER PRIMARY KEY AUTOINCREMENT,ad_org_id Text," +
                "m_warehouse_id Text,report_date Text, employeeid TEXT)";
        String createMortalityLineTable="Create Table t_mortalityline(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "doctype TEXT, docno text," +
                "t_block_id TEXT, t_plot_id TEXT, m_product_id TEXT, qty TEXT, image BLOB, reasontype TEXT, reasonname TEXT," +
                "pro_name TEXT, pro_code TEXT, employeeid TEXT, remarks TEXT)";

String createDateTable="Create Table date(date Text)";

        db.execSQL(createProductOrderLineTable);
        db.execSQL(createProductTable);
        db.execSQL(createProductCatagoryTable);
        db.execSQL(createSalesOrderTable);
        db.execSQL(createSalesOrderLineTable);
        db.execSQL(createChemistProposalTable);
        db.execSQL(createDrTable);
        db.execSQL(createDrInfoTable);
        db.execSQL(createEmployeeTable);
        db.execSQL(createChemistTable);
        db.execSQL(createPrescriptionTable);
        db.execSQL(createDCRTable);
        db.execSQL(createPromoToolsTable);
        db.execSQL(CreatePromoDetailsTable);
        db.execSQL(createPromoOrderTable);
        db.execSQL(createPromoOrderLineTable);
        db.execSQL(createOrganizationTable);
        db.execSQL(createSalesTypeTable);
        db.execSQL(createOrderApprovalTable);
        db.execSQL(createSalesOrderRecord);
        db.execSQL(createSupervisorTable);
        db.execSQL(createWarehouseTable);
        db.execSQL(createCustomerTable);
        db.execSQL(createMortalityReasonTable);
        db.execSQL(createDateTable);
        db.execSQL(createMortalityTable);
        db.execSQL(createMortalityLineTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP  TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP  TABLE IF EXISTS " + "product");
        db.execSQL("DROP  TABLE IF EXISTS  product_catagory");
        db.execSQL("DROP  TABLE IF EXISTS sales_order");
        db.execSQL("DROP  TABLE IF EXISTS sales_orderline");
        db.execSQL("DROP  TABLE IF EXISTS chemist_proposal");
        db.execSQL("DROP  TABLE IF EXISTS dr_information");
        db.execSQL("DROP  TABLE IF EXISTS dr_proposal");
        db.execSQL("DROP  TABLE IF EXISTS employee");
        db.execSQL("DROP  TABLE IF EXISTS chemist");
        db.execSQL("DROP  TABLE IF EXISTS prescription");
        db.execSQL("DROP  TABLE IF EXISTS dcr");
        db.execSQL("DROP  TABLE IF EXISTS promo_tools");
        db.execSQL("DROP  TABLE IF EXISTS promo");
        db.execSQL("DROP  TABLE IF EXISTS promo_order");
        db.execSQL("DROP  TABLE IF EXISTS promo_orderline");
        db.execSQL("DROP  TABLE IF EXISTS organization");
        db.execSQL("DROP  TABLE IF EXISTS salesType");
        db.execSQL("DROP  TABLE IF EXISTS orderApproval");
        db.execSQL("DROP  TABLE IF EXISTS sales_order_record");
        db.execSQL("DROP  TABLE IF EXISTS supervisor");
        db.execSQL("DROP  TABLE IF EXISTS warehouse");
        db.execSQL("DROP  TABLE IF EXISTS customer");
        db.execSQL("DROP  TABLE IF EXISTS MortalityReason");
        db.execSQL("DROP  TABLE IF EXISTS date");
        db.execSQL("DROP  TABLE IF EXISTS t_mortality");
        db.execSQL("DROP  TABLE IF EXISTS t_mortalityline");
        onCreate(db);
    }

    public boolean insertIntomortality(String ad_org_id, String m_warehouse_id ,String report_date, String employeeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ad_org_id", ad_org_id);
        contentValues.put("m_warehouse_id", m_warehouse_id);
        contentValues.put("report_date", report_date);
        contentValues.put("employeeid", employeeid);

        long result = db.insert("t_mortality",null,contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntomortalityline(String doctype ,String docno,byte[] image,String qty ,String m_product_id ,String pro_code,
                                       String pro_name,String reasontype,String reasonname ,String t_block_id ,String t_plot_id, String employeeid,
                                           String remarks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("doctype", doctype);
        contentValues.put("docno", docno);
        contentValues.put("t_block_id", t_block_id);
        contentValues.put("t_plot_id", t_plot_id);
        contentValues.put("m_product_id", m_product_id);
        contentValues.put("qty", qty);
        contentValues.put("image", image); // Corrected: Store byte[] instead of BLOB
        contentValues.put("reasontype", reasontype);
        contentValues.put("reasonname", reasonname);
        contentValues.put("pro_name", pro_name);
        contentValues.put("pro_code", pro_code);
        contentValues.put("employeeid", employeeid);
        contentValues.put("remarks", remarks);

        long result = db.insert("t_mortalityline",null,contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getmortalityline(String employeeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM t_mortalityline where employeeid = '"+employeeid+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getmortality(String employeeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM t_mortality where employeeid = '"+employeeid+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getcountmortality(String employeeid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT count(m_product_id) FROM t_mortalityline where employeeid = '"+employeeid+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deletemortality(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  t_mortality";
        db.execSQL(query);
    }

    public void deletemortalityline(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  t_mortalityline";
        db.execSQL(query);
    }

    public void deleteOneMortality(String m_product_id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM t_mortalityline where m_product_id='"+m_product_id+"'";
        db.execSQL(query);
    }

    public boolean insertIntoMortalityReason(String reason_type ,String reason_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("reason_type", reason_type);
        contentValues.put("reason_name", reason_name);

        long result = db.insert("MortalityReason",null,contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoOCustomer(String customer_id,String customer_name,String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_id", customer_id);
        contentValues.put("customer_name", customer_name);
        contentValues.put("code", code);
        long result = db.insert("customer",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoOrderRecord(String docNo,String date,String customer_name,String grand_total,String product_name,String uom,String ordered_qty,String pricePerProduct,String status,String orgid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("date", date);
        contentValues.put("customer_name", customer_name);
        contentValues.put("grand_total", grand_total);
        contentValues.put("product_name", product_name);
        contentValues.put("uom", uom);
        contentValues.put("ordered_qty", ordered_qty);
        contentValues.put("pricePerProduct", pricePerProduct);
        contentValues.put("status", status);
        contentValues.put("orgid", orgid);


        long result = db.insert("sales_order_record",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoOrderApproval(String order_doc,String erp_orderNo,String grand_total,String prepared_by,String sales_type,String sent_date,String doctype_id,String orgid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_doc", order_doc);
        contentValues.put("erp_orderNo", erp_orderNo);
        contentValues.put("grand_total", grand_total);
        contentValues.put("prepared_by", prepared_by);
        contentValues.put("sales_type", sales_type);
        contentValues.put("sent_date", sent_date);
        contentValues.put("doctype_id",doctype_id);
        contentValues.put("orgid",orgid);

        long result = db.insert("orderApproval",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    public boolean insertIntoProductOrderLine(int id,String code,String name,int qty,double price,double total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productId", id);
        contentValues.put("productCode", code);
        contentValues.put("productName", name);
        contentValues.put("productQTY", qty);
        contentValues.put("pricePerUnit", price);
        contentValues.put("totalCost", total);

        long result = db.insert(TABLE_NAME,null,contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getProductbyCode(String productCode) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" where productName = '"+productCode+"'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteMortalityReason() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  MortalityReason";
        db.execSQL(query);
    }
    public void deleteOrganization() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  organization";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public void deleteProductOrderLine()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderline";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }
    public void deleteWarehouse() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  warehouse";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteEmployee() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  employee";
        //Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public Cursor getProductOrderLine(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM productorderline";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getOrganization() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT DISTINCT ad_org_id,org_name FROM organization";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public void deleteProductCatagory(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  product_catagory";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public void deleteProducts(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  product";
        Log.d(TAG, "deleteName: query: " + query);
        db.execSQL(query);
    }

    public Cursor getBranch() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT branch_name,branch_id FROM organization where branch_name != 'TRANSIT WH' and branch_name != 'RAW MATERIALS WH'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public void deleteAllProductOrderLine(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderline";
       // Log.d(TAG, "deleteAllProductOrderLine: query: " + query);
        System.out.println("deleteAllProductOrderLine: query: " +query);
        db.execSQL(query);
    }

    public void deleteOneProductOrderLine(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  productorderline WHERE productId='"+id+"'";
      //  Log.d(TAG, "deleteOneProductOrderLine: query: " + query);
        System.out.println("deleteOneProductOrderLine: query: " +query);
        db.execSQL(query);
    }


    public boolean updateProductOrderLine(int id,int newQTY,double newTotal){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues args = new ContentValues();
        args.put("productQTY", newQTY);
        args.put("totalCost", newTotal);

        int i = db.update(TABLE_NAME, args, "productId = "+id, null);
        if( i > 0){
            return true;
        }
        return false;

    }
    public boolean insertIntoSalesType(String typename, String c_doctype_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("salesType_name", typename);
        contentValues.put("c_doctype_id", c_doctype_id);

        long result = db.insert("salesType", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    public boolean insertIntoOrganization(String ad_org_id,String org_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ad_org_id", ad_org_id);
        contentValues.put("org_name", org_name);


        long result = db.insert("organization", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoWarehouse(String warehouse_id,String ad_org_id,String warehouse_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("warehouse_id", warehouse_id);
        contentValues.put("ad_org_id", ad_org_id);
        contentValues.put("warehouse_name", warehouse_name);


        long result = db.insert("warehouse", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    public boolean insertIntoEmployee(String ad_user_id,String user_level,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ad_user_id", ad_user_id);
        contentValues.put("user_level", user_level);
        contentValues.put("name", name);



        long result = db.insert("employee", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntodrInfo(String dr_code,String name,String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dr_code", dr_code);
        contentValues.put("drName", name);
        contentValues.put("dr_address", address);



        long result = db.insert("dr_information",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoPromoTools(String name,String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", desc);



        long result = db.insert("promo_tools",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean insertIntoSupervisor(String userid,String docid,String supid,String supname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", userid);
        contentValues.put("docid", docid);
        contentValues.put("supid", supid);
        contentValues.put("supname", supname);


        long result = db.insert("supervisor",null,contentValues);


        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoSalesOrderLine(String docNo,String productID,String quantity,double price,String productName,String unitPrice,String uom) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("productID", productID);
        contentValues.put("quantity", quantity);
        contentValues.put("price", price);
        contentValues.put("product_name",productName);
        contentValues.put("unit_price",unitPrice);
        contentValues.put("uom",uom);
        long result = db.insert("sales_orderline", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }





    public boolean insertIntoSalesOrder(String docNo,String date,String chemID,String userID,String delivaryDate,String des,String amount,String salesType,String billingLocation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("date", date);
        contentValues.put("chemist_code", chemID);
        contentValues.put("userid", userID);
        contentValues.put("delivary_date", delivaryDate);
        contentValues.put("description", des);
        contentValues.put("promised_amount", amount);
        contentValues.put("sales_type", salesType);
        contentValues.put("billing_location", billingLocation);

        long result = db.insert("sales_order", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoPromoOrder(String docNo,String date,String drID,String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("date", date);
        contentValues.put("dr_code", drID);
        contentValues.put("userid", userID);



        long result = db.insert("promo_order", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoPromoOrderLine(String docNo,String productID,String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("docNo", docNo);
        contentValues.put("productID", productID);
        contentValues.put("quantity", quantity);


        long result = db.insert("promo_orderline", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntodrProposal(List<String> stringList){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("Name", stringList.get(0));
        contentValues.put("qualification", stringList.get(1));
        contentValues.put("institution", stringList.get(2));
        contentValues.put("specialisation", stringList.get(3));
        contentValues.put("designation", stringList.get(4));
        contentValues.put("Phone", stringList.get(5));
        contentValues.put("EMail", stringList.get(6));
        contentValues.put("mailingAddress", stringList.get(7));
        contentValues.put("DOB", stringList.get(8));
        contentValues.put("homeDistrict", stringList.get(9));
        contentValues.put("spouseName", stringList.get(10));
        contentValues.put("marriageDay", stringList.get(11));
        contentValues.put("likings", stringList.get(12));
        contentValues.put("dislikings", stringList.get(13));
        contentValues.put("chamberAddress", stringList.get(14));
        contentValues.put("practicingDay", stringList.get(15));
        contentValues.put("MobileNumber", stringList.get(16));
        contentValues.put("visitingHour", stringList.get(17));
        contentValues.put("visitingTimeMPO", stringList.get(18));
        contentValues.put("avgPatientNo_day", stringList.get(19));
        contentValues.put("call_month", stringList.get(20));
        contentValues.put("dr_status", stringList.get(21));
        contentValues.put("productname", stringList.get(22));


        long result = db.insert("dr_proposal", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoProduct(String productID,String productName,int catagoryID,String price,String unit,String searchkey,String onhandQty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("productID", productID);
        contentValues.put("productName", productName);
        contentValues.put("catagoryID", catagoryID);
        contentValues.put("price", price);
        contentValues.put("unit", unit);
        contentValues.put("availableQty", searchkey);
        contentValues.put("onhandQty", onhandQty);
        long result = db.insert("product", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }




    public boolean insertIntoProductCatagory(int catagoryID,String catagoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("catagoryID", catagoryID);
        contentValues.put("catagoryName", catagoryName);

        long result = db.insert("product_catagory", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public List<String> getProductCategory(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT catagoryID,catagoryName FROM product_catagory order by catagoryName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1)+" "+cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public boolean insertIntoDCR(String code,String promotools,String workstation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("promo_tools", promotools);
        contentValues.put("work_station", workstation);



        long result = db.insert("dcr", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoPrescription(String dr_code,String product,String image_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("dr_code", dr_code);
        contentValues.put("product_name", product);
        contentValues.put("image_name", image_name);



        long result = db.insert("prescription", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertIntoChemist(String chemist_code,String chemist_name,String address,String balance,String phoneNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("chemist_code", chemist_code);
        contentValues.put("chemist_name", chemist_name);
        contentValues.put("chemist_address", address);
        contentValues.put("customer_balance", balance);
        contentValues.put("phone", phoneNo);

        long result = db.insert("chemist", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertIntoPromo(String bp_code,String bp_name,String pr_code,String pr_name,String m_locator_id,String m_warehouse_id,String ad_userid,String movementqty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bpcode", bp_code);
        contentValues.put("bpname", bp_name);
        contentValues.put("product_code", pr_code);
        contentValues.put("product_name", pr_name);
        contentValues.put("m_locatorto_id", m_locator_id);
        contentValues.put("m_warehouse_id", m_warehouse_id);
        contentValues.put("ad_user_id", ad_userid);
        contentValues.put("movementqty", movementqty);



        long result = db.insert("promo", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }



    public boolean insertIntoChemistProposal(String chemistName,String shopAddress,String tradeLicenseNo,String drugLicenseNo,
                                             String contactPerson,String mobile,String email,String nearbyHospital,String estimatedDailySales,String ownerName,String ownerAddress)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("shopName", chemistName);
        contentValues.put("shopAddress", shopAddress);
        contentValues.put("tradeLicenseNo", tradeLicenseNo);
        contentValues.put("drugLicenseNo", drugLicenseNo);
        contentValues.put("contactPerson", contactPerson);
        contentValues.put("mobile", mobile);
        contentValues.put("email", email);
        contentValues.put("nearbyHospital", nearbyHospital);
        contentValues.put("estimatedDailySales", estimatedDailySales);
        contentValues.put("ownerName", ownerName);
        contentValues.put("ownerAddress", ownerAddress);



        long result = db.insert("chemist_proposal", null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */


    public List<String> searchProduct(String name){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product inner join product_catagory ON product.catagoryID=product_catagory.catagoryID where upper(product_catagory.catagoryName) = 'FINISHED GOODS' and  product.productName LIKE '%"+name+"%' Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> searchProductByCode(String code){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product  inner join product_catagory ON product.catagoryID=product_catagory.catagoryID where upper(product_catagory.catagoryName) = 'FINISHED GOODS' and product.productID LIKE '%"+code+"%'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }



    public List<String> searchCustomerByName(String name){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product where productName LIKE '"+name+"%' Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public Cursor getBPGroup() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM bpgroup  order by name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getProductCategoryCursor() {

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT catagoryID,catagoryName FROM product_catagory order by catagoryName";

        Cursor data = db.rawQuery(selectQuery, null);
        return data;
    }
    public Cursor getProductCursor(String catID) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM product order by productName";
        Log.d("DEBUG","query"+query);
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getProductbyID(String catID) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM product where productID = '"+catID+"'";
        Log.d("DEBUG","query"+query);
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getCustomer() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM customer  order by customer_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor searchCustomerByNameNew(String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM chemist  where chemist_name LIKE '%"+name+"%' Order by chemist_name";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public Cursor getWarehouseByOrg(String ad_org_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM warehouse  where ad_org_id ='"+ad_org_id+"' Order by warehouse_id";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public String getCustomerAddressByCode(String code) {

        SQLiteDatabase db = this.getWritableDatabase();
        String address="";
        String selectQuery = "SELECT chemist_address FROM chemist where chemist_code='"+code+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                address=(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return address;
    }
    public String getLastDate() {

        SQLiteDatabase db = this.getWritableDatabase();
        String date="";
        String selectQuery = "SELECT date FROM date order by date desc";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                date=(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return date;
    }

    public String getCustomerBalanceByCode(String code) {

        SQLiteDatabase db = this.getWritableDatabase();
        String balance="";
        String selectQuery = "SELECT customer_balance FROM chemist where chemist_code='"+code+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                balance=(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return balance;
    }
    public Cursor searchCustomerByCodeNew(String code) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT productName FROM product  where availableQty LIKE '%"+code+"%' Order by productName";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public List<String> searchCustomerByCode(String code){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product where availableQty LIKE '%"+code+"%' Order by productName";



        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getAllProduct(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productName FROM product inner join product_catagory ON product.catagoryID=product_catagory.catagoryID Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public List<String> getAllProductCode(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT productID FROM product Order by productName";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }


    public List<String> getAllPromoTools(){
        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT name FROM promo_tools Order by name";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public Cursor getSalesOrderLineData(String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT product_name,uom,ordered_qty,pricePerProduct,customer_name,orgid FROM sales_order_record where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesOrderLineDataTemp(String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM sales_orderline where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesOrderListForApproval(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM orderApproval";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getPromoOrderLineData(String docNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT productID,quantity FROM promo_orderline where docNo='"+docNo+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getSupervisorData(String userid,String docid){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT supid,supnmae FROM supervisor where userid='"+userid+"' and docid='"+docid+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getSalesType(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT salesType_name from salesType where salesType_name != 'Customer Return Material'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deletesalesType(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM  salesType";
        db.execSQL(query);
    }


    public String getUserIDFromEmployee(){


        String ad_user_id = null;

        // Select All Query
        String selectQuery = "select ad_user_id from employee order by emp_id desc limit 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ad_user_id=(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return ad_user_id;
    }

    public List<String> getUserDetailsFromEmployee(){


        List<String> labels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "select ad_user_id,user_level,name from employee order by emp_id desc limit 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
                labels.add(cursor.getString(2));
                labels.add(cursor.getString(3));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public String getDrCodeByName(String name){


        String code = null;

        // Select All Query
        String selectQuery = "SELECT dr_code FROM dr_information where drName='"+name+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                code=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return code;
    }

    public String getProductName(String productID){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT productName FROM product where productID='"+productID+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }
    public String getProductNameFromPromo(String productID){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT product_name FROM promo where product_code='"+productID+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }


    public String getChemistName(String chemId){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT chemist_name FROM chemist where chemist_code='"+chemId+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }

    public String getDrNameByCode(String drID){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT drName FROM dr_information where dr_code='"+drID+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }

    public String getdrCodeByDocNo(String docNo){


        String name = null;

        // Select All Query
        String selectQuery = "SELECT dr_code FROM promo_order where docNo='"+docNo+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                name=(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return name;
    }


    public Cursor getMortalitype(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select distinct reason_type from MortalityReason ";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getMortalityname(String reason_type) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "select reason_name from MortalityReason where reason_type = '"+reason_type+"'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}

