package com.ringly.customer_app.views.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

import com.ringly.customer_app.R;
import com.ringly.customer_app.models.SampleSearchModel;
import com.ringly.customer_app.views.activities.RingtoneDetailActivity;
import com.ringly.customer_app.views.activities.UploadActivity;
import com.ringly.customer_app.entities.AppUtils;
import com.ringly.customer_app.entities.CheckNetwork;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.ringly.customer_app.entities.FragmentLifecycle;
import com.ringly.customer_app.entities.MySharedPref;
import com.ringly.customer_app.models.RingtoneModel;
import com.ringly.customer_app.views.adapters.RingtoneAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class FeatureRingtoneFragment extends Fragment implements FragmentLifecycle {

    private String ringCatId;
    private RecyclerView mRecyclerView;
    private RingtoneAdapter mAdapter;


    private DatabaseReference ringtoneDatabaseReference;
    private DatabaseReference userFavDbRef;
    private List<RingtoneModel> ringtoneModelList = new ArrayList<>();
    private ArrayList<SampleSearchModel> ringtoneNameList = new ArrayList<>();
    private MySharedPref sharedPref;
    private KProgressHUD progressHUD;
    private RelativeLayout rlParent;
    private FloatingActionButton fabUpload;
    private FloatingActionButton fabSearch;
    private RelativeLayout rlMain;
    private LinearLayout llOptions;

    public FeatureRingtoneFragment(String trendCategoryId) {
        ringCatId = trendCategoryId;

    }

    public FeatureRingtoneFragment() {
    }

    public RingtoneAdapter getmAdapter() {
        return mAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured_ringtone, container, false);
        ringtoneDatabaseReference = DatabaseReferences.getRingtoneReference();
        sharedPref = new MySharedPref(getActivity());
        /*assigning reference for widget*/
        assignReference(view);
        inititeProgessHud();
        fetchRingtoneWS();
        String userId = sharedPref.readString(Constant.USER_ID, "");
        if (!userId.isEmpty()) {
            userFavDbRef = DatabaseReferences.getUserFavoriteRingtoneReference(userId);
            getUserFavRingtoneList();
        }
        return view;
    }

    private void fetchRingtoneWS() {
        if (CheckNetwork.checkNet(getActivity())) {
            getRingtoneFromServer();
        } else {
            Snackbar.make(getActivity().findViewById(R.id.rlParent), Constant.NO_INTERNET, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void getUserFavRingtoneList() {
        final HashMap<String, Boolean> favHash = new HashMap<>();
        userFavDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favHash.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String ringtoneId = postSnapshot.getValue(RingtoneModel.class).getRingtoneId();
                    favHash.put(ringtoneId, true);
                }
                if (!favHash.isEmpty())
                    mAdapter.setFavData(favHash);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @SuppressLint("RestrictedApi")
    private void assignReference(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        llOptions = view.findViewById(R.id.llOptions);
        fabUpload = view.findViewById(R.id.fabUpload);
        fabSearch = view.findViewById(R.id.fabSearch);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        /*layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);*/
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RingtoneAdapter(getContext(), false, "");
        mRecyclerView.setAdapter(mAdapter);

        fabUpload.setOnClickListener(view1 -> {
            if (userSignedIn()) {
                startActivityForResult(new Intent(getActivity(), UploadActivity.class), 101);
            } else {
                AppUtils.moveToSignInActivity(getActivity());
            }
        });

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SimpleSearchDialogCompat(getActivity(), "Search...",
                        "What are you looking for...?", null, ringtoneNameList,
                        new SearchResultListener<SampleSearchModel>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog, SampleSearchModel item, int position) {
                                Intent intent = new Intent(getActivity(), RingtoneDetailActivity.class);
                                /*intent.putParcelableArrayListExtra("Ringtone", allSongs);*/
                                intent.putExtra("Position", position);
                                intent.putExtra("CategoryID", "Search");
                                intent.putExtra("Scroll", false);
                                intent.putExtra("RingtoneTitle", item.getTitle());
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        }).show();
               /*new SimpleSearchDialogCompat(getActivity(), "Search...", "What are you looking for ", null, ringtoneModelList, new SearchResultListener<RingtoneModel>() {
                   @Override
                   public void onSelected(BaseSearchDialogCompat dialog, RingtoneModel item, int position) {

                   }
               });*/
            }
        });


    }

    private boolean userSignedIn() {
        return new MySharedPref(getActivity()).readBoolean(Constant.IS_USER_LOGIN, false);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null)
            mAdapter.stopAudioAndTimer(mAdapter.getCurrentView());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            fetchRingtoneWS();
        }
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


    private void getRingtoneFromServer() {
        if (ringCatId != null && !ringCatId.isEmpty()) {
            showProgressHud();
            DatabaseReference mDatabaseRef = DatabaseReferences.getRingtoneReference();
            Query query = mDatabaseRef.orderByChild("ringtoneCategoryId").equalTo(ringCatId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ringtoneModelList.clear();
                    ringtoneNameList.clear();
                    int counter =0;
                    SampleSearchModel searchModel;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        RingtoneModel ringtoneModel = postSnapshot.getValue(RingtoneModel.class);
                        ringtoneModel.setPosition(counter);
                        searchModel = new SampleSearchModel(ringtoneModel.getRingtoneName());
                        ringtoneModelList.add(ringtoneModel);
                        ringtoneNameList.add(searchModel);
                        counter++;
                    }
                    Collections.reverse(ringtoneModelList);
                    mAdapter.setList(ringtoneModelList);
                    llOptions.setVisibility(View.VISIBLE);
                    hideProgressHud();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    hideProgressHud();
                    llOptions.setVisibility(View.GONE);
                }
            });
        } else {
            showProgressHud();
            ringtoneDatabaseReference.orderByChild("ringtoneCreatedDate").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ringtoneModelList.clear();
                    ringtoneNameList.clear();
                    int counter =0;
                    SampleSearchModel searchModel;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        RingtoneModel ringtoneModel = postSnapshot.getValue(RingtoneModel.class);
                        ringtoneModel.setPosition(counter);
                        searchModel = new SampleSearchModel(ringtoneModel.getRingtoneName());
                        ringtoneModelList.add(ringtoneModel);
                        ringtoneNameList.add(searchModel);
                        counter++;
                    }
                    Collections.reverse(ringtoneModelList);
                    mAdapter.setList(ringtoneModelList);
                    llOptions.setVisibility(View.VISIBLE);
                    hideProgressHud();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    llOptions.setVisibility(View.GONE);
                    hideProgressHud();
                }
            });
        }
    }

    @Override
    public void onPauseFragment() {
        /*if (mAdapter!=null)
            mAdapter.stopAudioAndTimer();*/
        onPause();
    }

    @Override
    public void onResumeFragment() {

    }
}
