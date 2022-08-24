package com.mite.movie.core.service;

import java.util.List;
import java.util.Set;

import com.mite.movie.database.entity.Director;
import com.mite.movie.database.entity.Movie;

/**
 * Interface that defines the business functionality for {@link Director}
 * 
 * @author Mykhaylo.T
 *
 */
public interface DirectorService {
	
	Director save(Director request);
	
	Director update(Long directorId, Director request);
	
	Director findById(Long directorId);
	
	List<Director> findAll();
	
	Set<Movie> findMoviesByDirectorFullName(String firstname, String lastname);
	
	void deleteById(Long directorId);
	
	void deleteAll();
	
	Director addOrRemoveMovieFromDirector(Long movieId, Long directorId, boolean add);
	

}
