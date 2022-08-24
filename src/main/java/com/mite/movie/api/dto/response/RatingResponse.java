package com.mite.movie.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * Class used to send the JSON payload to the client.
 * 
 * @author Mykhaylo.T
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RatingResponse {

	private Long ratingId;

	private String text;

	private int score;

	private Long movieId;

	public Long getRatingId() {
		return ratingId;
	}

	public void setRatingId(Long ratingId) {
		this.ratingId = ratingId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}
}
