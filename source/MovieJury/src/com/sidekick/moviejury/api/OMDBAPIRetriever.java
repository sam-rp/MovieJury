package com.sidekick.moviejury.api;

import org.json.JSONException;
import org.json.JSONObject;

import com.sidekick.moviejury.util.LoggingUtil;



public class OMDBAPIRetriever extends AbstractMovieRetriever {

	
	public OMDBAPIRetriever(){
		this.setURL("http://www.omdbapi.com/?t=");
	}

	@Override
	protected boolean isValidResponse(String responseText) {
		boolean isValid = true;
		try {
			final JSONObject jsonObject = new JSONObject(responseText);
			if (jsonObject.has("Response") && jsonObject.has("Error")) {
				isValid = false;
			}
		} catch (JSONException e) {
			LoggingUtil.error(getClass(), "Not a valid response from OMDB API", e);
		}
		return isValid;
	}

	@Override
	protected String getIMDBId(String responseText) {
		String imdbId=null;
		try {
			final JSONObject jsonObject = new JSONObject(responseText);
			imdbId = jsonObject.getString("imdbID");
			
		} catch (JSONException e) {
			LoggingUtil.error(getClass(), "Error getting the imdb id...", e);
		}
		return imdbId;
	}

}
