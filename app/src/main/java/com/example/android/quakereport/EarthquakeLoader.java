package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by jimad12 on 3/9/18.
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthQuake>> {
    /** Tag for log message */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    /** Query URl */
    private String mUrl;

    /**
     * Constructs a new {@link EarthquakeLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public  EarthquakeLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

/**
 *This is the background Thread
*/

    @Override
    public List<EarthQuake> loadInBackground(){
        if (mUrl == null){
            return null;
        }

        //Perform the network request, parse the response, and extract list of earthquake.
        List<EarthQuake> earthQuakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthQuakes;
    }



} // end class
