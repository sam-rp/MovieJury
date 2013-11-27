package com.sidekick.moviejury.api;

import org.json.JSONException;
import org.json.JSONObject;

import com.sidekick.moviejury.util.LoggingUtil;

public class IMDBAPIRetriever extends AbstractMovieRetriever {
	
	public IMDBAPIRetriever(){
		this.setURL("http://deanclatworthy.com/imdb/?q=");
	}

	@Override
	protected boolean isValidResponse(String response) {
		boolean isValid = true;
		try {
			final JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.has("code") && jsonObject.has("error")) {
				isValid = false;
			}
		} catch (JSONException e) {
			LoggingUtil.error(getClass(), "Not a valid response from IMDB API", e);
		}
		return isValid;
	}

	@Override
	protected String getIMDBId(String responseText) {
		String imdbId=null;
		try {
			final JSONObject jsonObject = new JSONObject(responseText);
			imdbId = jsonObject.getString("imdbid");
			
		} catch (JSONException e) {
			LoggingUtil.error(getClass(), "Error getting the imdb id...", e);
		}
		return imdbId;
	}

}
