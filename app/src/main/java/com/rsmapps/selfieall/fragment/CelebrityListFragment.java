package com.rsmapps.selfieall.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.adapter.CelebritiesAdapter;
import com.rsmapps.selfieall.model.Celebrity;

import java.util.ArrayList;


public class CelebrityListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<Celebrity> mCelebriies = new ArrayList<>();
    private String mParam2;


    private CelebritiesAdapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;




    public CelebrityListFragment() {
    }


    public static CelebrityListFragment newInstance(ArrayList<Celebrity> celebrities, String param2) {
        CelebrityListFragment fragment = new CelebrityListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, celebrities);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCelebriies = (ArrayList<Celebrity>) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_celebrity_list, container, false);
        getActivity().setTitle(mParam2);
        setupRecyclerView(view);
        return view;
    }

    private void setupRecyclerView(View view) {
        mAdapter = new CelebritiesAdapter(getActivity(),mCelebriies);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }


}