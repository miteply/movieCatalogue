package com.mite.movie.api.dto.request;
/**
 * Class used to send the JSON payload from the client.
 * 
 * @author Mykhaylo.T
 *
 */
public class RatingRequest {

	private String text;

	private int score;

	public RatingRequest() {}

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

}
