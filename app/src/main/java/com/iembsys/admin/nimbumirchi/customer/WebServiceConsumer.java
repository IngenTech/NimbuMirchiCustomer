package com.iembsys.admin.nimbumirchi.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iembsys.admin.nimbumirchi.customer.data.DBAdapter;
import com.iembsys.admin.nimbumirchi.customer.util.AppConstant;
import com.iembsys.admin.nimbumirchi.customer.util.MyUtility;

import android.content.SharedPreferences;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by WRMS on 26-05-2016.
 */
public class WebServiceConsumer {

    public final int SYNC_STATE = 1;
    public final int SYNC_CITY = 2;
    public final int SYNC_MATERIAL = 3;
    private Activity activity;
    private DBAdapter db;
    public int syncCount = 0;
    String dialogMessage;
    List<String> stateIdArrayList;


    public WebServiceConsumer(Activity activity, DBAdapter db) {
        this.activity = activity;
        this.db = db;
    }

    /**
     * This method will sync all data like city, material, block etc.syncing start from city list
     */
    public void syncAllData() {
        setSyncCount(SYNC_STATE);
        syncData();
    }

    SharedPreferences prefs;

    public void syncData() {
        if (!MyUtility.isOnline(activity)) {
            Toast.makeText(activity, activity.getResources().getString(R.string.network_problem), Toast.LENGTH_LONG).show();
            return;
        }

        if (prefs == null) {
            prefs = activity.getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, activity.MODE_PRIVATE);
        }
        String accountId = (prefs.getString(AppConstant.Preference.ACCOUNT_ID, "0"));


