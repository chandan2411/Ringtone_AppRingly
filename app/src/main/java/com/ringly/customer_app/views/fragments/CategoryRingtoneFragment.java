package com.ringly.customer_app.views.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.CheckNetwork;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.ringly.customer_app.entities.FragmentLifecycle;
import com.ringly.customer_app.models.RingtoneCategoryModel;
import com.ringly.customer_app.views.adapters.CategoryRingAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CategoryRingtoneFragment extends Fragment implements FragmentLifecycle {

    private RecyclerView mRecyclerView;
    private CategoryRingAdapter mAdapter;

    private ProgressBar progressBar;

    private DatabaseReference mDatabaseRef;
    private List<RingtoneCategoryModel> mCatObject;
    private KProgressHUD progressHUD;

    public CategoryRingtoneFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured_ringtone, container, false);

        getActivity().setTitle("Ringtone");


        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = view.findViewById(R.id.progress_circular);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        inititeProgessHud();
        if (CheckNetwork.checkNet(getActivity())){
            getDataFromFirebaseDb();
        }else {
            Snackbar.make(getActivity().findViewById(R.id.rlParent), Constant.NO_INTERNET, Snackbar.LENGTH_SHORT).show();
        }
        return view;
    }

    private void inititeProgessHud() {
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

    }

    public void showProgressHud() {
        progressHUD.show();
    }

    public void hideProgressHud() {
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }

    private void getDataFromFirebaseDb() {
        showProgressHud();
        mDatabaseRef = DatabaseReferences.getRingtoneCategoryRefence();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCatObject = new ArrayList<>();
                Log.i("Ringtone", mCatObject.toString());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RingtoneCategoryModel model = postSnapshot.getValue(RingtoneCategoryModel.class);
                    mCatObject.add(model);
                }
                mAdapter = new CategoryRingAdapter(getContext(), mCatObject);
                mRecyclerView.setAdapter(mAdapter);
                hideProgressHud();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                hideProgressHud();
            }
        });
    }

    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {

    }
}