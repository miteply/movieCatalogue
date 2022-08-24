package com.mite.movie.api.controller;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mite.movie.api.dto.request.MovieRequest;
import com.mite.movie.api.dto.request.RatingRequest;
import com.mite.movie.api.dto.response.DirectorResponse;
import com.mite.movie.api.dto.response.MovieResponse;
import com.mite.movie.api.dto.response.RatingResponse;
import com.mite.movie.core.exception.NotFoundException;
import com.mite.movie.core.service.DirectorService;
import com.mite.movie.core.service.MovieService;
import com.mite.movie.core.util.ObjectConverter;
import com.mite.movie.database.entity.Movie;
import com.mite.movie.database.entity.Rating;

@RunWith(SpringRunner.class)
@WebMvcTest(MovieController.class)
@ContextConfiguration(classes = {MovieController.class,ObjectConverter.class,ModelMapper.class,ExceptionHandlerController.class})
class MovieControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	MovieService movieService;
	@MockBean
	DirectorService directorService;
	
	private JacksonTester<MovieResponse> jsonMovieResponse;
	private JacksonTester<MovieRequest> jsonMovieRequest;

	private Movie titanic, matrix;
	private Rating rating1, rating2, rating3;
	private List<Movie> movies;
	
	private MovieRequest actualMovieRequest;
	private MovieResponse actualMovieResponse;
	
	@BeforeEach
	void setUp() {
		JacksonTester.initFields(this, new ObjectMapper());
		
		actualMovieRequest = new MovieRequest();
		actualMovieRequest.setTitle("Matrix");
		actualMovieRequest.setRatings(new ArrayList<RatingRequest>());
		
		
		actualMovieResponse = new MovieResponse();
		actualMovieResponse.setMovieId(1L);
		actualMovieResponse.setTitle("Titanic");
		actualMovieResponse.setDescription("Desc of Titanic");
		actualMovieResponse.setReleaseDate(1919);
		List<RatingResponse> ratingResponses = new ArrayList<RatingResponse>();
		RatingResponse ratingResponse1= new RatingResponse();
		ratingResponse1.setRatingId(1L);
		ratingResponse1.setText("I like it");
		ratingResponse1.setScore(10);
		ratingResponse1.setMovieId(1L);
		RatingResponse ratingResponse2= new RatingResponse();
		ratingResponse2.setRatingId(2L);
		ratingResponse2.setText("I do not like it");
		ratingResponse2.setScore(2);
		ratingResponse2.setMovieId(1L);
		ratingResponses.add(ratingResponse1);
		ratingResponses.add(ratingResponse2);
		actualMovieResponse.setRatings(ratingResponses);
		actualMovieResponse.setDirectors(new HashSet<DirectorResponse>());
		
		titanic = new Movie();
		titanic.setMovieId(1L);
		titanic.setTitle("Titanic");
		titanic.setDescription("Desc of Titanic");
		titanic.setReleaseDate(1919);
		
		matrix = new Movie();
		matrix.setMovieId(2L);
		matrix.setTitle("Matrix");
		matrix.setDescription("Desc of Matrix");
		matrix.setReleaseDate(1995);
		
		rating1 = new Rating();
		rating1.setRatingId(1L);
		rating1.setScore(10);
		rating1.setText("I like it");
		
		rating2 = new Rating();
		rating2.setRatingId(2L);
		rating2.setScore(2);
		rating2.setText("I do not like it");
		
		rating3 = new Rating();
		rating3.setRatingId(3L);
		rating3.setScore(40);
		rating3.setText("Amazing");
		
		titanic.getRatings().add(rating1);
		titanic.getRatings().add(rating2);
		rating1.setMovie(titanic);
		rating2.setMovie(titanic);
		
		matrix.getRatings().add(rating3);
		rating3.setMovie(matrix);
		
		movies = new ArrayList<>();
		movies.add(titanic);
		movies.add(matrix);
	}
	
	@DisplayName("Test for find a list of movies")
	@Test
	void get_all_movies_returns_list_of_movies() throws Exception {
		
		Mockito.when(movieService.findAll()).thenReturn(movies);
		
		mockMvc.perform(get("/api/v1/movies")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].movieId",is(1)))
				.andExpect(jsonPath("$[0].title",is("Titanic")))
				.andExpect(jsonPath("$[1].movieId",is(2)))
				.andExpect(jsonPath("$[1].title",is("Matrix")));
		
		
	}	
	@DisplayName("Test for find movie by id operation")
	@Test
	void retrieve_movie_by_id_when_exists() throws Exception {
		
		when(movieService.findById(1L)).thenReturn(titanic);
		
		MockHttpServletResponse expected = mockMvc.perform(get("/api/v1/movies/{id}",1)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.movieId").value(1))
				.andExpect(jsonPath("$.title").value("Titanic"))
				.andReturn().getResponse();
		

		assertEquals(expected.getContentAsString(),
				jsonMovieResponse.write(actualMovieResponse).getJson());
		
		verify(movieService, times(1)).findById(anyLong());
	}

	@DisplayName("Test to throw excetion when movie id is not found")
	@Test
	void throw_exception_when_movie_id_not_found() throws Exception {
		
		when(movieService.findById(2L)).thenThrow(new NotFoundException(Movie.class,2L));
		
		MvcResult expected = mockMvc.perform(get("/api/v1/movies/2")
				.accept(MediaType.APPLICATION_JSON))
				//.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Object [Movie] with id [2] not found"))
				.andReturn();
		
		assertTrue(expected.getResolvedException() instanceof NotFoundException);
		
		verify(movieService, times(1)).findById(anyLong());
	}
	
	@DisplayName("Test for find movie by name ")
	@Test
	void should_retrieve_movie_by_name_when_exists() throws Exception {
		
		when(movieService.findByTitle(anyString())).thenReturn(titanic);
		
		MockHttpServletResponse expected = mockMvc.perform(get("/api/v1/movies?title=Matrix")
				.accept(MediaType.APPLICATION_JSON))
				//.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse();
		
		assertEquals(expected.getContentAsString(),
				jsonMovieResponse.write(actualMovieResponse).getJson());
		
		verify(movieService, times(1)).findByTitle(anyString());
	}
	
	@DisplayName("Test for create a movie ")
	@Test
	void should_create_a_new_movie() throws Exception {
		
		when(movieService.save(any(Movie.class))).thenReturn(titanic);
		
		MockHttpServletResponse expected = mockMvc.perform(post("/api/v1/movies")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMovieRequest.write(actualMovieRequest).getJson())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andReturn().getResponse();
		
		assertEquals(expected.getContentAsString(),
				jsonMovieResponse.write(actualMovieResponse).getJson());
		
		verify(movieService, times(1)).save(any(Movie.class));
	}
	
	@DisplayName("Test for find movies with total ratings greater than ")
	@Test
	void should_retrieve_a_movie_with_total_ratings_greater_than() throws Exception {
			
		when(movieService.getMoviesWithTotalRatingsGreaterThan(1)).thenReturn(movies);
		
		mockMvc.perform(get("/api/v1/movies/totalRatings/1")
				.contentType(MediaType.APPLICATION_JSON)
				
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].movieId",is(1)))
				.andExpect(jsonPath("$[0].title",is("Titanic")))
				.andExpect(jsonPath("$[0].ratings",hasSize(2)));
		
		verify(movieService, times(1)).getMoviesWithTotalRatingsGreaterThan(1);
	}
	
	@DisplayName("Test for find movies with total score greater than ")
	@Test
	void should_retrieve_a_movie_list_with_score_greater_than() throws Exception {

		when(movieService.getMoviesWithScoreGreaterThan(40))
		.thenReturn(new HashSet<>(movies));
		
		mockMvc.perform(get("/api/v1/movies/greaterScore/40")
				.contentType(MediaType.APPLICATION_JSON)
				
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].movieId",is(1)))
				.andExpect(jsonPath("$[0].title",is("Titanic")))
				.andExpect(jsonPath("$[0].ratings",hasSize(2)))
				.andExpect(jsonPath("$[1].movieId",is(2)))
				.andExpect(jsonPath("$[1].title",is("Matrix")))
				.andExpect(jsonPath("$[1].ratings",hasSize(1)));
		
		 verify(movieService, times(1)).getMoviesWithScoreGreaterThan(40);
	}
	
	@DisplayName("Test for delete movie by id ")
	@Test
	void should_delete_movie_by_id() throws Exception {
		
		doNothing().when(movieService).deleteById(anyLong());
	
		
		mockMvc.perform(delete("/api/v1/movies/1")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		
		verify(movieService, times(1)).deleteById(anyLong());
	}
	
	@DisplayName("Test for update movie by id operation")
	@Test
	void should_update_movie_by_id() throws Exception {
		Movie updatedEntity = new Movie();
		updatedEntity.setMovieId(1L);
		updatedEntity.setTitle("Matrix 2");
		
		when(movieService.update(anyLong(),any( Movie.class))).thenReturn(updatedEntity);
	
		
		mockMvc.perform(put("/api/v1/movies/{id}",1)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonMovieRequest.write(actualMovieRequest).getJson())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.movieId",is(1)))
				.andExpect(jsonPath("$.title", is("Matrix 2")));
		
		verify(movieService, times(1)).update(anyLong(),any( Movie.class));
	}
	
}
