package com.demo.movies;

import android.content.Intent;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

        private ListView listView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            listView = (ListView) findViewById(R.id.list);
            listView.setOnItemClickListener(this);

            new CheckConnectionStatus().execute("https://api.themoviedb.org/3/movie/upcoming?api_key=b7cd3340a794e5a2f35e3abb820b497f");
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("MOVIE_DETAILS", (MovieDetails)adapterView.getItemAtPosition(i));
            startActivity(intent);
        }

            class CheckConnectionStatus extends AsyncTask<String, Void, String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected String doInBackground(String... params) {
                    URL url = null;
                    try {
                        url = new URL(params[0]);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    try {
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                        //Getting inputstream from connection, that is response which we got from server
                        InputStream inputStream = urlConnection.getInputStream();
                        //Reading the response
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String s = bufferedReader.readLine();
                        bufferedReader.close();
                        //Returning the reponse to onPostExexute Method
                        return s;
                    } catch (IOException e) {
                        Log.e("Error", e.getMessage(), e);
                    }
                    return null;
                }

                @Override
                //This method runs on UIThread and it will execute when doInBackground is completed
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        ArrayList<MovieDetails> movieList = new ArrayList<>();

                        JSONArray jsonArray = jsonObject.getJSONArray("results");

                        for(int i=0 ; i<jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            MovieDetails movieDetails = new MovieDetails();
                            movieDetails.setOriginal_title(object.getString("original_title"));
                            movieDetails.setVote_average(object.getDouble("vote_average"));
                            movieDetails.setOverview(object.getString("overview"));
                            movieDetails.setRelease_date(object.getString("release_date"));
                            movieDetails.setPoster_path(object.getString("poster_path"));
                            movieDetails.setAdult(Boolean.parseBoolean(object.getString("adult")));
                            movieList.add(movieDetails);
                        }
                        MovieArrayAdapter movieArrayAdapter = new MovieArrayAdapter(MainActivity.this, R.layout.movie_list, movieList);
                        listView.setAdapter(movieArrayAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

