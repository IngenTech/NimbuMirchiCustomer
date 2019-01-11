package com.iembsys.admin.nimbumirchi.customer.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 03-12-2016.
 */

public class CustomerPendingOrder implements Parcelable {

    String docketNo;
    String orderDate;
    String refrigeratedStatus;
    String doorStatus;
    String cnt;
    String fromCityId;
    String toCityId;
    String toCityName;
    String fromCityName;
    String tripDate;
    String invoiceString;
    String roadPermitSttring;
    String transporterName;
    String contactName;


    public ArrayList<ContentData> getContentData(){

        ArrayList<ContentData> contentDatas = new ArrayList<>();

        contentDatas.add(ContentData.getContentData("Trip Id",""));
        contentDatas.add(ContentData.getContentData("Docket No",getDocketNo()));
        contentDatas.add(ContentData.getContentData("From",getFromCityName()));
        contentDatas.add(ContentData.getContentData("To",getToCityName()));
        contentDatas.add(ContentData.getContentData("Transporter Name",getTransporterName()));
        contentDatas.add(ContentData.getContentData("Order Date",getOrderDate()));
        contentDatas.add(ContentData.getContentData("Dispatch Date",getTripDate()));
      // contentDatas.add(ContentData.getContentData("Material",""));
        contentDatas.add(ContentData.getContentData("Door Status",getDoorStatus()));
        contentDatas.add(ContentData.getContentData("Refrigerated Status",getRefrigeratedStatus()));
        contentDatas.add(ContentData.getContentData("Rate","0"+" / KM."));
        contentDatas.add(ContentData.getContentData("Expected Arrival",""));
        contentDatas.add(ContentData.getContentData("Transporter Contact No.",""));
        contentDatas.add(ContentData.getContentData("Vehicle No.",""));
        contentDatas.add(ContentData.getContentData("Vehicle Running Status",""));

        return contentDatas;
    }


    public CustomerPendingOrder(JSONObject jsonString, DBAdapter db) {
        try {
            /*"CustomerID": 103,
"DocketNo": "860954408N68",
"TransporterFirmID": null,
"FromCityID": 2664,
"FromAddress": "",
"ToCityID": 2681,
"ToAddress": null,
"VehicleQuantity": 5,
"Capacity": 50,
"Rate": null,
"DoorStatus": 1,
"Referigerated": 0,
"TripDate": "0000-00-00",
"OrderDateTime": "2017-02-02 10:35:32",
"OrderStatus": 2,
"EditID": 0,
"EditDate": "0000-00-00 00:00:00",
"Remark": null,
"Invoices": [],
"RoadPermits": [],
"CustomerName": null*/
            this.docketNo = jsonString.getString("DocketNo");
            this.orderDate = jsonString.getString("OrderDateTime");
            this.refrigeratedStatus = jsonString.getString("Referigerated");
            this.doorStatus = jsonString.getString("DoorStatus");
            this.cnt = jsonString.getString("OrderStatus");
            this.fromCityId = jsonString.getString("FromCityID");
            this.toCityId = jsonString.getString("ToCityID");
            this.tripDate = jsonString.getString("TripDate");
            this.invoiceString = jsonString.getString("Invoices");
            this.roadPermitSttring = jsonString.getString("RoadPermits");

            this.transporterName = jsonString.getString("TransporterName");
            this.contactName = jsonString.getString("ContactName");

            Cursor cursor = db.cityById(fromCityId);
            if (cursor.moveToFirst()) {
                this.fromCityName = cursor.getString(cursor.getColumnIndex(DBAdapter.CITY_NAME));
            }
            cursor.close();

            Cursor cursor1 = db.cityById(toCityId);
            if (cursor1.moveToFirst()) {
                this.toCityName = cursor1.getString(cursor1.getColumnIndex(DBAdapter.CITY_NAME));
            }
            cursor1.close();


        } catch (Exception e) {


            e.printStackTrace();
        }

    }

    public String getDocketNo() {
        return docketNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getRefrigeratedStatus() {
        return refrigeratedStatus;
    }

    public String getDoorStatus() {
        return doorStatus;
    }

    public String getCnt() {
        return cnt;
    }

    public String getFromCityId() {
        return fromCityId;
    }

    public String getToCityId() {
        return toCityId;
    }

    public String getToCityName() {
        return toCityName;
    }

    public String getFromCityName() {
        return fromCityName;
    }

    public String getTripDate() {
        return tripDate;
    }

    public String getTransporterName(){return transporterName;}

    public String getContactName(){return contactName;}


    public CustomerPendingOrder(Parcel in) {
        String[] data = new String[14];
        in.readStringArray(data);
        this.docketNo = data[0];
        this.orderDate = data[1];
        this.refrigeratedStatus = data[2];
        this.doorStatus = data[3];
        this.cnt = data[4];
        this.fromCityId = data[5];
        this.toCityId = data[6];
        this.toCityName = data[7];
        this.fromCityName = data[8];
        this.tripDate = data[9];
        this.invoiceString = data[10];
        this.roadPermitSttring = data[11];
        this.transporterName = data[12];
        this.contactName = data[13];

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.docketNo,
                this.orderDate,
                this.refrigeratedStatus,
                this.doorStatus,
                this.cnt,
                this.fromCityId,
                this.toCityId,
                this.toCityName,
                this.fromCityName,
                this.tripDate,
                this.invoiceString,
                this.roadPermitSttring,
                this.transporterName,
                this.contactName
        });
    }

    public static final Creator<CustomerPendingOrder> CREATOR = new Creator<CustomerPendingOrder>() {

        @Override
        public CustomerPendingOrder createFromParcel(Parcel source) {
            return new CustomerPendingOrder(source); // using parcelable constructor
        }

        @Override
        public CustomerPendingOrder[] newArray(int size) {
            return new CustomerPendingOrder[size];
        }
    };
}
