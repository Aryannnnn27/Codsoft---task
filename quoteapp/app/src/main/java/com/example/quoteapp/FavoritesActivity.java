package com.example.quoteapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private ListView favoritesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> favoriteQuotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoritesListView = findViewById(R.id.favoritesListView);
        favoriteQuotes = getIntent().getStringArrayListExtra("favorites");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favoriteQuotes);
        favoritesListView.setAdapter(adapter);
    }
}
