package com.mubiridziri.qrscnr.appdatabase;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mubiridziri.qrscnr.entity.StoredData;
import com.mubiridziri.qrscnr.repository.LinkRepository;

@Database(entities = {StoredData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LinkRepository getLinkRepository();
}