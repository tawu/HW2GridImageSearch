package com.codepath.gridimagesearch.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.gridimagesearch.adapters.EndlessScrollListener;
import com.codepath.gridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.gridimagesearch.models.ImageResult;
import com.codepath.gridimagesearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class SearchActivity extends ActionBarActivity {

    private int maxSearchReturnItems = 8; //max number is 8, check "rsz" on https://developers.google.com/image-search/v1/jsondevguide#json_reference
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;

    private String googleImageSearchURLFormat = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=%s&rsz=%d&start=%d";

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

        //Standard steps for adapter attach
        //Creates the data source
        imageResults = new ArrayList<ImageResult>();
        //Attach the data source to an adapter
        aImageResults = new ImageResultsAdapter(this, imageResults);
        //Link the adapter to the adapterview (gridview)
        gvResults.setAdapter(aImageResults);

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page * maxSearchReturnItems);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

    }

    private void setupViews()
    {
        etQuery = (EditText)findViewById(R.id.etQuery);
        gvResults = (GridView)findViewById(R.id.gvResults);

        //launch second window when click the image
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //launch the image display activity, launch a second window and pass data to second window
                //create an intent
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                //get the image result to display
                ImageResult result = imageResults.get(position);
                //pass the image result into the intent
                //the passing data has to be either serializable, or parcelable
                //String is OK
                //i.putExtra("url", result.fullUrl);
                i.putExtra("result", result);
                //launch the new activity
                startActivity(i);
            }
        });

    }


    public void customLoadMoreDataFromApi(int offset)
    {
        String query = etQuery.getText().toString();

        //generate query string for URL
        String queryUrl = String.format(googleImageSearchURLFormat, query, maxSearchReturnItems, offset);
        //setup HTTP client
        AsyncHttpClient client = new AsyncHttpClient();
        //GET query
        client.get(queryUrl, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //log info in debug view
                        //Log.d("DEBUG", response.toString());

                        //iterate each of the photo items and decode the item into a java object
                        JSONArray imageResultsJSON = null;
                        try {
                            //{"responseData" => "results" => [x: array] => attribute}
                            imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                            //clear existing images from the array in cases where its a new search
                            //imageResults.clear();
                            /*
                            //Standard steps:
                            //add images to imageResults array
                            //imageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                            //Now we can refresh ImageResultsAdapter
                            aImageResults.notifyDataSetChanged();
                            */

                            //alternative way:
                            // since adapter is an Array, it has similar method too,
                            // and this automatically triggers the notify
                            aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //check result
                        //Log.i("INFO", imageResults.toString());
                    }
                }
        );

    }

    //button btnSearch onClick event handler
    public void onImageSearch(View v)
    {
        //find the query string in etQuery view
        String query = etQuery.getText().toString();

        //use Toast to temporary show the content of query for debug purpose
        //Toast.makeText(this, query, Toast.LENGTH_SHORT).show();


        //get images

        //generate query string for URL
        String queryUrl = String.format(googleImageSearchURLFormat, query, maxSearchReturnItems, 0);
        //setup HTTP client
        AsyncHttpClient client = new AsyncHttpClient();
        //GET query
        client.get(queryUrl, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        //log info in debug view
                        //Log.d("DEBUG", response.toString());

                        //iterate each of the photo items and decode the item into a java object
                        JSONArray imageResultsJSON = null;
                        try {
                            //{"responseData" => "results" => [x: array] => attribute}
                            imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
                            //clear existing images from the array in cases where its a new search
                            imageResults.clear();
                            /*
                            //Standard steps:
                            //add images to imageResults array
                            //imageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                            //Now we can refresh ImageResultsAdapter
                            aImageResults.notifyDataSetChanged();
                            */

                            //alternative way:
                            // since adapter is an Array, it has similar method too,
                            // and this automatically triggers the notify
                            aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //check result
                        //Log.i("INFO", imageResults.toString());
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
