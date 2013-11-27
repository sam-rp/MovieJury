package com.sidekick.moviejury.api;

public final class MovieDetailsProcessor {
	
	private IMovieRetriever retriever=null;
	
	public void setMovieRetriever(final IMovieRetriever retriever){
		
		if(null!=this.retriever){
			this.retriever.setNextRetriever(retriever);
		}
		this.retriever=retriever;		
		
	}
	
}