        System.out.println(accountId + " Sync Data Called Position" + syncCount);
        Map<String, String> param = new HashMap<>();
        switch (getSyncCount()) {
            case SYNC_STATE:
                dialogMessage = "State";
                param.put("api_key", BuildConfig.API_KEY);
                param.put("account_id", accountId);
                syncData(activity, db, param, AppConstant.API.STATE_API, dialogMessage);
                break;
            case SYNC_CITY:
                dialogMessage = "City";
                param.put("api_key", BuildConfig.API_KEY);
                param.put("account_id", accountId);
                syncData(activity, db, param, AppConstant.API.CITY_API, dialogMessage);
                break;
            case SYNC_MATERIAL:
                dialogMessage = "Material";
                param.put("api_key", BuildConfig.API_KEY);
                param.put("account_id", accountId);
                syncData(activity, db, param, AppConstant.API.MATERIAL_API, dialogMessage);

             //   onSignupSuccess(succMessage);


                break;
        }
    }

    /**
     * Volley Request to sync the application data
     *
     * @param context
     * @param db
     * @param param
     * @param URL
     * @param dialogMessage shows message on the progress bar
     */
    public void syncData(final Context context, final DBAdapter db, final Map<String, String> param, String URL, String dialogMessage) {

        final ProgressDialog dialog = ProgressDialog.show(context, dialogMessage,
                "Please wait...", true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Response : " + response);

                        Log.v("response_on_sync", response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("Result")) {
                                if (jsonObject.getString("Result").equals("Success")) {

                                    if (dialog != null && dialog.isShowing()) {
                                        dialog.cancel();
                                    }
                                    switch (getSyncCount()) {
                                        case SYNC_STATE:
                                            state(jsonObject, db, context);
                                            break;
                                        case SYNC_CITY:
                                            city(jsonObject, db, context);
                                            break;
                                        case SYNC_MATERIAL:
                                            material(jsonObject, db, context);
                                            break;
                                    }

                                } else {
                                    Toast.makeText(context, "request has been refused", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(context, "Blank Response", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Json Parser Error!", Toast.LENGTH_LONG).show();
                        }
                        if (dialog != null && dialog.isShowing()) {
                            dialog.cancel();
                        }
                        syncCount++;
                        syncData();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                if (dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                }
                syncCount++;
                syncData();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (prefs == null) {
                    prefs = context.getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, context.MODE_PRIVATE);
                }

           /* String user_id = (prefs1.getString(AppConstant.Preference.USER_ID, "0"));
            String physical_address = (prefs1.getString(AppConstant.Preference.PHYSICAL_ADDRESS, "0"));*/
                String access_token = (prefs.getString(AppConstant.Preference.ACCESS_TOKEN, "0"));

                String accountId = (prefs.getString(AppConstant.Preference.USER_ID, "0"));
                Map<String, String> map = new HashMap<>();
                map.put("AccessToken",access_token);
                map.put("UserId", accountId);

                Log.v("access_token", access_token);
                Log.v("account_id", accountId);

                return map;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        NimbuMirchiApplication.getInstance().addToRequestQueue(stringRequest);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void state(JSONObject jsonObject, DBAdapter db, Context context) {
        SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
        SqliteDB.beginTransaction();
        try {
            JSONArray jArray = jsonObject.getJSONArray("States");
            if (jArray.length() == 0) {
                System.out.println("No data found For State");
            } else {
                db.db.execSQL("delete from " + DBAdapter.TABLE_STATE);
            }
            String query = "INSERT INTO " + DBAdapter.TABLE_STATE + "(" +
                    DBAdapter.ID + "," +
                    DBAdapter.STATE_ID + "," +
                    DBAdapter.STATE_NAME + "," +
                    DBAdapter.ROAD_PERMIT + "," +
                    DBAdapter.MIN_ROAD_PERMIT_AMOUNT +
                    ") VALUES (?,?,?,?,? )";
            SQLiteStatement stmt = SqliteDB.compileStatement(query);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                stmt.bindString(1, "" + i);
                stmt.bindString(2, jObject.getString("StateId"));
                stmt.bindString(3, jObject.getString("StateName"));
                stmt.bindString(4, "No");
                stmt.bindString(5, "0");
                stmt.execute();
            }
            SqliteDB.setTransactionSuccessful();
            SqliteDB.endTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            SqliteDB.setTransactionSuccessful();
            SqliteDB.endTransaction();
            Toast.makeText(context, "Could not insert state in local database", Toast.LENGTH_LONG).show();
        }

    }

    public void city(JSONObject jsonObject, DBAdapter db, Context context) {
        SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
        SqliteDB.beginTransaction();
        try {
            JSONArray jArray = jsonObject.getJSONArray("Cities");
            if (jArray.length() == 0) {
                System.out.println("No data found For city");
            } else {
                db.db.execSQL("delete from " + DBAdapter.TABLE_CITY);
            }

            String query = "INSERT INTO " + DBAdapter.TABLE_CITY + "(" + DBAdapter.ID + "," + DBAdapter.CITY_ID + "," + DBAdapter.CITY_NAME + "," + DBAdapter.STATE_ID + ") VALUES (?,?,?,? )";

            SQLiteStatement stmt = SqliteDB.compileStatement(query);

            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jObject = jArray.getJSONObject(i);
                stmt.bindString(1, "" + i);
                stmt.bindString(2, jObject.getString("CityId"));
                stmt.bindString(3, jObject.getString("CityName"));
                stmt.bindString(4, jObject.getString("StateId"));

                stmt.execute();

            }
            SqliteDB.setTransactionSuccessful();
            SqliteDB.endTransaction();

        } catch (Exception e) {
            e.printStackTrace();
            SqliteDB.setTransactionSuccessful();
            SqliteDB.endTransaction();
            Toast.makeText(context, "Could not insert city in local database", Toast.LENGTH_LONG).show();
        }

    }

    public void material(JSONObject jsonObject, DBAdapter db, Context context) {

        System.out.println("Sync Data Called Position After" + syncCount);
        SQLiteDatabase SqliteDB = db.getSQLiteDatabase();
        SqliteDB.beginTransaction();
        try {
            JSONArray jArray = jsonObject.getJSONArray("MaterialType");
            if (jArray.length() == 0) {
                System.out.println("No data found For Material");
            } else {
                db.db.execSQL("delete from " + DBAdapter.TABLE_MATERIAL);
            }
            String query = "INSERT INTO " + DBAdapter.TABLE_MATERIAL + "(" +
                    DBAdapter.ID + "," +
                    DBAdapter.MATERIAL_ID + "," +
                    DBAdapter.MATERIAL_NAME +
                    ") VALUES (?,?,? )";
            SQLiteStatement stmt = SqliteDB.compileStatement(query);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                stmt.bindString(1, "" + i);
                stmt.bindString(2, jObject.getString("MaterialTypeId"));
                stmt.bindString(3, jObject.getString("MaterialTypeName"));
                stmt.execute();
            }
            SqliteDB.setTransactionSuccessful();
            SqliteDB.endTransaction();

            if (prefs == null) {
                prefs = activity.getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, activity.MODE_PRIVATE);
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(AppConstant.Preference.IS_LOGIN, true);
            editor.commit();
            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.finish();

        } catch (Exception e) {
            e.printStackTrace();
            SqliteDB.setTransactionSuccessful();
            SqliteDB.endTransaction();
            Toast.makeText(context, "Could not insert material in local database", Toast.LENGTH_LONG).show();

            if (prefs == null) {
                prefs = activity.getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, activity.MODE_PRIVATE);
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(AppConstant.Preference.IS_LOGIN, true);
            editor.commit();
            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public int getSyncCount() {
        return syncCount;
    }

    public void setSyncCount(int syncCount) {
        this.syncCount = syncCount;
    }
}
