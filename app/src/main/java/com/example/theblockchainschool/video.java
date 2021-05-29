package com.example.theblockchainschool;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class video extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        MediaController mediaController = new MediaController(this);


/*
        videoView = findViewById(R.id.video);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        Uri uri = Uri.parse("gs://the-blockchain-school.appspot.com/The Blockchain School - Learn Blockchain & Beyond - Introduction V.1.0 (1).mp4");
        videoView.setVideoURI(uri);
        videoView.start();

 */
    }
}
