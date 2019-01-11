package com.iembsys.admin.nimbumirchi.customer;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.iembsys.admin.nimbumirchi.customer.data.DBAdapter;
import com.iembsys.admin.nimbumirchi.customer.fcm.Config;
import com.iembsys.admin.nimbumirchi.customer.util.AppConstant;
import com.iembsys.admin.nimbumirchi.customer.util.MyUtility;
import com.iembsys.admin.nimbumirchi.customer.util.SharedPreference;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    DBAdapter db;
    SharedPreferences prefs;
    SharedPreferences prefs1;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isLogin = SharedPreference.getBooleanValue(this, AppConstant.Preference.IS_LOGIN);
        if (!isLogin) {
            finish();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            overridePendingTransition(0, 0);
            return;
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBAdapter(this);
        db.open();

        /*db = new DBAdapter(this);
        db.open();

        if(!db.getStateList().moveToFirst()){
            new WebServiceConsumer(this,db).syncAllData();
        }*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = (View) LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        System.out.println("(headerView!=null) : " + (headerView != null));
        if (headerView != null) {
            TextView accountName = (TextView) headerView.findViewById(R.id.accountName);
            TextView vehicleCount = (TextView) headerView.findViewById(R.id.vehicleCount);
            if (prefs == null) {
                prefs = getSharedPreferences(AppConstant.Preference.USER_ID, MODE_PRIVATE);
            }
            String accountNameString = prefs.getString(AppConstant.Preference.ACCOUNT_NAME, "");
            accountName.setText(accountNameString);
        }
        navigationView.addHeaderView(headerView);


        Fragment fragment = null;
        fragment = SearchVehicleFragment.newInstance("", "");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();

        /*Cursor stateCursor = db.getStateList();
        Cursor cityCursor = db.getCityList();
        Cursor materialCursor = db.materialList();

        if(!stateCursor.moveToFirst() && cityCursor.moveToFirst() && !materialCursor.moveToFirst()){
            WebServiceConsumer webService = new WebServiceConsumer(this,db);
            webService.syncAllData();
        }

        stateCursor.close();
        cityCursor.close();
        materialCursor.close();*/

        /*Handler handler = new Handler();

        getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, new ContentObserver(handler){
            @Override
            public void onChange(boolean selfChange, Uri uri){
                super.onChange(selfChange, uri);
                String key = uri.getPath();
                key = key.substring(key.lastIndexOf("/") + 1, key.length());
                write_file("setting value log", true, getDateTime() + "Key : "+key+" value "+Settings.System.getString(getContentResolver(), key) + "\n\r");

                System.out.println("Key : "+key+" value "+Settings.System.getString(getContentResolver(), key));

                if (key.equals("user_powersaver_enable") || key.equals("psm_switch")){
                    boolean batterySaverEnabled = Settings.System.getString(getContentResolver(), key).equals("1");
                    // do something
                }
            }
        });*/

    }

    private String getDateTime() {
        String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_NOW);
        String dateTime = null;
        Date date = new Date();
        dateTime = SIMPLE_DATE_FORMAT.format(date.getTime());
        if (dateTime != null) {
            return dateTime;
        }
        return "0000-00-00 00:00:00";
    }

    static boolean mExternalStorageAvailable = false;
    static boolean mExternalStorageWriteable = false;

    public static boolean status() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        return mExternalStorageAvailable;
    }

    public static boolean chk_mount() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void write_file(String filename, boolean append, String data) {
        if (status()) {
            if (chk_mount()) {
                File root = Environment.getExternalStorageDirectory();

                try {
                    if (root.canWrite()) {

                        File logfile = new File(root.getAbsolutePath() + "/" + "MYfile");
                        if (!logfile.exists()) {
                            logfile.mkdir();
                        }
                        File myfile = new File(logfile + "/" + filename + ".txt");

                        if (!myfile.exists()) {
                            myfile.createNewFile();
                        }

                        FileWriter myFileWriter = new FileWriter(myfile, append);

                        BufferedWriter out = new BufferedWriter(myFileWriter);
                        out.write(data);
                        out.close();
                        myFileWriter.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            FragmentManager fm = this.getSupportFragmentManager();

            if (fm.getBackStackEntryCount() < 1) {
                exitMethod();
            } else {

                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    //  fm.popBackStack();
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        //             switch (item.getItemId()) {
        switch (item.getItemId()) {


            case R.id.action_share_error: {
                File logFile = new File(Environment.getExternalStorageDirectory(), "NimbuMircheeErrorLog.txt");
                if (logFile.exists()) {

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    // set the type to 'email'
                    emailIntent.setType("vnd.android.cursor.dir/email");
                    String to[] = {"vishal.tripathi@iembsys.com"};
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                    // the attachment
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(logFile));
                    // the mail subject
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "NimbuMirchee Error log");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Sent from NimbuMirchee Customer app");

                    if (emailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    } else {
                        Toast.makeText(this, "No email application is available to share error log file", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this, "NimbuMirchee ErrorLog file does not exist ", Toast.LENGTH_LONG).show();
                }
                break;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_book_vehicle:
                fragment = SearchVehicleFragment.newInstance("", "");
                break;
            case R.id.nav_confirm_order:
                fragment = CustomerConfirmOrderFragment.newInstance("", "");
                break;
            case R.id.nav_pending_orders:
                fragment = CustomerPendingOrderFragment.newInstance("", "");
                break;
            case R.id.nav_track_shipment:
                fragment = CustomerRunningTripFragment.newInstance("", "");
                break;
            case R.id.nav_shipment_history:
                fragment = CustomerTripHistoryFragment.newInstance("", "");
                break;

            case R.id.nav_profile:
                Intent editProfilIntent = new Intent(this, ProfileActivity.class);
                startActivity(editProfilIntent);
                break;

            case R.id.nav_logoff:

                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                if (prefs1 == null) {
                    prefs1 = getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, MODE_PRIVATE);
                }
                String user_id = (prefs1.getString(AppConstant.Preference.USER_ID, "0"));
                String physical_address = (prefs1.getString(AppConstant.Preference.PHYSICAL_ADDRESS, "0"));
                String access_token = (prefs1.getString(AppConstant.Preference.ACCESS_TOKEN, "0"));


                logoutRequest(user_id, access_token, physical_address, progressDialog);

                break;

            default:

                /*nav_settings
                * nav_logoff*/

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }

        return true;
    }

    private void logoutRequest(final String userId, final String accessToken, final String physicalAddress, final ProgressDialog progressDialog) {
        StringRequest stringLoginRequest = new StringRequest(Request.Method.POST, AppConstant.API.LOGOUT_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String loginResponse) {
                        progressDialog.dismiss();
                        Log.d("LogOut", "LogOut  Response : " + loginResponse);
                        try {

                            JSONObject jsonObject = new JSONObject(loginResponse);

                            String status = jsonObject.has("Status") ? jsonObject.getString("Status") : "";


                            if (status != null && status.equalsIgnoreCase("1")) {
                                if (jsonObject.getString("Result").equalsIgnoreCase("success")) {

                                    String succMessage = jsonObject.has("Message") ? jsonObject.getString("Message") : "Blank Response";

                                    onLogoutSuccess(succMessage);

                                }
                            } else {
                                String failMessage = jsonObject.has("Message") ? jsonObject.getString("Message") : "Blank Response";
                                onLogoutFailed(failMessage);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            onLogoutFailed("Not able parse response");
                        }
                        progressDialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                progressDialog.cancel();
                onLogoutFailed("Not able to connect with server");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("PhysicalAddress", physicalAddress);
                map.put("UserId", userId);
                map.put("AccessToken", accessToken);

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                return map;
            }
        };
        NimbuMirchiApplication.getInstance().addToRequestQueue(stringLoginRequest);
    }

    public void onLogoutSuccess(String message) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                SharedPreferences preferences = getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                db.resetDatabase();

                dialogInterface.dismiss();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }


    public void onLogoutFailed(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Failed");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                SharedPreferences preferences = getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                db.resetDatabase();

                dialogInterface.dismiss();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
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
                    || y < w.getTop() || y > w.getBottom())) {

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        }
        return ret;
    }

    private void exitMethod() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("EXIT").
                setMessage("Do You want to exit?").
                setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        finish();
                    }
                }).
                setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


}
