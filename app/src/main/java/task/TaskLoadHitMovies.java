package task;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.example.dell.mymuviz.HitMoviesLoadedListener;

import java.util.ArrayList;

import extras.MovieUtils;
import pojo.Movie;
import singleton.MySingleton;

public class TaskLoadHitMovies extends AsyncTask<Void, Void, ArrayList<Movie>> {
    private HitMoviesLoadedListener myComponent;
    private MySingleton  mySingleton;
    private RequestQueue requestQueue;


    public TaskLoadHitMovies(HitMoviesLoadedListener myComponent) {

        this.myComponent = myComponent;
        mySingleton = MySingleton.getInstance();
        requestQueue = mySingleton.getRequestQueue();
    }


    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {

        ArrayList<Movie> listMovies = MovieUtils.loadBoxOfficeMovies(requestQueue);
        return listMovies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> listMovies) {
        if (myComponent != null) {
            myComponent.onBoxOfficeMoviesLoaded(listMovies);
        }
    }


}
