package com.example.autoplaylist.catalog;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import model.DatabaseUpdateAssister;
import model.DatabaseUpdaterTra;

/**
 * Automatic Playlist Generation with Active Learning
 * Submitted for the Degree of B.Sc. in Computer Science, 2015/2016
 * University of Strathclyde
 * Created by Luke Robert Beveridge
 * 30/03/2016.
 */

public class TrackInfoActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    /**
     * Method to create TrackInfoActivity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_info);

        // Allowing navigation to previous activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Creating instances of other classes
        final DatabaseUpdaterTra dbUpdater = new DatabaseUpdaterTra(this);
        final DatabaseUpdateAssister dbAssist = new DatabaseUpdateAssister(this);

        // Getting details of the currently selected track name in the list
        String track = getIntent().getStringExtra(TrackListActivity.TRACK);
        TextView tvTrack = (TextView) findViewById(R.id.trackText);
        tvTrack.setText(track);

        // Getting details of the currently selected artist name in the list
        String artist = getIntent().getStringExtra(TrackListActivity.ARTIST);
        TextView tvArtist = (TextView) findViewById(R.id.artistText);
        tvArtist.setText(artist);

        // Getting details of the currently selected position in the list
        final String pos = getIntent().getStringExtra(TrackListActivity.POS);
        final int position = Integer.parseInt(pos);

        // Creating a listener for when the rating bar is changed
        RatingBar rBar = (RatingBar) findViewById(R.id.trackRatingBar);
        rBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                linearLayout = (LinearLayout) findViewById(R.id.trackInfo);

                // Creating a SnackBar message
                Snackbar.make(linearLayout, "Track has been rated", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();

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
            }
        });
    }
}
