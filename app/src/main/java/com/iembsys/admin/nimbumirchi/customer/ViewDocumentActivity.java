package com.iembsys.admin.nimbumirchi.customer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iembsys.admin.nimbumirchi.customer.bean.InvoiceBean;
import com.iembsys.admin.nimbumirchi.customer.bean.RoadPermitBean;
import com.iembsys.admin.nimbumirchi.customer.data.CustomerPendingOrder;
import com.iembsys.admin.nimbumirchi.customer.data.DBAdapter;
import com.iembsys.admin.nimbumirchi.customer.fragments.InvoiceFragment;
import com.iembsys.admin.nimbumirchi.customer.fragments.RoadPermitFragment;
import com.iembsys.admin.nimbumirchi.customer.util.AppConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 20-03-2017.
 */
public class ViewDocumentActivity extends AppCompatActivity {

    public static final String ORDER_NO = "order_no";
    public static final String UPLOAD_TYPE = "upload_type";

    String orderNo;
    DBAdapter db;

    TextView viewText;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_view_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_btn);

        try {
            orderNo = getIntent().getStringExtra(UploadDocumentActivity.ORDER_NO);

        }catch (Exception e) {
            e.printStackTrace();
        }
      /*  viewText = (TextView) findViewById(R.id.viewText);


        db = new DBAdapter(this);
        db.open();
*/
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        setupViewPager(viewPager);
        orderNo = getIntent().getStringExtra(UploadDocumentActivity.ORDER_NO);


        LinearLayout linearLayout = (LinearLayout)tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.WHITE);
        drawable.setSize(2, 1);
        linearLayout.setDividerPadding(1);
        linearLayout.setDividerDrawable(drawable);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InvoiceFragment(), "Invoice");
        adapter.addFragment(new RoadPermitFragment(), "Road Permit");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}