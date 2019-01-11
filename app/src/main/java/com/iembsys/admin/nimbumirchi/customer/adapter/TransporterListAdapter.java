package com.iembsys.admin.nimbumirchi.customer.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iembsys.admin.nimbumirchi.customer.BuildConfig;
import com.iembsys.admin.nimbumirchi.customer.NimbuMirchiApplication;
import com.iembsys.admin.nimbumirchi.customer.R;
import com.iembsys.admin.nimbumirchi.customer.data.CustomerPendingOrder;
import com.iembsys.admin.nimbumirchi.customer.data.TransporterData;
import com.iembsys.admin.nimbumirchi.customer.util.AppConstant;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 18-01-2017.
 */

public class TransporterListAdapter  extends BaseAdapter implements View.OnClickListener {

    public static final String TITLE = "Confirm Orders";

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList<TransporterData> data;
    private static LayoutInflater inflater=null;
    TransporterData tempValues=null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public TransporterListAdapter(Activity context, ArrayList<TransporterData> data) {

        /********** Take passed values **********/
        this.activity = context;
        this.data=data;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView txtTitle ;
        public TextView txtRightTitle ;
        public TextView txtRightContent1;
        public TextView txtContent1;
        public TextView txtContent3;
        public Button action1;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.transporter_list_card, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.txtTitle = (TextView) vi
                    .findViewById(R.id.titleTxt);
            holder.txtRightTitle = (TextView) vi
                    .findViewById(R.id.rightTitleTxt);
            holder.txtRightContent1 = (TextView) vi
                    .findViewById(R.id.rightContect1Txt);
            holder.txtContent1 = (TextView) vi
                    .findViewById(R.id.content1Txt);
            holder.txtContent3 = (TextView) vi
                    .findViewById(R.id.content3Txt);
            holder.txtContent3.setVisibility(View.VISIBLE);
            holder.action1 = (Button) vi
                    .findViewById(R.id.action1Button);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.txtTitle.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( TransporterData ) data.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.txtTitle.setText(tempValues.getFirmName());
            holder.txtContent3.setText("Contact Name : "+tempValues.getContactName());
            String contactNo = "Contact No."+tempValues.getPersonContactNo();
            holder.txtContent1.setText(contactNo);
//            holder.txtRightTitle.setText(tempValues.getCapacity()+"/"+tempValues.getAvailableVehicle()+"/"+tempValues.getRate());
            holder.txtRightTitle.setText(tempValues.getFromCityName());
            holder.txtRightContent1.setText(tempValues.getAddress().toString().trim());

            /******** Set Item Click Listner for LayoutInflater for each row *******/
            holder.action1.setVisibility(View.GONE);

        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

}
