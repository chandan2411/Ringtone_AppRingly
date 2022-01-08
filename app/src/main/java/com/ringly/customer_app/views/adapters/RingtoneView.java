package com.ringly.customer_app.views.adapters;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.ringly.customer_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RingtoneView extends Fragment implements RingtoneAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private RingtoneAdapter mAdapter;

    private ProgressBar progressBar;

    private DatabaseReference mDatabaseRef;
    private List<RingtoneObject> mRingtoneObject;
    private List<RelatedRingtone> mRelatedRingtone;

    public RingtoneView() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ringtoneview, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.scrollToPosition(0);
        progressBar = view.findViewById(R.id.progress_circular);

        mRingtoneObject = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("ringtone");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRingtoneObject.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RingtoneObject ringtoneObject = postSnapshot.getValue(RingtoneObject.class);
                    mRingtoneObject.add(ringtoneObject);
                }
//                mAdapter = new RingtoneAdapter(getContext(), mRingtoneObject);
                mRecyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }


}