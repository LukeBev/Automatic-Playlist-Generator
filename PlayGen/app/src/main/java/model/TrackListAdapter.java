package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.autoplaylist.catalog.R;

import java.util.List;

/**
 * Automatic Playlist Generation with Active Learning
 * Submitted for the Degree of B.Sc. in Computer Science, 2015/2016
 * University of Strathclyde
 * Created by Luke Robert Beveridge
 * 30/03/2016.
 */

public class TrackListAdapter extends ArrayAdapter<SongDetails> {

    // Initialising list of SongDetails objects
    private List<SongDetails> songDetails;

    public TrackListAdapter(Context context, int resource, List<SongDetails> objects) {
        super(context, resource, objects);
        songDetails = objects;
    }

    /**
     * Creating a custom adapter to give the ability to display both
     * track name and artist name.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_items, parent, false);
        }

        // Get the position of the current SongDetails object to provide its details.
        SongDetails songDetail = songDetails.get(position);

        // Getting track and artist name details
        TextView trackText = (TextView) convertView.findViewById(R.id.trackText);
        TextView artistText = (TextView) convertView.findViewById(R.id.artistText);

        // Setting the text for track and artist name on the view
        trackText.setText(songDetail.getTrack());
        artistText.setText(songDetail.getArtist());

        return convertView;
    }
}
