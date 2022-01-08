package com.ringly.customer_app.models;

import android.graphics.Bitmap;

public class ContactModel {
    private String contactName;
    private String contactNumber;
    private Bitmap imageBitmap;
    private String id;

    public String getId() {
        return id;
    }

    public ContactModel(String contactName, String contactNumber, Bitmap imageBitmap, String id) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.imageBitmap = imageBitmap;
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }
}
