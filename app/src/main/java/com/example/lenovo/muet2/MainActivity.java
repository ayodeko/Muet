package com.example.lenovo.muet2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    TextView lyricsText;
    EditText track_name;
    EditText artist_name;
    Button lyricsButton;

    public void getLyrics(View view) {


    }

    /*public class DownlooadImage extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {

            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL (urls[0]);
                connection = (HttpURLConnection) url.openConnection();

                InputStream in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);

                return myBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }*/

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url0;
            HttpURLConnection urlConnection = null;

            try {

                url0 = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url0.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;
                    data = reader.read();

                }

                return result;

            } catch (Exception e) {

                e.printStackTrace();
                return "Failed";

            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lyricsText = findViewById(R.id.lyrics_body);
        artist_name = findViewById(R.id.artist);
        track_name = findViewById(R.id.track);

        lyricsButton = findViewById(R.id.button);

        lyricsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DownloadTask tasks = new DownloadTask();

                String result = null;
                String siteName = "deezer";

                String lyrics = "";
                String lyrics_body = "";
                String body = "";


                try {

                    String artist_name_text = artist_name.getText().toString();
                    String track_name_text = track_name.getText().toString();

                    result = tasks.execute("http://api.musixmatch.com/ws/1.1/matcher.lyrics.get?q_track=" + track_name_text + "&q_artist=" + artist_name_text + "&apikey=40dd7cdb65f07fa070efbca3ff6f9061", "https://api.deezer.com/search?limit=5&index=0&q=track:%22soco%22").get();

                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("message");

                    Log.i("Message", message);

                    Pattern p = Pattern.compile("\"lyrics_body\":(.*?)\"lyrics_language\"");
                    Matcher m = p.matcher(message);


                    while (m.find()) {

                        lyrics_body = m.group(1);

                    }


                    String format_lyrics_body = lyrics_body.replace("\\n", System.getProperty("line.separator"));
                    Log.i("Lyrics_Body", lyrics_body);

                    Log.i("artistName", artist_name_text);
                    Log.i("track_name", track_name_text);


                    lyricsText.setText(format_lyrics_body);


                } catch (InterruptedException e) {

                    e.printStackTrace();

                } catch (ExecutionException e) {

                    e.printStackTrace();

                } catch (Exception e) {

                    e.printStackTrace();

                }


                Log.i("CONTENTS OF PAGE", result);
                Log.i("Json Content", result);

                Log.i("Body", body);

                Log.i("lyrics_body", lyrics_body);

                Log.i("lyrics", lyrics);

            }
        });


    }


}

