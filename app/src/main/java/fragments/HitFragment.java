package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.mymuviz.HitMoviesLoadedListener;
import com.example.dell.mymuviz.MyApplication;
import com.example.dell.mymuviz.R;

import java.util.ArrayList;

import adapter.HitAdapter;
import extras.MovieSorter;
import extras.MySqlAdapter;
import extras.SortListener;
import pojo.Movie;
import task.TaskLoadHitMovies;


public class HitFragment extends Fragment implements SortListener,HitMoviesLoadedListener, SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String MOVIE_LIST = "movie_list";
    MySqlAdapter mySqlAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView hitMovieErrorText;
    private static final String TAG = HitFragment.class.getSimpleName();
    ArrayList<Movie> movieList = new ArrayList<>();
    RecyclerView hitMovieList;
    HitAdapter hitAdapter;
    MovieSorter movieSorter;
    private SwipeRefreshLayout swipe_hit;

    public HitFragment() {
        movieSorter=new MovieSorter();
    }


    public static HitFragment newInstance(String param1, String param2) {
        HitFragment fragment = new HitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        outState.putParcelableArrayList(MOVIE_LIST,movieList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hit,container, false);
        hitMovieErrorText=(TextView)view.findViewById(R.id.hitMovieErrorText);
        hitMovieList = (RecyclerView) view.findViewById(R.id.hitMovieList);
        swipe_hit=(SwipeRefreshLayout)view.findViewById(R.id.swipe_hit);
//        hitMovieList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
//        hitMovieList.setHasFixedSize(true);
        hitAdapter = new HitAdapter(getActivity());
        hitMovieList.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipe_hit.setOnRefreshListener(this);
        if(savedInstanceState!=null){
            movieList=savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            //L.t(getActivity(), "OnSaved Instance Called");

        }
        else
        {
            movieList= MyApplication.getWritableAdapter().readMovies(MySqlAdapter.BOX_OFFICE);
            if(movieList.isEmpty()){
                new TaskLoadHitMovies(this).execute();
            }
//            L.t(getActivity(), "New Database Created");
        }
        //if(movieList!=null) {
            hitAdapter.setMovieList(movieList);
            hitMovieList.setAdapter(hitAdapter);
        /*}
        else
        {
            hitMovieErrorText.setVisibility(View.VISIBLE);
            hitMovieErrorText.setText("ERROR");
        }*/
            return view;
    }


    @Override
    public void onSortByName() {
        movieSorter.sortMoviesByName(movieList);
        hitAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSortByDate() {
        movieSorter.sortMoviesByDate(movieList);
        System.out.print(movieList.toString());
        hitAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSortByRating() {
        movieSorter.sortMoviesByRating(movieList);
        hitAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBoxOfficeMoviesLoaded(ArrayList<Movie> listMovies) {
        if(swipe_hit.isRefreshing()){
            swipe_hit.setRefreshing(false);
        }
        hitAdapter.setMovieList(movieList);
    }

    @Override
    public void onRefresh() {
        new TaskLoadHitMovies(this).execute();

    }
}
