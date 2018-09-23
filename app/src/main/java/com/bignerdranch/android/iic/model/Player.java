package com.bignerdranch.android.iic.model;

import java.util.UUID;

public class Player {

    private UUID mId;
    private String mName;
    private boolean mCheckBox;

    public Player() {
        this(UUID.randomUUID());
    }

    public Player(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isCheckBox() {
        return mCheckBox;
    }

    public void setCheckBox(boolean checkBox) {
        mCheckBox = checkBox;
    }
}
