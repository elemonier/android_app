package io.linger;

import android.util.Log;

import com.google.gson.Gson;

/**
 * Class to package a variable number of aggregated objects (e.g. lists of
 * contacts or messages) into a single object to be able to send everything
 * in a single HTTP request.
 * 
 * @author Emily Pakulski
 *
 */

public class DataAggregator 
{
	Aggregatable[] aggregatableObjects;
	
	public DataAggregator()
	{}
	
	public DataAggregator(Aggregatable ... aggregatables)
	{
		aggregatableObjects = new Aggregatable[aggregatables.length];
		for (int i = 0; i < aggregatableObjects.length; i++)
		{
			aggregatableObjects[i] = aggregatables[i];
		}
	}
	
	public void postToDatabase()
	{
		Gson gson = new Gson();
		Log.v("Testing", gson.toJson(aggregatableObjects));
	}
}
