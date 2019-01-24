package com.aligohershabir.photocollage.view.dialogFragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.aligohershabir.photocollage.R;

public class CollageOptionsDialog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = CollageOptionsDialog.class.getSimpleName();

    private Context context;
    private OnItemClickedListener onItemClickedListener;

    public CollageOptionsDialog getInstance(Context context, OnItemClickedListener onItemClickedListener){

        CollageOptionsDialog collageOptionsDialog = new CollageOptionsDialog();
        collageOptionsDialog.onItemClickedListener = onItemClickedListener;
        collageOptionsDialog.context = context;
        return collageOptionsDialog;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private LinearLayout llEdit, llChange, llCancel;

    private void getInfo(View view){
        llEdit = view.findViewById(R.id.llEdit);
        llChange = view.findViewById(R.id.llChange);
        llCancel = view.findViewById(R.id.llCancel);
    }

    private void setInfo(){

        llEdit.setOnClickListener(this);
        llChange.setOnClickListener(this);
        llCancel.setOnClickListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_collage_options,container,false);
        getInfo(view);
        setInfo();
        try{

            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.llEdit) {
            onItemClickedListener.onEditClicked();

        } else if (i == R.id.llChange) {
            onItemClickedListener.onChangeClicked();

        } else if (i == R.id.llCancel) {
        }
        dismiss();
    }

    public interface OnItemClickedListener{
        void onEditClicked();
        void onChangeClicked();
    }
}
