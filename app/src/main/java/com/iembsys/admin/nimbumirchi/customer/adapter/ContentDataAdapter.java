package com.iembsys.admin.nimbumirchi.customer.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iembsys.admin.nimbumirchi.customer.R;
import com.iembsys.admin.nimbumirchi.customer.data.ContentData;

import java.util.ArrayList;

/**
 * Created by Admin on 05-12-2016.
 */

public class ContentDataAdapter extends BaseAdapter {

    public static final String TITLE = "Confirm Orders";

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList<ContentData> data;
    private static LayoutInflater inflater=null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public ContentDataAdapter(Activity context, ArrayList<ContentData> data) {

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
        public TextView txtContent;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.content_data_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ContentDataAdapter.ViewHolder();
            holder.txtTitle = (TextView) vi
                    .findViewById(R.id.titleTxt);
            holder.txtContent = (TextView) vi
                    .findViewById(R.id.contentTxt);

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
            ContentData tempValues = ( ContentData ) data.get(position);

            holder.txtTitle.setText(tempValues.getTitle());

            String strs = tempValues.getValue();
            Log.v("dddd",tempValues.getTitle()+"___"+strs);

            if (strs!=null && strs.equalsIgnoreCase("1")){

                holder.txtContent.setText("Yes");

            }else if (strs!=null && strs.equalsIgnoreCase("0")){

                holder.txtContent.setText("No");

            }else {
                holder.txtContent.setText(tempValues.getValue());
            }



        }
        return vi;
    }

}