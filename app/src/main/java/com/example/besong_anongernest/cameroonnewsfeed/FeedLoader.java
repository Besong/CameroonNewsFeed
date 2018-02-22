package com.example.besong_anongernest.cameroonnewsfeed;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Besong-Anong Ernest on 11/19/2017.
 */

public class FeedLoader  extends AsyncTaskLoader<List<Feed>> {

    //Query Url
   private String mUrl;

    /**
     *
     * @param context of the activity
     * @param url to load data from
     */
    public FeedLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     *  This is on a background thread
     * @return List<Feed>
     */
    @Override
    public List<Feed> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of news feeds.
        List<Feed> newsFeed  = QueryUtils.fetchNewsFeedData(mUrl);
        return newsFeed;
    }
}
