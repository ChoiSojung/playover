package com.playover;

import android.content.SearchRecentSuggestionsProvider;

public class RecentSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.RecentSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public RecentSuggestionProvider(){
        setupSuggestions(AUTHORITY, MODE);
    }
}
