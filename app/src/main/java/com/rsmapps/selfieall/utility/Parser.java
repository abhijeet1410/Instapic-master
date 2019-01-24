package com.rsmapps.selfieall.utility;

import com.rsmapps.selfieall.model.Celebrity;
import com.rsmapps.selfieall.model.Industry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class Parser {

    public static ArrayList<Industry> parseIndustry(String response){
        ArrayList<Industry> list = new ArrayList<>();
        try{
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("data");
            for(int i=0; i < data.length(); i++){
                JSONObject object = data.getJSONObject(i);
                Industry industry = new Industry();
                industry.setId(object.getString("id"));
                industry.setName(object.getString("industry"));
                industry.setGenderwise(object.optString("type","1"));
                industry.setImage(object.optString("image",""));
                list.add(industry);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }


    public static ArrayList<Celebrity> parseCelebrity(String response){
        ArrayList<Celebrity> list = new ArrayList<>();
        try{
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray data = jsonResponse.getJSONArray("data");
            for(int i=0; i < data.length(); i++){
                JSONObject object = data.getJSONObject(i);
                Celebrity celebrity = new Celebrity();
                celebrity.setId(object.getString("id"));
                celebrity.setName(object.getString("name"));
                celebrity.setGender(object.getString("gender"));
                if(object.has("gallery_pics")){
                    ArrayList<String> images = new ArrayList<>();
                    JSONArray jsonArrayImages = object.getJSONArray("gallery_pics");
                    for(int j = 0; j < jsonArrayImages.length(); j++ ){
                        images.add(jsonArrayImages.getString(j));
                    }
                    celebrity.setImages(images);
                }
                list.add(celebrity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
