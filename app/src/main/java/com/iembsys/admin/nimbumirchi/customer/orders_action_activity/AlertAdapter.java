package com.iembsys.admin.nimbumirchi.customer.orders_action_activity;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iembsys.admin.nimbumirchi.customer.R;
import com.iembsys.admin.nimbumirchi.customer.bean.AlertBean;

import java.util.ArrayList;


/**
 * Created by Admin on 26-04-2017.
 */
public class AlertAdapter   extends RecyclerView.Adapter<AlertAdapter.ViewHolder>{



    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList<AlertBean> data;

    AlertBean tempValues=null;


    /*************  CustomAdapter Constructor *****************/
    public AlertAdapter(Activity context, ArrayList<AlertBean> data) {

        /********** Take passed values **********/
        this.activity = context;
        this.data=data;


    }

    public Object getItem(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_row, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        tempValues=null;
        tempValues = ( AlertBean ) data.get(position);



        holder.alertMessage.setText(tempValues.getMessage());

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


        public TextView alertMessage;

        public ViewHolder(View vi) {
            super(vi);

            this.alertMessage = (TextView) vi.findViewById(R.id.alert_message);


        }


    }

}