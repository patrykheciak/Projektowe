package pl.patrykheciak.projektowe.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MyMovieDao {

    @Query("SELECT * FROM mymovie")
    List<MyMovie> getAll();

    @Query("SELECT * FROM mymovie WHERE uid IN (:movieIds)")
    List<MyMovie> loadAllByIds(int[] movieIds);

    @Delete
    void delete(MyMovie movie);

    @Insert
    void insertAll(MyMovie... movies);


    //    @Query("SELECT * FROM user WHERE first_name LIKE :first AND "
    //            + "last_name LIKE :last LIMIT 1")
    //    User findByName(String first, String last);

}