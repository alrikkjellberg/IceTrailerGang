package io.hamo.qdio.view.playing;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.util.Log;


import io.hamo.qdio.music.Track;
import io.hamo.qdio.playback.PlayerFactory;

public class PlayingStatusViewModel extends ViewModel {
    private static final int STATUS_POLL_INTERVAL = 500;
    private final MutableLiveData<Track> currentlyPlayingTrack;
    private final MutableLiveData<Long> currentPosition;
    private final MutableLiveData<String> currentAlbumImageUrl;
    //private final MutableLiveData<PlayerState> currentPlayerState;
    private Handler handler = new Handler();

    public PlayingStatusViewModel() {
        currentlyPlayingTrack = new MutableLiveData<>();
        currentPosition = new MutableLiveData<>();
        currentAlbumImageUrl = new MutableLiveData<>();
        //TODO change this
        handler.postDelayed(runnableCode, 2000);
        //currentPlayerState = new MutableLiveData<>();


    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            currentlyPlayingTrack.postValue(PlayerFactory.getPlayer().getCurrentTrack());
            currentAlbumImageUrl.postValue(PlayerFactory.getPlayer().getCurrentTrack().getImageURL());
            Log.d("Handlers", "Called on main thread");
            // Repeat this the same runnable code block again another .5 seconds
            // 'this' is referencing the Runnable object
            handler.postDelayed(this, STATUS_POLL_INTERVAL);
        }
    };


    //public LiveData<String> //Bitmap

    public LiveData<Track> getCurrentlyPlayingTrack() {
        return currentlyPlayingTrack;
    }

    public LiveData<Long> getCurrentPosition() {
        return currentPosition;
    }

}
