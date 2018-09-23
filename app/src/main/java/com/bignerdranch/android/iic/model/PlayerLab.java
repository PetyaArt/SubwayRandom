package com.bignerdranch.android.iic.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.iic.database.PlayerBaseHelper;
import com.bignerdranch.android.iic.database.PlayerCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bignerdranch.android.iic.database.PlayerDbSchema.*;

public class PlayerLab {

    private static PlayerLab sPlayerLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private PlayerLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PlayerBaseHelper(mContext).getWritableDatabase();
    }

    public void addPlayer(Player p) {
        ContentValues values = getContentValues(p);

        mDatabase.insert(PlayersTable.NAME, null, values);
    }

    public void deletePlayer(Player p) {
        ContentValues values = getContentValues(p);

        mDatabase.delete(
                PlayersTable.NAME,
                PlayersTable.Cols.UUID + " = ?" ,
                new String[] {(String) values.get(PlayersTable.Cols.UUID)});
    }

    public void deletePlayer(String p) {

        mDatabase.delete(
                PlayersTable.NAME,
                PlayersTable.Cols.NAME + " = ?" ,
                new String[] {(p)});
    }

    public List<Player> getPlayer() {
        List<Player> crimes = new ArrayList<>();
        PlayerCursorWrapper cursor = queryPlayer(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                crimes.add(cursor.getPlayers());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    public Player getPlayer(UUID id) {

        PlayerCursorWrapper cursor = queryPlayer(
                PlayersTable.Cols.UUID + " = ?",
                new String[] {id.toString()});

        try {
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getPlayers();
        } finally {
            cursor.close();
        }
    }

    public void updatePlayer(Player player) {
        String uuidString = player.getId().toString();
        ContentValues values = getContentValues(player);

        mDatabase.update(
                PlayersTable.NAME, values,
                PlayersTable.Cols.UUID + " = ? ",
                new String[] {uuidString});
    }

    public Cursor getCursor() {
        Cursor cursor = mDatabase.query(
                PlayersTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        return cursor;
    }

    private static ContentValues getContentValues(Player p) {
        ContentValues values = new ContentValues();
        values.put(PlayersTable.Cols.UUID, p.getId().toString());
        values.put(PlayersTable.Cols.NAME, p.getName());
        values.put(PlayersTable.Cols.CHECKBOX, p.isCheckBox());
        return values;
    }

    private PlayerCursorWrapper queryPlayer(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PlayersTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new PlayerCursorWrapper(cursor);
    }

    public static PlayerLab get(Context context) {
        if (sPlayerLab == null) {
            sPlayerLab = new PlayerLab(context);
        }
        return sPlayerLab;
    }
}
