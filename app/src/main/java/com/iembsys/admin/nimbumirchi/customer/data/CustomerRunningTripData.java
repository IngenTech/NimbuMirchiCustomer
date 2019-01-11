package com.iembsys.admin.nimbumirchi.customer.data;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 05-12-2016.
 */

public class CustomerRunningTripData {
    String docketNo;
    String fromCity;
    String fromCityName;
    String toCity;
    String toCityName;
    String orderDate;
    String dispatchDate;
    String material;
    String materialName;
    String doorStatus;
    String refrigeratedStatus;
    String rate;
    String expectedArrival;
    String transporterName;
    String contactNo;
    String vehicleNo;
    String latitude;
    String longitude;
    String vehicleRunningStatus;
    String tripId;



    public ArrayList<ContentData> getContentData(){

        ArrayList<ContentData> contentDatas = new ArrayList<>();

        contentDatas.add(ContentData.getContentData("Trip Id",getTripId()));
        contentDatas.add(ContentData.getContentData("Docket No",getDocketNo()));
        contentDatas.add(ContentData.getContentData("From",getFromCityName()));
        contentDatas.add(ContentData.getContentData("To",getToCityName()));
        contentDatas.add(ContentData.getContentData("Transporter Name",getTransporterName()));
        contentDatas.add(ContentData.getContentData("Order Date",getOrderDate()));
        contentDatas.add(ContentData.getContentData("Dispatch Date",getDispatchDate()));
       // contentDatas.add(ContentData.getContentData("Material",getMaterialName()));
        contentDatas.add(ContentData.getContentData("Door Status",getDoorStatus()));
        contentDatas.add(ContentData.getContentData("Refrigerated Status",getRefrigeratedStatus()));
        contentDatas.add(ContentData.getContentData("Rate",getRate()+" / KM."));
        contentDatas.add(ContentData.getContentData("Expected Arrival",getExpectedArrival()));
        contentDatas.add(ContentData.getContentData("Transporter Contact No.",getContactNo()));
        contentDatas.add(ContentData.getContentData("Vehicle No.",getVehicleNo()));
        contentDatas.add(ContentData.getContentData("Vehicle Running Status",getVehicleRunningStatus()));

        return contentDatas;
    }

    public CustomerRunningTripData(JSONObject jsonString, DBAdapter db) {
        try {

            this.tripId = jsonString.getString("TripId");
            this.docketNo = jsonString.getString("DocketNo");
            this.fromCity = jsonString.getString("FromCityId");
            this.toCity = jsonString.getString("ToCityId");
            this.transporterName = jsonString.getString("TransporterName");
            this.orderDate = jsonString.getString("OrderDate");
            this.dispatchDate = jsonString.getString("DispatchDate");
           // this.material = jsonString.getString("MaterialId");
            this.doorStatus = jsonString.getString("DoorStatus");
            this.refrigeratedStatus = jsonString.getString("Refrigerated");
            this.rate = jsonString.getString("Rate");
            this.expectedArrival = jsonString.getString("ExpectedArrival");
            this.contactNo = jsonString.getString("TransporterContactNo");
            this.vehicleNo = jsonString.getString("VehicleNo");
            this.latitude = jsonString.getString("Latitude");
            this.longitude = jsonString.getString("Longitude");
            this.vehicleRunningStatus = jsonString.getString("VehicleRunningStatus");

          /*  JSONArray invoiceArray = jsonString.getJSONArray("InvoicList");
            JSONArray materialArray = jsonString.getJSONArray("RoadPermitList");
            if(invoiceArray.length()>0){
                this.invoiceList = "";
                for(int i = 0 ; i<invoiceArray.length(); i++) {
                    JSONObject jsonObject = invoiceArray.getJSONObject(i);
                    this.invoiceList = this.invoiceList + jsonObject.getString("InvoiceNo")+"_";
                }
            }

                this.invoiceList = jsonString.getString("TripDate");*/

            Cursor cursor = db.cityById(fromCity);
            if (cursor.moveToFirst()) {
                this.fromCityName = cursor.getString(cursor.getColumnIndex(DBAdapter.CITY_NAME));
            }
            cursor.close();

            Cursor cursor1 = db.cityById(toCity);
            if (cursor1.moveToFirst()) {
                this.toCityName = cursor1.getString(cursor1.getColumnIndex(DBAdapter.CITY_NAME));
            }
            cursor1.close();

            Cursor cursor2 = db.materialById(material);
            if (cursor2.moveToFirst()) {
                this.materialName = cursor2.getString(cursor2.getColumnIndex(DBAdapter.MATERIAL_NAME));
            }
            cursor2.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDocketNo() {
        return docketNo;
    }

    public String getFromCity() {
        return fromCity;
    }

    public String getFromCityName() {
        return fromCityName;
    }

    public String getToCity() {
        return toCity;
    }

    public String getToCityName() {
        return toCityName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getDispatchDate() {
        return dispatchDate;
    }

    public String getMaterial() {
        return material;
    }

    public String getMaterialName() {
        return materialName;
    }

    public String getDoorStatus() {
        return doorStatus;
    }

    public String getRefrigeratedStatus() {
        return refrigeratedStatus;
    }

    public String getRate() {
        return rate;
    }

    public String getExpectedArrival() {
        return expectedArrival;
    }

    public String getTransporterName() {
        return transporterName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getVehicleRunningStatus() {
        return vehicleRunningStatus;
    }

    public String getTripId() {
        return tripId;
    }

   /* public String getInvoiceList() {
        return invoiceList;
    }*/
}
