package com.inducesmile.androidmusicplayer.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inducesmile.androidmusicplayer.R;
import com.inducesmile.androidmusicplayer.adapter.SongAdapter;
import com.inducesmile.androidmusicplayer.entities.SongObject;

import java.util.ArrayList;
import java.util.List;

public class SongFragment extends Fragment {

    public SongFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song, container, false);

        getActivity().setTitle("Songs");
        RecyclerView songRecyclerView = (RecyclerView)view.findViewById(R.id.song_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        songRecyclerView.setLayoutManager(linearLayoutManager);
        songRecyclerView.setHasFixedSize(true);

        SongAdapter mAdapter = new SongAdapter(getActivity(), getTestData());
        songRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public List<SongObject> getTestData() {
        List<SongObject> recentSongs = new ArrayList<SongObject>();
        recentSongs.add(new SongObject("Adele", "Someone Like You", ""));
        recentSongs.add(new SongObject("Adele", "Someone Like You", ""));
        recentSongs.add(new SongObject("Adele", "Someone Like You", ""));
        recentSongs.add(new SongObject("Adele", "Someone Like You", ""));
        recentSongs.add(new SongObject("Adele", "Someone Like You", ""));
        recentSongs.add(new SongObject("Adele", "Someone Like You", ""));
        return recentSongs;
    }
}
