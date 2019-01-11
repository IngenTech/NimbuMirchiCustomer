package com.iembsys.admin.nimbumirchi.customer.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.iembsys.admin.nimbumirchi.customer.R;
import com.iembsys.admin.nimbumirchi.customer.bean.DistanceData;
import com.iembsys.admin.nimbumirchi.customer.data.TransporterData;

import java.util.ArrayList;

/**
 * Created by Admin on 19-04-2017.
 */
public class DistanceReportAdapter  extends RecyclerView.Adapter<DistanceReportAdapter.ViewHolder>{

    public static final String TITLE = "Confirm Orders";

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList<DistanceData> data;

    DistanceData tempValues=null;


    /*************  CustomAdapter Constructor *****************/
    public DistanceReportAdapter(Activity context, ArrayList<DistanceData> data) {

        /********** Take passed values **********/
        this.activity = context;
        this.data=data;


    }

    public Object getItem(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distance_row, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        tempValues=null;
        tempValues = ( DistanceData ) data.get(position);


        holder.dateFrom.setText(tempValues.getDateFrom());
        holder.dateTo.setText(tempValues.getDateTo());
        holder.distance.setText(tempValues.getDistance());
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView dateFrom;
        public TextView dateTo;
        public TextView distance;

        public ViewHolder(View vi) {
            super(vi);

            this.dateFrom = (TextView) vi.findViewById(R.id.distance_date_from);
            this.dateTo = (TextView) vi.findViewById(R.id.distance_date_to);
            this.distance = (TextView) vi.findViewById(R.id.distance_distance);

        }


    }

}