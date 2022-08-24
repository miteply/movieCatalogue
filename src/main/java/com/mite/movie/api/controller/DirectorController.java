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

import com.mite.movie.api.dto.request.DirectorRequest;
import com.mite.movie.api.dto.response.DirectorResponse;
import com.mite.movie.api.dto.response.MovieResponse;
import com.mite.movie.core.service.DirectorService;
import com.mite.movie.core.util.ObjectConverter;
import com.mite.movie.database.entity.Director;

@RestController
@RequestMapping("/api/v1/directors")
public class DirectorController {
	
	private final DirectorService directorService;
	private final ObjectConverter<Object, Object> objectConverter;
	
	public DirectorController(final DirectorService directorService, ObjectConverter<Object, Object> objectConverter) {
		this.directorService = directorService;
		this.objectConverter = objectConverter;
	}
	
	@PostMapping 
	public ResponseEntity<DirectorResponse> save(@RequestBody DirectorRequest request){
		Director savedEntity = directorService.save(convertToEntity(request));
		
		if (Optional.ofNullable(savedEntity.getMovies()).isPresent())
			removeMovieRecursion(savedEntity);
		
		return new ResponseEntity<>(convertToDto(savedEntity), 
									HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<DirectorResponse> update(@PathVariable("id") Long id, @RequestBody DirectorRequest request ){
		return new ResponseEntity<>(convertToDto(directorService.update(id, convertToEntity(request))), 
									HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DirectorResponse> getById(@PathVariable("id") Long id){
		return new ResponseEntity<>(convertToDto(directorService.findById(id)), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<DirectorResponse>> getAllDirectors(){
		return new ResponseEntity<>(directorService.findAll().stream()
				.map(this::removeMovieRecursion)
																.map(this::convertToDto)
																.collect(Collectors.toList()),HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteDirectorById(@PathVariable("id") Long id){
		directorService.deleteById(id);
		return new ResponseEntity<>("Director deleted successfully!", HttpStatus.OK);
	}
	@DeleteMapping
	public ResponseEntity<String> deleteAllDirectors(){
		directorService.deleteAll();
		return new ResponseEntity<>("All Directors deleted successfully!", HttpStatus.OK);
	}
	
	@PutMapping("{directorId}/movies/{movieId}")
	public ResponseEntity<DirectorResponse> addDirectorToMovie(@PathVariable("directorId") Long directorId,
			@PathVariable ("movieId") Long movieId) {
		Director directorEntity = directorService.addOrRemoveMovieFromDirector(movieId, directorId, true);
		removeMovieRecursion(directorEntity);
		
		return  new ResponseEntity<>(convertToDto(directorEntity),HttpStatus.OK);
	}
	
	@DeleteMapping("{directorId}/movies/{movieId}")
	public ResponseEntity<DirectorResponse> removeDirectorFromMovie(@PathVariable("directorId") Long directorId,
			@PathVariable ("movieId") Long movieId) {
		Director directorEntity = directorService.addOrRemoveMovieFromDirector(movieId, directorId, false);
		removeMovieRecursion(directorEntity);
		
		return  new ResponseEntity<>(convertToDto(directorEntity),HttpStatus.OK);
	}
	
	private Director convertToEntity(DirectorRequest request) {
		return (Director)objectConverter.toEntity(request, Director.class);
	}
	private DirectorResponse convertToDto(Director entity) {
		return (DirectorResponse)objectConverter.toEntity(entity, DirectorResponse.class);
	}	

	private Director removeMovieRecursion(Director entity) {
	
		if (Optional.ofNullable(entity.getMovies()).isPresent())
			return Stream.of(entity).map(director -> {
				director.getMovies().stream().forEach(movie -> movie.setDirectors(null));
				return director;
			}).findFirst().orElse(entity);
		return entity;
}	
}
