package com.iembsys.admin.nimbumirchi.customer;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.iembsys.admin.nimbumirchi.customer.data.DBAdapter;
import com.iembsys.admin.nimbumirchi.customer.data.ProfileData;
import com.iembsys.admin.nimbumirchi.customer.util.AppConstant;
import com.iembsys.admin.nimbumirchi.customer.util.MyUtility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.google.ads.AdRequest.LOGTAG;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "NimbooMirchiCustomer " + LoginActivity.class.getSimpleName();
    EditText userName;
    EditText pass;

    DBAdapter db;
    CheckBox remeberCheck;
    String remeberType = "0";
    SharedPreferences sharedpreferences;
    TextView website_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        userName = (EditText) findViewById(R.id.userName);
        pass = (EditText) findViewById(R.id.pass);

        website_link = (TextView)findViewById(R.id.website_link);

        if (!checkGooglePlayServicesAvailable()) {
            return;
        }

        if (!checkPermissionPhone()) {
            return;
        }

        db = new DBAdapter(this);
        db.open();

        findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signUpIntent);
            }
        });


       sharedpreferences = getSharedPreferences("LoginSession", Context.MODE_PRIVATE);

        remeberCheck = (CheckBox) findViewById(R.id.check_password_remeber);

        remeberCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {

                    remeberType = "1";
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("remember", remeberType);
                    editor.commit();
                } else {
                    remeberCheck.setChecked(false);
                    remeberType = "0";
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("remember", remeberType);
                    editor.commit();
                }
            }
        });

        String str = sharedpreferences.getString("remember", null);

        if (str != null && str.length() > 0) {
            if (str.equalsIgnoreCase("1")) {
                remeberCheck.setChecked(true);
                remeberType = "1";
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("remember", remeberType);

                String name = sharedpreferences.getString("userName", null);
                String pas = sharedpreferences.getString("userPass", null);
                userName.setText(name);
                pass.setText(pas);

                editor.commit();
            } else {
                remeberCheck.setChecked(false);
                remeberType = "0";
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("remember", remeberType);
                editor.commit();
            }
        }

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()) {
                    String userId = userName.getText().toString();
                    String password = pass.getText().toString();

                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();

                    String physicalAdd = macAddress();


                    loginRequest(userId,password,physicalAdd,progressDialog);
                }
            }
        });

        website_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.nimbumirchee.co.in/"));
                startActivity(intent);
            }
        });

    }

    private boolean isValid(){
        if(!(userName.getText().toString()!=null && userName.getText().toString().trim().length()>0)){
            Toast.makeText(this,"Please enter username",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!(pass.getText().toString()!=null && pass.getText().toString().trim().length()>0)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkGooglePlayServicesAvailable() {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {
            return true;
        }
        Log.e(LOGTAG, "Google Play Services not available: " + GooglePlayServicesUtil.getErrorString(status));
        if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            final Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(status, this, 1);
            if (errorDialog != null) {
                errorDialog.show();
            }
        }

        return false;
    }

    public static final int WRITE_EXTERNAL_STORAGE = 101;
    public static final int EXCESS_FINE_LOCATION = 102;
    public static final int EXCESS_COURSE_LOCATION = 103;
    public static final int READ_PHONE_STATE = 104;
    public static final int READ_CONTACTS = 105;

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                return false;
            }
            /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        EXCESS_FINE_LOCATION);
                return false;
            }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        EXCESS_COURSE_LOCATION);
                return false;
            }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        READ_PHONE_STATE);
                return false;
            }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                        READ_CONTACTS);
                return false;
            }*/
        }
        return true;
    }

    private boolean checkPermissionPhone() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                return false;
            }

        }
        return true;
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_PHONE_STATE:
            case WRITE_EXTERNAL_STORAGE:
            case EXCESS_COURSE_LOCATION:
            case EXCESS_FINE_LOCATION:
            case READ_CONTACTS:
                activityRestart();
                break;
        }
    }

    private void loginRequest(final String userId, final String pass,final String physicalAddress, final ProgressDialog progressDialog) {
        StringRequest stringLoginRequest = new StringRequest(Request.Method.POST, AppConstant.API.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String loginResponse) {
                        progressDialog.dismiss();
                        Log.d(TAG, "Login Response : " + loginResponse);
                        try {

                            JSONObject jsonObject = new JSONObject(loginResponse);

                            String status = jsonObject.has("Status") ? jsonObject.getString("Status") : "";

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("userName", userId);
                            editor.putString("userPass", pass);
                            editor.commit();

                            if (status!=null && status.equalsIgnoreCase("1")) {
                                if (jsonObject.getString("Result").equalsIgnoreCase("success")) {

                                    String succMessage = jsonObject.has("Message") ? jsonObject.getString("Message") : "Blank Response";

                                    insertAccountInfo(jsonObject,physicalAddress);
                               //     onSignupSuccess(succMessage);

                                }
                            } else {
                                String failMessage = jsonObject.has("Message") ? jsonObject.getString("Message") : "Blank Response";
                                onSignupFailed(failMessage);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            onSignupFailed("Not able parse response");
                        }
                        progressDialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                progressDialog.cancel();
                onSignupFailed("Not able to connect with server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                String md5_pass = MyUtility.md5(pass);
                Log.v("sha1",md5_pass+"");

                map.put("PhysicalAddress", physicalAddress);
                map.put("Username", userId);
                map.put("Password", md5_pass);
                map.put("UserType", "5");

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                return map;
            }
        };
        NimbuMirchiApplication.getInstance().addToRequestQueue(stringLoginRequest);
    }

    SharedPreferences prefs;
    private boolean insertAccountInfo(JSONObject jsonObject,String physicalAddr) {
        boolean result = false;

        if (prefs == null) {
            prefs = getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = prefs.edit();

        try {

            ProfileData profileData = new ProfileData();

            editor.putString(AppConstant.Preference.PHYSICAL_ADDRESS, physicalAddr);

            String accountName = jsonObject.has("UserName") ? jsonObject.getString("UserName") : "";
            String accountName1 = accountName.equals("null") ? "" : accountName;

            editor.putString(AppConstant.Preference.ACCOUNT_NAME, accountName1);
            profileData.setAccountName(accountName1);

            String accountId = jsonObject.has("UserId") ? jsonObject.getString("UserId") : "";
            editor.putString(AppConstant.Preference.ACCOUNT_ID, accountId);
            profileData.setAccountId(accountId);

            String contactNo = jsonObject.has("Phone") ? jsonObject.getString("Phone") : "";
            editor.putString(AppConstant.Preference.CONTACT_NO, contactNo);
            profileData.setPersonContactNo(contactNo);

            String accountType = jsonObject.has("CustmoerType") ? jsonObject.getString("CustmoerType") : "";
            editor.putString(AppConstant.Preference.ACCOUNT_TYPE, accountType);

            String userId = jsonObject.has("UserId") ? jsonObject.getString("UserId") : "";
            editor.putString(AppConstant.Preference.USER_ID, userId);

            editor.putString(AppConstant.Preference.PASSWORD, pass.getText().toString());

            String accessToken = jsonObject.has("AccessToken") ? jsonObject.getString("AccessToken") : "";
            editor.putString(AppConstant.Preference.ACCESS_TOKEN, accessToken);

            result = editor.commit();

            String user_name = jsonObject.has("Name") ? jsonObject.getString("Name") : "";
            String userName = user_name.equals("null") ? "" : user_name;

            String pan_no = jsonObject.has("PanNo") ? jsonObject.getString("PanNo") : "";
            String panNo = pan_no.equals("null") ? "" : pan_no;

            String firm_name = jsonObject.has("FirmName") ? jsonObject.getString("FirmName") : "";
            String firmName = firm_name.equals("null") ? "" : firm_name;

            String contact_name = jsonObject.has("ContactName") ? jsonObject.getString("ContactName") : "";
            String contactName = contact_name.equals("null") ? "" : contact_name;

            String personal_contact_no = jsonObject.has("PersonalContactNo") ? jsonObject.getString("PersonalContactNo") : "";
            String personalContactNo = personal_contact_no.equals("null") ? "" : personal_contact_no;

            String home_contact_no = jsonObject.has("HomeContactNo") ? jsonObject.getString("HomeContactNo") : "";
            String homeContactNo = home_contact_no.equals("null") ? "" : home_contact_no;

            String office_contact_no = jsonObject.has("OfficeContactNo") ? jsonObject.getString("OfficeContactNo") : "";
            String officeContactNo = office_contact_no.equals("null") ? "" : office_contact_no;

            String tin_no = jsonObject.has("TinNo") ? jsonObject.getString("TinNo") : "";
            String tinNo = tin_no.equals("null") ? "" : tin_no;

            String state_id = jsonObject.has("StateId") ? jsonObject.getString("StateId") : "";
            String stateId = state_id.equals("null") ? "" : state_id;

            String city_id = jsonObject.has("CityId") ? jsonObject.getString("CityId") : "";
            String cityId = city_id.equals("null") ? "" : city_id;

            String address = jsonObject.has("Address") ? jsonObject.getString("Address") : "";
            String Address = address.equals("null") ? "" : address;

            String email_id = jsonObject.has("Email") ? jsonObject.getString("Email") : "";
            String emailId = email_id.equals("null") ? "" : email_id;

            String do_no_call = jsonObject.has("DoNotCall") ? jsonObject.getString("DoNotCall") : "";
            String doNoCall = do_no_call.equals("null") ? "" : do_no_call;

            String bank_name = jsonObject.has("BankName") ? jsonObject.getString("BankName") : "";
            String bankName = bank_name.equals("null") ? "" : bank_name;

            String bank_account_no = jsonObject.has("BankAccountNo") ? jsonObject.getString("BankAccountNo") : "";
            String bankAccountNo = bank_account_no.equals("null") ? "" : bank_account_no;

            String bank_branch_name = jsonObject.has("BankBranchName") ? jsonObject.getString("BankBranchName") : "";
            String bankBranchName = bank_branch_name.equals("null") ? "" : bank_branch_name;

            String bank_branch_code = jsonObject.has("BankBranchCode") ? jsonObject.getString("BankBranchCode") : "";
            String bankBranchCode = bank_branch_code.equals("null") ? "" : bank_branch_code;

            profileData.setAccountName(userName);
            profileData.setPanNo(panNo);
            profileData.setAccountName(firmName);
            profileData.setContactName(contactName);
            profileData.setPersonContactNo(personalContactNo);
            profileData.setHomeContactNo(homeContactNo);
            profileData.setOfficeContactNo(officeContactNo);
            profileData.setTinNo(tinNo);
            profileData.setStateId(stateId);
            profileData.setCityId(cityId);
            profileData.setAddress(Address);
            profileData.setEmail(emailId);
            profileData.setDoNoCall(doNoCall);
            profileData.setBankName(bankName);
            profileData.setBankAccountNo(bankAccountNo);
            profileData.setBankBranchCode(bankBranchCode);
            profileData.setBankBranchName(bankBranchName);

            boolean isSavedInDb = profileData.save(db,this);

           // result = editor.commit();

            System.out.println("editor result : "+result+" isSavedInDb : "+isSavedInDb);

        } catch (Exception e) {
            e.printStackTrace();
        }

        db = new DBAdapter(this);
        db.open();

        if(!db.getStateList().moveToFirst()){
            new WebServiceConsumer(this,db).syncAllData();
        }

        return result;
    }
            /*
"user_name":null,
"customer_type":0,
"pan_no":null,
"firm_name":null,
"contact_name":"Kanchan",
"personal_contact_no":"9795990090",
"home_contact_no":null,
"office_contact_no":null,
"tin_no":null,
"country_id":null,
"state_id":null,
"city_id":null,
"address":null,
"email_id":"bkanchan@gmail.com",
"do_no_call":null,
"bank_name":null,
"bank_account_no":null,
"bank_branch_name":null,
"bank_branch_code":null,
"create_id":0,
"create_date":"2017-02-01 12:55:25",
"edit_id":null,
"edit_date":null,
"status":1,
"remarks":null}*/


    public void onSignupSuccess(String msg) {

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (prefs == null) {
                    prefs = getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, MODE_PRIVATE);
                }
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(AppConstant.Preference.IS_LOGIN, true);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        };

        handler.postDelayed(runnable, 10000);


    }


    public void onSignupFailed(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Failed");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    public void activityRestart() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }


    public String macAddress(){

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();

        return macAddress;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {

                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }catch (Exception e) {
                    // TODO: handle exception
                }

            }
        }
        return ret;
    }

}
