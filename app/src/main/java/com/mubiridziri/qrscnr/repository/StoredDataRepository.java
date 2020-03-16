package com.mubiridziri.qrscnr.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mubiridziri.qrscnr.entity.StoredData;

import java.util.List;

@Dao
public interface StoredDataRepository {
    @Query("SELECT * FROM StoredData ORDER BY uid DESC")
    List<StoredData> getAll();

    @Query("SELECT * FROM StoredData WHERE uid IN (:linkUids)")
    List<StoredData> loadAllByIds(int[] linkUids);

    @Insert
    void insertAll(StoredData... users);

    @Delete
    void delete(StoredData user);
}


