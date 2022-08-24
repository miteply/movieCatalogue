package com.mite.movie.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mite.movie.api.dto.request.MovieRequest;
import com.mite.movie.api.dto.response.MovieResponse;
import com.mite.movie.core.service.DirectorService;
import com.mite.movie.core.service.MovieService;
import com.mite.movie.core.util.ObjectConverter;
import com.mite.movie.database.entity.Movie;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

	private final MovieService movieService;
	private final DirectorService directorService;
	private final ObjectConverter<Object, Object> objectConverter;

	public MovieController(MovieService movieRepository, DirectorService directorService, ObjectConverter<Object, Object> objectConverter) {

		this.movieService = movieRepository;
		this.directorService = directorService;
		this.objectConverter = objectConverter;
	}
	
	@PostMapping 
	public ResponseEntity<MovieResponse> save(@RequestBody MovieRequest request){
		Movie savedEntity = movieService.save(convertToEntity(request));
		removeDirectorRecursion(savedEntity);
		
		return new ResponseEntity<>(convertToDto(savedEntity), 
									HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<MovieResponse> update(@PathVariable("id") Long id, @RequestBody MovieRequest request ){
		return new ResponseEntity<>(convertToDto(movieService.update(id, convertToEntity(request))), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<MovieResponse> getById(@PathVariable("id") Long id){
		return new ResponseEntity<>(convertToDto(removeDirectorRecursion(movieService.findById(id))), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<?> getAllOrByTitle(@RequestParam (required = false) String title){
		return !Optional.ofNullable(title).isPresent() ?  
				new ResponseEntity<>(movieService.findAll()
						.stream()
							.map(this::removeDirectorRecursion)
							.map(this::convertToDto)
							.collect(Collectors.toList()), HttpStatus.OK)  :  
					new ResponseEntity<>(convertToDto(movieService.findByTitle(title)), HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteById(@PathVariable("id") Long id){
		movieService.deleteById(id);
		return new ResponseEntity<>("Movie deleted successfully!", HttpStatus.OK);
	}
	
	@DeleteMapping
	public ResponseEntity<String> deleteAll(){
		movieService.deleteAll();
		return new ResponseEntity<>("All Movies deleted successfully!", HttpStatus.OK);
	}
	
	@GetMapping("/totalRatings/{numOfRatings}")
	public ResponseEntity<List<MovieResponse>> getMoviesWithTotalRatingsGreaterThan(@PathVariable("numOfRatings") int numOfRatings) {
		return new ResponseEntity<>(movieService.getMoviesWithTotalRatingsGreaterThan(numOfRatings)
														.stream()
														.map(this::convertToDto)
														.collect(Collectors.toList()),HttpStatus.OK);
	}
	
	@GetMapping("/greaterScore/{score}")
	public ResponseEntity<List<MovieResponse>> getMoviesWithScoreGreaterThan(@PathVariable("score") int score){
		return new ResponseEntity<>(movieService.getMoviesWithScoreGreaterThan(score)
														.stream()
														.map(this::convertToDto)
														.collect(Collectors.toList()),HttpStatus.OK);
	}
	
	@GetMapping("/directors")
	public ResponseEntity<List<MovieResponse>> getMoviesByDirectorFullName(
			@RequestParam(required = true) String firstname, @RequestParam(required = true) String lastname) {
		
		List<MovieResponse> collect = directorService.findMoviesByDirectorFullName(firstname, lastname).stream()
		.map(this::removeDirectorRecursion)
		.map(this::convertToDto).collect(Collectors.toList());
		return new ResponseEntity<>(collect, HttpStatus.OK);
	}
	
	
	
	
	private Movie convertToEntity(MovieRequest request) {
		return (Movie)objectConverter.toEntity(request, Movie.class);
	}
	private MovieResponse convertToDto(Movie entity) {
		return (MovieResponse)objectConverter.toEntity(entity, MovieResponse.class);
	}

	private Movie removeDirectorRecursion(Movie entity) {
		if (Optional.ofNullable(entity.getDirectors()).isPresent())
			return Stream.of(entity).map(movie -> {
				movie.getDirectors().stream().forEach(director -> director.setMovies(null));
				return movie;
			}).findFirst().orElse(entity);
		return entity;
	}
}
