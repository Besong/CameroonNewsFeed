package com.example.besong_anongernest.cameroonnewsfeed;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Besong-Anong Ernest on 11/19/2017.
 */

public final class QueryUtils {

    /** Log Tag for messages **/
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Feed> fetchNewsFeedData(String feedRequestUrl) {

        // Create a new URL Object
     URL  url = createUrl(feedRequestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extract relevant fields from the JSON response and create a list of {@link Feed}s
        List<Feed> newsFeed = extractContentFromJson(jsonResponse);

        //Return the list of {@link Feed}s
        return newsFeed;

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Feed JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //Parsing the JSON response.
    private static List<Feed> extractContentFromJson(String feedJson) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(feedJson)) {
            return null;
        }

        // Empty arraylist which newsFeeds can be added to.
        List<Feed> newsFeeds = new ArrayList<>();

        try {
            //Create a JSON Object from the Json response string
            JSONObject baseJsonResponse = new JSONObject(feedJson);

            //Extract the JSON Object relating to "response"
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");

            //Extract the JSON Array relating to "results"
            JSONArray resultsArray = responseObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentResultsObject = resultsArray.getJSONObject(i);

                //Extract the value for the key sectionName
                String sectionName = currentResultsObject.getString("sectionName");

                //Extract the value for the key webTitle
                String webTitle = currentResultsObject.getString("webTitle");

                //Extract the value for the key webUrl
                String webUrl = currentResultsObject.getString("webUrl");

                Feed feed = new Feed(webTitle, sectionName, webUrl);

                newsFeeds.add(feed);
            }

        } catch (JSONException e) {
            Log.v(LOG_TAG, "Problem parsing the newsFeed JSON results", e);
        }

        return  newsFeeds;
    }
}
