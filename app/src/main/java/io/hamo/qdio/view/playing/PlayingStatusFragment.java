package io.hamo.qdio.view.playing;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.hamo.qdio.R;
import io.hamo.qdio.music.Track;

public class PlayingStatusFragment extends Fragment {

    private PlayingStatusViewModel viewModel;
    private TextView trackName;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new PlayingStatusViewModel();

        viewModel.getCurrentlyPlayingTrack().observe(this, new Observer<Track>() {
            @Override
            public void onChanged(@Nullable Track track) {
                trackName.setText(track.getName());
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playing_status, container, false);
        trackName = (TextView)view.findViewById(R.id.trackName);
        return view;

    }
}
