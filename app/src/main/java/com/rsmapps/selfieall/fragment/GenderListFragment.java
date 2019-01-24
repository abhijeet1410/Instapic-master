package com.rsmapps.selfieall.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.activity.KohaliActivity;
import com.rsmapps.selfieall.model.Celebrity;
import com.rsmapps.selfieall.model.Industry;
import com.rsmapps.selfieall.utility.Constant;
import com.rsmapps.selfieall.utility.NetworkUtils;
import com.rsmapps.selfieall.utility.Parser;
import com.rsmapps.selfieall.utility.ResponseCallBack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenderListFragment extends Fragment implements View.OnClickListener,ResponseCallBack {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Industry mParam1;
    private String mParam2;

    LinearLayout llMale;
    LinearLayout llFemale;
    TextView male;
    TextView female;



    public GenderListFragment() {
    }


    public static GenderListFragment newInstance(Industry industry, String param2) {
        GenderListFragment fragment = new GenderListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, industry);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Industry) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gender_list, container, false);

        llMale = view.findViewById(R.id.llyMale);
        llFemale = view.findViewById(R.id.llyFemale);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);

        male.setText(mParam1.getName() + " " + "Actors");
        female.setText(mParam1.getName() + " " + "Actress");


        getActivity().setTitle(mParam1.getName());

        llMale.setOnClickListener(this);
        llFemale.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {

        //CelebrityListFragment fragment = CelebrityListFragment.newInstance(celebrities,"");
        HashMap<String,String> params = new HashMap<>();
        params.put("industry_id",mParam1.getId());
        if(view == llMale){
            mParam2 = male.getText().toString();
            params.put("gender","male");
            new NetworkUtils(getActivity()).postForm(Constant.GET_CELEBRITY_URL,GenderListFragment.this,params,true,Constant.GET_CELEBRITY_URL);
        }else if(view == llFemale){
            mParam2 = female.getText().toString();
            params.put("gender","female");
            new NetworkUtils(getActivity()).postForm(Constant.GET_CELEBRITY_URL,GenderListFragment.this,params,true,Constant.GET_CELEBRITY_URL);
        }
    }


    @Override
    public void invoke(String response, String flag, String status) {
        try{
            if(status.equalsIgnoreCase(Constant.RESPONSE_SUCCESS)){
                ArrayList<Celebrity> list = Parser.parseCelebrity(response);
                CelebrityListFragment fragment = CelebrityListFragment.newInstance(list,mParam2);
                ((KohaliActivity)getActivity()).replaceFragment(fragment,CelebrityListFragment.class.getSimpleName(),true,false);
            }else {

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}