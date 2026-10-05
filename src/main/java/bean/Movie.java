package bean;

import java.io.Serializable;
import java.sql.Date;

public class Movie implements Serializable {
	
	private int movieId;
	
	private String title;
	
	private int duration;
	
	private Date releaseStartDate;
	
	private Date releaseEndDate;
	
	private String genre;
	
	private String ageLimit;
	
	private String description;
	
	private String director;
	
	private String cast;
	
	private String posterUrl;
	
	public int getMovieId() {
		return movieId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public Date getReleaseStartDate() {
		return releaseStartDate;
	}
	
	public Date getReleaseEndDate() {
		return releaseEndDate;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public String getAgeLimit() {
		return ageLimit;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getDirector() {
		return director;
	}
	
	public String getCast() {
		return cast;
	}
	
	public String getPosterUrl() {
		return posterUrl;
	}
	
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	 
	public void setReleaseStartDate(Date releaseStartDate) {
		this.releaseStartDate = releaseStartDate;
	}
	
	public void setReleaseEndDate(Date releaseEndDate) {
		this.releaseEndDate = releaseEndDate;
	}
	
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	public void setAgeLImit(String ageLimit) {
		this.ageLimit = ageLimit;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setDirector(String director) {
		this.director = director;
	}
	
	public void setCast(String cast) {
		this.cast = cast;
	}
	
	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}
}
