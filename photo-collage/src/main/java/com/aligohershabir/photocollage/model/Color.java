package com.aligohershabir.photocollage.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Color {

    public static final String COLOR_TRANS = "Transparent";

    private String hex, name, rgb;

    public Color(JSONObject colorJsObj) {
        try {
            this.hex = colorJsObj.getString("hex");
            this.name = colorJsObj.getString("name");
            this.rgb = colorJsObj.getString("rgb");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRgb() {
        return rgb;
    }

    public void setRgb(String rgb) {
        this.rgb = rgb;
    }
}
