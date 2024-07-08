package com.example.quoteapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView quoteTextView;
    private Button shareButton;
    private Button refreshButton;
    private Button viewFavoritesButton;
    private String[] quotes;
    private String currentQuote;
    private ArrayList<String> favoriteQuotes;
    private SharedPreferences sharedPreferences;
    private ImageButton favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quoteTextView = findViewById(R.id.quoteTextView);
        shareButton = findViewById(R.id.shareButton);
        refreshButton = findViewById(R.id.refreshButton);
        viewFavoritesButton = findViewById(R.id.viewFavoritesButton);
        favoriteButton = findViewById(R.id.favoriteButton);

        sharedPreferences = getSharedPreferences("QuoteAppPrefs", Context.MODE_PRIVATE);
        favoriteQuotes = new ArrayList<>(sharedPreferences.getStringSet("favorites", new HashSet<>()));

        quotes = new String[]{
                "The best way to predict the future is to invent it.",
                "Life is 10% what happens to us and 90% how we react to it.",
                "Your time is limited, don’t waste it living someone else’s life.",
                "The only way to do great work is to love what you do.",
                "You miss 100% of the shots you don’t take.",
                "Every moment is a fresh beginning.",
                "Aspire to inspire before we expire."
        };

        currentQuote = getRandomQuote();
        quoteTextView.setText(currentQuote);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQuote(currentQuote);
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshQuote();
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });

        viewFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFavorites();
            }
        });

        // Initially update the favorite button state
        updateFavoriteButton();
    }

    private String getRandomQuote() {
        Random random = new Random();
        return quotes[random.nextInt(quotes.length)];
    }

    private void refreshQuote() {
        currentQuote = getRandomQuote();
        quoteTextView.setText(currentQuote);
        updateFavoriteButton();
    }

    private void shareQuote(String quote) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, quote);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void toggleFavorite() {
        String message;
        if (favoriteQuotes.contains(currentQuote)) {
            favoriteQuotes.remove(currentQuote);
            message = "Removed from favorites";
        } else {
            favoriteQuotes.add(currentQuote);
            message = "Added to favorites";
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>(favoriteQuotes);
        editor.putStringSet("favorites", set);
        editor.apply();

        updateFavoriteButton();
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void updateFavoriteButton() {
        if (favoriteQuotes.contains(currentQuote)) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_favorite_outline);
        }
    }

    private void viewFavorites() {
        Intent intent = new Intent(this, FavoritesActivity.class);
        intent.putStringArrayListExtra("favorites", favoriteQuotes);
        startActivity(intent);
    }
}
