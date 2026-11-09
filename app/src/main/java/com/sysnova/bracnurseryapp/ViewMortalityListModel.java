package com.sysnova.bracnurseryapp;

public class ViewMortalityListModel {
    String m_product_id, pro_code,pro_name,mor_qty;

    public ViewMortalityListModel(String m_product_id,String pro_code,String pro_name,String mor_qty) {
        this.m_product_id = m_product_id;
        this.pro_code = pro_code;
        this.pro_name = pro_name;
        this.mor_qty = mor_qty;
    }

    public String getM_product_id() {
        return m_product_id;
    }

    public void setM_product_id(String m_product_id) {
        this.m_product_id = m_product_id;
    }

    public String getPro_code() {
        return pro_code;
    }

    public void setPro_code(String pro_code) {
        this.pro_code = pro_code;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getMor_qty() {
        return mor_qty;
    }

    public void setMor_qty(String mor_qty) {
        this.mor_qty = mor_qty;
    }
}
