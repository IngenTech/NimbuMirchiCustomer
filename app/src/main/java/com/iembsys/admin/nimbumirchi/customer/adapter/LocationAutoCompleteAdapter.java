package com.iembsys.admin.nimbumirchi.customer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.iembsys.admin.nimbumirchi.customer.R;
import com.iembsys.admin.nimbumirchi.customer.data.LocationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 29-12-2016.
 */

public class LocationAutoCompleteAdapter extends ArrayAdapter<LocationModel> {
    private final Context mContext;
    private final List<LocationModel> mDepartments;
    private final List<LocationModel> mDepartments_All;
    private final List<LocationModel> mDepartments_Suggestion;
    private final int mLayoutResourceId;

    public LocationAutoCompleteAdapter(Context context, int resource, List<LocationModel> locationModels) {
        super(context, resource, locationModels);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mDepartments = new ArrayList<>(locationModels);
        this.mDepartments_All = new ArrayList<>(locationModels);
        this.mDepartments_Suggestion = new ArrayList<>();
    }

    public int getCount() {
        return mDepartments.size();
    }

    public LocationModel getItem(int position) {
        return mDepartments.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }
            LocationModel department = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.textView);
            name.setText(department.getLocationName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((LocationModel) resultValue).getLocationName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    mDepartments_Suggestion.clear();
                    for (LocationModel department : mDepartments_All) {
                        if (department.getLocationName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            mDepartments_Suggestion.add(department);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mDepartments_Suggestion;
                    filterResults.count = mDepartments_Suggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                mDepartments.clear();

                try {


                    if (results != null && results.count > 0) {
                        // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                        List<?> result = (List<?>) results.values;
                        for (Object object : result) {
                            if (object instanceof LocationModel) {
                                mDepartments.add((LocationModel) object);
                            }
                        }
                    } else if (constraint == null) {
                        // no filter, add entire original list back in
                        mDepartments.addAll(mDepartments_All);
                    }

                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }
}