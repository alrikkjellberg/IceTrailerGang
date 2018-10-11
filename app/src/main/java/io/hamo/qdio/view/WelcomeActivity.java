package io.hamo.qdio.view;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import io.hamo.qdio.R;
import io.hamo.qdio.playback.ConnectFragment;
import io.hamo.qdio.playback.PlayerFactory;
import io.hamo.qdio.room.Room;
import io.hamo.qdio.room.RoomInstanceHolder;
import io.hamo.qdio.room.init.RoomCreationService;

public class WelcomeActivity extends FragmentActivity {
    private ProgressBar roomConnectProgress;
    private LinearLayout buttonGroup;
    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        checkPermissions();
        roomConnectProgress = findViewById(R.id.roomConnectProgress);
        buttonGroup = findViewById(R.id.buttonGroup);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new CustomWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.loadUrl("");
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.w(getClass().getSimpleName(), "Permission not granted", null);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);

        }
    }

    public void createRoom(View view) {
        buttonGroup.setVisibility(View.INVISIBLE);
        roomConnectProgress.setVisibility(View.VISIBLE);
        Fragment connectFragment = new ConnectFragment();
        getSupportFragmentManager().beginTransaction()
            .add(connectFragment, ConnectFragment.TAG).commit();

        final Intent intent = new Intent(this, MainActivity.class);
        final RoomCreationService roomCreationService = new RoomCreationService(this);
        PlayerFactory.getIsInstantiated().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean == null || !aBoolean) {
                    return;
                }
                Room room = roomCreationService.createRoom(getRandomRoomName());
                RoomInstanceHolder.setRoomInstance(room);
                startActivity(intent);
            }
        });
    }

    private static String getRandomRoomName() {
        return String.valueOf((int)Math.floor(100 * Math.random()));
    }

    public void joinRoom(View view) {
        Intent intent = new Intent(this, RoomDiscoveryActivity.class);
        startActivity(intent);
    }
    //class to use for animated progress bar, like a GIF
    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView webView, String url, Bitmap gif) {
            webView.setVisibility(webView.INVISIBLE);
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            roomConnectProgress.setVisibility(View.GONE);

            webView.setVisibility(View.VISIBLE);
            super.onPageFinished(webView, url);

        }
    }


}
