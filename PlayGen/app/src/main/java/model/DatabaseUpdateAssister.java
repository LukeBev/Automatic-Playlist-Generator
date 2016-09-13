package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

/**
 * Automatic Playlist Generation with Active Learning
 * Submitted for the Degree of B.Sc. in Computer Science, 2015/2016
 * University of Strathclyde
 * Created by Luke Robert Beveridge
 * 30/03/2016.
 */

public class DatabaseUpdateAssister extends SQLiteAssetHelper {

    // Initialising database variables
    private static final String DATABASE_NAME = "metadata_copy4.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseUpdateAssister(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Getting the total amount of unique artists.
     * @return
     */
    public int getArtistCount() {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"artist_id"};
        String sqlTable = "songs";

        // Build database query to select all rows, grouping by artist_id.
        qBuilder.setTables(sqlTable);
        Cursor cur = qBuilder.query(db, sqlSelect, null, null,
                "artist_id", null, null);

        int count = 0;
        while (cur.moveToNext()) {
            count++;
        }

        cur.close();
        db.close();
        return count;
    }

    /**
     * Updating the overall ranking for each row in the songs table.
     * This is done by summing both the values of artist and genre ranking together.
     */
    public void updateOverallRanking() {

        SQLiteDatabase db = getWritableDatabase();
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"track_id", "artist_id", "artist_ranking + genre_ranking", "overall_ranking"};
        String sqlTable = "songs";

        // Build database query to select all rows
        qBuilder.setTables(sqlTable);
        Cursor cur = qBuilder.query(db, sqlSelect, null, null,
                null, null, null);

        // Arraylist initialisation
        ArrayList<String> trackIdList = new ArrayList<>();
        ArrayList<Double> overallList = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            cur.moveToNext();
            String currentTrackId = cur.getString(cur.getColumnIndex("track_id"));
            double currentOverallRanking = cur.getDouble(cur.getColumnIndex("artist_ranking + genre_ranking"));
            trackIdList.add(currentTrackId);
            overallList.add(currentOverallRanking);
        }
        cur.close();

        System.out.println("Updating overall ranking values... ");
        // Updating overall ranking values
        ContentValues content = new ContentValues();
        for (int i = 0; i < 10000; i++) {
            content.put("overall_ranking", overallList.get(i));
            db.update("songs", content, "track_id = '" + trackIdList.get(i) + "'", null);
        }
        System.out.println("...complete ");
    }

}
