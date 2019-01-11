package com.iembsys.admin.nimbumirchi.customer.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.iembsys.admin.nimbumirchi.customer.data.DocumentData;
import com.iembsys.admin.nimbumirchi.customer.util.AppConstant;
import com.iembsys.admin.nimbumirchi.customer.util.MyUtility;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 21-01-2017.
 */

public class DocumentAdapter extends BaseAdapter {

    public static final String TITLE = "Document Adapter";

    /***********
     * Declare Used Variables
     *********/
    private Activity activity;
    private ArrayList<DocumentData> data;
    private static LayoutInflater inflater = null;
    DocumentData tempValues = null;
    int i = 0;

    /*************
     * CustomAdapter Constructor
     *****************/
    public DocumentAdapter(Activity context, ArrayList<DocumentData> data) {

        /********** Take passed values **********/
        this.activity = context;
        this.data = data;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /********
     * What is the size of Passed Arraylist Size
     ************/
    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public static class ViewHolder {

        public ImageView docImageView;
        public TextView txtDocDetail;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.document_card, null);

            holder = new ViewHolder();
            holder.docImageView = (ImageView) vi
                    .findViewById(R.id.docImageViev);
            holder.txtDocDetail = (TextView) vi
                    .findViewById(R.id.docDetail);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.txtDocDetail.setText("No Data");

        } else {
            tempValues = null;
            tempValues = (DocumentData) data.get(position);

            holder.docImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isImageExists(tempValues.getDocumentImagePath())) {
                        downloadImage(tempValues);
                    }else{
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(tempValues.getDocumentImagePath()), "image/*");
                        activity.startActivity(intent);
                    }
                }
            });

            holder.txtDocDetail.setText(tempValues.getDocumentDetail());
            if (isImageExists(tempValues.getDocumentImagePath())){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(tempValues.getDocumentImagePath(), options);
                holder.docImageView.setImageBitmap(bitmap);
            }
        }
        return vi;
    }

    private boolean isImageExists(String path) {
        boolean exists = false;

        if (path == null) {
            return false;
        }

        File imageFile = new File(path);
        if (imageFile.exists()) {
            exists = true;
        }

        return exists;
    }

    ProgressDialog dialog;

    private void downloadImage(final DocumentData documentData) {

        System.out.println("document order id" + documentData.getOrderId());
        StringRequest imageDownloadRequest = new StringRequest(Request.Method.POST, AppConstant.API.CANCEL_ORDER_API + documentData.getOrderId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String imageResponse) {
                        dialog.dismiss();
                        try {
                            System.out.println("Image Response : " + imageResponse);
                            JSONObject jsonObject = new JSONObject(imageResponse);

                            if (jsonObject.has("result")) {
                                if (jsonObject.getString("result").equals("success")) {
                                    String encodedImage = jsonObject.getString("base_64");
                                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    String imageName = MyUtility.getDocumentImageName(documentData);
                                    saveToInternalStorage(decodedByte,imageName);
                                    data.remove(documentData);
                                    DocumentAdapter.this.notifyDataSetChanged();

                                } else {
                                    Toast.makeText(activity, "Invalid Account Detail", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(activity, "Blank Response", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(activity, "Not able parse response", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                dialog.dismiss();
                Toast.makeText(activity, "Not able to connect with server", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("api_key", BuildConfig.API_KEY);
                map.put("orderId", documentData.getOrderId());
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + " : " + entry.getValue());
                }
                return map;
            }
        };

        dialog = ProgressDialog.show(activity, "",
                "downloading.....", true);
        NimbuMirchiApplication.getInstance().addToRequestQueue(imageDownloadRequest);

    }

    private String saveToInternalStorage(Bitmap bitmapImage,String imageName){
        ContextWrapper cw = new ContextWrapper(activity.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,imageName+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

}
