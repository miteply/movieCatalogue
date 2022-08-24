package com.mite.movie.api.dto.response;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Class used to send the JSON payload to the client.
 * 
 * @author Mykhaylo.T
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DirectorResponse {

	private Long directorId;
	
	private String firstname;
	
	private String lastname;
	
	private String gender;
	
	private Set<MovieResponse> movies;
	
	public Long getDirectorId() {
		return directorId;
	}
	public void setDirectorId(Long directorId) {
		this.directorId = directorId;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Set<MovieResponse> getMovies() {
		return movies;
	}
	public void setMovies(Set<MovieResponse> movies) {
		this.movies = movies;
	}
	
	
}
