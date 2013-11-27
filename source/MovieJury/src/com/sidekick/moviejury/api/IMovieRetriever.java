package com.sidekick.moviejury.api;

import com.sidekick.moviejury.model.Movie;

public interface IMovieRetriever {
	public Movie getMovieDetails(final Movie movie);
	public void setNextRetriever(IMovieRetriever retriever);
}
