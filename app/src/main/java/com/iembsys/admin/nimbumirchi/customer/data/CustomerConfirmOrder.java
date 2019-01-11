package com.iembsys.admin.nimbumirchi.customer.data;

import android.database.Cursor;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 02-12-2016.
 */

public class CustomerConfirmOrder {

     /*{
"TripId": 19,


      "IMEI": "862170018313708",
      "OrderDate": "2017-04-03 17:45:15",
      "Latitude": "26.45697N",
      "Longitude": "80.22826E",
      "speed": "31.06",
      "lastHaltTime": "2017-05-17 15:45:50",
      "VehicleRunningStatus": "Running"
}*/

    String IMEI;
    String OrderDate;
    String speed;
    String lastHaltTime;
    String VehicleRunningStatus;
    String TripId;
    String DispatchDate;
    String ExpectedArrival;
    String DoorStatus;
    String RefrigeratedStatus;
    String Rate;
    String TransporterContactNo;
    String VehicleId;
    String VehicleNo;

    String docketNo;
    String transporterName;
    String contactName;
    String fromCityId;
    String toCityId;
    String toCityName;
    String fromCityName;
    String tripDate;


    ArrayList<InvoiceData> invoices = new ArrayList<>();


    public ArrayList<ContentData> getContentData(){

        ArrayList<ContentData> contentDatas = new ArrayList<>();

        contentDatas.add(ContentData.getContentData("Trip Id",getTripId()));
        contentDatas.add(ContentData.getContentData("Docket No",getDocketNo()));
        contentDatas.add(ContentData.getContentData("From",getFromCityName()));
        contentDatas.add(ContentData.getContentData("To",getToCityName()));
        contentDatas.add(ContentData.getContentData("Transporter Name",getTransporterName()));
        contentDatas.add(ContentData.getContentData("Order Date",getOrderDate()));
        contentDatas.add(ContentData.getContentData("Dispatch Date",getDispatchDate()));
       // contentDatas.add(ContentData.getContentData("Material",""));
        contentDatas.add(ContentData.getContentData("Door Status",getDoorStatus()));
        contentDatas.add(ContentData.getContentData("Refrigerated Status",getRefrigeratedStatus()));
        contentDatas.add(ContentData.getContentData("Rate",getRate()+" / KM."));
        contentDatas.add(ContentData.getContentData("Expected Arrival",getExpectedArrival()));
        contentDatas.add(ContentData.getContentData("Transporter Contact No.",getTransporterContactNo()));
        contentDatas.add(ContentData.getContentData("Vehicle No.",getVehicleNo()));
        contentDatas.add(ContentData.getContentData("Vehicle Running Status",getVehicleRunningStatus()));

        return contentDatas;
    }


    public CustomerConfirmOrder(JSONObject jsonString, DBAdapter db) {
        try {
            this.docketNo = jsonString.getString("DocketNo");
            this.transporterName = jsonString.getString("TransporterName");
            this.TripId = jsonString.getString("TripId");
            this.fromCityId = jsonString.getString("FromCityId");
            this.toCityId = jsonString.getString("ToCityId");
            this.tripDate = jsonString.getString("TripDate");

            this.IMEI= jsonString.getString("IMEI");
            this.OrderDate= jsonString.getString("OrderDate");
            this.speed= jsonString.getString("speed");
            this.lastHaltTime= jsonString.getString("lastHaltTime");
            this.VehicleRunningStatus= jsonString.getString("VehicleRunningStatus");
            this.TripId= jsonString.getString("TripId");
            this.DispatchDate= jsonString.getString("DispatchDate");
            this.ExpectedArrival= jsonString.getString("ExpectedArrival");
            this.DoorStatus= jsonString.getString("DoorStatus");
            this.RefrigeratedStatus= jsonString.getString("RefrigeratedStatus");
            this.Rate= jsonString.getString("Rate");
            this.TransporterContactNo= jsonString.getString("TransporterContactNo");
            this.VehicleId= jsonString.getString("VehicleId");
            this.VehicleNo= jsonString.getString("VehicleNo");

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

    public String getTransporterName() {
        return transporterName;
    }

    public String getContactName() {
        return contactName;
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

    public void addInvoice(InvoiceData invoiceData){
        invoices.add(invoiceData);
    }

    public ArrayList<InvoiceData> getInvoices() {
        return invoices;
    }


    public String getIMEI() {
        return IMEI;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public String getSpeed() {
        return speed;
    }

    public String getLastHaltTime() {
        return lastHaltTime;
    }

    public String getVehicleRunningStatus() {
        return VehicleRunningStatus;
    }

    public String getTripId() {
        return TripId;
    }

    public String getDispatchDate() {
        return DispatchDate;
    }

    public String getExpectedArrival() {
        return ExpectedArrival;
    }

    public String getDoorStatus() {
        return DoorStatus;
    }

    public String getRefrigeratedStatus() {
        return RefrigeratedStatus;
    }

    public String getRate() {
        return Rate;
    }

    public String getTransporterContactNo() {
        return TransporterContactNo;
    }

    public String getVehicleId() {
        return VehicleId;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }
}

