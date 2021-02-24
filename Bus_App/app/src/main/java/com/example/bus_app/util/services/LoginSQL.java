package com.example.bus_app.util.services;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.bus_app.util.table.LoginTable;
import com.example.bus_app.util.table.UserTable;

public class LoginSQL extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Login.db";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + LoginTable.TABLE_NAME + "(" +
            LoginTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LoginTable.COLUMN_NAME_USERNAME + " TEXT, " +
            LoginTable.COLUMN_NAME_PASSWORD + " TEXT)";

    private  static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + LoginTable.TABLE_NAME;

    public LoginSQL(@Nullable Context context) {
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
