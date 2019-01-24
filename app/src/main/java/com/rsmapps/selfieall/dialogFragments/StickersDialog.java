package com.rsmapps.selfieall.dialogFragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.adapter.StickerAdapter;
import com.rsmapps.selfieall.databinding.DialogFragmentSelfieStickersBinding;
import com.rsmapps.selfieall.utility.AssetUtil;

import java.util.ArrayList;

public class StickersDialog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = StickersDialog.class.getSimpleName();

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private static final String TEXT_STICKER = "sticker/Text Sticker";
    private static final String STYLISH_TEXT = "sticker/Stylish Text";
    private static final String LOVE_STICKER = "sticker/Love Sticker";
    private static final String ANIMAL_STICKER = "sticker/Animal Sticker";
    private static final String CARTOON_STICKER = "sticker/cartoon Sticker";
    private static final String EMOJI_STICKER = "sticker/Emoticons";
    private static final String STICKER_SELECTED_ITEM = "sticker selected item";
    private static final String PATH = "path";

    private StickerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int stickerSelectedItem = R.id.llyText;
    private OnStickerSelectedListener onStickerSelectedListener;

    public void setOnStickerSelectedListener(OnStickerSelectedListener onStickerSelectedListener) {
        this.onStickerSelectedListener = onStickerSelectedListener;
    }

    private DialogFragmentSelfieStickersBinding binding;

    private void setInfo() {

        binding.ivClose.setOnClickListener(this);

        binding.llyText.setOnClickListener(this);
        binding.llyAnimal.setOnClickListener(this);
        binding.llyCartoon.setOnClickListener(this);
        binding.llyEmoji.setOnClickListener(this);
        binding.llyLove.setOnClickListener(this);
        binding.llyStylish.setOnClickListener(this);


        /**
         * Setting Stickers Adapter
         */
        initStickersAdapter();

        onFooterSelected(stickerSelectedItem);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_selfie_stickers, container, false);
        setInfo();

        try {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    private void initStickersAdapter() {
        mAdapter = new StickerAdapter(getActivity(), true);
        mAdapter.setData(new ArrayList<String>());
        mLayoutManager = new GridLayoutManager(context, 3);
        binding.rvStickers.setLayoutManager(mLayoutManager);
        binding.rvStickers.setAdapter(mAdapter);

        binding.rvStickers.addOnItemTouchListener(new StickerAdapter.RecyclerTouchListener(context,
                binding.rvStickers, new StickerAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                onStickerSelectedListener.onStickerSelected(mAdapter.getSelectedPath(position));
                dismiss();
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ivClose){
            dismiss();
        }else{
            onFooterSelected(view.getId());
        }
    }

    private void onFooterSelected(int selectedItem){
        stickerSelectedItem = selectedItem;
        switch (selectedItem){
            case R.id.llyText:
                binding.imgText.setSelected(true);
                binding.imgStylish.setSelected(false);
                binding.imgLove.setSelected(false);
                binding.imgAnimal.setSelected(false);
                binding.imgCartoon.setSelected(false);
                binding.imgEmoji.setSelected(false);
                mAdapter.setData(AssetUtil.getDataFromAsser(getActivity(),TEXT_STICKER));
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.llyStylish:
                binding.imgText.setSelected(false);
                binding.imgStylish.setSelected(true);
                binding.imgLove.setSelected(false);
                binding.imgAnimal.setSelected(false);
                binding.imgCartoon.setSelected(false);
                binding.imgEmoji.setSelected(false);
                mAdapter.setData(AssetUtil.getDataFromAsser(getActivity(),STYLISH_TEXT));
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.llyLove:
                binding.imgText.setSelected(false);
                binding.imgStylish.setSelected(false);
                binding.imgLove.setSelected(true);
                binding.imgAnimal.setSelected(false);
                binding.imgCartoon.setSelected(false);
                binding.imgEmoji.setSelected(false);
                mAdapter.setData(AssetUtil.getDataFromAsser(getActivity(),LOVE_STICKER));
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.llyAnimal:
                binding.imgText.setSelected(false);
                binding.imgStylish.setSelected(false);
                binding.imgLove.setSelected(false);
                binding.imgAnimal.setSelected(true);
                binding.imgCartoon.setSelected(false);
                binding.imgEmoji.setSelected(false);
                mAdapter.setData(AssetUtil.getDataFromAsser(getActivity(),ANIMAL_STICKER));
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.llyCartoon:
                binding.imgText.setSelected(false);
                binding.imgStylish.setSelected(false);
                binding.imgLove.setSelected(false);
                binding.imgAnimal.setSelected(false);
                binding.imgCartoon.setSelected(true);
                binding.imgEmoji.setSelected(false);
                mAdapter.setData(AssetUtil.getDataFromAsser(getActivity(),CARTOON_STICKER));
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.llyEmoji:
                binding.imgText.setSelected(false);
                binding.imgStylish.setSelected(false);
                binding.imgLove.setSelected(false);
                binding.imgAnimal.setSelected(false);
                binding.imgCartoon.setSelected(false);
                binding.imgEmoji.setSelected(true);
                mAdapter.setData(AssetUtil.getDataFromAsser(getActivity(),EMOJI_STICKER));
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    public interface OnStickerSelectedListener{
        void onStickerSelected(String path);
    }
}
