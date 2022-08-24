package com.mite.movie.core.service.impl;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mite.movie.core.exception.NotFoundException;
import com.mite.movie.core.service.DirectorService;
import com.mite.movie.core.service.MovieService;
import com.mite.movie.database.entity.Director;
import com.mite.movie.database.entity.Movie;
import com.mite.movie.database.repository.DirectorRepository;
import com.mite.movie.database.repository.MovieRepository;
/**
 * Class that implements {@link DirectorService} interface, to handle the logic 
 * of inserting, creating, retrieving and deleting the data 
 * of {@link Director} by using {@link DirectorRepository} . 
 * 
 * @author Mykhaylo.T
 *
 */
@Service
public class DirectorServiceImpl implements DirectorService {
	
	private final DirectorRepository directorRepository;
	private final MovieRepository movieRepository;

	public DirectorServiceImpl(DirectorRepository directorRepository,  MovieRepository movieRepository) {
		this.directorRepository = directorRepository;
		this.movieRepository = movieRepository;
	}
	
	@Transactional
	@Override
	public Director save(Director request) {
		doesDirectorExists(request.getFirstname(), request.getLastname());
		return directorRepository.save(request);
	}

	@Override
	public Director update(Long directorId, Director request) {
		return save(updateDirectorWithNewValues(findById(directorId), request));
	}

	@Override
	public Director findById(Long directorId) {
		return directorRepository.findById(directorId)
				.orElseThrow(() -> new NotFoundException(Director.class, directorId));
	}

	@Override
	public List<Director> findAll() {
		return directorRepository.findAll();
	}

	@Override
	public Set<Movie> findMoviesByDirectorFullName(String firstname, String lastname) {
		return directorRepository.findByFirstnameAndLastname(firstname, lastname).get().getMovies();
	}

	@Override
	public void deleteById(Long directorId) {
		Director director = findById(directorId);
		director.getMovies().stream()
								.forEach(movie-> {
									movie.removeDirector(director);
									movieRepository.save(movie);
									});
		directorRepository.deleteById(directorId);
		
	}

	@Override
	public void deleteAll() {
		directorRepository.findAll().forEach(directorEntity -> deleteById(directorEntity.getDirectorId()));
	}

	@Override
	public Director addOrRemoveMovieFromDirector(Long movieId, Long directorId, boolean add) {
		Director directorEntity = findById(directorId);
		Movie movieEntity = movieRepository.findById(movieId)
				.orElseThrow(() -> new NotFoundException(Movie.class, movieId));
		if(add) {
			directorEntity.addMovie(movieEntity);
		}else {
			directorEntity.removeMovie(movieEntity);
		}
		return directorRepository.save(directorEntity);
	}
	
	private Director updateDirectorWithNewValues(Director oldDirector, Director newDirector) {
		if (newDirector.getFirstname()!= null && !newDirector.getFirstname().isEmpty())
			oldDirector.setFirstname(newDirector.getFirstname());
		if (newDirector.getLastname()!= null && !newDirector.getLastname().isEmpty())
			oldDirector.setLastname(newDirector.getLastname());
		if (newDirector.getGender()!= null && !newDirector.getGender().isEmpty())
			oldDirector.setGender(newDirector.getGender());
		
		return oldDirector;
	}
	
	private void doesDirectorExists(String firstname, String lastname) {
		if(directorRepository.findByFirstnameAndLastname(firstname, lastname).isPresent())
			throw new NotFoundException(Director.class, firstname + " " + lastname, "Object [%s] with name [%s] already exists");
	}

}
