package com.ringly.customer_app.entities;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ringly.customer_app.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import ir.mirrajabi.searchdialog.adapters.SearchDialogAdapter;
import ir.mirrajabi.searchdialog.core.BaseFilter;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.FilterResultListener;
import ir.mirrajabi.searchdialog.core.OnPerformFilterListener;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by RAJ ARYAN on 2020-01-14.
 */
public class  SimpleSearch<T extends Searchable> extends BaseSearchDialogCompat<T> {


    private String mTitle;
    private String mSearchHint;
    private SearchResultListener<T> mSearchResultListener;

    private TextView mTxtTitle;
    private EditText mSearchBox;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private boolean mCancelOnTouchOutside = true;

    // In case you are doing process in another thread
    // and wanted to update the progress in that thread
    private Handler mHandler;
    public SimpleSearch(
            Context context, String title, String searchHint,
            @Nullable Filter filter, ArrayList<T> items,
            SearchResultListener<T> searchResultListener) {
        super(context, items, filter, null, null);
        init(title, searchHint, searchResultListener);
    }

    private void init(String title, String searchHint, SearchResultListener<T> searchResultListener) {

        mTitle = title;
        mSearchHint = searchHint;
        mSearchResultListener = searchResultListener;
        setFilterResultListener(new FilterResultListener<T>() {
            @Override
            public void onFilter(ArrayList<T> items) {
                ((SearchDialogAdapter) getAdapter())
                        .setSearchTag(mSearchBox.getText().toString())
                        .setItems(items);
            }
        });
        mHandler = new Handler();
    }

    public SimpleSearch(Context context, ArrayList<T> items, Filter filter, RecyclerView.Adapter adapter, FilterResultListener filterResultListener, int theme) {
        super(context, items, filter, adapter, filterResultListener, theme);
    }

    @Override
    protected void getView(View view) {
        setContentView(view);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setCancelable(true);
        mTxtTitle = view.findViewById(R.id.txt_title);
        mSearchBox = view.findViewById(getSearchBoxId());
        mRecyclerView = view.findViewById(getRecyclerViewId());
        mProgressBar = view.findViewById(R.id.progress);
        mTxtTitle.setText(mTitle);
        mSearchBox.setHint(mSearchHint);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.GONE);
        view.findViewById(R.id.dummy_background)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mCancelOnTouchOutside) {
                            dismiss();
                        }
                    }
                });
        final SearchDialogAdapter adapter = new SearchDialogAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, getItems()
        );
        adapter.setSearchResultListener(mSearchResultListener);
        adapter.setSearchDialog(this);
        setFilterResultListener(getFilterResultListener());
        setAdapter(adapter);
        mSearchBox.requestFocus();
        ((BaseFilter<T>) getFilter()).setOnPerformFilterListener(new OnPerformFilterListener() {
            @Override
            public void doBeforeFiltering() {
//                setLoading(true);
            }

            @Override
            public void doAfterFiltering() {
//                setLoading(false);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected int getSearchBoxId() {
        return 0;
    }

    @Override
    protected int getRecyclerViewId() {
        return 0;
    }
}

