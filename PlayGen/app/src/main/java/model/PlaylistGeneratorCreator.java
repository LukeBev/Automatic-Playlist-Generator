package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Automatic Playlist Generation with Active Learning
 * Submitted for the Degree of B.Sc. in Computer Science, 2015/2016
 * University of Strathclyde
 * Created by Luke Robert Beveridge
 * 30/03/2016.
 */

public class PlaylistGeneratorCreator extends SQLiteAssetHelper {

    // Initialising database variables
    private static final String DATABASE_NAME = "metadata_copy4.db";
    private static final int DATABASE_VERSION = 1;

    // Initialising remaining variables
    private Cursor cur;
    public static List<SongDetails> detailList = new ArrayList<>();


    public PlaylistGeneratorCreator(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This method will be called before calling the custom made adapter called TrackListAdapter
     * The purpose of the method is to populate a list with Detail Objects containing both track and artist name.
     * @return
     */
    public List<SongDetails> fetchDetails() {

        ArrayList<String> filteredList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        // Any changes to this query to format the ordering of the playlist generator must also been done
        // in the DatabaseUpdaterGen class, this will include changing orderBy and/or groupBy statements.
        String[] sqlSelect = {"title", "artist_name", "overall_ranking"};
        String sqlTable = "songs";
        String orderBy = "overall_ranking DESC";

        qBuilder.setTables(sqlTable);
        cur = qBuilder.query(db, sqlSelect, null, null,
                null, null, orderBy, null);

        // Clear previous list to prevent the new list from stacking onto the old.
        detailList.clear();

        int i = 0;
        while (i < 50) {
            cur.moveToNext();
            String track = cur.getString(cur.getColumnIndex("title"));
            String artist = cur.getString(cur.getColumnIndex("artist_name"));

            SongDetails tempSongDetails = new SongDetails(track, artist);
            int occurrences = Collections.frequency(filteredList, artist);

            //Populating the filtered list where the occurrences determine how many times an artist appears.
            if (occurrences < 2) {
                filteredList.add(artist);
                detailList.add(tempSongDetails);
            } else {
                cur.moveToNext();
            }
            i++;
        }
        db.close();
        closeCursor();
        return detailList;
    }

    public synchronized void closeCursor() {
        cur.close();
    }

}
