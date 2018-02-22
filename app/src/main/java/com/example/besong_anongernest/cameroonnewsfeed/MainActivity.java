package com.example.besong_anongernest.cameroonnewsfeed;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Feed>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // URL to get data of news feed
    public static final String FEED_CONTENT_URL = "http://content.guardianapis.com/search?q=cameroon&api-key=paste your api_key here";

    /**
     * Constant value for the feed loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int FEED_LOADER_ID = 1;

    /** Adapter for the list of news feeds**/
    private FeedAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView newsFeedlistView = (ListView)findViewById(R.id.list);

        mEmptyStateTextView = (TextView)findViewById(R.id.empty_tv);
        newsFeedlistView.setEmptyView(mEmptyStateTextView);

        // Create a new Adapter that takes an empty list of Feed as input.
        mAdapter = new FeedAdapter(this, new ArrayList<Feed>());

        //Set the adapter on the {@link listView}
        newsFeedlistView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news Feed.
       newsFeedlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               //Find the current news feed that was clicked
               Feed currentFeed = mAdapter.getItem(position);

               //Convert the string URL into a URI Object
               Uri uri = Uri.parse(currentFeed.getWebUrl());

               //Create intent to view the feed URI
               Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);

               // Send websiteIntent to launch activity
               startActivity(websiteIntent);

           }
       });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
          LoaderManager loaderManager = getSupportLoaderManager();

            loaderManager.initLoader(FEED_LOADER_ID, null, this);

        }
        else {
            //Hide loading indicator, so that error message will be visible
            View loadingIndicator = findViewById(R.id.load_indicator);
            loadingIndicator.setVisibility(View.GONE);

            //Update empty state text with no internet connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }


    @Override
    public Loader<List<Feed>> onCreateLoader(int id, Bundle args) {
        return new FeedLoader(this, FEED_CONTENT_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Feed>> loader, List<Feed> feeds) {

        //Hide loading indicator because data has been loaded
        View loadingIndicator = findViewById(R.id.load_indicator);
        loadingIndicator.setVisibility(View.GONE);

        //Set "no_news_feed_found" string to emptyTextView
        mEmptyStateTextView.setText(R.string.no_news_feed_found);

        //Clear the adapter of previous news feed.
        mAdapter.clear();

        // If there is a valid list of {@link Feed}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (feeds != null && !feeds.isEmpty()) {
            mAdapter.addAll(feeds);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Feed>> loader) {
        //Clear up existing data when feed data resets
        mAdapter.clear();
    }
}





