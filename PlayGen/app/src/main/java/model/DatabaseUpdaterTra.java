package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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

public class DatabaseUpdaterTra extends SQLiteAssetHelper {

    // Initialising database variables
    private static final String DATABASE_NAME = "metadata_copy4.db";
    private static final int DATABASE_VERSION = 1;

    // Initialising remaining variables
    private int position = 0;
    private int rating = 0;
    private double artistRanking = 0;
    private double genreRanking = 0;

    public DatabaseUpdaterTra(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Setting the position value.
     * @param pos
     */
    public void setPosition(int pos) {
        position = pos;
    }

    /**
     * Getting the position value.
     * @return
     */
    public int getPosition() {
        return position;
    }

    /**
     * Setting the rating value.
     * @param trackRating
     */
    public void setRating(int trackRating) {
        rating = trackRating;
    }

    /**
     * Getting the rating value.
     * @return
     */
    public int getRating() {
        return rating;
    }

    /**
     * Updating the rating value based on the current position selected.
     */
    public void updateDetails() {

        int currentPosition = getPosition();
        SQLiteDatabase db = getWritableDatabase();
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"track_id", "title", "artist_name", "artist_familiarity"};
        String sqlTable = "songs";
        String orderBy = "artist_familiarity DESC";

        qBuilder.setTables(sqlTable);
        Cursor cur = qBuilder.query(db, sqlSelect, null, null,
                null, null, orderBy);

        for (int i = 0; i < currentPosition; i++) {
            cur.moveToNext();
        }
        String TrackId = cur.getString(cur.getColumnIndex("track_id"));

        ContentValues content = new ContentValues();
        content.put("Rating", getRating());
        db.update("songs", content, "track_id = '" + TrackId + "'", null);

        cur.close();
        db.close();
    }

    /**
     * Obtaining the artist_id value based on the current position selected.
     * @return
     */
    public String getArtistID() {

        int currentPosition = getPosition();
        SQLiteDatabase db = getWritableDatabase();
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"artist_id", "artist_familiarity"};
        String sqlTable = "songs";
        String orderBy = "artist_familiarity DESC";

        qBuilder.setTables(sqlTable);
        Cursor cur = qBuilder.query(db, sqlSelect, null, null,
                null, null, orderBy);

        for (int i = 0; i < currentPosition; i++) {
            cur.moveToNext();
        }
        String ArtistID = cur.getString(cur.getColumnIndex("artist_id"));

