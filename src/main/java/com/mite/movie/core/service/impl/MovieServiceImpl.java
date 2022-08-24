package com.mite.movie.core.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mite.movie.core.exception.NotFoundException;
import com.mite.movie.core.service.MovieService;
import com.mite.movie.core.service.RatingService;
import com.mite.movie.database.entity.Movie;
import com.mite.movie.database.entity.Rating;
import com.mite.movie.database.repository.MovieRepository;
/**
 * Class that implements {@link MovieService} interface, to handle the logic 
 * of inserting, creating, retrieving and deleting the data 
 * of {@link Movie} by using {@link MovieRepository} . 
 * 
 * @author Mykhaylo.T
 *
 */
@Service
public class MovieServiceImpl implements MovieService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MovieServiceImpl.class);

	private final MovieRepository movieRepository;
	private final RatingService ratingService;
	
	public MovieServiceImpl(MovieRepository movieRepository, RatingService ratingService) {

		this.movieRepository = movieRepository;
		this.ratingService = ratingService;
	}
	
	@Override
	@Transactional
	public Movie save(Movie movie) {
		LOG.info("Saving movie with title: {}", movie.getTitle());
		doesTitleExists(movie.getTitle());
		return movieRepository.save(movie);
	}

	@Override
	public Movie update(Long movieId, Movie request) {
		LOG.info("Updating movie with id: {}", movieId);
		return movieRepository.save(updateMovieWithNewValues(findById(movieId), request));
	}

	@Override
	public Movie findById(Long movieId) {
		LOG.info("Finding movie with id: {}", movieId);
		return movieRepository.findById(movieId)
				.orElseThrow(() -> new NotFoundException(Movie.class, movieId));
	}

	@Override
	public Movie findByTitle(String movieTitle) {
		LOG.info("Finding movie with title: {}", movieTitle);
		return movieRepository.findByTitle(movieTitle)
				.orElseThrow(() -> new NotFoundException(Movie.class, movieTitle, ""));
	}

	@Override
	public List<Movie> findAll() {
		LOG.info("Finding all movies");
		return movieRepository.findAll();
	}

	@Override
	public void deleteById(Long movieId) {
		LOG.info("Deleting movie with id: {}", movieId);
		findById(movieId);
		movieRepository.deleteById(movieId);
	}

	@Override
	public void deleteAll() {
		LOG.info("Deleting all movies.");
		movieRepository.deleteAll();
	}

	@Override
	public List<Movie> getMoviesWithTotalRatingsGreaterThan(int numOfRatings) {
		LOG.info("Getting movies with total ratings greater than : {}", numOfRatings);
		return findAll()
				.stream()
	 			.filter(movie-> movie.getRatings().size() > numOfRatings)
	 			.collect(Collectors.toList());
	}

	@Override
	public Set<Movie> getMoviesWithScoreGreaterThan(int score) {
		LOG.info("Getting movies with score greater than : {}", score);
		return ratingService.findRatingsWithScoreGreaterThan(score)
				.stream()
				.map(Rating::getMovie)
				.map(movieEntity -> {
						movieEntity.setRatings(null);
						return movieEntity; })
				.collect(Collectors.toSet());
	}
	
	private Movie updateMovieWithNewValues(Movie oldMovie, Movie newMovie) {
		if(newMovie.getTitle()!= null && !newMovie.getTitle().isEmpty())
			oldMovie.setTitle(newMovie.getTitle());
		if(newMovie.getDescription()!= null && !newMovie.getDescription().isEmpty())	
			oldMovie.setDescription(newMovie.getDescription());
		if(Optional.ofNullable(newMovie.getReleaseDate()).orElse(oldMovie.getReleaseDate()) != 0)	
			oldMovie.setReleaseDate(newMovie.getReleaseDate());
		
		return oldMovie;
	}
	
	private void doesTitleExists (String title ) {
		if (movieRepository.findByTitle(title).isPresent()) 
			throw new NotFoundException(Movie.class, title, "Object [%s] with title [%s] already exists");
	}
}
