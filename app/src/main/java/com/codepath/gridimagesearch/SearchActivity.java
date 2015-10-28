package com.codepath.gridimagesearch;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class SearchActivity extends ActionBarActivity {

    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;

    private String googleImageSearchURLFormat = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=%s&rsz=%d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this is to display icon on ActionBar, or AppBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_images);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_search);

        //finding views with this application, i.e. init:
        //etQuery
        //gvResults
        setupViews();

        imageResults = new ArrayList<ImageResult>();
    }

    private void setupViews()
    {
        etQuery = (EditText)findViewById(R.id.etQuery);
        gvResults = (GridView)findViewById(R.id.gvResults);
    }

    //button btnSearch onClick event handler
    public void onImageSearch(View v)
    {
        //find the query string in etQuery view
        String query = etQuery.getText().toString();

        //use Toast to temporary show the content of query for debug purpose
        //Toast.makeText(this, query, Toast.LENGTH_SHORT).show();


        //generate query string for URL
        int resultCount = 4;
        String queryUrl = String.format(googleImageSearchURLFormat, query, resultCount);
        //setup HTTP client
        AsyncHttpClient client = new AsyncHttpClient();
        //GET query
        client.get(queryUrl, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response)
                    {
                        //log info in debug view
                        //Log.d("DEBUG", response.toString());

                        //iterate each of the photo items and decode the item into a java object
                        JSONArray imageResultsJSON = null;
                        try
                        {
                            //{"responseData" => "results" => [x: array] => attribute}
                            imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                            //clear existing images from the array in cases where its a new search
                            imageResults.clear();
                            imageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
