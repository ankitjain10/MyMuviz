package com.example.dell.mymuviz;

import java.util.ArrayList;

import pojo.Movie;

/**
 * Created by dell on 10/7/2015.
 */
public interface SearchMoviesLoadedListener {
    public void onSearchMoviesLoaded(ArrayList<Movie> listMovies);
}
