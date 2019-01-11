package com.iembsys.admin.nimbumirchi.customer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iembsys.admin.nimbumirchi.customer.data.DBAdapter;
import com.iembsys.admin.nimbumirchi.customer.data.ProfileData;
import com.iembsys.admin.nimbumirchi.customer.util.AppConstant;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Admin on 21-03-2017.
 */
public class ProfileActivity extends AppCompatActivity{

    EditText edtTxtFirmName;
    EditText edtTxtContactName;
    EditText edtTxtPersonalContact;
    EditText edtTxtOfficeContact;
    EditText edtTxtAddress;
    EditText edtTxtEmail;

    EditText edtTxtPanNo;
    EditText edtTxtHomeContact;
    EditText edtTxtTinNo;
    EditText edtTxtBankName;
    EditText edtTxtBankAccountNo;
    EditText edtTxtBankBranchName;
    EditText edtTxtBankBranchCode;


    Spinner spinnerState;
    Spinner spinnerCity;
    CheckBox doNotCall;
    Button btnSaveChanges;

    Cursor stateListCursor;
    Cursor cityListCursor;
    DBAdapter db;

    SharedPreferences prefs;
    ProgressDialog dialog;
    ProfileData profileData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);

        Log.v("state selected","compareValue");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);

        edtTxtFirmName = (EditText) findViewById(R.id.edtTxtFirmName);
        edtTxtContactName = (EditText) findViewById(R.id.edtTxtContactName);
        edtTxtPersonalContact = (EditText) findViewById(R.id.edtTxtPersonalContact);
        edtTxtOfficeContact = (EditText) findViewById(R.id.edtTxtOfficeContact);
        edtTxtAddress = (EditText) findViewById(R.id.edtTxtAddress);
        edtTxtEmail = (EditText) findViewById(R.id.edtTxtEmail);

        edtTxtPanNo = (EditText) findViewById(R.id.edtTxtPanNo);
        edtTxtHomeContact = (EditText) findViewById(R.id.edtTxtHomeContact);
        edtTxtTinNo = (EditText) findViewById(R.id.edtTxtTinNo);
        edtTxtBankName = (EditText) findViewById(R.id.edtTxtBankName);
        edtTxtBankAccountNo = (EditText) findViewById(R.id.edtTxtBankAccountNo);
        edtTxtBankBranchName = (EditText) findViewById(R.id.edtTxtBankBranchName);
        edtTxtBankBranchCode = (EditText) findViewById(R.id.edtTxtBankBranchCode);

        spinnerState = (Spinner) findViewById(R.id.spinnerState);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        btnSaveChanges = (Button) findViewById(R.id.btnSaveChanges);
        doNotCall = (CheckBox) findViewById(R.id.doNotCall);


        db = new DBAdapter(this);
        db.open();

        String firmId = "";
        String firmName = "Kanchan";
        String contactName = "Kanchan";
        String personContactNo = "9795990090";
        String homeContactNo = "";
        String officeContactNo = "";
        String panNo = "Tin 3896";
        String tinNo = "Tin 3896";
        String address = "116/832 Rawatpur gaon, Kanpur";
        String email = "";
        String stateId = "";
        String cityId = "";
        String doNoCall = "";
        String bankName = "";
        String bankAccountNo = "";
        String bankBranchName = "";
        String bankBranchCode = "";
        String remark = "";

        Cursor firmListCursor = db.getProfile();
        if (firmListCursor.moveToFirst()) {
            firmId = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.FIRM_ID));
            firmName = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.FIRM_NAME));
            contactName = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.CONTACT_NAME));
            personContactNo = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.PERSON_CONTACT_NO));
            homeContactNo = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.HOME_CONTACT_NO));
            officeContactNo = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.OFFICE_CONTACT_NO));
            panNo = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.PAN_NO));
            tinNo = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.TIN_NO));
            address = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.ADDRESS));
            email = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.EMAIL));
            stateId = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.STATE_ID));
            cityId = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.CITY_ID));

            doNoCall = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.DO_NO_CALL));
            bankName = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.BANK_NAME));
            bankAccountNo = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.BANK_ACCOUNT_NO));
            bankBranchName = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.BANK_BRANCH_NAME));
            bankBranchCode = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.BANK_BRANCH_CODE));
            remark = firmListCursor.getString(firmListCursor.getColumnIndex(DBAdapter.REMARK));
        }
        firmListCursor.close();

        profileData = new ProfileData();

        profileData.setAccountId(firmId);
        profileData.setAccountName(firmName);
        profileData.setPersonContactNo(personContactNo);
        profileData.setHomeContactNo(homeContactNo);
        profileData.setContactName(contactName);
        profileData.setOfficeContactNo(officeContactNo);
        profileData.setTinNo(tinNo);
        profileData.setPanNo(panNo);
        profileData.setAddress(address);
        profileData.setEmail(email);
        profileData.setStateId(stateId, db);
        profileData.setCityId(cityId, db);
        profileData.setDoNoCall(doNoCall);
        profileData.setBankName(bankName);
        profileData.setBankAccountNo(bankAccountNo);
        profileData.setBankBranchName(bankBranchName);
        profileData.setBankBranchCode(bankBranchCode);
        profileData.setRemark(remark);



        stateListCursor = db.getStateList();


        final ArrayList<String> stateNameArray = new ArrayList<>();

        stateNameArray.add("select state");

        if (stateListCursor.moveToFirst()) {
            do {
                String stateString = stateListCursor.getString(stateListCursor.getColumnIndex(DBAdapter.STATE_NAME));
                stateNameArray.add(stateString);
            } while (stateListCursor.moveToNext());
        }
        ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateNameArray);

        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(state_adapter);


        String compareValue = profileData.getStateName();
        if (!compareValue.equals(null)) {
            int spinnerPosition = state_adapter.getPosition(compareValue);
            spinnerState.setSelection(spinnerPosition);
        }


        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item was selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos>0) {
                    stateListCursor.moveToPosition(pos - 1);
                    String stateId = stateListCursor.getString(stateListCursor.getColumnIndex(DBAdapter.STATE_ID));
                    String stateName = stateListCursor.getString(stateListCursor.getColumnIndex(DBAdapter.STATE_NAME));
                    profileData.setStateId(stateId);
                    profileData.setStateName(stateName);

                    cityListCursor = db.cityListByStateId(stateId);

                    final ArrayList<String> cityNameArray = new ArrayList<>();
                    cityNameArray.add("select city");
                    if (cityListCursor.moveToFirst()) {
                        do {
                            String cityString = cityListCursor.getString(cityListCursor.getColumnIndex(DBAdapter.CITY_NAME));
                            cityNameArray.add(cityString);
                        } while (cityListCursor.moveToNext());
                    }

                    ArrayAdapter<String> city_adapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_spinner_item, cityNameArray);
                    city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCity.setAdapter(city_adapter);
                    spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        /**
                         * Called when a new item was selected (in the Spinner)
                         */
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            if (pos>0) {
                                cityListCursor.moveToPosition(pos-1);
                                String cityId = cityListCursor.getString(cityListCursor.getColumnIndex(DBAdapter.CITY_ID));
                                String cityName = cityListCursor.getString(cityListCursor.getColumnIndex(DBAdapter.CITY_NAME));
                                profileData.setCityId(cityId);
                                profileData.setCityName(cityName);
                            }
                        }

                        public void onNothingSelected(AdapterView parent) {
                            // Do nothing.
                        }
                    });

                    /***********Set Default city on state selection*************/

                    if (profileData.getCityId() != null && profileData.getCityId().trim().length() > 0) {
                        if (cityListCursor.moveToFirst()) {
                            for (int i = 0; i < cityListCursor.getCount(); i++) {
                                if (cityListCursor.getString(cityListCursor.getColumnIndex(DBAdapter.CITY_ID)).equals(profileData.getCityId())) {
                                    spinnerCity.setSelection(i);
                                    break;
                                }
                                cityListCursor.moveToNext();
                            }
                        }
                    }
                }

            }

            public void onNothingSelected(AdapterView parent) {
                // Do nothing.
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    saveChanges();
                }
            }
        });

        Log.v("state_selected1",compareValue);
        setProfileData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_save) {
           /* if(isValid()) {
                saveChanges();
            }*/

            Intent intent  = new Intent(getApplicationContext(),ChangePasswordActivity.class);
            startActivity(intent);

            return true;
        } else if (id==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }







    private void setProfileData() {
        if (profileData != null) {
            edtTxtFirmName.setText(profileData.getAccountName());
            edtTxtContactName.setText(profileData.getContactName());
            edtTxtPersonalContact.setText(profileData.getPersonContactNo());
            edtTxtOfficeContact.setText(profileData.getOfficeContactNo());
            edtTxtAddress.setText(profileData.getAddress());
            edtTxtEmail.setText(profileData.getEmail());
            edtTxtPanNo.setText(profileData.getPanNo());
            edtTxtHomeContact.setText(profileData.getHomeContactNo());
            edtTxtTinNo.setText(profileData.getTinNo());
            edtTxtBankName.setText(profileData.getBankName());
            edtTxtBankAccountNo.setText(profileData.getBankAccountNo());
            edtTxtBankBranchName.setText(profileData.getBankBranchName());
            edtTxtBankBranchCode.setText(profileData.getBankBranchCode());

           /* if (profileData.getStateId() != null && profileData.getStateId().trim().length() > 0) {
                if (stateListCursor.moveToFirst()) {
                    for (int i = 0; i < stateListCursor.getCount(); i++) {
                        if (stateListCursor.getString(stateListCursor.getColumnIndex(DBAdapter.STATE_ID)).equals(profileData.getStateId())) {
                            spinnerState.setSelection(i, true);
                            break;
                        }
                        stateListCursor.moveToNext();
                    }
                }
            }*/


            if (profileData.getDoNoCall().equals("true")) {
                doNotCall.setChecked(true);
            }

        }
    }

    private boolean isValid() {
        boolean isValid = true;

        String pan = edtTxtPanNo.getText().toString().trim();

        if (pan != null && pan.length() > 4) {
            profileData.setPanNo(pan);
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Pan No atleast 5 digits", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtFirmName.getText() != null && edtTxtFirmName.getText().toString().trim().length() > 1) {
            profileData.setAccountName(edtTxtFirmName.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Firm Name is Blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtContactName.getText() != null && edtTxtContactName.getText().toString().trim().length() > 0) {
            profileData.setContactName(edtTxtContactName.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Contact Name is Blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtPersonalContact.getText() != null && edtTxtPersonalContact.getText().toString().trim().length() > 9) {
            profileData.setPersonContactNo(edtTxtPersonalContact.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Please enter valid Personal Contact Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        /*if (edtTxtHomeContact.getText() != null && edtTxtHomeContact.getText().toString().trim().length() > 0) {
            profileData.setHomeContactNo(edtTxtHomeContact.getText().toString());
        }*/

        profileData.setHomeContactNo(edtTxtHomeContact.getText().toString());


        if (edtTxtOfficeContact.getText() != null && edtTxtOfficeContact.getText().toString().trim().length() > 9) {
            profileData.setOfficeContactNo(edtTxtOfficeContact.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter valid Office Contact Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtTinNo.getText() != null && edtTxtTinNo.getText().toString().trim().length() > 4) {
            profileData.setTinNo(edtTxtTinNo.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Valid Tin No", Toast.LENGTH_SHORT).show();

            return false;
        }
        if (!(profileData.getStateId() != null && profileData.getStateId().trim().length() > 0)) {
            Toast.makeText(getApplicationContext(), "Please Select State", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!(profileData.getCityId() != null && profileData.getCityId().trim().length() > 0)) {
            Toast.makeText(getApplicationContext(), "Please Select City", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (edtTxtAddress.getText() != null && edtTxtAddress.getText().toString().trim().length() > 0) {
            profileData.setAddress(edtTxtAddress.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Address", Toast.LENGTH_SHORT).show();
            return false;
        }

        String target = edtTxtEmail.getText().toString().trim();

        if (edtTxtEmail.getText() != null && isValidEmail(target)) {
            profileData.setEmail(edtTxtEmail.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Please enter valid email id.", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (edtTxtBankName.getText() != null && edtTxtBankName.getText().toString().trim().length() > 2) {
            profileData.setBankName(edtTxtBankName.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Minimum 2 Charaters Bank Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtBankAccountNo.getText() != null && edtTxtBankAccountNo.getText().toString().trim().length() > 6) {
            profileData.setBankAccountNo(edtTxtBankAccountNo.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Valid Bank Account No", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtBankBranchName.getText() != null && edtTxtBankBranchName.getText().toString().trim().length() > 3) {
            profileData.setBankBranchName(edtTxtBankBranchName.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Please Enter Valid Bank Branch Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtBankBranchCode.getText() != null && edtTxtBankBranchCode.getText().toString().trim().length() > 2) {
            profileData.setBankBranchCode(edtTxtBankBranchCode.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Please enter Valid Bank Branch Code", Toast.LENGTH_SHORT).show();
            return false;
        }



        if (doNotCall.isChecked()) {
            profileData.setDoNoCall(String.valueOf(true));
        } else {
            profileData.setDoNoCall(String.valueOf(false));
        }

        return isValid;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void saveChanges() {
        System.out.println("URL : " + AppConstant.API.EDIT_PROFILE_API);
        StringRequest stringLoginRequest = new StringRequest(Request.Method.POST, AppConstant.API.EDIT_PROFILE_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String editProfileResponse) {

                        Log.v("responseEdit Profile",""+editProfileResponse);

                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(editProfileResponse);

                            if (jsonObject.has("Status")&&(jsonObject.getString("Status").equalsIgnoreCase("1"))) {
                                if (jsonObject.getString("Result").equals("Success")) {
                                    profileData.save(db, ProfileActivity.this);
                                    new AlertDialog.Builder(ProfileActivity.this)
                                            .setMessage("Profile has been updated")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
//
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .show();
                                } else {

                                    String msg = jsonObject.getString("Message");

                                    Toast.makeText(getApplicationContext(), ""+msg, Toast.LENGTH_LONG).show();
                                }
                            } else  if (jsonObject.has("Status")&&(jsonObject.getString("Status").equalsIgnoreCase("2"))) {

                                String msg = jsonObject.getString("Message");

                                Toast.makeText(getApplicationContext(), ""+msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Not able parse response", Toast.LENGTH_LONG).show();
                        }
                        dialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                volleyError.printStackTrace();
                dialog.cancel();
                Toast.makeText(getApplicationContext(), "Not able to connect with server", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                if (prefs == null) {
                    prefs = getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, MODE_PRIVATE);
                }
                String userID = (prefs.getString(AppConstant.Preference.USER_ID, "0"));
                String customerType = (prefs.getString(AppConstant.Preference.ACCOUNT_TYPE, "0"));
                String accessToken = (prefs.getString(AppConstant.Preference.ACCESS_TOKEN, "0"));

                Map<String, String> map = new HashMap<>();
                map.put("AccessToken", accessToken);
                map.put("UserId", userID);
                map.put("CustomerType", "0");
                map.put("PanNo", profileData.getPanNo());
                map.put("FirmName", profileData.getAccountName());
                map.put("ContactName", profileData.getContactName());
                map.put("PersonalContactNo", profileData.getPersonContactNo());
                map.put("HomeContactNo", profileData.getHomeContactNo());
                map.put("OfficeContactNo", profileData.getOfficeContactNo());
                map.put("TinNo", profileData.getTinNo());
                map.put("CountryId", "1");
                map.put("StateId", profileData.getStateId());
                map.put("CityId", profileData.getCityId());
                map.put("Address", profileData.getAddress());
                map.put("EmailId", profileData.getEmail());
                map.put("DoNotCall", profileData.getDoNoCall());
                map.put("BankName", profileData.getBankName());
                map.put("BankAccountNo", profileData.getBankAccountNo());
                map.put("BankBranchName", profileData.getBankBranchName());
                map.put("BankBranchCode", profileData.getBankBranchCode());
                map.put("EditId", profileData.getAccountId(ProfileActivity.this));
//                map.put("edit_date", profileData.getDoNoCall());

                return checkParams(map);
            }
        };
        dialog = ProgressDialog.show(this, "Saving Changes",
                "Please wait...", true);
        NimbuMirchiApplication.getInstance().addToRequestQueue(stringLoginRequest);
    }

    private Map<String, String> checkParams(Map<String, String> map) {
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
            if (pairs.getValue() == null) {
                map.put(pairs.getKey(), "");
            }
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        return map;
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


    public void onProfileUpdateSuccess(String message) {


        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                dialogInterface.dismiss();

            }
        });
        builder.show();
    }


}