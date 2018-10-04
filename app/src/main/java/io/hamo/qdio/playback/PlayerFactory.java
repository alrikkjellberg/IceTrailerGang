package io.hamo.qdio.playback;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.spotify.android.appremote.api.SpotifyAppRemote;

public class PlayerFactory {
    private static SpotifyPlayer spotifyPlayer;
    private static MutableLiveData<Boolean> isInstantiated = new MutableLiveData<>();

    public static void instantiatePlayer(SpotifyAppRemote spotifyAppRemote) {
        spotifyPlayer = new SpotifyPlayer(spotifyAppRemote);
        isInstantiated.setValue(true);
    }

    public static Player getPlayer() {
        if(spotifyPlayer == null) {
            String message = "Spotify Player has not yet been instantiated, use the connect fragment to instantiate it";
            Log.e(PlayerFactory.class.getSimpleName(), message);
            throw new RuntimeException(message);
        }
        return spotifyPlayer;
    }

    public static LiveData<Boolean> getIsInstantiated() {
        if(isInstantiated.getValue() == null) isInstantiated.setValue(false);
        return isInstantiated;
    }
}
