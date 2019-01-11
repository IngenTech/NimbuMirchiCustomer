package com.iembsys.admin.nimbumirchi.customer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iembsys.admin.nimbumirchi.customer.BuildConfig;
import com.iembsys.admin.nimbumirchi.customer.MainActivity;
import com.iembsys.admin.nimbumirchi.customer.data.DocumentData;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Admin on 26-12-2016.
 */

public class MyUtility {

    private static MyUtility MyUtility;
    private String mGCMRegistrationId = "";

    private MyUtility() {
    }

    public static MyUtility getInstance() {

        if (MyUtility == null)
            MyUtility = new MyUtility();
        return MyUtility;
    }

    public void generateGCM_ID(Context context) {
        mGCMRegistrationId = SharedPreference.getStringValue(context, AppConstant.Preference.APP_PREFERENCE); ////////
        if (mGCMRegistrationId != null) {
            boolean sharedStatus = SharedPreference.getBooleanValue(context, AppConstant.Preference.IS_GCM_SHARED);
            Log.d(MainActivity.TAG, "sharedStatus " + sharedStatus);
            if (!sharedStatus) {
                if (MyUtility.isOnline(context)) {
                    shareData(context, mGCMRegistrationId);
                }
            } else {
                if (MyUtility.isOnline(context)) {
                    shareData(context, mGCMRegistrationId);
                }
            }
        } else {
            if (isOnline(context)) {
                getRegistrationId(context);
            }
        }
    }

    private void getRegistrationId(final Context context) {
        GCMClientManager pushClientManager = new GCMClientManager(context, BuildConfig.PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                Log.d("Registration id", registrationId);
                if (registrationId != null && registrationId.trim().length() > 0) {
                    mGCMRegistrationId = registrationId;
                    SharedPreference.setValue(context, AppConstant.Preference.PREF_GCM_ID, mGCMRegistrationId);
                    if (MyUtility.isOnline(context)) {
                        shareData(context, mGCMRegistrationId);
                    }
                }
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }

    private void shareData(final Context context, final String gcmId) {

        String url = AppConstant.API.SHARE_GCM_ID_API + SharedPreference.getStringValue(context, AppConstant.Preference.PREF_GCM_ID);
        Log.d(MainActivity.TAG, "share GCM with url : " + url);

        StringRequest stringCustomerRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String customerResponse) {
                        Log.d(MainActivity.TAG, "GCM Registration Response : " + customerResponse);
                        if (customerResponse.contains("success")) {
                            SharedPreference.setValue(context, AppConstant.Preference.IS_GCM_SHARED, true);
                            Log.d(MainActivity.TAG, "GCM ID SHARED");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                SharedPreference.setValue(context, AppConstant.Preference.IS_GCM_SHARED, false);
                Log.d(MainActivity.TAG, "GCM ID NOT SHARED");
                Toast.makeText(context, "Not able to share data with server", Toast.LENGTH_LONG).show();

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("gcm_id", gcmId);
                map.put("version", BuildConfig.VERSION_NAME);

                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    Log.d(MainActivity.TAG, pair.getKey() + " = " + pair.getValue());
                }

                return map;
            }
        };

        stringCustomerRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        PersonApplication.getInstance().addToRequestQueue(stringCustomerRequest);
    }

    public static String getDocumentImageName(DocumentData documentData) {
        String documentName = documentData.getDocumentId();

        return documentName;
    }

    public static String getImagePath(String orderId, String docId) {
        String myDirectory = Environment.getExternalStorageDirectory()
                + File.separator
                + AppConstant.Constants.APP_DIR
                + File.separator
                + orderId;
        File imagefile = new File(myDirectory);
        if (!imagefile.exists()) {
            imagefile.mkdirs();
        }
        return myDirectory + File.separator + docId + AppConstant.Constants.IMAGE_EXTENSIONS;
    }

    public static boolean isOnline(Context context) {
        boolean isConnected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            isConnected = cm.getActiveNetworkInfo().isConnected();
        } catch (Exception ex) {
            isConnected = false;
        }
        return isConnected;
    }


    public static String md5(String input) {

        String md5 = null;


        if (null == input) return null;

        try {
        String aaa = "AmitKumarPriyadarshi";
        String xyz = aaa.concat(input);

            md5 = SHA1(xyz);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return md5;
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

}
