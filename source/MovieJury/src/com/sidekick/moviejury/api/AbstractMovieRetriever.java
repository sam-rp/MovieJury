package com.sidekick.moviejury.api;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.sidekick.moviejury.model.Movie;
import com.sidekick.moviejury.util.LoggingUtil;


public abstract class AbstractMovieRetriever implements IMovieRetriever {

	private String URL=null;
	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	private IMovieRetriever retriever;
	@Override
	public void setNextRetriever(IMovieRetriever retriever) {
		this.retriever=retriever;
	}
	
	protected IMovieRetriever getNextRetriever(){
		return this.retriever;
	}
	
	protected abstract boolean isValidResponse(final String response);
	
	@Override
	public Movie getMovieDetails(Movie movie) {
		Movie tempMovie=null;
		final String connectURL = this.getURL()+this.getSearchText(movie.getName());
		LoggingUtil.debug(getClass(), "Connecting to..."+connectURL);
		final HttpResponse response = this.connect(connectURL);
		boolean iCantHandle = false;
		if (null != response) {
			if (200 != response.getStatusLine().getStatusCode()) {
				// return this.getNextRetriever().getMovieDetails(movie);
				iCantHandle = true;
			}
			if (!iCantHandle) {
				try {
					final String responseText = EntityUtils.toString(response
							.getEntity());
					boolean isValid = this.isValidResponse(responseText);
					LoggingUtil.debug(getClass(), "Response is ..."+isValid);

					if (!isValid) {
						iCantHandle = true;
					}
					if(!iCantHandle){
						final String imdbId = this.getIMDBId(responseText);
						LoggingUtil.debug(getClass(), "IMDB ID :::"+imdbId);
						tempMovie = this.getIMDBMetadata(imdbId, movie);
						tempMovie.setImdbId(imdbId);
					}
				} catch (ParseException e) {
					LoggingUtil.error(getClass(), "Unable to get Movie details for movie .."+movie.getName(), e);
				} catch (IOException e) {
					LoggingUtil.error(getClass(), "Unable to get Movie details for movie .."+movie.getName(), e);				}
			}
		}
		if(iCantHandle && null!=this.getNextRetriever()){
			LoggingUtil.debug(getClass(), "Next REtriever is :::"+this.getNextRetriever());
			return this.getNextRetriever().getMovieDetails(movie);
		}
		return tempMovie;
		
	}	
	
	protected HttpResponse connect(final String URL){
		final HttpClient httpClient=new DefaultHttpClient();
		HttpGet httpGet=new HttpGet(URL);
		HttpResponse response=null;
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			LoggingUtil.error(getClass(), "Exception..", e);
			
		} catch (IOException e) {
			LoggingUtil.error(getClass(), "Exception..", e);
		}
		return response;
	}
	
	protected String getSearchText(final String name){
		String title=name;
		final String[] tempArr=title.split(" ");
		if(tempArr.length > 1){
			//title=tempArr[0]+"+"+tempArr[1]+"+"+tempArr[2]+"+"+tempArr[3];
			final StringBuilder builder=new StringBuilder();
			int i=0;
			for(final String temp:tempArr){
				builder.append(temp);
				if(i < tempArr.length-1){
					builder.append("+");
				}
				i++;
			}
			title = builder.toString();
		}
		LoggingUtil.debug(getClass(), "Title to be used ::"+title);
		return title;
	}
	
	protected abstract String getIMDBId(final String responseText);
	
	private static final String OMDB_BY_ID = "http://www.omdbapi.com/?i=";
	
	private Movie getIMDBMetadata(final String imdbId, final Movie movie){
		final HttpResponse response=this.connect(OMDB_BY_ID+imdbId);
		Movie tempMovie=movie;
		if(null!=response && 200 == response.getStatusLine().getStatusCode()){
			try {
				LoggingUtil.debug(getClass(), "Going to get details from IMDB...");
				final String responseText = EntityUtils.toString(response.getEntity());
				LoggingUtil.debug(getClass(), "Going to get details from IMDB..."+responseText);
				tempMovie=getMovieMetadata(responseText, movie);
				
			} catch (ParseException e) {
				LoggingUtil.error(getClass(), "Failed to get Movie metadata by imdb id...", e);
			} catch (IOException e) {
				LoggingUtil.error(getClass(), "Failed to get Movie metadata by imdb id...", e);
			}
		}
		return tempMovie;
	}
	
	private Movie getMovieMetadata(final String responseText,final Movie movie){
		final Movie tempMovie=movie;
		try {
			final JSONObject jsonObject=new JSONObject(responseText);
			if(!jsonObject.has("Error")){
				tempMovie.setGenres(jsonObject.getString("Genre"));
				tempMovie.setDirectors(jsonObject.getString("Director"));
				tempMovie.setActors(jsonObject.getString("Actors"));
				tempMovie.setSynopsis(jsonObject.getString("Plot"));
				tempMovie.setPosterUrl(jsonObject.getString("Poster"));
			}
			
		} catch (JSONException e) {
			LoggingUtil.error(getClass(), "Failed to get Movie metadata by imdb id...", e);
		}
		
		return tempMovie;
	}
	
	

}
