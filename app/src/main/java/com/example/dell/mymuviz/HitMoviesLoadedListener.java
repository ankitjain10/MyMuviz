package com.example.dell.mymuviz;

import java.util.ArrayList;

import pojo.Movie;


public interface HitMoviesLoadedListener {
    public void onBoxOfficeMoviesLoaded(ArrayList<Movie> listMovies);
}
