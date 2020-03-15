package com.mubiridziri.qrscnr.appdatabase;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mubiridziri.qrscnr.entity.Link;
import com.mubiridziri.qrscnr.repository.LinkRepository;

@Database(entities = {Link.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LinkRepository getLinkRepository();
}