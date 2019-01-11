package com.iembsys.admin.nimbumirchi.customer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.iembsys.admin.nimbumirchi.customer.data.DBAdapter;
import com.iembsys.admin.nimbumirchi.customer.data.ProfileData;

public class EditProfileActivity extends AppCompatActivity implements
        EditProfileFragment.OnFragmentInteractionListener {

    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);

        db = new DBAdapter(this);
        db.open();

        String firmId = "";
        String firmName = "";
        String contactName = "";
        String personContactNo = "";
        String homeContactNo = "";
        String officeContactNo = "";
        String panNo = "";
        String tinNo = "";
        String address = "";
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
        Log.i("Profile Cursor Length", String.valueOf(firmListCursor.getCount()));
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

        ProfileData data = new ProfileData();

        data.setAccountId(firmId);
        data.setAccountName(firmName);
        data.setPersonContactNo(personContactNo);
        data.setHomeContactNo(homeContactNo);
        data.setContactName(contactName);
        data.setOfficeContactNo(officeContactNo);
        data.setTinNo(tinNo);
        data.setPanNo(panNo);
        data.setAddress(address);
        data.setEmail(email);
        data.setStateId(stateId, db);
        data.setCityId(cityId, db);
        data.setDoNoCall(doNoCall);
        data.setBankName(bankName);
        data.setBankAccountNo(bankAccountNo);
        data.setBankBranchName(bankBranchName);
        data.setBankBranchCode(bankBranchCode);
        data.setRemark(remark);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment profileListFragment = EditProfileFragment.newInstance(data, "");
        fragmentTransaction.add(R.id.frameLayoutInner, profileListFragment);
        fragmentTransaction.commit();


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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