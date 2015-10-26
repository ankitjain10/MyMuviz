package extras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import pojo.Movie;


public class MovieSorter {
    public void sortMoviesByName(ArrayList<Movie> movies) {
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
    }

    public void sortMoviesByDate(ArrayList<Movie> movies) {
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                Date lhsDate = lhs.getReleaseDateTheater();
                Date rhsDate = rhs.getReleaseDateTheater();
                System.out.println("Date lhsDate "+lhsDate);
                System.out.println("Date rhsDate "+rhsDate);
                if (lhs.getReleaseDateTheater() == null || rhs.getReleaseDateTheater() == null ||
                        lhs.getReleaseDateTheater().equals(Constants.NA) || rhs.getReleaseDateTheater().equals(Constants.NA)
                        )
                    return 1;
                else {
                    //return lhsDate.compareTo(rhsDate);
                    return rhs.getReleaseDateTheater().compareTo(lhs.getReleaseDateTheater());
                }

            }
        });
    }

    public void sortMoviesByRating(ArrayList<Movie> movies) {
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                int ratingLhs = lhs.getAudienceScore();
                int ratingRhs = rhs.getAudienceScore();
                if (ratingLhs < ratingRhs) {
                    return 1;
                } else if (ratingLhs > ratingRhs) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }
}
