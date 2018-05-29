package com.example.niamhtohill.movieappudacity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    public List<ReviewObject> reviews = new ArrayList<>();

    TableLayout reviewsTable;
    TextView noReviewsTV;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews);
        Intent intent = getIntent();
        String movieId = intent.getExtras().getString("movieId");
        new ReviewLoader(ReviewActivity.this, movieId).execute();
    }

    public void setReviewList(List reviews){
        this.reviews = reviews;
        displayReviews();
    }

    public void displayReviews(){
        reviewsTable =findViewById(R.id.reviews_table);
        System.out.println("*************REVIEWS = " + reviews.size());
        if(reviews.size() !=0) {
            noReviewsTV=findViewById(R.id.no_reviews_available);
            noReviewsTV.setVisibility(View.GONE);
            for (int i = 0; i < reviews.size(); i++) {
                View reviewTableRow = LayoutInflater.from(this).inflate(R.layout.review_table_row, null, false);
                TextView reviewAuthor = reviewTableRow.findViewById(R.id.author_tv);
                TextView reviewContent = reviewTableRow.findViewById(R.id.content_tv);
                final ReviewObject reviewObject = reviews.get(i);
                reviewAuthor.setText(reviewObject.getAuthor());
                reviewContent.setText(reviewObject.getContent());
                reviewsTable.addView(reviewTableRow);
                reviewsTable.setVisibility(View.VISIBLE);

            }
        }
    }
}
