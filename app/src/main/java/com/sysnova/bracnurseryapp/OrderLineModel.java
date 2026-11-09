package com.sysnova.bracnurseryapp;

public class OrderLineModel {
    int id;
    private String code,name;
    private double price,total;
    private int qty;

    public OrderLineModel(int id,String code, String name, int qty, double price,double total) {
        this.id=id;
        this.code = code;
        this.name = name;
        this.qty = qty;
        this.price=price;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
