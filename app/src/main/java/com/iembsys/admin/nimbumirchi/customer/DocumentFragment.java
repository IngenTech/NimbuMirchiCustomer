package com.iembsys.admin.nimbumirchi.customer;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iembsys.admin.nimbumirchi.customer.adapter.CustomerTripHistoryAdapter;
import com.iembsys.admin.nimbumirchi.customer.adapter.DocumentAdapter;
import com.iembsys.admin.nimbumirchi.customer.data.CustomerTripHistoryData;
import com.iembsys.admin.nimbumirchi.customer.data.DBAdapter;
import com.iembsys.admin.nimbumirchi.customer.data.DocumentData;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DOCUMENT_LIST = "document_list";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<DocumentData> documentDataArrayList;
    private String mParam2;


    public DocumentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param documentDataArrayList Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocumentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentFragment newInstance(ArrayList<DocumentData> documentDataArrayList, String param2) {
        DocumentFragment fragment = new DocumentFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(DOCUMENT_LIST, documentDataArrayList);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onResume() {
        super.onResume();
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity())
                    .setActionBarTitle(getResources().getString(R.string.document_fragment_title));
        }
    }

    DBAdapter db;
    DocumentAdapter adapter;
    ProgressDialog dialog;
    ListView listview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            documentDataArrayList = getArguments().getParcelableArrayList(DOCUMENT_LIST);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_document, container, false);;
        listview = (ListView)view.findViewById(R.id.listview);
        adapter = new DocumentAdapter(getActivity(), documentDataArrayList);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;

    }

}
