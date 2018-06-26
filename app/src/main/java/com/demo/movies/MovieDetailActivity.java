package com.demo.movies;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by Harshit on 6/24/2018.
 */

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView image;

    private TextView title, rating, overview;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        image = (ImageView) findViewById(R.id.poster);

        title = (TextView) findViewById(R.id.title);
        overview = (TextView)findViewById(R.id.overview);
        rating = (TextView)findViewById(R.id.rating);

        MovieDetails details = (MovieDetails) getIntent().getExtras().getSerializable("MOVIE_DETAILS");

        if(details != null){

            Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+ details.getPoster_path()).into(image);
            title.setText(details.getOriginal_title());
            rating.setText(Double.toString(details.getVote_average()));
            overview.setText(details.getOverview());
        }
    }
}
