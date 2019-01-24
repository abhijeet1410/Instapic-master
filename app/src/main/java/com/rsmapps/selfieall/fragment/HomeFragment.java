package com.rsmapps.selfieall.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.activity.KohaliActivity;
import com.rsmapps.selfieall.activity.MainActivity;
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
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ResponseCallBack {

    private static final String TITLE = "Select Celebrity";
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private IndustryAdapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Industry> mIndustries = new ArrayList<>();

    private SliderLayout mDemoSlider;


    public HomeFragment() {
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getActivity().setTitle(TITLE);

        mAdapter = new IndustryAdapter((AppCompatActivity) getActivity(), mIndustries);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


        view.findViewById(R.id.offline_images).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Celebrity offlineCelebrity = KohaliActivity.getOfflineCelebrity();
                offlineCelebrity.getImages().addAll(Utils.getLocalImages(getActivity()));
                ((KohaliActivity) getActivity())
                        .replaceFragment(CelebrityImagesFragment.newInstance(offlineCelebrity, ""),
                                CelebrityImagesFragment.class.getSimpleName(), true, false);
            }
        });

        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);
        mDemoSlider.getLayoutParams().height = width / 2;
        init();


        new NetworkUtils(getActivity())
                .get(Constant.GET_INDUSTRY_URL, this, new JSONObject(),
                        false, Constant.GET_INDUSTRY_URL);

        return view;
    }

    private void init() {
        mDemoSlider.removeAllSliders();
        ArrayList<Celebrity> list = new ArrayList<>();

        Celebrity celebrity1 = new Celebrity();
//        Celebrity celebrity2 = new Celebrity();
        Celebrity celebrity3 = new Celebrity();
        Celebrity celebrity4 = new Celebrity();
        Celebrity celebrity5 = new Celebrity();
//        Celebrity celebrity6 = new Celebrity();
        Celebrity celebrity7 = new Celebrity();
        Celebrity celebrity8 = new Celebrity();
        Celebrity celebrity9 = new Celebrity();
        Celebrity celebrity10 = new Celebrity();

        celebrity1.setImage(R.drawable.banner1);
//        celebrity2.setImage(R.drawable.banner2);
        celebrity3.setImage(R.drawable.banner3);
        celebrity4.setImage(R.drawable.banner4);
        celebrity5.setImage(R.drawable.banner5);
//        celebrity6.setImage(R.drawable.banner6);
        celebrity7.setImage(R.drawable.banner7);
        celebrity8.setImage(R.drawable.banner8);
        celebrity9.setImage(R.drawable.banner9);
        celebrity10.setImage(R.drawable.banner10);

        celebrity1.setName("Sonakshi Sinha");
//        celebrity2.setName("Salman Khan");
        celebrity3.setName("Shahrukh Khan");
        celebrity4.setName("Shahid Kapoor");
        celebrity5.setName("Deepika Padukone");
//        celebrity6.setName("Sonam Kapoor");
        celebrity7.setName("Varun Dhawan");
        celebrity8.setName("Aiswarya Rai");
        celebrity9.setName("Anushka Sharma");
        celebrity10.setName("Sidharth Malhotra");


        list.add(celebrity1);
//        list.add(celebrity2);
        list.add(celebrity3);
        list.add(celebrity4);
        list.add(celebrity5);
//        list.add(celebrity6);
        list.add(celebrity7);
        list.add(celebrity8);
        list.add(celebrity9);
        list.add(celebrity10);

        for (final Celebrity playlist : list) {
            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView
                    .image(playlist.getImage())
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .description("Available Offline")
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView baseSliderView) {

                            AdsUtil.showInterstitialAd(getActivity(), new AdListener() {
                                @Override
                                public void onAdFailedToLoad(int i) {
                                    super.onAdFailedToLoad(i);
                                    Celebrity offlineCelebrity = KohaliActivity.getOfflineCelebrity();
                                    offlineCelebrity.getImages().addAll(Utils.getLocalImages(getActivity()));
                                    ((KohaliActivity) getActivity()).replaceFragment(CelebrityImagesFragment.newInstance(offlineCelebrity, ""), CelebrityImagesFragment.class.getSimpleName(), true, false);
                                    AdsUtil.preloadInterstitialAd(getActivity());

                                }

                                @Override
                                public void onAdClosed() {
                                    super.onAdLoaded();
                                    Celebrity offlineCelebrity = KohaliActivity.getOfflineCelebrity();
                                    offlineCelebrity.getImages().addAll(Utils.getLocalImages(getActivity()));
                                    ((KohaliActivity) getActivity()).replaceFragment(CelebrityImagesFragment.newInstance(offlineCelebrity, ""), CelebrityImagesFragment.class.getSimpleName(), true, false);
                                    AdsUtil.preloadInterstitialAd(getActivity());
                                }
                            });
/*                                Celebrity offlineCelebrity = KohaliActivity.getOfflineCelebrity();
                                offlineCelebrity.getImages().addAll(Utils.getLocalImages(getActivity()));
                                ((KohaliActivity)getActivity()).replaceFragment(CelebrityImagesFragment.newInstance(offlineCelebrity,""),CelebrityImagesFragment.class.getSimpleName(),true,false);*/

                        }
                    });
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", playlist.getName());
            mDemoSlider.addSlider(textSliderView);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mDemoSlider.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setDuration(4000);
    }


    @Override
    public void invoke(String response, String flag, String status) {
        try {
            if (status.equalsIgnoreCase(Constant.RESPONSE_SUCCESS)) {
                ArrayList<Industry> list = Parser.parseIndustry(response);
                mIndustries.clear();
                mIndustries.addAll(list);
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(context, "NO INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}