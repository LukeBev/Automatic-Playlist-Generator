package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Automatic Playlist Generation with Active Learning
 * Submitted for the Degree of B.Sc. in Computer Science, 2015/2016
 * University of Strathclyde
 * Created by Luke Robert Beveridge
 * 30/03/2016.
 */

public class TrackListCreator extends SQLiteAssetHelper {

    // Initialising database variables
    private static final String DATABASE_NAME = "metadata_copy4.db";
    private static final int DATABASE_VERSION = 1;

    // Initialising remaining variables
    private Cursor cur;
    public static List<SongDetails> detailList = new ArrayList<>();


    public TrackListCreator(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method which when called closes the cursor
     */
    public synchronized void closeCursor() {
        cur.close();
    }

    /**
     * This method will be called before calling the custom made adapter called TrackListAdapter
     * The purpose of the method is to populate a list with Detail Objects containing both track and artist name.
     * @return
     */
    public List<SongDetails> fetchDetails() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"title", "artist_name", "artist_familiarity"};
        String sqlTable = "songs";
        String orderBy = "artist_familiarity DESC";

        // Build database query to select all rows ordering by familiarity of artist
        qBuilder.setTables(sqlTable);
        cur = qBuilder.query(db, sqlSelect, null, null,
                null, null, orderBy);

        // Looping to store all 10000 values for title of song and artist name
        for (int i = 0; i < 10000; i++) {
            cur.moveToNext();
            String track = cur.getString(cur.getColumnIndex("title"));
            String artist = cur.getString(cur.getColumnIndex("artist_name"));

            // Creating a temporary SongDetails object to store the data collected from query
            SongDetails tempSongDetails = new SongDetails(track, artist);
            detailList.add(tempSongDetails);
        }
        db.close();
        closeCursor();
        return detailList;
    }

}

