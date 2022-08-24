package com.mite.movie.api.dto.request;

import java.util.List;

/**
 * Class used to send the JSON payload from the client.
 * 
 * @author Mykhaylo.T
 *
 */
public class MovieRequest {
	
	private String title;

	private String description;
	
	private int releaseDate;
	
	private List<RatingRequest> ratings;
	
	public MovieRequest() {
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(int releaseDate) {
		this.releaseDate = releaseDate;
	}

	public List<RatingRequest> getRatings() {
		return ratings;
	}

	public void setRatings(List<RatingRequest> ratings) {
		this.ratings = ratings;
	}
	
	

}
