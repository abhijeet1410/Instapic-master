package com.aligohershabir.photocollage.utils.customViews;

import android.graphics.Typeface;

public class FontPojo {

    private String text;
    private int textStyle;
    private Typeface typeface;
    private int TextColor;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public int getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(int textStyle) {
        this.textStyle = textStyle;
    }

    public int getTextColor() {
        return TextColor;
    }

    public void setTextColor(int textColor) {
        TextColor = textColor;
    }


}

