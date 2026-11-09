package com.sysnova.bracnurseryapp;

public class ViewProductDetailsModal {
    String product, price, qty, total, uom;

    public ViewProductDetailsModal(String product, String price, String qty, String total, String uom) {
        this.product = product;
        this.price=price;
        this.qty = qty;
        this.total = total;
        this.uom = uom;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
