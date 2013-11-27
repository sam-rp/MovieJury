package com.sidekick.moviejury.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.sidekick.moviejury.util.LoggingUtil;

public class MyMovieAPIRetriever extends AbstractMovieRetriever {

	public MyMovieAPIRetriever() {
		this.setURL("http://mymovieapi.com/?type=json&plot=simple&episode=1&limit=1&yg=1&mt=M&lang=en-US&offset=&aka=full&year=2013&title=");
	}

	@Override
	protected String getIMDBId(String responseText) {
		String imdbId = null;
		try {
			//final JSONObject jsonObject = new JSONObject(responseText);
			final JSONArray array = new JSONArray(responseText);
			final JSONObject jsonObject = array.getJSONObject(0);
			imdbId = jsonObject.getString("imdb_id");

		} catch (JSONException e) {
			LoggingUtil.error(getClass(), "Error getting the imdb id...", e);
		}
		return imdbId;
	}

	@Override
	protected boolean isValidResponse(final String response) {
		boolean isValid = true;
		 try {
		// final JSONObject jsonObject = new JSONObject(response);
		final Object json = new JSONTokener(response).nextValue();
		if (json instanceof JSONObject) {

			if (((JSONObject) json).has("code")
					&& ((JSONObject) json).has("error")) {
				isValid = false;
			}
		}
		 } catch (JSONException e) {
		 LoggingUtil.error(getClass(),
		 "Not a valid response from myMovie API", e);
		 }
		return isValid;

	}

}
