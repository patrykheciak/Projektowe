package pl.patrykheciak.projektowe.movies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.patrykheciak.projektowe.MovieRepository;
import pl.patrykheciak.projektowe.MyRVAdapter;
import pl.patrykheciak.projektowe.R;

public class MoviesFragment extends Fragment {


    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movies, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recyclerMovies);

        MovieRepository movieRepository = new MovieRepository();
        MyRVAdapter adapter = new MyRVAdapter(getContext(), movieRepository);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return v;
    }

}
