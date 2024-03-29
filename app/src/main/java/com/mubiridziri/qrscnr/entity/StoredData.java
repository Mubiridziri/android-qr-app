package com.mubiridziri.qrscnr.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class StoredData {

    public static String URL_TYPE = "url";
    public static String TEXT_TYPE = "text";

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "type")
    public String type;
}

