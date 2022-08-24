package com.mite.movie.core.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.mite.movie.core.exception.NotFoundException;
import com.mite.movie.core.service.MovieService;
import com.mite.movie.core.service.RatingService;
import com.mite.movie.database.entity.Rating;
import com.mite.movie.database.repository.RatingRepository;

/**
 * Class that implements {@link RatingService} interface, to handle the logic 
 * of inserting, creating, retrieving and deleting the data 
 * of {@link Rating} by using {@link RatingRepository} . 
 * 
 * @author Mykhaylo.T
 *
 */
@Service
public class RatingServiceImpl implements RatingService {

	private static final Logger LOG = LoggerFactory.getLogger(RatingServiceImpl.class);
	
	private final RatingRepository ratingRepository;
	private final MovieService movieService;
	
	public RatingServiceImpl(RatingRepository ratingRepository,@Lazy MovieService movieService) {

		this.ratingRepository = ratingRepository;
		this.movieService = movieService;
	}
	
	@Override
	public Rating save(Rating request) {
		return ratingRepository.save(request);
	}
	
	@Override
	public Rating update(Long ratingId, Rating request) {
		return save(updateRatingWithNewValues(findById(ratingId),request));
	}

	@Override
	public Rating findById(Long ratingId) {
		return ratingRepository.findById(ratingId)
				.orElseThrow(() -> new NotFoundException(Rating.class, ratingId));
	}

	@Override
	public void deleteById(Long ratingId) {
		findById(ratingId);
		ratingRepository.deleteById(ratingId);	
	}

	@Override
	public Rating addRatingToMovie(Long movieId, Rating request) {
		request.setMovie(movieService.findById(movieId));
		return save(request);
	}

	@Override
	public List<Rating> findRatingsWithScoreGreaterThan(int score) {
		LOG.info("Finding ratings with score greater that: {}", score);
		return ratingRepository.findByScoreGreaterThan(score);
	}
	
	private Rating updateRatingWithNewValues(Rating oldRating, Rating newRating) {
		if(newRating.getText() != null && !newRating.getText().isEmpty())
			oldRating.setText(newRating.getText());
		if(Optional.ofNullable(newRating.getScore()).orElse(oldRating.getScore()) != 0)
			oldRating.setScore(newRating.getScore());
		
		return oldRating;
	}
}
