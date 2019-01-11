package com.iembsys.admin.nimbumirchi.customer.data;

import java.util.ArrayList;

/**
 * Created by Admin on 07-02-2017.
 */

public class InvoiceData {

    String invoiceId;
    String invoiceNo;
    String invoiceDate;
    String invoiceAmount;
    String orderId;
    String remark;
    String imageUrl;
    ArrayList<MaterialData> materialDatas = new ArrayList<>();

    public InvoiceData() {
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addMaterial(MaterialData materialData){
        materialDatas.add(materialData);
    }

    public ArrayList<MaterialData> getMaterialDatas() {
        return materialDatas;
    }
}
