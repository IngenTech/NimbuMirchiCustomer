package com.iembsys.admin.nimbumirchi.customer.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Admin on 21-01-2017.
 */

public class DocumentData implements Parcelable{

    private String orderId;
    private String documentId;
    private String documentType;
    private String documentDetail;
    private String documentImagePath;

    public DocumentData(){
        super();
    }

    protected DocumentData(Parcel in) {
        orderId = in.readString();
        documentId = in.readString();
        documentType = in.readString();
        documentDetail = in.readString();
        documentImagePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(documentId);
        dest.writeString(documentType);
        dest.writeString(documentDetail);
        dest.writeString(documentImagePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DocumentData> CREATOR = new Creator<DocumentData>() {
        @Override
        public DocumentData createFromParcel(Parcel in) {
            return new DocumentData(in);
        }

        @Override
        public DocumentData[] newArray(int size) {
            return new DocumentData[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentDetail() {
        return documentDetail;
    }

    public void setDocumentDetail(String documentDetail) {
        this.documentDetail = documentDetail;
    }

    public String getDocumentImagePath() {
        return documentImagePath;
    }

    public void setDocumentImagePath(String documentImagePath) {
        this.documentImagePath = documentImagePath;
    }
}
