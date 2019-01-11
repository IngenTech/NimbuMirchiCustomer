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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PROFILE_DATA = "profile_data";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ProfileData profileData;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param profileData Parameter 1.
     * @param param2      Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(ProfileData profileData, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(PROFILE_DATA, profileData);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            profileData = getArguments().getParcelable(PROFILE_DATA);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_save) {
           /* if(isValid()) {
                saveChanges();
            }*/

            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);

            return true;
        } else if (id == android.R.id.home) {
            getActivity().finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        edtTxtFirmName = (EditText) view.findViewById(R.id.edtTxtFirmName);
        edtTxtContactName = (EditText) view.findViewById(R.id.edtTxtContactName);
        edtTxtPersonalContact = (EditText) view.findViewById(R.id.edtTxtPersonalContact);
        edtTxtOfficeContact = (EditText) view.findViewById(R.id.edtTxtOfficeContact);
        edtTxtAddress = (EditText) view.findViewById(R.id.edtTxtAddress);
        edtTxtEmail = (EditText) view.findViewById(R.id.edtTxtEmail);

        edtTxtPanNo = (EditText) view.findViewById(R.id.edtTxtPanNo);
        edtTxtHomeContact = (EditText) view.findViewById(R.id.edtTxtHomeContact);
        edtTxtTinNo = (EditText) view.findViewById(R.id.edtTxtTinNo);
        edtTxtBankName = (EditText) view.findViewById(R.id.edtTxtBankName);
        edtTxtBankAccountNo = (EditText) view.findViewById(R.id.edtTxtBankAccountNo);
        edtTxtBankBranchName = (EditText) view.findViewById(R.id.edtTxtBankBranchName);
        edtTxtBankBranchCode = (EditText) view.findViewById(R.id.edtTxtBankBranchCode);

        spinnerState = (Spinner) view.findViewById(R.id.spinnerState);
        spinnerCity = (Spinner) view.findViewById(R.id.spinnerCity);
        btnSaveChanges = (Button) view.findViewById(R.id.btnSaveChanges);
        doNotCall = (CheckBox) view.findViewById(R.id.doNotCall);

        db = new DBAdapter(getActivity());
        db.open();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        stateListCursor = db.getStateList();

        final ArrayList<String> stateNameArray = new ArrayList<>();
        if (stateListCursor.moveToFirst()) {
            do {
                String stateString = stateListCursor.getString(stateListCursor.getColumnIndex(DBAdapter.STATE_NAME));
                stateNameArray.add(stateString);
            } while (stateListCursor.moveToNext());
        }
        ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, stateNameArray);

        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(state_adapter);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item was selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                stateListCursor.moveToPosition(pos);
                String stateId = stateListCursor.getString(stateListCursor.getColumnIndex(DBAdapter.STATE_ID));
                String stateName = stateListCursor.getString(stateListCursor.getColumnIndex(DBAdapter.STATE_NAME));
                profileData.setStateId(stateId);
                profileData.setStateName(stateName);

                cityListCursor = db.cityListByStateId(stateId);

                final ArrayList<String> cityNameArray = new ArrayList<>();
                if (cityListCursor.moveToFirst()) {
                    do {
                        String cityString = cityListCursor.getString(cityListCursor.getColumnIndex(DBAdapter.CITY_NAME));
                        cityNameArray.add(cityString);
                    } while (cityListCursor.moveToNext());
                }

                ArrayAdapter<String> city_adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, cityNameArray);
                city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCity.setAdapter(city_adapter);
                spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    /**
                     * Called when a new item was selected (in the Spinner)
                     */
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int pos, long id) {
                        cityListCursor.moveToPosition(pos);
                        String cityId = cityListCursor.getString(cityListCursor.getColumnIndex(DBAdapter.CITY_ID));
                        String cityName = cityListCursor.getString(cityListCursor.getColumnIndex(DBAdapter.CITY_NAME));
                        profileData.setCityId(cityId);
                        profileData.setCityName(cityName);
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

        setProfileData();

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

            if (profileData.getStateId() != null && profileData.getStateId().trim().length() > 0) {
                if (stateListCursor.moveToFirst()) {
                    for (int i = 0; i < stateListCursor.getCount(); i++) {
                        if (stateListCursor.getString(stateListCursor.getColumnIndex(DBAdapter.STATE_ID)).equals(profileData.getStateId())) {
                            spinnerState.setSelection(i, true);
                            break;
                        }
                        stateListCursor.moveToNext();
                    }
                }
            }

            if (profileData.getDoNoCall().equals("true")) {
                doNotCall.setChecked(true);
            }

        }
    }

    private boolean isValid() {
        boolean isValid = true;
        if (edtTxtFirmName.getText() != null && edtTxtFirmName.getText().toString().trim().length() > 1) {
            profileData.setAccountName(edtTxtFirmName.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Firm Name is Blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtContactName.getText() != null && edtTxtContactName.getText().toString().trim().length() > 0) {
            profileData.setContactName(edtTxtContactName.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Contact Name is Blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtPersonalContact.getText() != null && edtTxtPersonalContact.getText().toString().trim().length() > 9) {
            profileData.setPersonContactNo(edtTxtPersonalContact.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please enter valid Personal Contact Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtOfficeContact.getText() != null && edtTxtOfficeContact.getText().toString().trim().length() > 9) {
            profileData.setOfficeContactNo(edtTxtOfficeContact.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please Enter valid Office Contact Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtAddress.getText() != null && edtTxtAddress.getText().toString().trim().length() > 0) {
            profileData.setAddress(edtTxtAddress.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please Enter Address", Toast.LENGTH_SHORT).show();
            return false;
        }

        String target = edtTxtEmail.getText().toString().trim();

        if (edtTxtEmail.getText() != null && isValidEmail(target)) {
            profileData.setEmail(edtTxtEmail.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please enter valid email id.", Toast.LENGTH_SHORT).show();
            return false;
        }

        String pan = edtTxtPanNo.getText().toString().trim();

        if (pan != null && pan.length() > 4) {
            profileData.setPanNo(pan);
        } else {
            Toast.makeText(getActivity(), "Please Enter Pan No atleast 5 digits", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtHomeContact.getText() != null && edtTxtHomeContact.getText().toString().trim().length() > 0) {
            profileData.setHomeContactNo(edtTxtHomeContact.getText().toString());
        }

        if (edtTxtTinNo.getText() != null && edtTxtTinNo.getText().toString().trim().length() > 4) {
            profileData.setTinNo(edtTxtTinNo.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please Enter Valid Tin No", Toast.LENGTH_SHORT).show();

            return false;
        }

        if (edtTxtBankName.getText() != null && edtTxtBankName.getText().toString().trim().length() > 2) {
            profileData.setBankName(edtTxtBankName.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please Enter Minimum 2 Charaters Bank Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtBankAccountNo.getText() != null && edtTxtBankAccountNo.getText().toString().trim().length() > 6) {
            profileData.setBankAccountNo(edtTxtBankAccountNo.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please Enter Valid Bank Account No", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtBankBranchName.getText() != null && edtTxtBankBranchName.getText().toString().trim().length() > 3) {
            profileData.setBankBranchName(edtTxtBankBranchName.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please Enter Valid Bank Branch Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (edtTxtBankBranchCode.getText() != null && edtTxtBankBranchCode.getText().toString().trim().length() > 2) {
            profileData.setBankBranchCode(edtTxtBankBranchCode.getText().toString());
        } else {
            Toast.makeText(getActivity(), "Please enter Valid Bank Branch Code", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!(profileData.getStateId() != null && profileData.getStateId().trim().length() > 0)) {
            Toast.makeText(getActivity(), "Please Select State", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!(profileData.getCityId() != null && profileData.getCityId().trim().length() > 0)) {
            Toast.makeText(getActivity(), "Please Select City", Toast.LENGTH_SHORT).show();
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
                        System.out.println("Edit Profile Response    : " + editProfileResponse);
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(editProfileResponse);

                            if (jsonObject.has("result")) {
                                if (jsonObject.getString("result").equals("success")) {

                                    profileData.save(db, EditProfileFragment.this.getActivity());

                                    new AlertDialog.Builder(getActivity())
                                            .setMessage("Profile has been updated")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
//                                                    getActivity().getSupportFragmentManager().popBackStack();
                                                    getActivity().finish();
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .show();
                                } else {
                                    Toast.makeText(getActivity(), "Invalid Account Detail", Toast.LENGTH_LONG).show();
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
//                volleyError.printStackTrace();
                dialog.cancel();
                Toast.makeText(getActivity(), "Not able to connect with server", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                if (prefs == null) {
                    prefs = getActivity().getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, getActivity().MODE_PRIVATE);
                }
                String accountId = (prefs.getString(AppConstant.Preference.ACCOUNT_ID, "0"));
                String customerType = (prefs.getString(AppConstant.Preference.ACCOUNT_TYPE, "0"));

                Map<String, String> map = new HashMap<>();
                map.put("api_key", BuildConfig.API_KEY);
                map.put("account_id", accountId);
                map.put("customer_type", customerType);
                map.put("pan_no", profileData.getPanNo());
                map.put("firm_name", profileData.getAccountName());
                map.put("contact_name", profileData.getContactName());
                map.put("personal_contact_no", profileData.getPersonContactNo());
                map.put("home_contact_no", profileData.getHomeContactNo());
                map.put("office_contact_no", profileData.getOfficeContactNo());
                map.put("tin_no", profileData.getTinNo());
                map.put("country_id", "1");
                map.put("state_id", profileData.getStateId());
                map.put("city_id", profileData.getCityId());
                map.put("address", profileData.getAddress());
                map.put("email_id", profileData.getEmail());
                map.put("do_no_call", profileData.getDoNoCall());
                map.put("bank_name", profileData.getBankName());
                map.put("bank_account_no", profileData.getBankAccountNo());
                map.put("bank_branch_name", profileData.getBankBranchName());
                map.put("bank_branch_code", profileData.getBankBranchCode());
                map.put("edit_id", profileData.getAccountId(getActivity()));
//                map.put("edit_date", profileData.getDoNoCall());

                return checkParams(map);
            }
        };
        dialog = ProgressDialog.show(getActivity(), "Saving Changes", "Please wait...", true);
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