        cur.close();
        return ArtistID;
    }

    /**
     * Getting the average rating for an artist based on the current artist ID.
     * @return
     */
    public double getAvgArtistRating() {

        SQLiteDatabase db = getWritableDatabase();
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        String artistID = getArtistID();
        String[] sqlSelect = {"artist_id", "avg(Rating)", "artist_familiarity"};
        String sqlTable = "songs";
        String where = "artist_id = '" + artistID + "' and Rating > 0";
        String orderBy = "artist_familiarity DESC";

        qBuilder.setTables(sqlTable);
        Cursor cur = qBuilder.query(db, sqlSelect, where, null,
                null, null, orderBy);

        cur.moveToFirst();
        double avg = cur.getDouble(cur.getColumnIndex("avg(Rating)"));

        cur.close();
        return avg;
    }

    /**
     * Setting the artist ranking value.
     * @param artRank
     */
    public void setArtistRanking(double artRank) {
        artistRanking = artRank;
    }

    /**
     * Getting the artist ranking value.
     * @return
     */
    public double getArtistRanking() {
        return artistRanking;
    }

    /**
     * Updating the artist ranking based on the current artist ID obtained.
     */
    public void updateArtistRanking() {

        SQLiteDatabase db = getWritableDatabase();
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        double ranking = getArtistRanking();

        String[] sqlSelect = {"artist_id", "artist_familiarity", "artist_ranking"};
        String sqlTable = "songs";
        String orderBy = "artist_familiarity DESC";

        qBuilder.setTables(sqlTable);
        Cursor cur = qBuilder.query(db, sqlSelect, null, null,
                null, null, orderBy);

        ContentValues content = new ContentValues();
        content.put("artist_ranking", ranking);
        db.update("songs", content, "artist_id = '" + getArtistID() + "'", null);

        cur.close();
    }

    /**
     * Return all terms from the currently selected artist.
     * @return
     */
    public String getTerm() {

        String artistID = getArtistID();
        String str = "";
        Boolean flag = false;
        Cursor cur = null;
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        String[] sqlSelect = {"term"};
        String sqlTable = "songs s LEFT OUTER JOIN artist_term t ON s.artist_id = t.artist_id";
        String where = "s.artist_id = '" + artistID + "'";
        String groupBy = "title";

        try {
            qBuilder.setTables(sqlTable);
            cur = qBuilder.query(db, sqlSelect, where, null,
                    groupBy, null, null);
            if (cur != null && cur.moveToFirst()) {
                str = cur.getString(cur.getColumnIndex("term"));
                flag = true;
                cur.close();
            }
        } catch (SQLiteException e) {
            System.out.println(e);
        }
        if (flag == false) {
            str = "blank";
        }
        return str;
    }

    /**
     * Loop round terms to find each of their avg ratings
     * @return
     */
    public double getAvgTermRating() {

        String term = getTerm();
        double avg = 0;
        if (term.equals("blank")) {
            avg = 0;
        } else {
            SQLiteDatabase db = getReadableDatabase();
            SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

            // Query returns average rating of all terms where Rating > 0
            String[] sqlSelect = {"title", "avg(Rating)", "term"};
            String sqlTable = "songs s LEFT OUTER JOIN artist_term t ON s.artist_id = t.artist_id";
            String where = "term = '" + term + "' and Rating > 0";
            String orderBy = "s.artist_familiarity DESC";

            qBuilder.setTables(sqlTable);
            Cursor cur = qBuilder.query(db, sqlSelect, where, null,
                    null, null, null);

            cur.moveToFirst();
            avg = cur.getDouble(cur.getColumnIndex("avg(Rating)"));
            cur.close();
        }
        return avg;
    }

    /**
     * Setting the genre ranking.
     * @param genRank
     */
    public void setGenreRanking(double genRank) {
        genreRanking = genRank;
    }

    /**
     * Getting the genre ranking.
     * @return
     */
    public double getGenreRanking() {
        return genreRanking;
    }

    /**
     * Updating the genre ranking.
     */
    public void updateGenreRanking() {
        String term = getTerm();
        double ranking = getGenreRanking();

        SQLiteDatabase db2 = getWritableDatabase();
        SQLiteQueryBuilder qBuilder2 = new SQLiteQueryBuilder();

        String[] sqlSelect2 = {"artist_id"};
        String sqlTable2 = "artist_term";
        String where2 = "term = '" + term + "'";

        // Setting up table to query what artist_id's have the passed in term associated with it.
        qBuilder2.setTables(sqlTable2);
        Cursor cur2 = qBuilder2.query(db2, sqlSelect2, where2, null,
                null, null, null);

        ArrayList<String> artistIdList = new ArrayList<>();
        ArrayList<String> termList = new ArrayList<>();
        String artistID = "";

        // Looping through the artists returned from the last query, and preparing them into a list.
        while (cur2.moveToNext()) {
            artistID = cur2.getString(cur2.getColumnIndex("artist_id"));
            artistIdList.add(artistID);
        }
        cur2.close();

        // Retrieving all terms for the artist_id's from the previously prepared list in the order that they were just found.
        for (String item : artistIdList) {
            SQLiteDatabase db3 = getWritableDatabase();
            SQLiteQueryBuilder qBuilder3 = new SQLiteQueryBuilder();

            String[] sqlSelect3 = {"term"};
            String sqlTable3 = "songs s LEFT OUTER JOIN artist_term t ON s.artist_id = t.artist_id";
            String where3 = "s.artist_id = '" + item + "'";
            String groupBy3 = "title";

            qBuilder3.setTables(sqlTable3);
            Cursor cur3 = qBuilder3.query(db3, sqlSelect3, where3, null,
                    groupBy3, null, null);

            cur3.moveToFirst();
            String tempTerm = cur3.getString(cur3.getColumnIndex("term"));
            termList.add(tempTerm);
            cur3.close();
        }

        ArrayList<String> modifiedTermList = new ArrayList<>();
        ArrayList<String> modifiedArtistList = new ArrayList<>();

        // Creating a modified artist_id list, where only the terms
        // from the artists that match the passed in term are accepted.
        for (int j = 0; j < termList.size(); j++) {
            if (term.equals(termList.get(j))) {
                modifiedArtistList.add(artistIdList.get(j));
                modifiedTermList.add(termList.get(j));
            }
        }

        // Looping through the modified artist list and updating the genre ranking for each.
        for (int k = 0; k < modifiedArtistList.size(); k++) {
            String artistItem = modifiedArtistList.get(k);

            SQLiteDatabase db = getWritableDatabase();
            SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

            String[] sqlSelect = {"artist_id", "artist_familiarity", "genre_ranking"};
            String sqlTable = "songs";
            String orderBy = "artist_familiarity DESC";

            qBuilder.setTables(sqlTable);
            Cursor cur = qBuilder.query(db, sqlSelect, null, null,
                    null, null, orderBy);

            // Update artist genre_ranking value.
            ContentValues content = new ContentValues();
            content.put("genre_ranking", ranking);
            db.update("songs", content, "artist_id = '" + artistItem + "'", null);
            cur.close();
        }
    }


}

