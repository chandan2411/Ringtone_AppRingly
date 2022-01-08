package com.ringly.customer_app.entities;

import com.ringly.customer_app.models.ContactModel;

import java.util.List;

public interface ContactFetchListener {
    void onContactFetch(List<ContactModel> contactModelList,boolean hasContactFetched);
}
