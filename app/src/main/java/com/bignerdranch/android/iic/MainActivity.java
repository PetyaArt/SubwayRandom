package com.bignerdranch.android.iic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.android.iic.model.Player;
import com.bignerdranch.android.iic.model.PlayerLab;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private PlayerLab mPlayerLab;
    private List<Player> mPlayers;
    private List<String> mPlayersRandom;
    private Player mPlayer;
    private View mCustomView;
    private Cursor cursor;
    private int mRandom;
    private ImageButton mButtonRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayerLab = PlayerLab.get(this);
        updateList();
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
                addAndDeleteInGame();
                updateList();
                break;
            case R.id.add_players:
                addPlayer();
                updateList();
                break;
            case R.id.delete_players:
                deletePlayer();
                updateList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addPlayer() {
        mCustomView = getLayoutInflater().inflate(R.layout.dialog, null);
        new AlertDialog.Builder(this)
                .setView(mCustomView)
                .setTitle("Add player")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editTextPlayer = mCustomView.findViewById(R.id.edit_text);
                        Player player = new Player();
                        player.setName(String.valueOf(editTextPlayer.getText()));
                        player.setCheckBox(false);
                        mPlayerLab.addPlayer(player);
                    }
                })
                .create()
                .show();

    }

    private void deletePlayer() {
        mCustomView = getLayoutInflater().inflate(R.layout.dialog, null);
        new AlertDialog.Builder(this)
                .setTitle("Delete player")
                .setView(mCustomView)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editTextPlayer = mCustomView.findViewById(R.id.edit_text);
                        mPlayerLab.deletePlayer(String.valueOf(editTextPlayer.getText()));
                    }
                })
                .create()
                .show();
    }

    public void addAndDeleteInGame(){
        if (mPlayers.isEmpty())
            return;
        cursor = mPlayerLab.getCursor();
        new AlertDialog.Builder(this)
                .setTitle("Players")
                .setMultiChoiceItems(cursor, "checkbox", "name", new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        mPlayer = mPlayerLab.getPlayer().get(which);
                        mPlayer.setCheckBox(isChecked);
                        mPlayerLab.updatePlayer(mPlayer);
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Log.d("myLogs", "setOnDismissListener");
                        cursor.close();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("myLogs", "setPositiveButton");
                    }
                })
                .create()
                .show();
    }

    public void random(View view) {
        mPlayers = mPlayerLab.getPlayer();
        mButtonRandom = findViewById(view.getId());
        mButtonRandom.setEnabled(false);

        int randomAnimation = (int) (Math.random() * 100);

        if (randomAnimation > 90) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.supratuturu);
            mButtonRandom.startAnimation(animation);

            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.supratuturu);
            mediaPlayer.start();
        } else if (randomAnimation > 60) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.tuturubis);
            mButtonRandom.startAnimation(animation);

            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.tuturubis);
            mediaPlayer.start();
        } else if (randomAnimation > 0) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.tuturu);
            mButtonRandom.startAnimation(animation);

            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.tuturu);
            mediaPlayer.start();
        }



        mPlayersRandom = new ArrayList<>();
        for (Player player: mPlayers) {
            if (player.isCheckBox())
                mPlayersRandom.add(player.getName());
        }

        if (isOnline()) {
            Link link = Controller.getApi();

            link.getRandom(1, 0, mPlayersRandom.size() - 1, 1, 10, "plain", "new").enqueue(new Callback<JsonPrimitive>() {
                @Override
                public void onResponse(Call<JsonPrimitive> call, Response<JsonPrimitive> response) {
                    JsonPrimitive jo = response.body();
                    mRandom = jo.getAsInt();

                    result();
                }
                @Override
                public void onFailure(Call<JsonPrimitive> call, Throwable t) {
                    Log.d("myLogs", t.toString());
                    mButtonRandom.setEnabled(true);
                }
            });
        } else {
            mRandom = (int) (Math.random() * mPlayersRandom.size());
            if (!mPlayersRandom.isEmpty()){
                result();
            } else {
                mButtonRandom.setEnabled(true);
            }
        }

    }

    public void updateList() {
        mPlayers = mPlayerLab.getPlayer();
    }

    private void result() {
        mCustomView = getLayoutInflater().inflate(R.layout.dialog_anime, null);
        TextView textViewRandom = mCustomView.findViewById(R.id.textRandom);
        textViewRandom.setText(mPlayersRandom.get(mRandom));
        new AlertDialog.Builder(this)
                .setView(mCustomView)
                .create()
                .show();
        mButtonRandom.setEnabled(true);
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
