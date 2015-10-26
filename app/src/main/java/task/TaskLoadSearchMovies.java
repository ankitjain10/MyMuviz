package task;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.example.dell.mymuviz.SearchMoviesLoadedListener;

import java.util.ArrayList;

import pojo.Movie;
import singleton.MySingleton;

/**
 * Created by dell on 10/7/2015.
 */
public class TaskLoadSearchMovies  extends AsyncTask<Void, Void, ArrayList<Movie>> {
    private SearchMoviesLoadedListener myComponent;
    private MySingleton mySingleton;
    private RequestQueue requestQueue;


    public TaskLoadSearchMovies(SearchMoviesLoadedListener myComponent) {

        this.myComponent = myComponent;
        mySingleton = MySingleton.getInstance();
        requestQueue = mySingleton.getRequestQueue();
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {
        ArrayList<Movie> movies=new ArrayList<Movie>();
                //ArrayList<Movie> listMovies = MovieUtils.loadSearchMovies(requestQueue);
        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> listMovies) {
        if (myComponent != null) {
            myComponent.onSearchMoviesLoaded(listMovies);
        }
    }


}
