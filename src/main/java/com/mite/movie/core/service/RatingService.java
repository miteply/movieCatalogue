package com.mite.movie.core.service;

import java.util.List;

import com.mite.movie.database.entity.Rating;

/**
 * Interface that defines the business functionality for {@link Rating}
 * @author Mykhaylo.T
 *
 */
public interface RatingService {
	
	Rating save (Rating rating);
	
	Rating update(Long ratingId, Rating request);
	
	Rating findById(Long ratingId);
	
	void deleteById(Long ratingId);
	
	Rating addRatingToMovie (Long movieId, Rating request);
	
	List<Rating> findRatingsWithScoreGreaterThan(int score);	

}
