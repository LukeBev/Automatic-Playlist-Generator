package com.example.autoplaylist.catalog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import model.TrackListCreator;
import model.SongDetails;
import model.TrackListAdapter;

/**
 * Automatic Playlist Generation with Active Learning
 * Submitted for the Degree of B.Sc. in Computer Science, 2015/2016
 * University of Strathclyde
 * Created by Luke Robert Beveridge
 * 30/03/2016.
 */

public class TrackListActivity extends AppCompatActivity {

    public static final String TRACK = "TRACK";
    public static final String ARTIST = "ARTIST";
    public static final String POS = "POS";
    private List<SongDetails> songDetails = TrackListCreator.detailList;

    /**
     * Method to create TrackListActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);

        // Allowing navigation to previous activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Fetching Object data - This will populate a list with Detail Objects
        TrackListCreator mDb = new TrackListCreator(this);
        mDb.fetchDetails();

        // Creating an instance of the custom adapter - TrackListAdapter
        TrackListAdapter trackAdapter =
                new TrackListAdapter(this, R.layout.list_items, songDetails);
        ListView lv = (ListView) findViewById(R.id.trackText);
        lv.setAdapter(trackAdapter);

        // Creating a listener for when an item from the generated list is selected
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TrackListActivity.this, TrackInfoActivity.class);

                // Storing details of the currently selected position in the list
                SongDetails songDetail = songDetails.get(position);
                intent.putExtra(TRACK, songDetail.getTrack());
                intent.putExtra(ARTIST, songDetail.getArtist());
                intent.putExtra(POS, String.valueOf(position));

                // Start TrackInfoActivity
                startActivity(intent);
            }
        });
    }
}




