package com.iembsys.admin.nimbumirchi.customer.orders_action_activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.iembsys.admin.nimbumirchi.customer.NimbuMirchiApplication;
import com.iembsys.admin.nimbumirchi.customer.R;
import com.iembsys.admin.nimbumirchi.customer.bean.AlertBean;
import com.iembsys.admin.nimbumirchi.customer.util.AppConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by Admin on 26-04-2017.
 */
public class AlertActivity  extends AppCompatActivity {

    RecyclerView listView;
    private String order_id;
    private String accessToken;
    private String userID;
    SharedPreferences prefs;

    TextView imei;
    TextView vehicalName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);

        order_id = getIntent().getStringExtra("trip_id");

        if (prefs == null) {
            prefs = getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, MODE_PRIVATE);
        }
        userID = prefs.getString(AppConstant.Preference.USER_ID, "");
        accessToken = prefs.getString(AppConstant.Preference.ACCESS_TOKEN, "");

        imei = (TextView)findViewById(R.id.alert_imei);
        vehicalName = (TextView)findViewById(R.id.alert_vehicalName);

        listView = (RecyclerView) findViewById(R.id.haltList);
        listView.setLayoutManager(new LinearLayoutManager(this));

        getHaltReport(userID,order_id,accessToken);

    }


    private void getHaltReport(final String accountId, final String orderId, final String accessToken) {


        StringRequest stringVarietyRequest = new StringRequest(Request.Method.POST, AppConstant.API.HALT_REPORT_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String travelResponse) {
                        try {


                            JSONObject jsonObject = new JSONObject(travelResponse);

                            if (jsonObject.has("Status") && (jsonObject.getString("Status").equalsIgnoreCase("1"))) {
                                if (jsonObject.get("Result").equals("Success")) {

                                    JSONArray haltArray = jsonObject.getJSONArray("HaltReport");
                                    if (haltArray.length() > 0) {

                                        List<AlertBean> listDataChildItem = new ArrayList<AlertBean>();

                                        if (haltArray.length() > 0) {
                                            for (int j = 0; j < haltArray.length(); j++) {

                                                JSONObject haltJsonObject = haltArray.getJSONObject(j);

                                                String imei_no = haltJsonObject.getString("DeviceIMEINo");
                                                String vehical_Name = haltJsonObject.getString("VehicleName");

                                                imei.setText(imei_no);
                                                vehicalName.setText(vehical_Name);

                                                String arrivalTime = haltJsonObject.getString("ArrivalTime");
                                                String departureTime = haltJsonObject.getString("DepartureTime");
                                                String haltDuration = haltJsonObject.getString("HaltDuration");
                                                String startLatitude = haltJsonObject.getString("Latitude");
                                                String startLongitude = haltJsonObject.getString("Longitude");
                                                LatLng startLatLng = new LatLng(Double.parseDouble(startLatitude), Double.parseDouble(startLongitude));
                                                AlertBean haltData = new AlertBean("Don't cross speed limit.", "",arrivalTime, departureTime,haltDuration, startLatLng);
                                                getAddressOfStartLatLng(haltData);
                                                listDataChildItem.add(haltData);


                                            }
                                        }


                                        if (listDataChildItem.size()>0){
                                            AlertAdapter adapter = new AlertAdapter(AlertActivity.this, (ArrayList<AlertBean>) listDataChildItem);
                                            listView.setAdapter(adapter);
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "No Data found for the vehicle", Toast.LENGTH_LONG).show();
                                    }

                                } else {

                                    String msg = jsonObject.getString("Message");

                                    Toast.makeText(getApplicationContext(), ""+msg, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Blank Response", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Not able parse response", Toast.LENGTH_LONG).show();
                        }

                        progressDialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.cancel();
                volleyError.printStackTrace();
                Toast.makeText(getApplicationContext(), "Not able to connect with server", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("UserId", accountId);
                map.put("AccessToken", accessToken);
                map.put("TripId", orderId);

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                return map;
            }
        };


        int x=2;// retry count
        stringVarietyRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                x, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        progressDialog = ProgressDialog.show(this, "", "Fetching Alert list...", true);
        NimbuMirchiApplication.getInstance().addToRequestQueue(stringVarietyRequest);

    }
    ProgressDialog progressDialog;
    public void getAddressOfStartLatLng(final AlertBean data) {

        String address = String.format(Locale.ENGLISH, AppConstant.API.ADDRESS_OF_LAT_LNG_API, data.getStartLatLong().latitude, data.getStartLatLong().longitude);

        System.out.println("requestedAddress : "+address);

        StringRequest stringVarietyRequest = new StringRequest(Request.Method.GET, address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String addressResponse) {
                        System.out.println("addressResponse : "+addressResponse);
                        try {
                            String address = "Could not retrieve address";
                            JSONObject jsonObject = new JSONObject();
                            jsonObject = new JSONObject(addressResponse.toString());

                            List<Address> addresses = new ArrayList<>();

                            if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
                                JSONArray results = jsonObject.getJSONArray("results");
                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject result = results.getJSONObject(i);
                                    String indiStr = result.getString("formatted_address");
                                    Address addr = new Address(Locale.getDefault());
                                    addr.setAddressLine(0, indiStr);
                                    addresses.add(addr);
                                }
                            }
                            address = addresses.get(0).getAddressLine(0);
                            String city = addresses.get(0).getAddressLine(1);
                            String country = addresses.get(0).getAddressLine(2);

                            if(city!=null){
                                address = city +" "+ address;
                            }

                            if(country!=null){
                                address = address +" "+ country;
                            }

                            address.replaceAll("null", "");


                            data.setPlace(address);


                            System.out.println("GOT ADDRESS FROM GOOGLE API : " + address);

                        } catch (Exception e) {
                            e.printStackTrace();
                            data.setPlace("Could not retrieve address");

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                data.setPlace("Could not retrieve address");

            }
        });

        NimbuMirchiApplication.getInstance().addToRequestQueue(stringVarietyRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}