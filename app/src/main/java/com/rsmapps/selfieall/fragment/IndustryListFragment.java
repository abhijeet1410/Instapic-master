package com.rsmapps.selfieall.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.activity.KohaliActivity;
import com.rsmapps.selfieall.adapter.IndustryAdapter;
import com.rsmapps.selfieall.helper.Utils;
import com.rsmapps.selfieall.model.Celebrity;
import com.rsmapps.selfieall.model.Industry;
import com.rsmapps.selfieall.utility.Constant;
import com.rsmapps.selfieall.utility.NetworkUtils;
import com.rsmapps.selfieall.utility.Parser;
import com.rsmapps.selfieall.utility.ResponseCallBack;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IndustryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndustryListFragment extends Fragment implements ResponseCallBack{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private IndustryAdapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Industry> mIndustries = new ArrayList<>();

    private View offlineImages;



    public IndustryListFragment() {
    }


    public static IndustryListFragment newInstance(String param1, String param2) {
        IndustryListFragment fragment = new IndustryListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_industry_list, container, false);

        getActivity().setTitle("Celebrities");

        mAdapter = new IndustryAdapter((AppCompatActivity)getActivity(),mIndustries);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        offlineImages = view.findViewById(R.id.offline_images);
        offlineImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Celebrity offlineCelebrity = KohaliActivity.getOfflineCelebrity();
                offlineCelebrity.getImages().addAll(Utils.getLocalImages(getActivity()));
                ((KohaliActivity)getActivity()).replaceFragment(CelebrityImagesFragment.newInstance(offlineCelebrity,""),CelebrityImagesFragment.class.getSimpleName(),true,false);
            }
        });

        new NetworkUtils(getActivity()).get(Constant.GET_INDUSTRY_URL,this,new JSONObject(),true,Constant.GET_INDUSTRY_URL);

        return view;
    }


    @Override
    public void invoke(String response, String flag, String status) {
        try{
            if(status.equalsIgnoreCase(Constant.RESPONSE_SUCCESS)){
                ArrayList<Industry> list = Parser.parseIndustry(response);
                mIndustries.clear();
                mIndustries.addAll(list);
                mAdapter.notifyDataSetChanged();
            }else {

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}