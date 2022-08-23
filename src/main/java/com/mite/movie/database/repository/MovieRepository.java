package com.mite.movie.database.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mite.movie.database.entity.Movie;
/**
 * 
 * Class that uses JPA repository to store the data of a @Movie 
 * in a relational database.
 * 
 * @author Mykhaylo.T
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

	Optional<Movie> findByTitle(String title);
}
