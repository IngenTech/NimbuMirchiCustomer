package com.iembsys.admin.nimbumirchi.customer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iembsys.admin.nimbumirchi.customer.adapter.ContentDataAdapter;
import com.iembsys.admin.nimbumirchi.customer.data.ContentData;
import com.iembsys.admin.nimbumirchi.customer.data.DocumentData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContentDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentDataFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CONTENT_DATA_ARRAY = "content_data_array";
    private static final String DOCUMENT_DATA_ARRAY = "document_data_array";

    private ArrayList<ContentData> contentDataArrayList;
    private ArrayList<DocumentData> documentDataArrayList;


    public ContentDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param contentDatas Parameter 1.
     * @param documentDataArrayList Parameter 2.
     * @return A new instance of fragment ContentDataFragment.
     */

    public static ContentDataFragment newInstance(ArrayList<ContentData> contentDatas, ArrayList<DocumentData> documentDataArrayList) {
        ContentDataFragment fragment = new ContentDataFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(CONTENT_DATA_ARRAY, contentDatas);
        args.putParcelableArrayList(DOCUMENT_DATA_ARRAY, documentDataArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    ContentDataAdapter adapter;
    ListView listview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contentDataArrayList = getArguments().getParcelableArrayList(CONTENT_DATA_ARRAY);
            documentDataArrayList = getArguments().getParcelableArrayList(DOCUMENT_DATA_ARRAY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content_data, container, false);;
        listview = (ListView)view.findViewById(R.id.listview);
        adapter = new ContentDataAdapter(getActivity(), contentDataArrayList);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return view;
    }



}
