package com.example.besong_anongernest.cameroonnewsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Besong-Anong Ernest on 11/19/2017.
 */

public class FeedAdapter extends ArrayAdapter<Feed> {

    /** Log tag message for this class **/
    public static final String LOG_TAG = FeedAdapter.class.getSimpleName();

    public FeedAdapter( Context context,  List<Feed> feeds) {
        super(context, 0, feeds);
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {

           listItemView = LayoutInflater.from(getContext()).inflate(R.layout.content_list, parent, false);
        }
            Feed currentFeed = getItem(position);

            //Find the TextView of feed section name
           TextView section = (TextView) listItemView.findViewById(R.id.section_name_tv);

         //Set text on section TextView
           section.setText(currentFeed.getSectionName());

            //Find the Textview of feed title
            TextView title = (TextView) listItemView.findViewById(R.id.title_tv);
            // Set text on title TextView
            title.setText(currentFeed.getWebTitle());

        return listItemView;
    }
}
