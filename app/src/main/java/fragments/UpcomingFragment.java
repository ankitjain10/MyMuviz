package fragments;


import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.mymuviz.DividerItemDecoration;
import com.example.dell.mymuviz.MyApplication;
import com.example.dell.mymuviz.R;
import com.example.dell.mymuviz.UpcomingMoviesLoadedListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import adapter.UpcomingAdapter;
import extras.L;
import extras.MovieSorter;
import extras.MySqlAdapter;
import extras.SortListener;
import pojo.Movie;
import task.TaskLoadUpcomingMovies;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingFragment extends Fragment implements SortListener,UpcomingMoviesLoadedListener, SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String MOVIE_LIST = "movie_list";
    MySqlAdapter mySqlAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView upcomingMovieErrorText;
    //private static final String TAG = HitFragment.class.getSimpleName();
    ArrayList<Movie> movieList = new ArrayList<>();
    RecyclerView upcomingMovieList;
    UpcomingAdapter upcomingAdapter;
    MovieSorter movieSorter;
    private SwipeRefreshLayout swipe_upcoming;
    public UpcomingFragment() {
        movieSorter=new MovieSorter();
    }


    public static UpcomingFragment newInstance(String param1, String param2) {
        UpcomingFragment fragment = new UpcomingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static String AssetJSONFile (String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();
        return new String(formArray);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Log.v(TAG,"HitFragment onCreate");

        super.onCreate(savedInstanceState);
        mySqlAdapter=new MySqlAdapter(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_LIST, movieList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        upcomingMovieErrorText=(TextView)view.findViewById(R.id.upcomingMovieErrorText);
        upcomingMovieList = (RecyclerView) view.findViewById(R.id.upcomingMovieList);
        swipe_upcoming= (SwipeRefreshLayout) view.findViewById(R.id.swipe_upcoming);
        upcomingMovieList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        upcomingMovieList.setHasFixedSize(true);
        upcomingAdapter = new UpcomingAdapter(getActivity());
        upcomingMovieList.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState!=null){
            movieList=savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            //L.t(getActivity(), "OnSaved Instance Called");

        }
        else
        {
            movieList= MyApplication.getWritableAdapter().readMovies(MySqlAdapter.UPCOMING);
            if(movieList.isEmpty()){
                new TaskLoadUpcomingMovies(this).execute();
            }
            L.t(getActivity(), "New Database Created");
        }
        //if(movieList!=null){
            upcomingAdapter.setMovieList(movieList);
            upcomingMovieList.setAdapter(upcomingAdapter);
        /*}
        else
        {
            upcomingMovieErrorText.setVisibility(View.VISIBLE);
            upcomingMovieErrorText.setText("ERROR");
        }
        */swipe_upcoming.setOnRefreshListener(this);
        return view;
    }


    @Override
    public void onSortByName() {
        movieSorter.sortMoviesByName(movieList);
        upcomingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSortByDate() {
        movieSorter.sortMoviesByDate(movieList);
        //System.out.print(movieList.toString());
        upcomingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSortByRating() {
        movieSorter.sortMoviesByRating(movieList);
        upcomingAdapter.notifyDataSetChanged();
    }


    @Override
    public void onUpcomingMoviesLoaded(ArrayList<Movie> listMovies) {
        if(swipe_upcoming.isRefreshing()){
            swipe_upcoming.setRefreshing(false);
        }
        upcomingAdapter.setMovieList(movieList);

    }

    @Override
    public void onRefresh() {
        new TaskLoadUpcomingMovies(this).execute();
    }
}
