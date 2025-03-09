package com.example.dr1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextSearch, editTextTag, dictionarySearch;
    private Button buttonSave, buttonClear, buttonDictionarySearch;
    private LinearLayout tagContainer;
    private TextView dictionaryResult;
    private Map<String, String> dictionary = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextTag = findViewById(R.id.editTextTag);
        buttonSave = findViewById(R.id.buttonSave);
        buttonClear = findViewById(R.id.buttonClear);
        dictionarySearch = findViewById(R.id.dictionarySearch);
        buttonDictionarySearch = findViewById(R.id.buttonDictionarySearch);
        dictionaryResult = findViewById(R.id.dictionaryResult);
        tagContainer = findViewById(R.id.tagContainer);

        loadDictionary();

        buttonSave.setOnClickListener(view -> {
            String searchQuery = editTextSearch.getText().toString().trim();
            String tag = editTextTag.getText().toString().trim();
            if (!searchQuery.isEmpty() && !tag.isEmpty()) {
                addTag(tag + " (" + searchQuery + ")");
                editTextTag.setText("");
                editTextSearch.setText("");
            }
        });

        buttonClear.setOnClickListener(view -> tagContainer.removeAllViews());

        buttonDictionarySearch.setOnClickListener(view -> {
            String query = dictionarySearch.getText().toString().trim().toLowerCase();
            String result = searchDictionary(query);
            dictionaryResult.setText(result);
        });
    }

    private void addTag(String tagText) {
        LinearLayout tagLayout = new LinearLayout(this);
        tagLayout.setOrientation(LinearLayout.HORIZONTAL);
        tagLayout.setPadding(10, 5, 10, 5);

        TextView tagView = new TextView(this);
        tagView.setText(tagText);
        tagView.setTextSize(16);
        tagView.setPadding(10, 5, 10, 5);
        tagView.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light, getTheme()));
        tagView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button editButton = new Button(this);
        editButton.setText("Edit");
        editButton.setTextSize(14);
        editButton.setPadding(10, 5, 10, 5);
        editButton.setOnClickListener(v -> {
            String[] parts = tagText.split(" \\(");
            if (parts.length == 2) {
                editTextSearch.setText(parts[1].replace(")", ""));
                editTextTag.setText(parts[0]);
            }
            tagContainer.removeView(tagLayout);
        });

        tagLayout.addView(tagView);
        tagLayout.addView(editButton);
        tagContainer.addView(tagLayout);
    }

    private void loadDictionary() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("en_mk_recnik.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length == 2) {
                    dictionary.put(parts[0].toLowerCase(), parts[1].toLowerCase());
                    dictionary.put(parts[1].toLowerCase(), parts[0].toLowerCase());
                }
            }
        } catch (IOException e) {
            Log.e("MainActivity", "Error loading dictionary", e);
        }
    }

    private String searchDictionary(String word) {
        return dictionary.getOrDefault(word, "Word not found.");
    }
}
