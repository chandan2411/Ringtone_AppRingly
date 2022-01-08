package com.ringly.customer_app.views.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.CheckNetwork;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.ringly.customer_app.entities.MySharedPref;
import com.ringly.customer_app.models.RingtoneModel;
import com.ringly.customer_app.views.adapters.RingtoneAdapter;
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

public class CategorizesActivity extends AppCompatActivity {

    private  String ringCatId;
    private String categoryName;
    private RecyclerView mRecyclerView;
    private RingtoneAdapter mAdapter;


    private DatabaseReference ringtoneDatabaseReference;
    private DatabaseReference userFavDbRef;
    private List<RingtoneModel> ringtoneModelList = new ArrayList<>();
    private MySharedPref sharedPref;
    private KProgressHUD progressHUD;
    private RelativeLayout rlParent;
    private RelativeLayout rlMain;
    private ImageView ivBack;
    private TextView tvTitle;
    private View ringViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorizes);

        ringtoneDatabaseReference = DatabaseReferences.getRingtoneReference();
        sharedPref = new MySharedPref(this);
        /*assigning reference for widget*/
        getIntentData();
        assignReference();
        inititeProgessHud();
        fetchRingtoneWS();
        String userId = sharedPref.readString(Constant.USER_ID, "");
        if (!userId.isEmpty()){
            userFavDbRef = DatabaseReferences.getUserFavoriteRingtoneReference(userId);
            getUserFavRintoneList();

        }
    }

    private void getIntentData() {
        if (getIntent()!=null){
            categoryName = getIntent().getStringExtra("CAT_NAME");
            ringCatId = getIntent().getStringExtra("CAT_ID");
        }
    }

    private void fetchRingtoneWS() {
        if (CheckNetwork.checkNet(this)){
            getRingtoneFromServer();
        }else {
            Snackbar.make(this.findViewById(R.id.rlParent), Constant.NO_INTERNET, Snackbar.LENGTH_SHORT).show();
        }
    }



    private void getUserFavRintoneList() {
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
                Toast.makeText(CategorizesActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @SuppressLint("RestrictedApi")
    private void assignReference() {
        mRecyclerView = findViewById(R.id.recycler_view);
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(categoryName);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategorizesActivity.this.finish();
            }
        });


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RingtoneAdapter(this, false, ringCatId);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void setRingtoneViewHolder(View viewHolder){
      ringViewHolder  = viewHolder;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter!=null)
            mAdapter.stopAudioAndTimer(mAdapter.getCurrentView());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101 && resultCode== Activity.RESULT_OK){
            fetchRingtoneWS();
        }
    }

    private void inititeProgessHud() {
        progressHUD = KProgressHUD.create(this)
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
        if (ringCatId!=null && !ringCatId.isEmpty()) {
            showProgressHud();
            DatabaseReference mDatabaseRef = DatabaseReferences.getRingtoneReference();
            Query query = mDatabaseRef.orderByChild("ringtoneCategoryId").equalTo(ringCatId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ringtoneModelList.clear();
                    int counter =0;
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
                    Toast.makeText(CategorizesActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    hideProgressHud();
                }
            });
        } else {
            showProgressHud();
            ringtoneDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ringtoneModelList.clear();
                    int counter =0;
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
                    Toast.makeText(CategorizesActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    hideProgressHud();
                }
            });
        }
    }
}
