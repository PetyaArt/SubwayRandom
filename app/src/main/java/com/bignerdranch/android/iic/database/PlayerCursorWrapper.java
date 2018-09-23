package com.bignerdranch.android.iic.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.iic.model.Player;

import java.util.UUID;

import static com.bignerdranch.android.iic.database.PlayerDbSchema.*;

public class PlayerCursorWrapper extends CursorWrapper{
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public PlayerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Player getPlayers() {
        String uuidString = getString(getColumnIndex(PlayersTable.Cols.UUID));
        String name = getString(getColumnIndex(PlayersTable.Cols.NAME));
        int isSolved = getInt(getColumnIndex(PlayersTable.Cols.CHECKBOX));


        Player player = new Player(UUID.fromString(uuidString));
        player.setName(name);
        player.setCheckBox(isSolved != 0);

        return player;
    }
}
