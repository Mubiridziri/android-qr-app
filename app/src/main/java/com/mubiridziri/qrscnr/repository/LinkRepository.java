package com.mubiridziri.qrscnr.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mubiridziri.qrscnr.entity.Link;

import java.util.List;

@Dao
public interface LinkRepository {
    @Query("SELECT * FROM link ORDER BY uid DESC")
    List<Link> getAll();

    @Query("SELECT * FROM link WHERE uid IN (:linkUids)")
    List<Link> loadAllByIds(int[] linkUids);

    @Insert
    void insertAll(Link... users);

    @Delete
    void delete(Link user);
}


