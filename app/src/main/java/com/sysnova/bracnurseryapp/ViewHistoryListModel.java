package com.sysnova.bracnurseryapp;

public class ViewHistoryListModel {
    String  reporting_date, serial, mortality_qty;

    public ViewHistoryListModel(String reporting_date,String serial,String mortality_qty) {
        this.reporting_date = reporting_date;
        this.serial = serial;
        this.mortality_qty = mortality_qty;
    }

    public String getReporting_date() {
        return reporting_date;
    }

    public void setReporting_date(String reporting_date) {
        this.reporting_date = reporting_date;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getMortality_qty() {
        return mortality_qty;
    }

    public void setMortality_qty(String mortality_qty) {
        this.mortality_qty = mortality_qty;
    }
}
