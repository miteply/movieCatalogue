package com.mite.movie.core.service;

import java.util.List;
import java.util.Set;

import com.mite.movie.database.entity.Movie;

/**
 * Interface that defines the business functionality for {@link Movie}
 * 
 * @author Mykhaylo.T
 *
 */
public interface MovieService {

	Movie save(Movie movie);

	Movie update(Long movieId, Movie request);

	Movie findById(Long movieId);

	Movie findByTitle(String movieTitle);

	List<Movie> findAll();

	void deleteById(Long movieId);

	void deleteAll();

	List<Movie> getMoviesWithTotalRatingsGreaterThan(int numOfRatings);

	Set<Movie> getMoviesWithScoreGreaterThan(int score);
}
