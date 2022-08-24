package com.mite.movie.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mite.movie.api.dto.request.RatingRequest;
import com.mite.movie.api.dto.response.RatingResponse;
import com.mite.movie.core.service.RatingService;
import com.mite.movie.core.util.ObjectConverter;
import com.mite.movie.database.entity.Rating;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController {
	
	private final RatingService ratingService;
	private final ObjectConverter<Object, Object> objectConverter;
	
	public RatingController(RatingService ratingService, ObjectConverter<Object, Object> objectConverter) {
		
		this.ratingService = ratingService;
		this.objectConverter = objectConverter;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RatingResponse> getById(@PathVariable ("id") Long ratingId){
		return new ResponseEntity<>(convertToDto(ratingService.findById(ratingId)),HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<RatingResponse> update(@PathVariable("id") Long ratingId, @RequestBody RatingRequest request ){
		return new ResponseEntity<>(convertToDto(ratingService.update(ratingId, convertToEntity(request))), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteRating(@PathVariable("id") Long id) {
		ratingService.deleteById(id);
		return new ResponseEntity<>("Rating deleted successfully!", HttpStatus.OK);
	}
	
	@PutMapping("/movies/{movieId}")
	public ResponseEntity<RatingResponse> addRatingToMovie(@PathVariable(value="movieId") Long movieId, @RequestBody RatingRequest request) {
		return new ResponseEntity<>(convertToDto(ratingService.addRatingToMovie(movieId, convertToEntity(request))), HttpStatus.OK);		
	}
	
	private Rating convertToEntity(RatingRequest request) {
		return (Rating)objectConverter.toEntity(request, Rating.class);
	}
	private RatingResponse convertToDto(Rating entity) {
		return (RatingResponse)objectConverter.toEntity(entity, RatingResponse.class);
	}

}
