package com.iembsys.admin.nimbumirchi.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iembsys.admin.nimbumirchi.customer.util.AppConstant;
import com.iembsys.admin.nimbumirchi.customer.util.MyUtility;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = SignupActivity.class.getSimpleName();

    @Bind(R.id.input_name)
    EditText mNameTxt;
    @Bind(R.id.input_contact)
    EditText mContactTxt;
    @Bind(R.id.input_email)
    EditText mEmailTxt;
    @Bind(R.id.input_user_id)
    EditText mUserIdText;
    @Bind(R.id.input_password)
    EditText mPasswordText;
    @Bind(R.id.input_reEnterPassword)
    EditText mReEnterPasswordText;
    @Bind(R.id.btn_signup)
    Button mSignupButton;
    @Bind(R.id.link_login)
    TextView mLoginLink;

    @Bind(R.id.radio_group)
    RadioGroup typeRadioGroup;
    @Bind(R.id.individual)
    RadioButton individualRadioButton;
    @Bind(R.id.corporate)
    RadioButton corporateRadioButton;

    private SharedPreferences prefs;
    private TelephonyManager mTelephonyManager;
    private String mIMEI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        mLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);*/
                finish();
            }
        });

        individualRadioButton.setChecked(true);
        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.individual) {

                }
                if (i == R.id.corporate) {

                } else {

                }
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed("Signup Failed");
            return;
        }

        mSignupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = mNameTxt.getText().toString();
        String emailID = mEmailTxt.getText().toString();
        String userId = mUserIdText.getText().toString();
        String password = mPasswordText.getText().toString();
        String contactNo = mContactTxt.getText().toString();

        otpRequest(name, emailID, userId, password, contactNo, progressDialog);

    }

    private void otpRequest(final String userName, final String userEmail,
                            final String userId,
                            final String pass,
                            final String contactNo,
                            final ProgressDialog progressDialog) {
        StringRequest stringLoginRequest = new StringRequest(Request.Method.POST, AppConstant.API.REGISTRATION_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String signupResponse) {
                        progressDialog.dismiss();
                        Log.d(TAG, "SIgnup Response : " + signupResponse);
                        try {

                            JSONObject jsonObject = new JSONObject(signupResponse);
                            String status = jsonObject.has("Status") ? jsonObject.getString("Status") : "";

                            if (status!=null && status.equalsIgnoreCase("1")) {

                                if (jsonObject.getString("Result").equalsIgnoreCase("success")) {
                                 //   String otpString = jsonObject.has("otp") ? jsonObject.getString("otp") : "";
                                    String messageString = jsonObject.has("Message") ? jsonObject.getString("Message") : "";
                                    String user_ID = jsonObject.has("UserId") ? jsonObject.getString("UserId") : "";

                                    Log.v("userID",user_ID);

                                    onSignupSuccess(messageString,user_ID);
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
               // map.put("Name", BuildConfig.API_KEY);
                String md5_pass = MyUtility.md5(pass);
                map.put("Username", userId);
                map.put("Password", md5_pass);
                map.put("UserType", "5");
                map.put("ContactNo", contactNo);
                map.put("Name", userName);
                map.put("Email", userEmail);

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                return map;
            }
        };
        NimbuMirchiApplication.getInstance().addToRequestQueue(stringLoginRequest);
    }


    public void onSignupSuccess(String msg,String user_ID) {
        mSignupButton.setEnabled(true);
        setResult(RESULT_OK, null);

        String userName = mNameTxt.getText().toString();
        String userId = mUserIdText.getText().toString();
        String password = mPasswordText.getText().toString();
        String contactNo = mContactTxt.getText().toString();
        String email = mEmailTxt.getText().toString();
        String userType = AppConstant.Constants.INDIVIDUAL_CUSTOMER;
        if (corporateRadioButton.isChecked()) {
            userType = AppConstant.Constants.CORPORATE_CUSTOMER;
        }

        finish();
        Intent otpVerificationIntent = new Intent(this, OtpVerificationActivity.class);
        //otpVerificationIntent.putExtra(OtpVerificationActivity.OTP, otp);
        otpVerificationIntent.putExtra(OtpVerificationActivity.CONTACT_NO, contactNo);
        otpVerificationIntent.putExtra(OtpVerificationActivity.USER_NAME, userName);
        otpVerificationIntent.putExtra(OtpVerificationActivity.USER_ID, userId);
        otpVerificationIntent.putExtra(OtpVerificationActivity.EMAIL, email);
        otpVerificationIntent.putExtra(OtpVerificationActivity.PASS, password);
        otpVerificationIntent.putExtra(OtpVerificationActivity.USER_TYPE, userType);
        otpVerificationIntent.putExtra(OtpVerificationActivity.OTP_TYPE, OtpVerificationActivity.SIGN_UP);
        otpVerificationIntent.putExtra("userID", user_ID);
        startActivity(otpVerificationIntent);

    }

    public void onSignupFailed(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Failed");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {dialogInterface.dismiss();
            }
        });
        builder.show();
        mSignupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String crnNo = mNameTxt.getText().toString();
        String userId = mUserIdText.getText().toString();
        String email = mEmailTxt.getText().toString();
        String contact = mContactTxt.getText().toString();
        String password = mPasswordText.getText().toString();
        String reEnterPassword = mReEnterPasswordText.getText().toString();

        if (crnNo.isEmpty() || crnNo.length() < 3) {
            mNameTxt.setError("at least 3 characters");
            valid = false;
        } else {
            mNameTxt.setError(null);
        }

        if (contact.isEmpty() || contact.length() < 10) {
            mContactTxt.setError("at least 10 characters");
            valid = false;
        } else {
            mContactTxt.setError(null);
        }

        if (email.isEmpty() || email.length() < 12) {
            mEmailTxt.setError("at least 12 characters");
            valid = false;
        } else {
            mNameTxt.setError(null);
        }

        if (userId.isEmpty()) {
            mUserIdText.setError("Enter Valid UserId");
            valid = false;
        } else {
            mUserIdText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            mReEnterPasswordText.setError("Password do not match");
            valid = false;
        } else {
            mReEnterPasswordText.setError(null);
        }

        return valid;
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
