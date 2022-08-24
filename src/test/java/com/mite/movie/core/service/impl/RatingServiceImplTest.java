package com.mite.movie.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.mite.movie.core.service.RatingService;
import com.mite.movie.database.entity.Movie;
import com.mite.movie.database.entity.Rating;
import com.mite.movie.database.repository.MovieRepository;
import com.mite.movie.database.repository.RatingRepository;
@RunWith(SpringRunner.class)
@SpringBootTest
class RatingServiceImplTest {
	
	@MockBean
	private RatingRepository ratingRepository;
	@MockBean
	private MovieRepository movieRepository;
	
	@Autowired
	private RatingService ratingService;
	
	private Rating rating1,rating2;
	private Movie matrix;
	private List<Rating> ratings;

	@BeforeEach
	void setUp() {
		rating1 = new Rating();
		rating1.setRatingId(1L);
		rating1.setScore(10);
		rating1.setText("I like it");
		
		rating2 = new Rating();
		rating2.setRatingId(1L);
		rating2.setScore(2);
		rating2.setText("I do not like it");
		
		matrix = new Movie();
		matrix.setMovieId(1L);
		matrix.setTitle("Matrix");
		matrix.setDescription("Desc of Matrix");
		matrix.setReleaseDate(1995);
		
		rating1.setMovie(matrix);
		
		ratings = new ArrayList<>();
		ratings.add(rating1);
		ratings.add(rating2);
		
		matrix.setRatings(ratings);
	}
	
	@DisplayName("Test to update rating")
	@Test
	void should_update_rating() {
		
		Mockito.when(ratingRepository.findById(anyLong())).thenReturn(Optional.of(rating1));
		
		rating1.setScore(20);
		
		Mockito.when(ratingRepository.save(any(Rating.class))).thenReturn(rating1);
		
		Rating updated = ratingService.update(1L, rating1);
		
		assertEquals(1L, updated.getRatingId());
		assertEquals(20, updated.getScore());
		
		verify(ratingRepository, times(1)).findById(anyLong());
		verify(ratingRepository, times(1)).save(any(Rating.class));

	}
	
	@DisplayName("Test to find rating with id")
	@Test
	void should_find_rating_with_id() {
		
		Mockito.when(ratingRepository.findById(anyLong())).thenReturn(Optional.of(rating1));
		
		Rating entity = ratingService.findById(1L);
		
		assertEquals(1L, entity.getRatingId());
		assertEquals("I like it", entity.getText());
		assertEquals(10, entity.getScore());
		assertEquals(matrix, entity.getMovie());
		
		
		verify(ratingRepository, times(1)).findById(anyLong());
	}
	
	
	@DisplayName("Test to a new rating to an excisting movie")
	@Test
	void should_add_rating_to_movie() {
		
		Mockito.when(movieRepository.findById(anyLong())).thenReturn(Optional.of(matrix));
		
		Mockito.when(ratingRepository.save(any(Rating.class))).thenReturn(rating1);
		
		Rating entity = ratingService.addRatingToMovie(1L, new Rating());
		
		assertEquals(1L, entity.getRatingId());
		assertEquals("I like it", entity.getText());
		assertEquals(10, entity.getScore());
		assertEquals(matrix, entity.getMovie());
		
		verify(movieRepository, times(1)).findById(anyLong());
		verify(ratingRepository, times(1)).save(any(Rating.class));
	}
	
	@DisplayName("Test to throw exception when rating id is not found")
	@Test
	void should_throw_exception_when_rating_is_not_found() {
		
		Mockito.when(ratingRepository.findById(anyLong()))
			   .thenThrow(new NotFoundException(Rating.class,5L));
	
		assertThrows(NotFoundException.class, () -> {
			 ratingService.findById(5L);
	    });
		
		verify(ratingRepository, times(1)).findById(anyLong());
	}
	
	@DisplayName("Test to delete rating by id")
	@Test
	void should_delete_movie_by_id() {
		Mockito.doNothing().when(movieRepository).deleteById(anyLong());
		Mockito.when(ratingRepository.findById(anyLong())).thenReturn(Optional.of(rating1));
		
		ratingService.deleteById(1L);
		
		verify(ratingRepository, times(1)).deleteById(anyLong());;
	}
}
