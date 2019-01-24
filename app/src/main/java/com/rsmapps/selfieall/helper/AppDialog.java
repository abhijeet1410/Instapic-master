package com.rsmapps.selfieall.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import java.util.ArrayList;
import com.rsmapps.selfieall.R;
import com.rsmapps.selfieall.adapter.AdapterSpinner;

public class AppDialog extends Dialog  {

    private Activity activity;
    private LinearLayout llyColor;
    private ArrayList<String> arrayListFont;
    private String[] fonts = {"blacklarch.ttf","blessd.ttf","fancy.ttf","fon.ttf","homestile.ttf",
            "personaluse.ttf","smartwatch.ttf"};
    private TextEditer textEditer;


    public AppDialog(Activity context) {
        super(context, R.style.CustomDialogTheme);
        this.activity = context;
        this.setCanceledOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        View root = activity.getLayoutInflater().inflate(R.layout.dialog_save, null);
        final Button btn1 = (Button) root.findViewById(R.id.btn1);
        final Button btn2 = (Button) root.findViewById(R.id.btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent more = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+ResourceManager.PACKAGE_NAME));
                activity.startActivity(more);
            }
        });

        setContentView(root);
    }

    public AppDialog(Activity context, final TextEditer textEditer) {
        super(context, R.style.CustomDialogTheme);
        this.activity = context;
        this.setCanceledOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.textEditer = textEditer;

        View root = activity.getLayoutInflater().inflate(R.layout.dialog_text, null);
        final EditText editText = (EditText) root.findViewById(R.id.edtTxt);
        final Spinner sprFont = (Spinner) root.findViewById(R.id.sprFont);
        llyColor = (LinearLayout) root.findViewById(R.id.llyColor);

        editText.setText("");
        editText.setTypeface(Utils.getCustomTypeface(context,"fonts/blacklarch.ttf"));
        editText.setTextColor(activity.getResources().getColor(R.color.white));

        final Button btnOK = (Button) root.findViewById(R.id.btnOK);
        final Button btnCancel = (Button) root.findViewById(R.id.btnCancel);

        llyColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPickerDialog(activity,editText);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(textEditer !=null){
                    FontPojo fontPojo =  new FontPojo();
                    fontPojo.setText(editText.getText().toString().trim());
                    fontPojo.setTextColor(editText.getCurrentTextColor());
                    fontPojo.setTypeface(editText.getTypeface());
                    textEditer.onTextEditingCompleted(fontPojo);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        sprFont.setAdapter(new AdapterSpinner(activity, R.layout.custom_textview_to_spinner,
                activity.getResources().getStringArray(R.array.fonts)));
        sprFont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Typeface typeface = Utils.getCustomTypeface(activity,"fonts/"+fonts[position]);
                editText.setTypeface(typeface);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        setContentView(root);
    }

    private void colorPickerDialog(final Activity activity, final EditText editText){
        ColorPickerDialogBuilder
                .with(activity)
                .setTitle("Choose color")
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .noSliders()
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        llyColor.setBackgroundColor(selectedColor);
                        editText.setTextColor(selectedColor);
                    }
                })
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        llyColor.setBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("CANCEL", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }
}
