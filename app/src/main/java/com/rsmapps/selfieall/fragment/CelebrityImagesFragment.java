package com.rsmapps.selfieall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.activity.MainActivity;
import com.rsmapps.selfieall.adapter.EffectAdapter;
import com.rsmapps.selfieall.adapter.KohaliAdapter;
import com.rsmapps.selfieall.model.Celebrity;

import static android.app.Activity.RESULT_OK;


public class CelebrityImagesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Celebrity mParam1;
    private String mParam2;


    private KohaliAdapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;




    public CelebrityImagesFragment() {
    }


    public static CelebrityImagesFragment newInstance(Celebrity param1, String param2) {
        CelebrityImagesFragment fragment = new CelebrityImagesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Celebrity) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_celebrity_images, container, false);
        setupRecyclerView(view);
        return view;
    }

    private void setupRecyclerView(View view) {

        getActivity().setTitle(mParam1.getName());
        mAdapter = new KohaliAdapter(getActivity(),mParam1);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        //mLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new EffectAdapter.RecyclerTouchListener(getActivity(), recyclerView, new EffectAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(getActivity().getIntent().hasExtra("request")) {
                    Intent full = new Intent();
                    full.putExtra("id", position);
                    full.putExtra("url", mParam1.getImages().get(position));
                    getActivity().setResult(RESULT_OK, full);
                    getActivity().finish();
                }else {
                    /*
                        Intent intent = new Intent(getContext(),PhotoSelectActivity.class);
                        intent.putExtra("url",mParam1.getImages().get(position));
                        startActivity(intent);
                        //getActivity().finish();
                    */
                   // ((KohaliActivity)getActivity()).requestGalleryPermission(mParam1.getImages().get(position));
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("url",mParam1.getImages().get(position));
                    startActivity(intent);
                }
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }
}