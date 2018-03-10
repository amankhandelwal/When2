package com.when.threemb.when;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.renderscript.Sampler;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        VideoView videoView =(VideoView)findViewById(R.id.videoView1);
        //Creating MediaController
        /*MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.hide();
        mediaController.setVisibility(View.GONE);*/

        //specify the location of media file
        String uril = "android.resource://" + getPackageName() + "/" + R.raw.splash;
        Uri uri=Uri.parse(uril/*Environment.getExternalStorageDirectory().getPath()+"/media/1.mp4"*/);

        //Setting MediaController and URI, then starting the videoView
        //videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        //videoView.setVisibility(View.INVISIBLE);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //finish();
                final Intent i=new Intent(Splash.this,MainActivity.class);
                startActivity(i);
                finish();
                //Toast.makeText(MainActivity.this,"VIDEO FINISHED",Toast.LENGTH_SHORT).show();

            }
        });
    }
}
