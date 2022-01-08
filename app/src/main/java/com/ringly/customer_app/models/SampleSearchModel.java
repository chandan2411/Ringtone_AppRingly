package com.ringly.customer_app.models;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by RAJ ARYAN on 2020-01-14.
 */
public class SampleSearchModel implements Searchable {
    private String mTitle;

    public SampleSearchModel(String title) {
        mTitle = title;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    public SampleSearchModel setTitle(String title) {
        mTitle = title;
        return this;
    }
}
