package pl.patrykheciak.projektowe;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

import pl.patrykheciak.projektowe.model.AppDatabase;
import pl.patrykheciak.projektowe.model.MyMovie;
import pl.patrykheciak.projektowe.model.MyMovieDao;

public class EnterMovieDataActivity extends AppCompatActivity {

    private EditText title;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_movie_data);

        title = findViewById(R.id.enter_movie_title);
        description = findViewById(R.id.enter_movie_description);
        findViewById(R.id.save_movie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMovie(title.getText().toString(), description.getText().toString());
            }
        });
    }

    private void saveMovie(String title, String description) {

        int duration = 0;
        String fileLocation = "";
        String imgFileLocation = "";

        MyMovie movie = new MyMovie();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setRecordedAt(Calendar.getInstance().getTime());
        movie.setDuration(duration);
        movie.setFileLocation(fileLocation);
        movie.setImgFileLocation(imgFileLocation);

        AppDatabase db = Room
                .databaseBuilder(
                        getApplicationContext(),
                        AppDatabase.class, "database-name")
                .allowMainThreadQueries()
                .build();
        MyMovieDao myMovieDao = db.movieDao();

        myMovieDao.insertAll(movie);
    }
}
