package com.ringly.customer_app.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ringly.customer_app.R;
import com.ringly.customer_app.views.activities.activity_about_us;
import com.ringly.customer_app.views.activities.activity_privacypolicy;

public class InfoFragment extends Fragment {

    public InfoFragment() {
    }
    TextView about_Us, privacy_Policy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_layout, container, false);

        getActivity().setTitle("Information");
        about_Us = view.findViewById(R.id.about);
        privacy_Policy = view.findViewById(R.id.term);

        about_Us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), activity_about_us.class));
            }
        });
        privacy_Policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), activity_privacypolicy.class));
            }
        });

        return view;
    }


}
