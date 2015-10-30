package com.codepath.gridimagesearch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.gridimagesearch.R;
import com.codepath.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tawu on 10/29/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult>
{
    public ImageResultsAdapter(Context context, List<ImageResult> images)
    {
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        ImageResult imageInfo = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false); //false means don't want to attach target layout, here is item_image_result, with parent yet
        }

        //find image view
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);

        // Clear out image from last time
        ivImage.setImageResource(0);
        //Populate title and remote download image url
        tvTitle.setText(Html.fromHtml(imageInfo.title));
        //remotely download the image data in the background with Picasso
        Picasso.with(getContext()).load(imageInfo.thumbUrl).into(ivImage);
        //Return the completed view to be displayed
        return convertView;
    }
}
