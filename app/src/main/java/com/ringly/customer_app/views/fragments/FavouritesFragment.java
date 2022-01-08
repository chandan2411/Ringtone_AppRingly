package com.ringly.customer_app.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.ringly.customer_app.entities.MySharedPref;
import com.ringly.customer_app.models.RingtoneModel;
import com.ringly.customer_app.models.WallPaperModel;
import com.ringly.customer_app.views.adapters.RecentPhotoAdapter;
import com.ringly.customer_app.views.adapters.RingtoneAdapter;
import com.ringly.customer_app.views.adapters.RingtoneDetailAdapter;
import com.ringly.customer_app.views.adapters.RingtoneObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private static final String TAG = FavouritesFragment.class.getSimpleName();
    List<RingtoneObject> favWalls;
    RecyclerView rvRintone;
    RecyclerView rvWallPaper;
    RingtoneAdapter mAdapter;

    private DatabaseReference userFavRingtoneDbRef;
    private DatabaseReference userFavWallpaperDbRef;
    private String userId;
    private RelativeLayout rlError;
    private boolean isWallPaperEmpty = true;
    private boolean isRingtoneEmpty = true;
    private KProgressHUD progressHUD;
    private RelativeLayout rlRingtone;
    private RelativeLayout rlWallpaper;
    private ScrollView scrollView;
    private RingtoneDetailAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favWalls = new ArrayList<>();
        rvWallPaper = view.findViewById(R.id.rvWallPaper);
        rvRintone = view.findViewById(R.id.rvRintone);
        rlError = view.findViewById(R.id.rlError);
        rlRingtone = view.findViewById(R.id.rlRingtone);
        rlWallpaper = view.findViewById(R.id.rlWallpaper);
        scrollView = view.findViewById(R.id.scrollView);

        userId = new MySharedPref(getActivity()).readString(Constant.USER_ID, "");
        inititeProgessHud();

        /*recycler view for ringtone*/
        rvRintone.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        /*layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);*/
        rvRintone.setLayoutManager(layoutManager);
        mAdapter = new RingtoneAdapter(getContext(), false, "");
        rvRintone.setAdapter(mAdapter);
//        SnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(rvRintone);
//        rvRintone.scrollToPosition(0);

        /*recycler view for wallpaper*/
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvWallPaper.hasFixedSize();
        rvWallPaper.setLayoutManager(gridLayoutManager);
        ViewCompat.setNestedScrollingEnabled(rvWallPaper, false);


        userFavRingtoneDbRef = DatabaseReferences.getUserFavoriteRingtoneReference(userId);
        userFavWallpaperDbRef = DatabaseReferences.getUserFavoriteWallpaperReference(userId);
        getUserFavRingtoneList();

    }

    private void getUserFavRingtoneList() {
        final HashMap<String, Boolean> favHash = new HashMap<>();
        showProgressHud();
        final List<RingtoneModel> datalist = new ArrayList<>();
        userFavRingtoneDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                datalist.clear();
                favHash.clear();
                int counter =0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RingtoneModel ringtoneModel = postSnapshot.getValue(RingtoneModel.class);
                    ringtoneModel.setPosition(counter);
                    datalist.add(ringtoneModel);
                    String ringtoneId = postSnapshot.getValue(RingtoneModel.class).getRingtoneId();
                    favHash.put(ringtoneId, true);
                    counter++;
                }
                if (!favHash.isEmpty())
                    mAdapter.setFavData(favHash);

                if (!datalist.isEmpty()) {
                    Collections.reverse(datalist);
                    /*adapter = new RingtoneDetailAdapter(getActivity(), datalist, favHash);
                    rvRintone.setAdapter(adapter);*/
                    mAdapter = new RingtoneAdapter(getContext(), true, "FAV");
                    rvRintone.setAdapter(mAdapter);
                    mAdapter.setList(datalist);
                    isRingtoneEmpty = false;
                    rlRingtone.setVisibility(View.VISIBLE);
                } else {
                    isRingtoneEmpty = true;
                    rlRingtone.setVisibility(View.GONE);
                }
                getUserFavWallPaperList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                isRingtoneEmpty = true;
                rlRingtone.setVisibility(View.GONE);
                getUserFavWallPaperList();
            }
        });
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

    private void getUserFavWallPaperList() {
        /*final List<PhotoModel> datalist = new ArrayList<>();*/
        final List<WallPaperModel> datalist = new ArrayList<>();
        userFavWallpaperDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideProgressHud();
                datalist.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    WallPaperModel wallPaperModel = postSnapshot.getValue(WallPaperModel.class);
                    datalist.add(wallPaperModel);
                }
                if (!datalist.isEmpty()) {
                    RecentPhotoAdapter adapter = new RecentPhotoAdapter(getActivity(), datalist, false);
                    adapter.notifyDataSetChanged();
                    rvWallPaper.setAdapter(adapter);
                    rvWallPaper.setVisibility(View.VISIBLE);
                    isWallPaperEmpty = false;
                } else {
                    isWallPaperEmpty = true;
                    rlWallpaper.setVisibility(View.GONE);
                }
                checkErrorView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressHud();
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                isWallPaperEmpty = true;
                rlWallpaper.setVisibility(View.GONE);
                checkErrorView();
            }
        });
    }

    private void checkErrorView() {
        if (isRingtoneEmpty && isWallPaperEmpty) {
            rlError.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            rlError.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }
}
