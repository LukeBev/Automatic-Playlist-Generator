package model;

/**
 * Automatic Playlist Generation with Active Learning
 * Submitted for the Degree of B.Sc. in Computer Science, 2015/2016
 * University of Strathclyde
 * Created by Luke Robert Beveridge
 * 30/03/2016.
 */

/**
 * Creating an object called SongDetails to hold track and artist name
 */
public class SongDetails {

    private String track;
    private String artist;

    /**
     * Getting value of a track name
     * @return
     */
    public String getTrack() { return track; }

    /**
     * Fetting value of a artist name
     * @return
     */
    public String getArtist() { return artist; }

    public SongDetails(String track, String artist) {
        this.track = track;
        this.artist = artist;
    }
}

