package com.example.autoplaylist.catalog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import model.DatabaseUpdateAssister;
import model.DatabaseUpdaterGen;

/**
 * Automatic Playlist Generation with Active Learning
 * Submitted for the Degree of B.Sc. in Computer Science, 2015/2016
 * University of Strathclyde
 * Created by Luke Robert Beveridge
 * 30/03/2016.
 */

public class PlaylistTrackInfoActivity extends AppCompatActivity {

    /**
     * Method to create PlaylistTrackInfoActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_track_info);

        // Allowing navigation to previous activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Creating instances of other classes
        final DatabaseUpdaterGen dbUpdater = new DatabaseUpdaterGen(this);
        final DatabaseUpdateAssister dbAssist = new DatabaseUpdateAssister(this);

        // Getting details of the currently selected track name in the list
        String track = getIntent().getStringExtra(PlaylistGeneratorActivity.TRACK);
        TextView tvTrack = (TextView) findViewById(R.id.trackText);
        tvTrack.setText(track);

        // Getting details of the currently selected artist name in the list
        String artist = getIntent().getStringExtra(PlaylistGeneratorActivity.ARTIST);
        TextView tvArtist = (TextView) findViewById(R.id.artistText);
        tvArtist.setText(artist);

        // Getting details of the currently selected position in the list
        final String pos = getIntent().getStringExtra(PlaylistGeneratorActivity.POS);
        final int position = Integer.parseInt(pos);

        // Creating a listener for when the rating bar is changed
        RatingBar rBar = (RatingBar) findViewById(R.id.trackRatingBar);
        rBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                // Setting current position
                dbUpdater.setRating((int) rating);
                dbUpdater.setPosition(position + 1);

                // Updating song rating
                dbUpdater.updateDetails();

                // Setting ranking for artist
                dbUpdater.setArtistRanking(dbUpdater.getAvgArtistRating() / dbAssist.getArtistCount());
                dbUpdater.updateArtistRanking();

                // Setting ranking for genre - Purposely dividing artist count to help balance overall ranking.
                dbUpdater.setGenreRanking(dbUpdater.getAvgTermRating() / dbAssist.getArtistCount());
                dbUpdater.updateGenreRanking();

                // This is in place to close this activity once the user has rated.
                finish();

            }
        });
    }
}