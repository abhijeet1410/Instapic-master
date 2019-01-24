package com.savedPhotos.dialogFragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.rsmapps.selfieall.R;

public class AlertDialog extends DialogFragment implements View.OnClickListener {
    public static final String TAG = AlertDialog.class.getSimpleName();

    private Context context;
    private OnItemClickListener onItemClickListener;

    private String title, description, subtitle;

    public AlertDialog getAlertDialog(Context context, OnItemClickListener onItemClickListener){
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public AlertDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public AlertDialog setDescription(String description){
        this.description = description;
        return this;
    }

    public AlertDialog setSubtitle(String subtitle){
        this.subtitle = subtitle;
        return this;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private TextView tvContinue, tvCancel, tvDescription, tvTitle, tvSubtitle;

    private void getInfo(View view){

        tvTitle = view.findViewById(R.id.tvTitle);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvSubtitle = view.findViewById(R.id.tvSubtitle);

        tvContinue = view.findViewById(R.id.tvContinue);
        tvCancel = view.findViewById(R.id.tvCancel);

    }

    private void setInfo(){

        tvTitle.setText(title);
        tvDescription.setText(description);
        tvSubtitle.setText(subtitle);

        tvContinue.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_alert_dialog, container,false);
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
        switch (v.getId()) {

            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.tvContinue:
                onItemClickListener.onContinueClicked();
                dismiss();
                break;

        }
    }

    public interface OnItemClickListener{
        void onContinueClicked();
    }
}
