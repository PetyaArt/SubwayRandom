package com.bignerdranch.android.iic.database;

public class PlayerDbSchema {
    public static final class PlayersTable {
        public static final String NAME = "player";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String CHECKBOX = "checkbox";
        }
    }
}
