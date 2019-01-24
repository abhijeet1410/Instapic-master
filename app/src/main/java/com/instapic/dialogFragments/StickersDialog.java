package com.instapic.dialogFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.activity.StickerActivity;
import com.rsmapps.selfieall.adapter.StickerAdapter;
import com.rsmapps.selfieall.databinding.DialogFragmentStickersBinding;
import com.rsmapps.selfieall.utility.AssetUtil;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class StickersDialog extends DialogFragment {
    public static final String TAG = StickersDialog.class.getSimpleName();

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private static final String INSTA_STICKER = "sticker/Insta Stickers";
//    private static final String STICKER_SELECTED_ITEM = "sticker selected item";
//    private static final String PATH = "path";

    private StickerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnStickerSelectedListener onStickerSelectedListener;

    public void setOnStickerSelectedListener(OnStickerSelectedListener onStickerSelectedListener) {
        this.onStickerSelectedListener = onStickerSelectedListener;
    }

    private DialogFragmentStickersBinding binding;

    private void setInfo() {

        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        /**
         * Setting Stickers Adapter
         */
        initStickersAdapter();

    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_stickers, container, false);
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
        mAdapter.setData(AssetUtil.getDataFromAsser(getActivity(),INSTA_STICKER));
        binding.rvStickers.setAdapter(mAdapter);

        binding.rvStickers.addOnItemTouchListener(new StickerAdapter.RecyclerTouchListener(context,
                binding.rvStickers, new StickerAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Intent full = new Intent();
//                full.putExtra(STICKER_SELECTED_ITEM, stickerSelectedItem);
//                full.putExtra(PATH, mAdapter.getSelectedPath(position));
//                setResult(RESULT_OK, full);
//                finish();

                onStickerSelectedListener.onStickerSelected(mAdapter.getSelectedPath(position));
                dismiss();
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    public interface OnStickerSelectedListener{
        void onStickerSelected(String path);
    }
}
