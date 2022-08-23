package com.mite.movie.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mite.movie.database.entity.Rating;

/**
 * 
 * Class that uses JPA repository to store the data of a @Rating 
 * in a relational database.
 * 
 * @author Mykhaylo.T
 */
public interface RatingRepository extends JpaRepository<Rating, Long>{
	
	List<Rating> findByScoreGreaterThan(int score);
}
