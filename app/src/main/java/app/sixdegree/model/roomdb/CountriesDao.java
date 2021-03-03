package app.sixdegree.model.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountriesDao {

    @Query("SELECT * FROM country")
    List<Country> getAll();

    @Query("SELECT * FROM country WHERE id IN (:userIds)")
    List<Country> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM country WHERE name LIKE :first  LIMIT 1")
    Country findByName(String first);

    @Insert
    void insertAll(Country... users);

    @Delete
    void delete(Country user);
}