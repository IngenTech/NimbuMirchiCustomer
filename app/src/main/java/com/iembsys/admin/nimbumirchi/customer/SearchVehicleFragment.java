package com.iembsys.admin.nimbumirchi.customer;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iembsys.admin.nimbumirchi.customer.adapter.LocationAutoCompleteAdapter;
import com.iembsys.admin.nimbumirchi.customer.data.DBAdapter;
import com.iembsys.admin.nimbumirchi.customer.data.LocationModel;
import com.iembsys.admin.nimbumirchi.customer.data.SearchTransporterData;
import com.iembsys.admin.nimbumirchi.customer.data.TransporterData;
import com.iembsys.admin.nimbumirchi.customer.util.AppConstant;
import com.iembsys.admin.nimbumirchi.customer.util.MyUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchVehicleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchVehicleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchVehicleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchVehicleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchVehicleFragment newInstance(String param1, String param2) {
        SearchVehicleFragment fragment = new SearchVehicleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = new DBAdapter(getActivity());
        db.open();
    }

    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.search_transporter));
        }
    }

    AutoCompleteTextView fromLocationAutocompleteTextView;
    AutoCompleteTextView toLocationAutocompleteTextView;
    LocationAutoCompleteAdapter fromLocationAutoCompleteAdapter;
    LocationAutoCompleteAdapter toLocationAutoCompleteAdapter;
    EditText edtTxtDate;
    Spinner selectCategory;
    EditText edtTxtItem;
    EditText edtTxtCapacity;
    CheckBox checkBoxDoorClose;
    CheckBox checkBoxRefrigerated;
    EditText edtTxtNoOfVehicles;
    Button btnSearchTransporter;

    SearchTransporterData data;
    ProgressDialog dialog;
    SharedPreferences prefs;
    ArrayList<TransporterData> transporterList = new ArrayList<>();

    private static String DATE_FORMAT_STRING = "yyyy-MM-dd";
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING);

    DBAdapter db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_vehicle, container, false);

        fromLocationAutocompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.fromLocationAutocompleteTextView);
        toLocationAutocompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.toLocationAutocompleteTextView);
        edtTxtDate = (EditText) view.findViewById(R.id.edtTxtDate);
        selectCategory = (Spinner) view.findViewById(R.id.selectCategory);
        edtTxtItem = (EditText) view.findViewById(R.id.edtTxtItem);
        edtTxtCapacity = (EditText) view.findViewById(R.id.edtTxtCapacity);
        checkBoxDoorClose = (CheckBox) view.findViewById(R.id.checkBoxDoorClose);
        checkBoxRefrigerated = (CheckBox) view.findViewById(R.id.checkBoxRefrigerated);
        edtTxtNoOfVehicles = (EditText) view.findViewById(R.id.edtTxtNoOfVehicles);
        btnSearchTransporter = (Button) view.findViewById(R.id.btnSearchTransporter);

        if (data == null) {
            data = new SearchTransporterData();
        }

        ArrayList<LocationModel> locationModels = new ArrayList<>();
        Cursor cityCursor = db.getCityList();
        if (cityCursor.moveToFirst()) {
            do {
                String cityId = cityCursor.getString(cityCursor.getColumnIndex(DBAdapter.CITY_ID));
                String cityName = cityCursor.getString(cityCursor.getColumnIndex(DBAdapter.CITY_NAME));
                String stateId = cityCursor.getString(cityCursor.getColumnIndex(DBAdapter.STATE_ID));

                Cursor stateCursor = db.stateById(stateId);
                if (stateCursor.moveToFirst()) {
                    String stateName = stateCursor.getString(stateCursor.getColumnIndex(DBAdapter.STATE_NAME));
                    String locationName = stateName + "," + cityName;

                    LocationModel locationModel = new LocationModel(stateId, stateName, cityId, cityName, locationName);
                    locationModels.add(locationModel);
                }
                stateCursor.close();

            } while (cityCursor.moveToNext());
        }
        cityCursor.close();

        ArrayList<LocationModel> fromLocationModels = new ArrayList<>();
        fromLocationModels.addAll(locationModels);
        fromLocationAutoCompleteAdapter = new LocationAutoCompleteAdapter(getActivity(), R.layout.auto_complete_textview_item, fromLocationModels);
        fromLocationAutocompleteTextView.setAdapter(fromLocationAutoCompleteAdapter);
        fromLocationAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                LocationModel selected = (LocationModel) arg0.getAdapter().getItem(arg2);
                data.setFromStateId(selected.getStateId());
                data.setFromCityId(selected.getCityId());
                /*Toast.makeText(getActivity(),
                        "From " + arg2 + " name: " + selected.getCityName(),
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        ArrayList<LocationModel> toLocationModels = new ArrayList<>();
        toLocationModels.addAll(locationModels);
        toLocationAutoCompleteAdapter = new LocationAutoCompleteAdapter(getActivity(), R.layout.auto_complete_textview_item, toLocationModels);
        toLocationAutocompleteTextView.setAdapter(toLocationAutoCompleteAdapter);
        toLocationAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                LocationModel selected = (LocationModel) arg0.getAdapter().getItem(arg2);
                data.setToStateId(selected.getStateId());
                data.setToCityId(selected.getCityId());
                /*Toast.makeText(getActivity(),
                        "To " + arg2 + " name: " + selected.getCityName(),
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        edtTxtDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                final View view = LayoutInflater.from(getActivity()).inflate(R.layout.date_picker, null);
                adb.setView(view);
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.datePicker1);

                //  date .getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker.setMinDate(System.currentTimeMillis() - 1000);

                final Dialog dialog;
                adb.setPositiveButton("Add", new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        java.util.Date date = null;
                        Calendar cal = GregorianCalendar.getInstance();
                        cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        date = cal.getTime();
                        String selectedDate = DATE_FORMAT.format(date);
                        edtTxtDate.setText(selectedDate);
                    }
                });
                dialog = adb.create();
                dialog.show();
            }
        });

        final Cursor materialList = db.materialList();

        Log.v("materialCounr",materialList.getCount()+"");

        final ArrayList<String> materialNameArray = new ArrayList<>();
        materialNameArray.add("select category");
        if (materialList.moveToFirst()) {
            do {
                String materialString = materialList.getString(materialList.getColumnIndex(DBAdapter.MATERIAL_NAME));
                materialNameArray.add(materialString);
            } while (materialList.moveToNext());
        }
        ArrayAdapter<String> material_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, materialNameArray);

        material_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCategory.setAdapter(material_adapter);
        selectCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item was selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos>0) {
                    materialList.moveToPosition(pos-1);
                    String materialId = materialList.getString(materialList.getColumnIndex(DBAdapter.MATERIAL_ID));
                    String materialName = materialList.getString(materialList.getColumnIndex(DBAdapter.MATERIAL_NAME));
                    data.setMaterialId(materialId);
                }
            }
            public void onNothingSelected(AdapterView parent) {
                // Do nothing.
            }
        });

        btnSearchTransporter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()) {
                    searchTransporterRequest();
                }
            }
        });

        return view;
    }

    private void searchTransporterRequest() {
        StringRequest stringLoginRequest = new StringRequest(Request.Method.POST, AppConstant.API.SEARCH_TRANSPORTER_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String tripResponse) {
                        dialog.dismiss();
                        try {

                            System.out.println("Search Transporter Response : " + tripResponse);
                            JSONObject jsonObject = new JSONObject(tripResponse);

                            if (jsonObject.has("Status")) {
                                if (jsonObject.getString("Status").equalsIgnoreCase("1")) {

//                                    String accountId = jsonObject.getString("account_id");
                                    JSONArray casesArray = jsonObject.getJSONArray("TransportersList");
                                    if (casesArray.length() > 0) {

                                        for (int i = 0; i < casesArray.length(); i++) {
                                            JSONObject jObject = casesArray.getJSONObject(i);
                                            TransporterData transporterData = new TransporterData();
                                            transporterData.setFirmId(jObject.getString("FirmId"));
                                            transporterData.setFirmName(jObject.getString("FirmName"));
                                            transporterData.setContactName(jObject.getString("ContactName"));
                                            transporterData.setPersonContactNo(jObject.getString("PersonalContactNo"));
                                            transporterData.setOfficeContactNo(jObject.getString("OfficeContactNo"));
                                            transporterData.setAddress(jObject.getString("Address"));

//                                            transporterData.setAddress(jObject.getString("transporter_name"));

                                            transporterData.setFromCityId(jObject.getString("CityId"), db);

                                            /*transporterData.setRefrigerated(jObject.getString("Refrigerated"));
                                            transporterData.setMaterialId(jObject.getString("Category"));
                                            transporterData.setCapacity(jObject.getString("Capacity"));
                                            transporterData.setRate(jObject.getString("RatePerKm"));
                                            transporterData.setAvailableVehicle(jObject.getString("AvailableVehicles"));
                                            transporterData.setFromStateId(data.getFromStateId(), db);
                                            transporterData.setFromCityId(data.getFromCityId(), db);
                                            transporterData.setToStateId(data.getToStateId(), db);
                                            transporterData.setToCityId(data.getToCityId(), db);*/
                                            transporterList.add(transporterData);
                                        }

                                        System.out.println("transporter list size : "+transporterList.size());
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.frame_container, TransporterListFragment.newInstance(transporterList,data));
                                        transaction.addToBackStack(null);
                                        transaction.commit();

                                    } else {
                                        Toast.makeText(getActivity(), "No Transporter Found in this Account", Toast.LENGTH_LONG).show();
                                    }
                                } else if (jsonObject.getString("Status").equalsIgnoreCase("2")) {
                                    String message = "No transporter found.";
                                    if (jsonObject.has("Message")) {
                                        message = jsonObject.getString("Message");
                                    }

                                    SharedPreferences preferences = getActivity().getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, getActivity().MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.commit();

                                    db.resetDatabase();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    getActivity().finish();

                                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                }else {
                                    String message = "No transporter found.";
                                    if (jsonObject.has("Message")) {
                                        message = jsonObject.getString("Message");
                                    }
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Blank Response", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Not able parse response", Toast.LENGTH_LONG).show();
                        }
                        dialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                dialog.cancel();
                Toast.makeText(getActivity(), "Not able to connect with server", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                if (prefs == null) {
                    prefs = getActivity().getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, getActivity().MODE_PRIVATE);
                }
                String userID = (prefs.getString(AppConstant.Preference.USER_ID, "0"));
                String accessToken = (prefs.getString(AppConstant.Preference.ACCESS_TOKEN, "0"));

                String physicalAddress = (prefs.getString(AppConstant.Preference.PHYSICAL_ADDRESS, "0"));

                Map<String, String> map = new HashMap<>();
                map.put("AccessToken", accessToken);
                map.put("UserId", userID);
                map.put("PhysicalAddress", physicalAddress);
                map.put("Capacity", data.getCapacity());
                map.put("FromCity", data.getFromCityId());
                map.put("ToCity", data.getToCityId());
                map.put("CotegoryId", data.getMaterialId());
                String doorClosed = "";
                if (data.isDoorClosed()) {
                    doorClosed = "1";
                } else {
                    doorClosed = "0";
                }
                map.put("DoorStatus", doorClosed);
                String refrigerated = "";
                if (data.isRefrigerated()) {
                    refrigerated = "1";
                } else {
                    refrigerated = "0";
                }
                map.put("RefrigeratedStatus", refrigerated);
                map.put("NoOfVehicles", data.getNoOfVehicles());
             //   map.put("app_type", BuildConfig.APP_TYPE);

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }

                return map;
            }
        };
        dialog = ProgressDialog.show(getActivity(), "Available Transporters",
                "Fetching data...", true);
        NimbuMirchiApplication.getInstance().addToRequestQueue(stringLoginRequest);
    }


    private boolean isValid() {
        boolean isValid = true;

        if (!(data.getFromCityId() != null && data.getFromCityId().trim().length() > 0)) {
            Toast.makeText(getActivity(), "Please Select Source City", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(data.getToCityId() != null && data.getToCityId().trim().length() > 0)) {
            Toast.makeText(getActivity(), "Please Select Destination City", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtDate.getText() != null && edtTxtDate.getText().toString().trim().length() > 0) {
            data.setDate(edtTxtDate.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!(data.getMaterialId() != null && data.getMaterialId().trim().length() > 0)) {
            Toast.makeText(getActivity(), "Please Select Category ", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtItem.getText() != null && edtTxtItem.getText().toString().trim().length() > 2) {
            data.setItem(edtTxtItem.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please Ente Valid Item", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtCapacity.getText() != null && edtTxtCapacity.getText().toString().trim().length() > 0) {
            data.setCapacity(edtTxtCapacity.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please Enter Valid Capacity", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtNoOfVehicles.getText() != null && edtTxtNoOfVehicles.getText().toString().trim().length() > 0) {
            data.setNoOfVehicles(edtTxtNoOfVehicles.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please Enter Valid no. of Vehicles", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (checkBoxDoorClose.isChecked()) {
            data.setDoorClosed(true);
        } else {
            data.setDoorClosed(false);
        }

        if (checkBoxRefrigerated.isChecked()) {
            data.setRefrigerated(true);
        } else {
            data.setRefrigerated(false);
        }

        /*if (!(data.getFromStateId() != null && data.getFromStateId().trim().length() > 0)) {
            Toast.makeText(getActivity(), "Please Select Source State", Toast.LENGTH_SHORT).show();
            return false;
        }*/





        return isValid;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
