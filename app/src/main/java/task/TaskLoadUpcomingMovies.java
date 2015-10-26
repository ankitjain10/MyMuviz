package task;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.example.dell.mymuviz.UpcomingMoviesLoadedListener;

import java.util.ArrayList;

import extras.MovieUtils;
import pojo.Movie;
import singleton.MySingleton;


public class TaskLoadUpcomingMovies extends AsyncTask<Void, Void, ArrayList<Movie>> {
    private UpcomingMoviesLoadedListener myComponent;
    private MySingleton volleySingleton;
    private RequestQueue requestQueue;


    public TaskLoadUpcomingMovies(UpcomingMoviesLoadedListener myComponent) {

        this.myComponent = myComponent;
        volleySingleton = MySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }


    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {

        ArrayList<Movie> listMovies = MovieUtils.loadUpcomingMovies(requestQueue);
        return listMovies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> listMovies) {
        if (myComponent != null) {
            myComponent.onUpcomingMoviesLoaded(listMovies);
        }
    }
}
