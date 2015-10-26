package fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.mymuviz.R;
import com.example.dell.mymuviz.SearchMoviesLoadedListener;

import java.util.ArrayList;

import adapter.SearchAdapter;
import extras.L;
import extras.SortListener;
import pojo.Movie;
import task.TaskLoadSearchMovies;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements SortListener, View.OnClickListener, SearchMoviesLoadedListener, SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText searchInput;
    SearchAdapter searchAdapter;
    ArrayList<Movie> movieList = new ArrayList<>();
    RecyclerView searchMovieList;
    SwipeRefreshLayout swipe_search;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_search, container, false);
        searchInput = (EditText) view.findViewById(R.id.searchMovieEditText);
        Button searchButton = (Button) view.findViewById(R.id.searchButton);
        swipe_search=(SwipeRefreshLayout)view.findViewById(R.id.swipe_search);

        searchMovieList= (RecyclerView) view.findViewById(R.id.searchMovieList);
        searchMovieList.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchButton.setOnClickListener(this);
        swipe_search.setOnRefreshListener(this);
        return view;
    }


    @Override
    public void onSortByName() {
        Toast.makeText(getActivity(), "onSortByName", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSortByDate() {
        Toast.makeText(getActivity(), "onSortByDate", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSortByRating() {
        Toast.makeText(getActivity(), "onSortByRating", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        String searchText = searchInput.getText().toString().toLowerCase();
        L.m(searchText);
        searchInput.setText("");
        searchAdapter=new SearchAdapter(getActivity());
        new TaskLoadSearchMovies(this).execute();
        searchAdapter.setMovieList(movieList);
        searchMovieList.setAdapter(searchAdapter);
    }

    @Override
    public void onSearchMoviesLoaded(ArrayList<Movie> listMovies) {
        if(swipe_search.isRefreshing()){
            swipe_search.setRefreshing(false);
        }
        searchAdapter.setMovieList(movieList);
    }

    @Override
    public void onRefresh() {
        new TaskLoadSearchMovies(this).execute();
    }
}
