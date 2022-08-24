package com.mite.movie.api.dto.request;

import java.util.Set;

/**
 * Class used to send the JSON payload from the client.
 * 
 * @author Mykhaylo.T
 *
 */
public class DirectorRequest {
	
	private String firstname;
	private String lastname;
	private String gender;
	private Set<MovieRequest> movies;
	
	
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
	public Set<MovieRequest> getMovies() {
		return movies;
	}
	public void setMovies(Set<MovieRequest> movies) {
		this.movies = movies;
	}
	
	

}
