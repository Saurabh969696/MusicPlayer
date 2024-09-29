package com.example.musicplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    MediaPlayer music;
    SeekBar seekBar, volumeControl;
    TextView currentTime, totalTime;
    Handler handler = new Handler();
    AudioManager audioManager;

    // Playlist and song index
    int[] playlist = {R.raw.song1, R.raw.song2, R.raw.song3};
    int currentSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize MediaPlayer with the first song in the playlist
        music = MediaPlayer.create(this, playlist[currentSongIndex]);

        // Initialize SeekBar and volume control like before
        initializePlayer();

        // Update SeekBar and current time every second
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (music != null && music.isPlaying()) {
                    seekBar.setProgress(music.getCurrentPosition());
                    currentTime.setText(formatTime(music.getCurrentPosition()));
                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    // Initialize SeekBar and TextViews
    private void initializePlayer() {
        // Initialize SeekBar for music progress
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(music.getDuration());

        // Initialize TextViews for current and total time
        currentTime = findViewById(R.id.currentTime);
        totalTime = findViewById(R.id.totalTime);
        totalTime.setText(formatTime(music.getDuration()));

        // Handle SeekBar for music progress
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    music.seekTo(progress);
                    currentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Initialize volume control SeekBar
        volumeControl = findViewById(R.id.volumeSeekBar);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeControl.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeControl.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        // Handle volume control changes
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Play music
    public void musicplay(View v) {
        if (music != null && !music.isPlaying()) {
            music.start();
        }
    }

    // Pause music
    public void musicpause(View v) {
        if (music != null && music.isPlaying()) {
            music.pause();
        }
    }

    // Stop music and reset the player
    public void musicstop(View v) {
        if (music != null) {
            music.stop();
            music = MediaPlayer.create(this, playlist[currentSongIndex]);
            seekBar.setProgress(0); // Reset SeekBar to 0
            currentTime.setText(formatTime(0)); // Reset current time to 0
        }
    }

    // Play next song (loop back to first song if at the end)
    public void playNextSong(View v) {
        if (currentSongIndex < playlist.length - 1) {
            currentSongIndex++;
        } else {
            currentSongIndex = 0; // Loop back to the first song
        }
        playSong();
    }

    // Play previous song (loop back to last song if at the beginning)
    public void playPreviousSong(View v) {
        if (currentSongIndex > 0) {
            currentSongIndex--;
        } else {
            currentSongIndex = playlist.length - 1; // Loop back to the last song
        }
        playSong();
    }

    // Play the song at the current index
    private void playSong() {
        if (music != null) {
            music.stop();
            music.release();
        }
        music = MediaPlayer.create(this, playlist[currentSongIndex]);
        music.start();
        seekBar.setMax(music.getDuration());
        totalTime.setText(formatTime(music.getDuration()));
        currentTime.setText(formatTime(0));
    }

    // Format milliseconds to mm:ss
    private String formatTime(int ms) {
        int minutes = ms / 1000 / 60;
        int seconds = (ms / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
