package com.rsmapps.selfieall.helper;

import android.graphics.Shader;
import android.graphics.Typeface;

/**
 * Created by MDIRFAN on 11/12/17.
 */

public class TextEffectPojo {

    private String text;
    private int textSize;
    private int textStyle;
    private Typeface typeface;
    private int TextColor;
    private Shader shader;
    private ShadowLayer shadowLayer;
    private int alpha;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
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

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public ShadowLayer getShadowLayer() {
        return shadowLayer;
    }

    public void setShadowLayer(ShadowLayer shadowLayer) {
        this.shadowLayer = shadowLayer;
    }
}
