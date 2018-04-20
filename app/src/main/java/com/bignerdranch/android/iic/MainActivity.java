package com.bignerdranch.android.iic;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<CharSequence> mItems = new ArrayList<>();
    private List<Integer> mIdPlayers = new ArrayList<>();
    private List<Boolean> mDefaultChoice = new ArrayList<>();
    private TextView mTextView;
    private boolean mFlag = false;

    private int mCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAddPlayers();
    }

    private void checkAddPlayers() {
        SQLiteDatabase mPlayersDB =
                openOrCreateDatabase("players.db", MODE_PRIVATE, null);

        mPlayersDB.execSQL(
                "CREATE TABLE IF NOT EXISTS user (ID INT, name VARCHAR(200))"
        );

        Cursor myCursor =
                mPlayersDB.rawQuery("select ID, name from user", null);


        while(myCursor.moveToNext()) {
            int ID = myCursor.getInt(0);
            String name = myCursor.getString(1);
            if (!mItems.contains(name)){
                mIdPlayers.add(ID);
                mItems.add(name);
                mDefaultChoice.add(true);
            }
        }
        myCursor.close();
        mPlayersDB.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_and_delete:
                setMultiChoiceItems();
                checkAddPlayers();
                break;
            case R.id.add_players:
                checkAddPlayers();
                addPlayers();
                checkAddPlayers();
                break;
            case R.id.delete_players:
                deletePlayers();
                checkAddPlayers();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletePlayers() {
        checkAddPlayers();
        View customView = getLayoutInflater().inflate(R.layout.dialog, null);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(customView)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dialog dialogObj = Dialog.class.cast(dialogInterface);
                        EditText mEditDelete = dialogObj.findViewById(R.id.edit_text);

                        SQLiteDatabase mPlayersDB =
                                openOrCreateDatabase("players.db", MODE_PRIVATE, null);

                        mPlayersDB.execSQL(
                                "CREATE TABLE IF NOT EXISTS user (ID INT, name VARCHAR(200))"
                        );

                        mPlayersDB.delete("user", "name = ?", new String[]{mEditDelete.getText().toString()});

                        Cursor myCursor =
                                mPlayersDB.rawQuery("select ID, name from user", null);

                        mItems = new ArrayList<>();
                        mIdPlayers = new ArrayList<>();
                        mDefaultChoice = new ArrayList<>();

                        myCursor.close();
                        mPlayersDB.close();
                    }
                })
                .create()
                .show();
        checkAddPlayers();
    }

    private void addPlayers() {
        View customView = getLayoutInflater().inflate(R.layout.dialog, null);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(customView)
                .setTitle("Add player")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase mPlayersDB =
                                openOrCreateDatabase("players.db", MODE_PRIVATE, null);

                        mPlayersDB.execSQL(
                                "CREATE TABLE IF NOT EXISTS user (ID INT, name VARCHAR(200))"
                        );

                        ContentValues row1 = new ContentValues();

                        Dialog dialogObj = Dialog.class.cast(dialogInterface);
                        EditText mEdit = dialogObj.findViewById(R.id.edit_text);

                        row1.put("ID", mCounter);
                        row1.put("name", mEdit.getText().toString());

                        mPlayersDB.insert("user", null, row1);

                        Cursor myCursor =
                                mPlayersDB.rawQuery("select ID, name from user", null);

                        mCounter++;
                        myCursor.close();
                        mPlayersDB.close();
                    }
                })
                .create()
                .show();
    }

    public void setMultiChoiceItems(){
        if(mIdPlayers.isEmpty())
            return;

        checkAddPlayers();
        CharSequence[] arr = mItems.toArray(new CharSequence[mItems.size()]);

        String[] stringBooleans = new String[mDefaultChoice.size()];
        for (int i = 0; i < mDefaultChoice.size(); i++) {
            stringBooleans[i] = String.valueOf(mDefaultChoice.get(i));
        }

        final boolean[] booleans = new boolean[stringBooleans.length];
        for (int i = 0; i < stringBooleans.length; i++) {
            booleans[i] = Boolean.parseBoolean(stringBooleans[i]);
        }

        new AlertDialog.Builder(this)
                .setMultiChoiceItems(arr, booleans, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        for (int j = 0; j < booleans.length; j++) {
                            mDefaultChoice.set(j, booleans[j]);
                        }
                    }
                })
                .create()
                .show();
    }


    public void random(View view) {
        checkAddPlayers();
        if(mIdPlayers.isEmpty())
            return;

        int wheel = 0;
        for (int i = 0; i < mDefaultChoice.size(); i++) {
            if (mDefaultChoice.get(i))
                wheel++;
        }

        String[] wheelMas = new String[wheel];
        int j = 0;
        for (int i = 0; i < mDefaultChoice.size(); i++) {
            if (mDefaultChoice.get(i)){
                wheelMas[j] = String.valueOf(mItems.get(i));
                j++;
            }
        }
        //Toast.makeText(getApplicationContext(), String.valueOf(wheelMas[(int) (Math.random() * (wheel))]), Toast.LENGTH_SHORT).show();
        View customView = getLayoutInflater().inflate(R.layout.dialog_anime, null);

        mTextView = customView.findViewById(R.id.textView);
        mTextView.setText("Just do it! " + String.valueOf(wheelMas[(int) (Math.random() * (wheel))]));

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(customView)
                .create()
                .show();

    }

}
