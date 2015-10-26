package extras;

import com.android.volley.RequestQueue;
import com.example.dell.mymuviz.MyApplication;

import org.json.JSONObject;

import java.util.ArrayList;

import json.Endpoints;
import json.Parser;
import json.Requestor;
import pojo.Movie;

public class MovieUtils {
    public static ArrayList<Movie> loadBoxOfficeMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.requestMoviesJSON(requestQueue, Endpoints.getRequestUrlBoxOfficeMovies(50));
        ArrayList<Movie> listMovies = Parser.parseMoviesJSON(response);
        MyApplication.getWritableAdapter().insertMovies(MySqlAdapter.BOX_OFFICE,listMovies, true);
        return listMovies;
    }
    public static ArrayList<Movie> loadUpcomingMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.requestMoviesJSON(requestQueue, Endpoints.getRequestUrlUpcomingMovies(50));
        ArrayList<Movie> listMovies = Parser.parseMoviesJSON(response);
        MyApplication.getWritableAdapter().insertMovies(MySqlAdapter.UPCOMING, listMovies, true);
        return listMovies;
    }
    /*public static ArrayList<Movie> loadSearchMovies(RequestQueue requestQueue) {
        JSONObject response = Requestor.requestMoviesJSON(requestQueue, Endpoints.getRequestUrlSearchMovies("godfather",50));
        ArrayList<Movie> listMovies = Parser.parseMoviesJSON(response);
        MyApplication.getWritableAdapter().insertMovies(MySqlAdapter.SEARCH, listMovies, true);
        return listMovies;
    }*/

}
