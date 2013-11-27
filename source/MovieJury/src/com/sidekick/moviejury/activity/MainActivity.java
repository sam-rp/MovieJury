package com.sidekick.moviejury.activity;

import java.util.List;

import org.jsoup.Jsoup;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import com.sidekick.moviejury.R;
import com.sidekick.moviejury.api.MovieListingAPI;
import com.sidekick.moviejury.api.MovieListingAPI.MOVIELIST_SOURCE;
import com.sidekick.moviejury.model.Movie;
import com.sidekick.moviejury.util.LoggingUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Jsoup.connect("http://www.google.co.in");
        //final List<Movie> movies = MovieListingAPI.getMovieListingAPI(MOVIELIST_SOURCE.GOOGLE).getMovies();
        //LoggingUtil.debug(this.getClass(), "something "+movies,null);
       // LoggingUtil.debug(this.getClass(), "something "+MovieListingAPI.getMovieListingAPI(MOVIELIST_SOURCE.GOOGLE).getMovies());
        final MovieListingAsyncTask asyncTask=new MovieListingAsyncTask();
        asyncTask.execute("google");
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private class MovieListingAsyncTask extends AsyncTask<String, String, List<Movie>>{

		@Override
		protected List<Movie> doInBackground(String... params) {
			LoggingUtil.debug(this.getClass(), "Running the background process...");
			final List<Movie> movies = MovieListingAPI.getMoviesListing(MOVIELIST_SOURCE.GOOGLE);
			LoggingUtil.debug(this.getClass(), "Movies returned ..."+movies);
			if(null!=movies && !movies.isEmpty()){
				for(final Movie movie:movies){
					LoggingUtil.debug(getClass(),"MOVIE ::"+movie);
					LoggingUtil.debug(getClass(), "NAME :::"+movie.getName());
					LoggingUtil.debug(getClass(), "LANG :::"+movie.getLang());
					LoggingUtil.debug(getClass(), "ACTORS :::"+movie.getActors());
					LoggingUtil.debug(getClass(), "directors :::"+movie.getDirectors());
					LoggingUtil.debug(getClass(), "Poster URL :::"+movie.getPosterUrl());
					LoggingUtil.debug(getClass(), "IMDB ID :::"+movie.getImdbId());
				}
			}
			return movies;
		}

		@Override
		protected void onPostExecute(List<Movie> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			LoggingUtil.debug(this.getClass(),"progressing ...."+values[0]);
		}






    	
    }
    
}
