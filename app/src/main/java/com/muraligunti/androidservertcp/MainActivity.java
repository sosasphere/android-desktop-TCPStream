package com.muraligunti.androidservertcp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    ServerSocket serverSocket;
    Socket clientSocket;
    TextView debugText;
    VideoView videoView;
    StartServerThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        debugText = (TextView) findViewById(R.id.dispText);
        videoView = (VideoView) findViewById(R.id.videoView);
        thread = new StartServerThread();
        new Thread(thread).start();
    }

    public class StartServerThread implements Runnable {
        FileOutputStream datafromTCP;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                clientSocket = serverSocket.accept();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        debugText.setText("Connection Accepted");
                    }
                });
                //if not SD card mounted, return
                if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    return;
                }
                byte[] buffer = new byte[1024];
                File videoFromStream = new File(getExternalFilesDir(null), "receivedVideo.mp4");
                videoFromStream.createNewFile();
                datafromTCP = new FileOutputStream(videoFromStream, true);
                int count = clientSocket.getInputStream().read(buffer, 0, 1024);
                while (count != -1) {
                    datafromTCP.write(buffer, 0, count);
                    count = clientSocket.getInputStream().read(buffer, 0, 1024);
                    datafromTCP.flush();
                }
                datafromTCP.close();
                clientSocket.close();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        debugText.setText("Done Receiving. File is stored on your android, Path: " + getExternalFilesDir(null).toString());

                        String uriPath = getExternalFilesDir(null).toString() + "/receivedVideo.mp4";
                        videoView.setVideoPath(uriPath);
                        videoView.start();
                    }
                });

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}