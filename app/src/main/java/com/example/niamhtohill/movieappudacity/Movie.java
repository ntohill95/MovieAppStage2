package com.example.niamhtohill.movieappudacity;

import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by niamhtohill on 24/05/2018.
 */

public class Movie {
    private String movieTitle;
    private String movieReleaseDate;
    private String movieSynopsis;
    private Double movieVoteAverage;
    private String moviePosterUrl;
    private Integer movieID;
    private String movieRunTime;

    public Integer getMovieID() {
        return movieID;
    }

    public void setMovieID(Integer movieID) {
        this.movieID = movieID;
    }

    public Movie(String movieTitle, String movieReleaseDate, String movieSynopsis, Double movieVoteAverage, String moviePosterUrl, Integer movieID, String movieRunTime) {
        this.movieTitle = movieTitle;
        this.movieReleaseDate = movieReleaseDate;
        this.movieSynopsis = movieSynopsis;
        this.movieVoteAverage = movieVoteAverage;
        this.moviePosterUrl = moviePosterUrl;
        this.movieID = movieID;
        this.movieRunTime = movieRunTime;

    }

    public String getMovieRunTime() {
        return movieRunTime;
    }

    public void setMovieRunTime(String movieRunTime) {
        this.movieRunTime = movieRunTime;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieSynopsis() {
        return movieSynopsis;
    }

    public void setMovieSynopsis(String movieSynopsis) {
        this.movieSynopsis = movieSynopsis;
    }

    public Double getMovieVoteAverage() {
        return movieVoteAverage;
    }

    public void setMovieVoteAverage(Double movieVoteAverage) {
        this.movieVoteAverage = movieVoteAverage;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }
}
