package com.mite.movie.core.service.impl;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.mite.movie.core.exception.NotFoundException;
import com.mite.movie.core.service.MovieService;
import com.mite.movie.database.entity.Movie;
import com.mite.movie.database.entity.Rating;
import com.mite.movie.database.repository.MovieRepository;
import com.mite.movie.database.repository.RatingRepository;
@RunWith(SpringRunner.class)
@SpringBootTest
class MovieServiceImplTest {
	
	@MockBean
	private MovieRepository movieRepository;
	@MockBean
	private RatingRepository ratingRepository;
	
	@Autowired
	private MovieService movieService;
	
	private Movie titanic, matrix;
	private Rating rating1, rating2, rating3;
	private List<Movie> movies;

	

	@BeforeEach
	void setUp() {
		titanic = new Movie();
		titanic.setMovieId(1L);
		titanic.setTitle("Titanic");
		titanic.setDescription("Desc of Titanic");
		titanic.setReleaseDate(1919);
		
		matrix = new Movie();
		matrix.setMovieId(1L);
		matrix.setTitle("Matrix");
		matrix.setDescription("Desc of Matrix");
		matrix.setReleaseDate(1995);
		
		rating1 = new Rating();
		rating1.setRatingId(1L);
		rating1.setScore(10);
		rating1.setText("I like it");
		
		rating2 = new Rating();
		rating2.setRatingId(1L);
		rating2.setScore(2);
		rating2.setText("I do not like it");
		
		rating3 = new Rating();
		rating3.setRatingId(1L);
		rating3.setScore(40);
		rating3.setText("Amazing");
		
		titanic.getRatings().add(rating1);
		titanic.getRatings().add(rating2);
		rating1.setMovie(titanic);
		rating2.setMovie(titanic);
		
		matrix.getRatings().add(rating3);
		
		movies = new ArrayList<>();
		movies.add(titanic);
		movies.add(matrix);
	}
	
	@DisplayName("Test to save movie")
	@Test
	void should_save_movie() {
		
		Mockito.when(movieRepository.save(any(Movie.class))).thenReturn(titanic);
		
		Movie entity = movieService.save(titanic);
		
		assertEquals(1L, entity.getMovieId());
		assertEquals("Titanic", entity.getTitle());
		
		verify(movieRepository, times(1)).save(any(Movie.class));
	}
	
	@DisplayName("Test to save movie with rating")
	@Test
	void should_save_movie_with_rating() {
		titanic.getRatings().add(rating1);
		
		Mockito.when(movieRepository.save(any(Movie.class))).thenReturn(titanic);
		
		Movie entity = movieService.save(titanic);
		
		assertEquals(1L, entity.getMovieId());
		assertEquals("Titanic", entity.getTitle());
		
		assertEquals(1L, entity.getRatings().get(0).getRatingId());
		assertEquals(10, entity.getRatings().get(0).getScore());
		assertEquals("I like it", entity.getRatings().get(0).getText());
		
		verify(movieRepository, times(1)).save(any(Movie.class));
	}
	
	@DisplayName("Test to update movie")
	@Test
	void should_update_movie() {
		
		Mockito.when(movieRepository.findById(anyLong())).thenReturn(Optional.of(titanic));
		
		titanic.setTitle("Titanic 2");
		titanic.setReleaseDate(2005);
		
		Mockito.when(movieRepository.save(any(Movie.class))).thenReturn(titanic);
		
		Movie entity = movieService.update(1L,titanic);
		assertEquals(1L, entity.getMovieId());
		assertEquals("Titanic 2", entity.getTitle());
		assertEquals(2005, entity.getReleaseDate());
		
		verify(movieRepository, times(1)).findById(anyLong());
		verify(movieRepository, times(1)).save(any(Movie.class));
	}
	
	@DisplayName("Test to find movie with id")
	@Test
	void should_find_movie_with_id() {
		
		Mockito.when(movieRepository.findById(anyLong())).thenReturn(Optional.of(titanic));
		
		Movie entity = movieService.findById(1L);
		
		assertEquals(1L, entity.getMovieId());
		assertEquals("Titanic", entity.getTitle());
		
		verify(movieRepository, times(1)).findById(anyLong());
	}
	
	@DisplayName("Test to throw exception when movie id is not found")
	@Test
	void should_throw_exception_when_movie_is_not_found() {
		
		Mockito.when(movieRepository.findById(anyLong()))
			   .thenThrow(new NotFoundException(Movie.class,5L));
	
		assertThrows(NotFoundException.class, () -> {
			 movieService.findById(5L);
	    });
		
		verify(movieRepository, times(1)).findById(anyLong());
	}
	
	@DisplayName("Test to find all movies")
	@Test
	void should_return_list_of_movies() {
		
		Mockito.when(movieRepository.findAll()).thenReturn(movies);
		
		List<Movie> foundMovies = movieService.findAll();
		
		assertNotNull(foundMovies);
		assertEquals(2, foundMovies.size());
		
		verify(movieRepository, times(1)).findAll();
	}
	
	@DisplayName("Test to delete movie by id")
	@Test
	void should_delete_movie_by_id() {
		
		movieService.deleteById(1L);
		
		verify(movieRepository, times(1)).deleteById(anyLong());;
	}
	
	@DisplayName("Test to find movies with score greater than")
	@Test
	void should_fine_movies_with_score_greater_than() {
		
		Movie saw = new Movie();
		saw.setMovieId(4L);
		saw.setTitle("The Saw");
		saw.setDescription("Desc SAW");
		Rating rating = new Rating();
		rating.setRatingId(6L);
		rating.setScore(50);
		rating.setText("Wonderfull");
		rating.setMovie(saw);
		saw.getRatings().add(rating);
		movies.add(saw);
		Mockito.when(ratingRepository.findByScoreGreaterThan(anyInt())).thenReturn(saw.getRatings());
		
		Set<Movie> ratingsHighestScoreSet = movieService.getMoviesWithScoreGreaterThan(40);
		assertNotNull(ratingsHighestScoreSet);
		assertEquals(1, ratingsHighestScoreSet.size());
		
		verify(ratingRepository, times(1)).findByScoreGreaterThan(anyInt());
	}
	
	
	@DisplayName("Test to find movies with total ratings greater than")
	@Test
	void should_find_movies_with_total_ratings_greater_than() {
		
		Movie saw = new Movie();
		saw.setMovieId(4L);
		saw.setTitle("The Saw");
		saw.setDescription("Desc SAW");
		Rating rating = new Rating();
		rating.setRatingId(6L);
		rating.setScore(50);
		rating.setText("Wonderfull");
		rating.setMovie(saw);
		saw.getRatings().add(rating);
		movies.add(saw);
		
		Mockito.when(movieRepository.findAll()).thenReturn(movies);
		
		List<Movie> moviesWithTotalRatingsGreaterThan = movieService.getMoviesWithTotalRatingsGreaterThan(1);
		
		assertNotNull(moviesWithTotalRatingsGreaterThan);
		assertEquals(1, moviesWithTotalRatingsGreaterThan.size());
		assertEquals("Titanic", moviesWithTotalRatingsGreaterThan.get(0).getTitle());
		
	}
}
