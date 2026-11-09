package com.sysnova.bracnurseryapp;

public class ViewOrderListModel {
    String serial,orderno,dateordered,mobile,t_sales_order_parent_ID, grandtotal, docstatus, t_qty, t_total;

    public ViewOrderListModel(String serial,String orderno,String dateordered,String mobile,String t_sales_order_parent_ID, String grandtotal, String docstatus, String t_qty,String t_total) {
        this.serial = serial;
        this.orderno = orderno;
        this.dateordered = dateordered;
        this.mobile = mobile;
        this.t_sales_order_parent_ID = t_sales_order_parent_ID;
        this.docstatus = docstatus;
        this.grandtotal = grandtotal;
        this.t_qty = t_qty;
        this.t_total = t_total;
    }

    public String getT_qty() {
        return t_qty;
    }

    public void setT_qty(String t_qty) {
        this.t_qty = t_qty;
    }

    public String getT_total() {
        return t_total;
    }

    public void setT_total(String t_total) {
        this.t_total = t_total;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getDateordered() {
        return dateordered;
    }

    public void setDateordered(String dateordered) {
        this.dateordered = dateordered;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDocstatus() {
        return docstatus;
    }

    public void setDocstatus(String docstatus) {
        this.docstatus = docstatus;
    }

    public String getT_sales_order_parent_ID() {
        return t_sales_order_parent_ID;
    }

    public void setT_sales_order_parent_ID(String t_sales_order_parent_ID) {
        this.t_sales_order_parent_ID = t_sales_order_parent_ID;
    }

    public String getGrandtotal() {
        return grandtotal;
    }

    public void setGrandtotal(String grandtotal) {
        this.grandtotal = grandtotal;
    }
}
