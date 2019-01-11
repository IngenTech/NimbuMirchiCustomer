package com.iembsys.admin.nimbumirchi.customer.util;

import java.util.Locale;

/**
 * Created by Admin on 26-12-2016.
 */

public class AppConstant {

    public static class Constants {
        public static final String APP_DIR = "NimbuMircheeCustomer";


        public static final String IMAGE_EXTENSIONS = ".jpg";

        public static final String INDIVIDUAL_CUSTOMER = "0";
        public static final String CORPORATE_CUSTOMER = "1";

        public static final String PENDING_ORDER_TYPE = "2";
        public static final String CONFIRM_ORDER_TYPE = "1";

        public static final String INVOICE_UPLOAD = "1";
        public static final String DECLARATION_UPLOAD = "2";
        public static final String ROAD_PERMIT_UPLOAD = "3";

    }

    public static class Preference {
        public static final String APP_PREFERENCE = "NimbuMirchiCustomer";

        public static final String PREF_GCM_ID = "prefGcmId";
        public static final String IS_GCM_SHARED = "isGcmShared";

        public static final String IS_LOGIN = "isLogin";
        public static final String USER_ID = "userId";
        public static final String PASSWORD = "password";

        public static final String ACCOUNT_ID = "account_id";
        public static final String ACCOUNT_NAME = "account_name";
        public static final String ACCOUNT_TYPE = "account_type";
        public static final String CONTACT_NO = "contact_no";

        public static final String PHYSICAL_ADDRESS = "physical_address";

        public static final String IMEI = "imei";
        public static final String ACCESS_TOKEN = "access_token";


    }

    public static class API {



        public static final String ADDRESS_OF_LAT_LNG_API = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="+ Locale.getDefault().getCountry();

        public static final String API_BASE = "http://184.72.75.33/new_nimbumirchee_api/v1/";

        public static final String TRACK_API = API_BASE+"track";

        public static final String SHARE_GCM_ID_API = "http://www.itracksolution.com/rest_gcm/v1/update_gcmdata/";

        public static final String LOGIN = API_BASE + "user_login";

        public static final String CHANGE_PASSWORD = API_BASE + "change_password";
        public static final String LOGOUT_API = API_BASE + "log_out";

        public static final String REGISTRATION_OTP_API = API_BASE + "register_otp";
        public static final String REGISTRATION_API = API_BASE + "register";
        public static final String OTP_VERIFY_API = API_BASE + "verify_otp";

        public static final String SEARCH_TRANSPORTER_API = API_BASE + "search_transporter";
        public static final String PLACE_ORDER_API = API_BASE + "place_order";
        public static final String CANCEL_ORDER_API = API_BASE + "cancel_order";
        public static final String CONFIRM_ORDER_LIST_API = API_BASE + "customer_orders";
        public static final String PENDING_ORDER_LIST_API = API_BASE + "customer_orders";
        public static final String RUNNING_TRIPS_LIST_API = API_BASE + "running_trip";
        public static final String TRIP_HISTORY_LIST_API = API_BASE + "trip_history";
        public static final String EDIT_PROFILE_API = API_BASE + "update_customer_profile";
        public static final String DOWNLOAD_DOCUMENT = API_BASE + "download_document";
        public static final String UPLOAD_INVOICE = API_BASE + "upload_invoice";
        public static final String UPLOAD_ROAD_PERMIT = API_BASE + "upload_road_permit";

        public static final String VIEW_INVICE_LIST = API_BASE+ "invoice_list";
        public static final String VIEW_ROADPERMIT_LIST = API_BASE+ "permit_list";

        public static final String STATE_API = API_BASE + "state";
        public static final String CITY_API = API_BASE + "city";
        public static final String MATERIAL_API = API_BASE + "material";

        //vishal's api
        public static final String TRAVEL_REPORT_API = API_BASE + "travel_report";

        public static final String HALT_REPORT_API = API_BASE + "halt_report";

        public static final String LIVE_API = API_BASE+"live";
        public static final String DOWNLOAD_INVOICE_IMAGE = API_BASE+"download_invoice";
        public static final String DOWNLOAD_ROADTRIP_IMAGE = API_BASE+"download_road_permit";

        public static final String DISTANCE_REPORT = API_BASE+"distance_report";

    }

    public static class Directory {
        public static final String APP_DIRECTORY = "NimbuMirchiCustomer";
        public static final String ERROR_FILE = "ErrorLog";

    }

}
