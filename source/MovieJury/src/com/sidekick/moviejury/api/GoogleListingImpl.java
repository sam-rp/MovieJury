package com.sidekick.moviejury.api;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sidekick.moviejury.model.Movie;
import com.sidekick.moviejury.util.LoggingUtil;

public class GoogleListingImpl implements IMovieListing {

	private static final String LISTING_URL = "http://www.google.com/movies?near=bangalore,+ka,+ind&sort=1";

	public enum LANGUAGE {
		ENGLISH, HINDI;
	}

	@Override
	public List<Movie> getMovies() {
		List<Movie> movies = new ArrayList<Movie>();
		try {
			LoggingUtil.debug(getClass(), "Entering..");
			Document document = null;

			// final Document document = Jsoup.connect(LISTING_URL).get();
			int start=0;
			while ((document = this.getMoviesDocument(start)) != null) {

				LoggingUtil.debug(getClass(), "after connecting..");

				final Elements elements = document.select("div.movie");
				
				if(null == elements || elements.isEmpty()){
					break;
				}
				
				if (null != elements && !elements.isEmpty()) {
					// LoggingUtil.debug(getClass(), "Eleme "+elements);
					String name = null;
					String langText = null;
					String lang = null;
					boolean doSkip = true;
					for (Element element : elements) {
						try {
							doSkip = true;
							name = element.select("h2 > a").get(0).text();
							// LoggingUtil.debug(getClass(),
							// "Name of he Movie :::: "+name);
							Elements element1 = element.select("div.info");
							int i = 0;
							if (element1.size() > 1) {
								i = 1;
							}
							langText = URLDecoder.decode(element1.get(i).text()
									.split("Cast")[0], "UTF-8");
							// LoggingUtil.debug(getClass(),
							// "LANG text ::::: "+langText.toUpperCase());
							if (langText.toUpperCase().contains(
									LANGUAGE.ENGLISH.name())) {
								lang = LANGUAGE.ENGLISH.name();
								doSkip = false;
							} else if (langText.toUpperCase().contains(
									LANGUAGE.HINDI.name())) {
								lang = LANGUAGE.HINDI.name();
								doSkip = false;
							}
							if (!doSkip) {
								final Movie movie = new Movie();
								movie.setName(name);
								movie.setLang(lang);
								movies.add(movie);
							}

						} catch (Exception e) {
							// deliberately supressing it
						}
					}
				}
				start=start+10;
			}
		} catch (IOException e) {
			LoggingUtil.error(this.getClass(),
					"Unable to connect to google listing URL", e);
			throw new RuntimeException("Unable to connect to google listing", e);
		}
		return movies;
	}

	private Document getMoviesDocument(int i) throws IOException {
		String connectURL = LISTING_URL+"&start="+i;
		LoggingUtil.debug(getClass(), "Google search URL ..."+connectURL);
		return Jsoup.connect(connectURL).get();
	}

}
