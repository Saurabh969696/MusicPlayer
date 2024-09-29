package com.example.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class MusicBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "PLAY":
                    // Handle play action
                    // Play music using MediaPlayer instance
                    break;
                case "PAUSE":
                    // Handle pause action
                    // Pause the music
                    break;
                case "NEXT":
                    // Handle next song action
                    // Play the next song
                    break;
                case "PREV":
                    // Handle previous song action
                    // Play the previous song
                    break;
            }
        }
    }
}
