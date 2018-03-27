package com.example.android.quakereport;

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
 * Created by jimad12 on 3/3/18.
 */


public final class QueryUtils {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {

    }


    /**
     * Query the USGS dataset and return a list of {@linkEarthquake} objects.
     */
    public static List<EarthQuake> fetchEarthquakeData(String requestUrl) {

        /*

        //A test to force background thread to sleep for 2 minutes, to view progress bar
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */


        //Create the url object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "problem making the HTTP request.", e);
        }
        //Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<EarthQuake> earthquakes = extractFeatureFromJson(jsonResponse);

        //Return the list of {@link Earthquake}
        return earthquakes;

    }


    //Let's create the helper methods for doInbackgound() in EarthquakeActivity class

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

        // if the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        } //Otherwise

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            //setting up the request
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect(); //this makes the connection

            //If the resquest was successful (response code 200), then read the input stream
            //and parse & parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
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

        //This is a String builder helps us built new strings
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {

            //InputStream represent a stream of bytes(small chunk of data)
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            //BufferReader helps us read text from an InputStream
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link EarthQuake} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private static List<EarthQuake> extractFeatureFromJson(String earthquakeJSON) {

        // If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        //Create an empty ArrayList that we can start adding earthquake to
        List<EarthQuake> earthQuakes = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < earthquakeArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                // For a given earthquake, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that earthquake.
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                // Extract the value for the key called "mag"
                double magnitude = properties.getDouble("mag");

                // Extract the value for the key called "time"
                long time = properties.getLong("time");

                // Extract the value for the key called "place"
                String location = properties.getString("place");

                // Extract the value for the key called "url"
                String url = properties.getString("url");


                // Create a new {@link Event} object
                EarthQuake earthQuake = new EarthQuake(magnitude, location, time, url);
                // Add the new {@link Earthquake} to the list of earthquakes.
                earthQuakes.add(earthQuake);
            }

        } catch (JSONException e) {

            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return earthQuakes;


    }// end ExtracFeatureFromJson


} // end Class
