package com.mite.movie.database.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mite.movie.database.entity.Director;
import com.mite.movie.database.entity.Movie;

@Repository
public interface DirectorRepository extends JpaRepository <Director, Long> {
	
	Optional<Director> findByFirstnameAndLastname(String firstname, String lastname);
}
