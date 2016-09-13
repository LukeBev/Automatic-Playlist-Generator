package com.example.autoplaylist.catalog;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import model.DatabaseRefresher;
import model.DatabaseUpdateAssister;

/**
 * Automatic Playlist Generation with Active Learning.
 * Submitted for the Degree of B.Sc. in Computer Science, 2015/2016
 * University of Strathclyde
 * Created by Luke Robert Beveridge
 * 30/03/2016.
 */

public class MainScreenActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    /**
     * Method to create MainScreenActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Find View-elements
        Button trackButton = (Button) findViewById(R.id.trackSelectButton);
        Button playGenButton = (Button) findViewById(R.id.generatedPlaylistButton);
        Button clearButton = (Button) findViewById(R.id.clearButton);
        Button updateButton = (Button) findViewById(R.id.updateButton);
        linearLayout = (LinearLayout) findViewById(R.id.coordinator);

        // Create click listener for trackSelectionButton
        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Track List Activity
                Intent trackScreen = new Intent(v.getContext(), TrackListActivity.class);
                startActivity(trackScreen);
            }
        });

        // Create click listener for generatedPlaylistButton
        playGenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Playlist Generator Activity
                Intent playGenScreen = new Intent(v.getContext(), PlaylistGeneratorActivity.class);
                startActivity(playGenScreen);
            }
        });

        // Create click listener for the clear preferences button
        final DatabaseRefresher refresher = new DatabaseRefresher(this);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a SnackBar message
                Snackbar.make(linearLayout, "Preferences reset", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                // Clear all rankings
                refresher.clearRankings();
            }
        });

        // Create click listener for the update preferences button
        final DatabaseUpdateAssister dbAssist = new DatabaseUpdateAssister(this);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a SnackBar message
                Snackbar.make(linearLayout, "Updated preferences", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();
                // Updating ranking for overall
                dbAssist.updateOverallRanking();
            }
        });

    }
}
