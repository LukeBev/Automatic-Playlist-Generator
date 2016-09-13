package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Automatic Playlist Generation with Active Learning
 * Submitted for the Degree of B.Sc. in Computer Science, 2015/2016
 * University of Strathclyde
 * Created by Luke Robert Beveridge
 * 30/03/2016.
 */

public class DatabaseRefresher extends SQLiteAssetHelper {

    // Initialising database variables
    private static final String DATABASE_NAME = "metadata_copy4.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseRefresher(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method purpose is to select all ratings and rankings and clear values
     */
    public void clearRankings() {

        double rating = 0;
        double artistRanking = 0;
        double genreRanking = 0;
        double overallRanking = 0;

        SQLiteDatabase db = getWritableDatabase();
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"artist_id", "artist_ranking", "genre_ranking", "overall_ranking"};
        String sqlTable = "songs";
        String orderBy = "artist_ranking DESC";

        // Build database query to select all rows in songs table
        qBuilder.setTables(sqlTable);
        Cursor cur = qBuilder.query(db, sqlSelect, null, null,
                null, null, orderBy);

        // Setting the values of the fields listed below to zero
        ContentValues content = new ContentValues();
        content.put("Rating", rating);
        content.put("artist_ranking", artistRanking);
        content.put("genre_ranking", genreRanking);
        content.put("overall_ranking", overallRanking);
        db.update("songs", content, null, null);

        cur.close();
    }
}
