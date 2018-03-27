package com.example.android.quakereport;

/**
 * Created by jimad12 on 3/3/18.
 */

public class EarthQuake {

    // Let's create 3 Global variables for the earth quake data.

    /**
     * Magnitude of the earth quake
     */
    private double mMagnitude;

    /**
     * Location of the earth quake
     */
    private String mLocation;

    /**
     * Date of the earth quake
     */
    private long mTimeInMilliseconds;

    /**
     * url of the earth quake site data
     */
    private String mUrl;


    /**
     * Construct a new {@link EarthQuake} object
     *
     * @param magnitude is the size of the earthquake
     * @param location  is the location earth quake
     * @paramdate is the date of the earth quake
     * @param  timeInMilliseconds is the time for the earthquake in milliseconds
     */
    public EarthQuake(double magnitude, String location, long timeInMilliseconds, String url) {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
    }

    //Since the Global variables are private,
    //let's create public getter method to return their values.

    /**
     * Return the magnitude of the earthquake
     */
    public double getMagnitude() {
        return mMagnitude;
    }


    /**
     * Return the Location of the earthquake
     */
    public String getLocation() {
        return mLocation;
    }

    /**
     * Return the date of the earthquake
     */
    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    /**
     * Return the Location of the earthquake
     */
    public String getUrl() {
        return mUrl;
    }


} // end class
