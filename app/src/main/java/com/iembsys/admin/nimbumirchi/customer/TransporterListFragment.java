package com.iembsys.admin.nimbumirchi.customer;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iembsys.admin.nimbumirchi.customer.adapter.TransporterListAdapter;
import com.iembsys.admin.nimbumirchi.customer.data.DBAdapter;
import com.iembsys.admin.nimbumirchi.customer.data.SearchTransporterData;
import com.iembsys.admin.nimbumirchi.customer.data.TransporterData;
import com.iembsys.admin.nimbumirchi.customer.util.AppConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransporterListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransporterListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TRANSPORTER_LIST = "transporter_list";

    private static final String SEARCH_TRANSPORTER_DATA = "searchTransporterData";
    // TODO: Rename and change types of parameters
    private ArrayList<TransporterData> transporterDatas;
    private SearchTransporterData searchTransporterData;

    public TransporterListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param customerConfirmOrders Parameter 1.
     * @param searchTransporterData Parameter 2.
     * @return A new instance of fragment TransporterListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransporterListFragment newInstance(ArrayList<TransporterData> customerConfirmOrders,
                                                      SearchTransporterData searchTransporterData) {
        TransporterListFragment fragment = new TransporterListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(TRANSPORTER_LIST, customerConfirmOrders);
        args.putParcelable(SEARCH_TRANSPORTER_DATA, searchTransporterData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transporterDatas = getArguments().getParcelableArrayList(TRANSPORTER_LIST);
            searchTransporterData = getArguments().getParcelable(SEARCH_TRANSPORTER_DATA);
        }
    }

    DBAdapter db;
    TransporterListAdapter adapter;
    ProgressDialog dialog;
    SharedPreferences prefs;
    ListView listview;
    Button placeOrder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transporter_list, container, false);;

        listview = (ListView)view.findViewById(R.id.listview);
        placeOrder = (Button) view.findViewById(R.id.place_order);

        adapter = new TransporterListAdapter(getActivity(), transporterDatas);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeOrder();
            }
        });
        return view;
    }

    private void placeOrder() {
        StringRequest stringLoginRequest = new StringRequest(Request.Method.POST, AppConstant.API.PLACE_ORDER_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String tripResponse) {
                        dialog.dismiss();
                        try {

                            System.out.println("Place Order Response : " + tripResponse);
                            JSONObject jsonObject = new JSONObject(tripResponse);
/*result": "success",
"docket_no": "518294761N74",
"account_id": "103",
"message": "Order Successfully Placed!"*/
                            if (jsonObject.has("Status") && (jsonObject.getString("Status").equalsIgnoreCase("1"))) {
                                if (jsonObject.getString("Result").equals("Success")) {
                                    String message = "Order Successfully Placed!";
                                    if (jsonObject.has("Message")) {
                                        message = jsonObject.getString("Message");
                                    }
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.frame_container, CustomerPendingOrderFragment.newInstance("",""));
//                                    transaction.addToBackStack(null);
                                    transaction.commit();

                                } else {
                                    String message = "Failed to place order";
                                    if (jsonObject.has("Message")) {
                                        message = jsonObject.getString("Message");
                                    }
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                }
                            } else if (jsonObject.has("Status") && (jsonObject.getString("Status").equalsIgnoreCase("2"))) {
                                SharedPreferences preferences = getActivity().getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, getActivity().MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.commit();
                                db.resetDatabase();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            }else {
                                Toast.makeText(getActivity(), "Blank Response", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Not able parse response", Toast.LENGTH_LONG).show();
                        }
                        dialog.cancel();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                dialog.cancel();
                Toast.makeText(getActivity(), "Not able to connect with server", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (prefs == null) {
                    prefs = getActivity().getSharedPreferences(AppConstant.Preference.APP_PREFERENCE, getActivity().MODE_PRIVATE);
                }
                String accountId = prefs.getString(AppConstant.Preference.USER_ID,"");
                String accessToken = prefs.getString(AppConstant.Preference.ACCESS_TOKEN,"");

                Map<String, String> map = new HashMap<>();
                map.put("AccessToken", accessToken);
                map.put("UserId", accountId);
                map.put("Capacity", searchTransporterData.getCapacity());
                map.put("FromCity", searchTransporterData.getFromCityId());
                map.put("ToCity", searchTransporterData.getToCityId());
                map.put("CotegoryId", searchTransporterData.getMaterialId());
                String doorClosed = "";
                if (searchTransporterData.isDoorClosed()) {
                    doorClosed = "1";
                } else {
                    doorClosed = "0";
                }
                map.put("DoorStatus", doorClosed);
                String refrigerated = "";
                if (searchTransporterData.isRefrigerated()) {
                    refrigerated = "1";
                } else {
                    refrigerated = "0";
                }
                map.put("RefrigeratedStatus", refrigerated);
                map.put("NoOfVehicles", searchTransporterData.getNoOfVehicles());
                map.put("TripDate", searchTransporterData.getDate());


                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }

                return map;
            }
        };
        dialog = ProgressDialog.show(getActivity(), "",
                "Placing Order...", true);
        NimbuMirchiApplication.getInstance().addToRequestQueue(stringLoginRequest);
    }


}
