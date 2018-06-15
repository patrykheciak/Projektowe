package pl.patrykheciak.projektowe.movies;


import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.patrykheciak.projektowe.MainActivity;
import pl.patrykheciak.projektowe.MovieRepository;
import pl.patrykheciak.projektowe.MyRVAdapter;
import pl.patrykheciak.projektowe.PlayerActivity;
import pl.patrykheciak.projektowe.R;
import pl.patrykheciak.projektowe.model.AppDatabase;
import pl.patrykheciak.projektowe.model.MyMovie;
import pl.patrykheciak.projektowe.model.MyMovieDao;

public class MoviesFragment extends Fragment {


    private MyRVAdapter adapter;

    public MoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movies, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recyclerMovies);

        adapter = new MyRVAdapter(getContext());
        adapter.setOnItemClickListener(new MyRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int adapterPosition, MyMovie movie) {
                Intent intent = new Intent(getContext(), PlayerActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int adapterPosition, final MyMovie movie) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Log.d("MoviesFragment", "Będziemy usuwać " + adapterPosition);
                                deleteMovie(movie, adapterPosition);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder
                        .setMessage("Czy chcesz usunąć ten film?")
                        .setPositiveButton("Tak", dialogClickListener)
                        .setNegativeButton("Nie", dialogClickListener)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        return v;
    }

    private void deleteMovie(MyMovie movie, int adapterPosition) {
//        removeFile(movie.getFileLocation());
//        removeFile(movie.getImgFileLocation());
        removeDbEntry(movie);
        adapter.removeByPosition(adapterPosition);
    }

    private void removeDbEntry(MyMovie movie) {
        if (getActivity() != null) {
            AppDatabase db = Room
                    .databaseBuilder(
                            getActivity().getApplicationContext(),
                            AppDatabase.class, "database-name")
                    .allowMainThreadQueries()
                    .build();

            MyMovieDao myMovieDao = db.movieDao();
            myMovieDao.delete(movie);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            AppDatabase db = Room
                    .databaseBuilder(
                            getActivity().getApplicationContext(),
                            AppDatabase.class, "database-name")
                    .allowMainThreadQueries()
                    .build();

            MyMovieDao myMovieDao = db.movieDao();

//            db.clearAllTables();
//
//            MovieRepository movieRepository = new MovieRepository();
//            List<MyMovie> hardcodedMovies = movieRepository.getMovies();
//            for (MyMovie movie : hardcodedMovies) {
//                myMovieDao.insertAll(movie);
//            }

            List<MyMovie> dbMovies = myMovieDao.getAll();

            if (adapter.getItemCount() != dbMovies.size())
                adapter.setMovies(dbMovies);
        }

    }

    public static MoviesFragment newInstance() {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void applySortType(int sortType) {
        adapter.setSortType(sortType);
    }
}
