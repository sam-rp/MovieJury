package com.sidekick.moviejury.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sidekick.moviejury.model.Movie;
import com.sidekick.moviejury.util.LoggingUtil;

public class MovieListingAPI {

	public enum MOVIELIST_SOURCE {
		GOOGLE;
	}

	private static String[] detailsAPI = new String[] {
			"com.sidekick.moviejury.api.MyMovieAPIRetriever",
			"com.sidekick.moviejury.api.OMDBAPIRetriever",
			"com.sidekick.moviejury.api.IMDBAPIRetriever" };

	private MovieListingAPI() {
	}

	private static Map<MOVIELIST_SOURCE, IMovieListing> sourceMap = new HashMap<MovieListingAPI.MOVIELIST_SOURCE, IMovieListing>();
	static {
		sourceMap.put(MOVIELIST_SOURCE.GOOGLE, new GoogleListingImpl());
	}

	public static IMovieListing getMovieListingAPI(
			final MOVIELIST_SOURCE listSource) {
		return sourceMap.get(listSource);
	}

	public static List<Movie> getMoviesListing(final MOVIELIST_SOURCE listSource) {
		final List<Movie> movies = getMovieListingAPI(listSource).getMovies();
		List<Movie> finalMovies=null;
		if (null != movies && !movies.isEmpty()) {
			LoggingUtil.debug(MovieListingAPI.class, "No of movies from GOOGLE :::"+movies.size());
			finalMovies=new ArrayList<Movie>();
			final MovieDetailsProcessor processor = new MovieDetailsProcessor();
			int i=0;
			IMovieRetriever firstRetriever=null;
			for (final String api : detailsAPI) {
				try {
					IMovieRetriever movieAPI = (IMovieRetriever) Class.forName(
							api).newInstance();
					if(i==0){
						firstRetriever=movieAPI;
					}
					processor.setMovieRetriever(movieAPI);
				} catch (InstantiationException e) {
					LoggingUtil.error(
							MovieListingAPI.class,
							"Unable to retrieve the movie details ", e);
				} catch (IllegalAccessException e) {
					LoggingUtil.error(
							MovieListingAPI.class,
							"Unable to retrieve the movie details", e);
				} catch (ClassNotFoundException e) {
					LoggingUtil.error(
							MovieListingAPI.class,
							"Unable to retrieve the movie details ", e);
				}
				i++;
			}
			for (final Movie movie : movies) {
				Movie tempMovie=null;
				if (firstRetriever != null) {
					tempMovie = firstRetriever.getMovieDetails(movie);
					if(null!=tempMovie){
						finalMovies.add(tempMovie);
					}
				}
			}

		}
		LoggingUtil.debug(MovieListingAPI.class, "No of movies from METADATA "+finalMovies.size());
		return finalMovies;
	}


}
