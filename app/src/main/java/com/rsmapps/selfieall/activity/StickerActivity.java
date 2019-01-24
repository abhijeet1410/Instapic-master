package com.rsmapps.selfieall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.rsmapps.selfieall.AdsUtil;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.adapter.StickerAdapter;
import com.rsmapps.selfieall.utility.AssetUtil;
import java.util.ArrayList;
import butterknife.InjectView;

public class StickerActivity extends BaseActivity implements View.OnClickListener {

    private static final String TEXT_STICKER = "sticker/Text Sticker";
    private static final String STYLISH_TEXT = "sticker/Stylish Text";
    private static final String LOVE_STICKER = "sticker/Love Sticker";
    private static final String ANIMAL_STICKER = "sticker/Animal Sticker";
    private static final String CARTOON_STICKER = "sticker/cartoon Sticker";
    private static final String EMOJI_STICKER = "sticker/Emoticons";
    private static final String STICKER_SELECTED_ITEM = "sticker selected item";
    private static final String PATH = "path";

    @InjectView(R.id.llyText)
    LinearLayout llyText;
    @InjectView(R.id.llyStylish)
    LinearLayout llyStylish;
    @InjectView(R.id.llyLove)
    LinearLayout llyLove;
    @InjectView(R.id.llyAnimal)
    LinearLayout llyllyAnimal;
    @InjectView(R.id.llyCartoon)
    LinearLayout llyCartoon;
    @InjectView(R.id.llyEmoji)
    LinearLayout llyEmoji;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.imgText)
    ImageView imgText;
    @InjectView(R.id.imgStylish)
    ImageView imgStylish;
    @InjectView(R.id.imgLove)
    ImageView imgLove;
    @InjectView(R.id.imgAnimal)
    ImageView imgAnimal;
    @InjectView(R.id.imgCartoon)
    ImageView imgCartoon;
    @InjectView(R.id.imgEmoji)
    ImageView imgEmoji;

    private StickerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int stickerSelectedItem = R.id.llyText;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sticker);
        setupRecyclerView();
        init();

        showAdd();
    }

    private void showAdd() {

        AdsUtil.showInterstitialAd(StickerActivity.this, new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                AdsUtil.preloadInterstitialAd(StickerActivity.this);

            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

                AdsUtil.preloadInterstitialAd(StickerActivity.this);
            }
        });
    }

    private void setupRecyclerView() {
        mAdapter = new StickerAdapter(this, false);
        mAdapter.setData(new ArrayList<String>());
        mLayoutManager = new GridLayoutManager(StickerActivity.this, 4);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new StickerAdapter.RecyclerTouchListener(this, recyclerView, new StickerAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent full = new Intent();
                full.putExtra(STICKER_SELECTED_ITEM, stickerSelectedItem);
                full.putExtra(PATH, mAdapter.getSelectedPath(position));
                setResult(RESULT_OK, full);
                finish();
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void init(){
	    Bundle bundle = getIntent().getExtras();
	    if(bundle!=null)
	        stickerSelectedItem = bundle.getInt(STICKER_SELECTED_ITEM,R.id.llyText);
	    onFooterSelected(stickerSelectedItem);
    }

    @Override
    public void onClick(View view) {
	    onFooterSelected(view.getId());
    }

    private void onFooterSelected(int selectedItem){
	    stickerSelectedItem = selectedItem;
        switch (selectedItem){
            case R.id.llyText:
                imgText.setSelected(true);
                imgStylish.setSelected(false);
                imgLove.setSelected(false);
                imgAnimal.setSelected(false);
                imgCartoon.setSelected(false);
                imgEmoji.setSelected(false);
                mAdapter.setData(AssetUtil.getDataFromAsser(this,TEXT_STICKER));
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.llyStylish:
                imgText.setSelected(false);
                imgStylish.setSelected(true);
                imgLove.setSelected(false);
                imgAnimal.setSelected(false);
                imgCartoon.setSelected(false);
                imgEmoji.setSelected(false);
                mAdapter.setData(AssetUtil.getDataFromAsser(this,STYLISH_TEXT));
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.llyLove:
                imgText.setSelected(false);
                imgStylish.setSelected(false);
                imgLove.setSelected(true);
                imgAnimal.setSelected(false);
                imgCartoon.setSelected(false);
                imgEmoji.setSelected(false);
                mAdapter.setData(AssetUtil.getDataFromAsser(this,LOVE_STICKER));
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.llyAnimal:
                imgText.setSelected(false);
                imgStylish.setSelected(false);
                imgLove.setSelected(false);
                imgAnimal.setSelected(true);
                imgCartoon.setSelected(false);
                imgEmoji.setSelected(false);
                mAdapter.setData(AssetUtil.getDataFromAsser(this,ANIMAL_STICKER));
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.llyCartoon:
                imgText.setSelected(false);
                imgStylish.setSelected(false);
                imgLove.setSelected(false);
                imgAnimal.setSelected(false);
                imgCartoon.setSelected(true);
                imgEmoji.setSelected(false);
                mAdapter.setData(AssetUtil.getDataFromAsser(this,CARTOON_STICKER));
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.llyEmoji:
                imgText.setSelected(false);
                imgStylish.setSelected(false);
                imgLove.setSelected(false);
                imgAnimal.setSelected(false);
                imgCartoon.setSelected(false);
                imgEmoji.setSelected(true);
                mAdapter.setData(AssetUtil.getDataFromAsser(this,EMOJI_STICKER));
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}
