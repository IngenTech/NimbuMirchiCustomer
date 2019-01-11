package com.iembsys.admin.nimbumirchi.customer.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

/**
 * Created by Admin on 29-12-2016.
 */

public class DBAdapter {

    public static final String DB_NAME = "transporter";
    private static final int DATABASE_VER = 1;
    private static final String TAG = "DBAdapter";

    private final Context context;

    private DatabaseHelper DBHelper;
    public SQLiteDatabase db = null;

    public static final String TABLE_STATE = "state";
    public static final String TABLE_CITY = "city";
    public static final String TABLE_MATERIAL = "material";
    public static final String TABLE_PROFILE = "getProfile";

    public static final String ID = "_id";
    public static final String STATE_ID = "state_id";
    public static final String STATE_NAME = "state_name";
    public static final String ROAD_PERMIT = "road_permit";
    public static final String MIN_ROAD_PERMIT_AMOUNT = "min_road_permit_amount";

    public static final String CITY_ID = "city_id";
    public static final String CITY_NAME = "city_name";

    public static final String MATERIAL_ID = "material_id";
    public static final String MATERIAL_NAME = "material_name";

    public static final String FIRM_ID = "id";
    public static final String FIRM_NAME = "firm_name";
    public static final String CONTACT_NAME = "contact_name";
    public static final String PERSON_CONTACT_NO = "person_contact_no";
    public static final String HOME_CONTACT_NO = "home_contact_no";
    public static final String OFFICE_CONTACT_NO = "office_contact_no";
    public static final String TIN_NO = "tin_no";
    public static final String PAN_NO = "pan_no";
    public static final String ADDRESS = "address";
    public static final String EMAIL = "email";
    public static final String DO_NO_CALL = "do_no_call";
    public static final String BANK_NAME = "bank_name";
    public static final String BANK_ACCOUNT_NO = "bank_account_no";
    public static final String BANK_BRANCH_NAME = "bank_branch_name";
    public static final String BANK_BRANCH_CODE = "bank_branch_code";
    public static final String REMARK = "remarks";


    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static final String CREATE_STATE = "CREATE TABLE " + TABLE_STATE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + STATE_ID + " TEXT,"
            + STATE_NAME + " TEXT,"
            + ROAD_PERMIT + " TEXT,"
            + MIN_ROAD_PERMIT_AMOUNT + " TEXT)";

    private static final String CREATE_CITY = "CREATE TABLE " + TABLE_CITY + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + CITY_ID + " TEXT,"
            + CITY_NAME + " TEXT,"
            + STATE_ID + " TEXT)";
    private static final String CREATE_MATERIAL = "CREATE TABLE " + TABLE_MATERIAL + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + MATERIAL_ID + " TEXT,"
            + MATERIAL_NAME + " TEXT)";

    private static final String CREATE_PROFILE = "CREATE TABLE " + TABLE_PROFILE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + FIRM_ID + " TEXT,"
            + FIRM_NAME + " TEXT,"
            + CONTACT_NAME + " TEXT,"
            + PERSON_CONTACT_NO + " TEXT,"
            + HOME_CONTACT_NO + " TEXT,"
            + OFFICE_CONTACT_NO + " TEXT,"
            + TIN_NO + " TEXT,"
            + PAN_NO + " TEXT,"
            + ADDRESS + " TEXT,"
            + EMAIL + " TEXT,"
            + DO_NO_CALL + " TEXT,"
            + BANK_NAME + " TEXT,"
            + BANK_ACCOUNT_NO + " TEXT,"
            + BANK_BRANCH_NAME + " TEXT,"
            + BANK_BRANCH_CODE + " TEXT,"
            + REMARK + " TEXT,"
            + STATE_ID + " TEXT,"
            + CITY_ID + " TEXT)";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DATABASE_VER);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_STATE);
            db.execSQL(CREATE_CITY);
            db.execSQL(CREATE_MATERIAL);
            db.execSQL(CREATE_PROFILE);

        }


        @Override

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            onCreate(db);

        }

    }

    public SQLiteDatabase getSQLiteDatabase() {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        return db;
    }


    public void deletefromtable(String tablename) {
        db.delete(tablename, null, null);
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;

    }


    public void resetDatabase() {
        SQLiteDatabase database = DBHelper.getWritableDatabase();
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MATERIAL);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
        database.execSQL(CREATE_STATE);
        database.execSQL(CREATE_CITY);
        database.execSQL(CREATE_MATERIAL);
        database.execSQL(CREATE_PROFILE);
        database.close();
    }


    public void close() {
        DBHelper.close();
    }

    public Cursor getStateList() throws SQLException {
        return db.query(TABLE_STATE,null, null,null, null, null, null, null);
    }

    public Cursor getCityList() throws SQLException {
        return db.query(TABLE_CITY,null, null,null, null, null, null, null);
    }

    public Cursor stateById(String stateId) throws SQLException {
        return db.query(TABLE_STATE, new String[]{ID, STATE_ID, STATE_NAME, ROAD_PERMIT, MIN_ROAD_PERMIT_AMOUNT}, STATE_ID+" = '"+stateId+"'",
                null, null, null, null, null);
    }

    public Cursor cityById(String cityId) throws SQLException {
        return db.query(TABLE_CITY, new String[]{ID, CITY_ID, CITY_NAME}, CITY_ID+" = '"+cityId+"'",
                null, null, null, null, null);
    }

    public Cursor cityListByStateId(String stateId) throws SQLException {
        return db.query(TABLE_CITY, null, STATE_ID+" = '"+stateId+"'",
                null, null, null, null, null);
    }

    public Cursor materialList() throws SQLException {
        return db.query(TABLE_MATERIAL, new String[]{ID, MATERIAL_ID, MATERIAL_NAME}, null,
                null, null, null, null, null);
    }

    public Cursor materialById(String materialTypeId) throws SQLException {
        return db.query(TABLE_MATERIAL, new String[]{ID, MATERIAL_ID, MATERIAL_NAME}, MATERIAL_ID+" ='"+materialTypeId+"'",
                null, null, null, null, null);
    }

    public Cursor getProfile() throws SQLException {
        return db.query(TABLE_PROFILE, null, null,
                null, null, null, null, null);
    }

    public Cursor getProfileByAccountId(String firmId) throws SQLException {
        return db.query(TABLE_PROFILE, null, FIRM_ID+" ='"+firmId+"'",
                null, null, null, null, null);
    }

}
