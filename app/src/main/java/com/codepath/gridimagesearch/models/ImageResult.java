package com.codepath.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tawu on 10/27/15.
 */
public class ImageResult implements Serializable
{
    //private static final long serialVersionUID =

    public String fullUrl;
    public String thumbUrl;
    public String title;
    public String width;
    public String height;
    public String thumbWidth;
    public String thumbHeight;

    //new ImageResult(.. raw item json)
    public ImageResult(JSONObject json)
    {
        try
        {
            //query following info from JSON results:
            //decode attributes of the json into a data model
            //title: {"responseData" => "results" => [x: array] => "title"}
            this.title = json.getString("title");
            //url: {"responseData" => "results" => [x: array] => "url"}
            this.fullUrl = json.getString("url");
            //tbUrl: {"responseData" => "results" => [x: array] => "tbUrl"}
            this.thumbUrl = json.getString("tbUrl");
            //width: {"responseData" => "results" => [x: array] => "width"}
            this.width = json.getString("width");
            //height: {"responseData" => "results" => [x: array] => "height"}
            this.height = json.getString("height");
            //tbWidth: {"responseData" => "results" => [x: array] => "tbWidth"}
            this.thumbWidth = json.getString("tbWidth");
            //tbHeight: {"responseData" => "results" => [x: array] => "tbHeight"}
            this.thumbHeight = json.getString("tbHeight");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //Take an array of json images and return arraylist of image results
    //ImageResult.fromJSONArray([... , ...]);
    public static ArrayList<ImageResult> fromJSONArray(JSONArray jsonArray)
    {
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();

        for(int i = 0; i < jsonArray.length(); i++)
        {
            try
            {
                results.add(new ImageResult(jsonArray.getJSONObject(i)));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return results;
    }
}
