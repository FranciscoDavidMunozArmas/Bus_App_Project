package com.example.bus_app.util.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.bus_app.util.table.HistoryTable;

public class HistorySQL extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "History.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + HistoryTable.TABLE_NAME + "(" +
            HistoryTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            HistoryTable.COLUMN_NAME_USER + " TEXT, " +
            HistoryTable.COLUMN_NAME_DATE + " TEXT, " +
            HistoryTable.COLUMN_NAME_DESCRIPTION + " TEXT, " +
            HistoryTable.COLUMN_NAME_AMOUNT + " TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + HistoryTable.TABLE_NAME;

    public HistorySQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
