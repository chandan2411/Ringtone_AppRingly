package com.ringly.customer_app.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.CheckNetwork;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.ringly.customer_app.entities.FragmentLifecycle;
import com.ringly.customer_app.entities.MySharedPref;
import com.ringly.customer_app.models.RingtoneModel;
import com.ringly.customer_app.views.adapters.RingtoneAdapter;
import com.ringly.customer_app.views.adapters.RingtoneObject;
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

public class PopularRingtoneFragment extends Fragment implements FragmentLifecycle {

    private RecyclerView mRecyclerView;
    private RingtoneAdapter mAdapter;


    private DatabaseReference mDatabaseRef;
    private List<RingtoneObject> mRingtoneObject;
    private List<RingtoneModel> ringtoneModelList = new ArrayList<>();
    private RelativeLayout rlParent;
    private KProgressHUD progressHUD;
    private String idForPopularCategory = "-LvWERVqPfR6nbrA4POR";
    private DatabaseReference userFavDbRef;
    private Object userFavRingtoneList;


    public PopularRingtoneFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured_ringtone, container, false);
        getActivity().setTitle("Ringtone");

        mRecyclerView = view.findViewById(R.id.recycler_view);
        rlParent = view.findViewById(R.id.rlParent);
        /*mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));*/
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RingtoneAdapter(getContext(), true, "Popular");
        mRecyclerView.setAdapter(mAdapter);
        mRingtoneObject = new ArrayList<>();
        inititeProgessHud();
        String userId = new MySharedPref(getActivity()).readString(Constant.USER_ID, "");
        if (CheckNetwork.checkNet(getActivity())){
            if (!userId.isEmpty()) {
                userFavDbRef = DatabaseReferences.getUserFavoriteRingtoneReference(userId);
                getUserFavRingtoneList();
            }
            getDataForPopularCategory();
        }else {
            Snackbar.make(getActivity().findViewById(R.id.rlParent), Constant.NO_INTERNET, Snackbar.LENGTH_SHORT).show();
        }
        return view;


    }

    @Override
    public void onPauseFragment() {
        if (mAdapter!=null)
            mAdapter.stopAudioAndTimer(mAdapter.getCurrentView());
    }

    @Override
    public void onResumeFragment() {

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

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter!=null)
            mAdapter.stopAudioAndTimer(mAdapter.getCurrentView());
    }


    private void getDataForPopularCategory() {
        showProgressHud();
        mDatabaseRef = DatabaseReferences.getRingtoneReference();
        Query query = mDatabaseRef.orderByChild(Constant.FB_RING_DOWNLOAD);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ringtoneModelList.clear();
                int counter = 0;
                Log.i("Ringtone", mRingtoneObject.toString());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RingtoneModel ringtoneModel = postSnapshot.getValue(RingtoneModel.class);
                    ringtoneModel.setPosition(counter);
                    ringtoneModelList.add(ringtoneModel);
                    counter++;
                }
                Collections.reverse(ringtoneModelList);
                mAdapter.setList(ringtoneModelList);
                hideProgressHud();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                hideProgressHud();
            }
        });
    }

    public void getUserFavRingtoneList() {
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

    public void setUserFavRingtoneList(Object userFavRingtoneList) {
        this.userFavRingtoneList = userFavRingtoneList;
    }
}
