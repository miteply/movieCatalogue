package com.mite.movie.api.dto.response;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * Class used to send the JSON payload to the client.
 * 
 * @author Mykhaylo.T
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieResponse {
	
	private Long movieId;
	
	private String title;
	
	private String description;
	
	private Integer releaseDate;
	
	private List<RatingResponse> ratings;

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
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

	public Integer getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Integer releaseDate) {
		this.releaseDate = releaseDate;
	}

	public List<RatingResponse> getRatings() {
		return ratings;
	}

	public void setRatings(List<RatingResponse> ratings) {
		this.ratings = ratings;
	}
	
	

}
