package com.example.autoplaylist.catalog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import model.PlaylistGeneratorCreator;
import model.SongDetails;
import model.TrackListAdapter;

/**
 * Automatic Playlist Generation with Active Learning
 * Submitted for the Degree of B.Sc. in Computer Science, 2015/2016
 * University of Strathclyde
 * Created by Luke Robert Beveridge
 * 30/03/2016.
 */

public class PlaylistGeneratorActivity extends AppCompatActivity {

    public static final String TRACK = "TRACK";
    public static final String ARTIST = "ARTIST";
    public static final String POS = "POS";
    private List<SongDetails> songDetails = PlaylistGeneratorCreator.detailList;

    /**
     * Method to create PlaylistGeneratorActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_generator);

        // Allowing navigation to previous activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting the action
        getIntent().setAction("Already created");

        // Fetching Object data - This will populate a list with Detail Objects
        PlaylistGeneratorCreator generatorCreator = new PlaylistGeneratorCreator(this);
        generatorCreator.fetchDetails();

        // Creating an instance of the custom adapter - TrackListAdapter
        TrackListAdapter trackAdapter =
                new TrackListAdapter(this, R.layout.list_items, songDetails);
        ListView lv = (ListView) findViewById(R.id.trackText);
        lv.setAdapter(trackAdapter);

        // Creating a listener for when an item from the generated list is selected
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlaylistGeneratorActivity.this, PlaylistTrackInfoActivity.class);

                // Storing details of the currently selected position in the list
                SongDetails songDetail = songDetails.get(position);
                intent.putExtra(TRACK, songDetail.getTrack());
                intent.putExtra(ARTIST, songDetail.getArtist());
                intent.putExtra(POS, String.valueOf(position));

                // Start PlaylistTrackInfoActivity
                startActivity(intent);
            }
        });
    }

    /**
     * Method to allow activity to unpause
     */
    @Override
    protected void onResume() {
        String action = getIntent().getAction();

        // Prevent endless loop by adding a unique action, don't restart if action is present
        if(action == null || !action.equals("Already created")) {
            Intent intent = new Intent(this, PlaylistGeneratorActivity.class);
            startActivity(intent);
            finish();
        }
        // Remove the unique action so the next time onResume is called it will restart
        else
            // Setting the unique action to null
            getIntent().setAction(null);

        super.onResume();
    }


}
