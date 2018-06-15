package pl.patrykheciak.projektowe;

import java.util.ArrayList;
import java.util.List;

import pl.patrykheciak.projektowe.model.MyMovie;

public class MovieRepository {
    private List<MyMovie> movies;

    public MovieRepository() {
        movies = new ArrayList<>();
        fill(movies);
    }

    public List<MyMovie> getMovies() {
        return movies;
    }


    private void fill(List<MyMovie> movies) {
        MyMovie m;

        m = new MyMovie();
        m.setTitle("Pierwszy filmik");
        movies.add(m);

        m = new MyMovie();
        m.setTitle("Drugi filmik");
        movies.add(m);

        m = new MyMovie();
        m.setTitle("Trzeci filmik");
        movies.add(m);

        m = new MyMovie();
        m.setTitle("Czwarty filmik");
        movies.add(m);

        m = new MyMovie();
        m.setTitle("Piąty filmik");
        movies.add(m);

        m = new MyMovie();
        m.setTitle("Szósty filmik");
        movies.add(m);

        m = new MyMovie();
        m.setTitle("Siódmy filmik");
        movies.add(m);

        m = new MyMovie();
        m.setTitle("Ósmy filmik");
        movies.add(m);

        m = new MyMovie();
        m.setTitle("Dziewiąty filmik");
        movies.add(m);
    }
}
