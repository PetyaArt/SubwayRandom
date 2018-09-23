package com.bignerdranch.android.iic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.bignerdranch.android.iic.database.PlayerDbSchema.*;


public class PlayerBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "playersBase.db";

    public PlayerBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PlayersTable.NAME + "(" +
                "_id integer primary key autoincrement," +
                PlayersTable.Cols.UUID + ", " +
                PlayersTable.Cols.NAME + ", " +
                PlayersTable.Cols.CHECKBOX  +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
