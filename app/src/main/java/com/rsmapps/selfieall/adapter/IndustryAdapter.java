package com.rsmapps.selfieall.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.activity.KohaliActivity;
import com.rsmapps.selfieall.fragment.CelebrityListFragment;
import com.rsmapps.selfieall.fragment.GenderListFragment;
import com.rsmapps.selfieall.model.Celebrity;
import com.rsmapps.selfieall.model.Industry;
import com.rsmapps.selfieall.utility.Constant;
import com.rsmapps.selfieall.utility.NetworkUtils;
import com.rsmapps.selfieall.utility.Parser;
import com.rsmapps.selfieall.utility.ResponseCallBack;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Lincoln on 31/03/16.
 */

public class IndustryAdapter extends RecyclerView.Adapter<IndustryAdapter.MyViewHolder> {
    private AppCompatActivity mContext;
    private ArrayList<Industry> mList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ResponseCallBack {
        public TextView title;
        public LinearLayout llyTitle;
        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            llyTitle = view.findViewById(R.id.llyTitle);
            icon = view.findViewById(R.id.icon);
            llyTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            /*if(mList.get(getAdapterPosition()).getGenderwise().equalsIgnoreCase("1")) {
                ((KohaliActivity) mContext).replaceFragment(GenderListFragment.newInstance(mList.get(getAdapterPosition()), ""), GenderListFragment.class.getSimpleName(), true, false);
            }else {
                HashMap<String,String> params = new HashMap<>();
                params.put("industry_id",mList.get(getAdapterPosition()).getId());
                params.put("gender","all");
                new NetworkUtils(mContext).postForm(Constant.GET_CELEBRITY_URL,this,params,true,Constant.GET_CELEBRITY_URL);
            }*/


            AdsUtil.showInterstitialAd(mContext,new AdListener(){
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    if(mList.get(getAdapterPosition()).getGenderwise().equalsIgnoreCase("1")) {
                        ((KohaliActivity) mContext).replaceFragment(GenderListFragment.newInstance(mList.get(getAdapterPosition()), ""), GenderListFragment.class.getSimpleName(), true, false);
                    }else {
                        HashMap<String,String> params = new HashMap<>();
                        params.put("industry_id",mList.get(getAdapterPosition()).getId());
                        params.put("gender","all");
                        new NetworkUtils(mContext).postForm(Constant.GET_CELEBRITY_URL,MyViewHolder.this,params,true,Constant.GET_CELEBRITY_URL);
                    }
                    AdsUtil.preloadInterstitialAd(mContext);
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if(mList.get(getAdapterPosition()).getGenderwise().equalsIgnoreCase("1")) {
                        ((KohaliActivity) mContext).replaceFragment(GenderListFragment.newInstance(mList.get(getAdapterPosition()), ""), GenderListFragment.class.getSimpleName(), true, false);
                    }else {
                        HashMap<String,String> params = new HashMap<>();
                        params.put("industry_id",mList.get(getAdapterPosition()).getId());
                        params.put("gender","all");
                        new NetworkUtils(mContext).postForm(Constant.GET_CELEBRITY_URL,MyViewHolder.this,params,true,Constant.GET_CELEBRITY_URL);
                    }
                    AdsUtil.preloadInterstitialAd(mContext);
                }
            });

        }

        @Override
        public void invoke(String response, String flag, String status) {
            try{
                if(status.equalsIgnoreCase(Constant.RESPONSE_SUCCESS)){
                    ArrayList<Celebrity> list = Parser.parseCelebrity(response);
                    CelebrityListFragment fragment = CelebrityListFragment.newInstance(list,mList.get(getAdapterPosition()).getName());
                    ((KohaliActivity)mContext).replaceFragment(fragment,CelebrityListFragment.class.getSimpleName(),true,false);
                }else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public IndustryAdapter(AppCompatActivity context,ArrayList<Industry> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.industry_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(mList.get(position).getName());
        Glide.with(mContext).load(mList.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.actor_placeholder).error(R.drawable.actor_placeholder)).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}