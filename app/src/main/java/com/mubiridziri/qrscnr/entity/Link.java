package com.mubiridziri.qrscnr.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity
public class Link {

    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "url_path")
    public String url;
}

