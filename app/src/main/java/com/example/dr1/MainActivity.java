package com.example.dr1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editTextSearch, editTextTag;
    private Button buttonSave, buttonClear;
    private LinearLayout tagContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextTag = findViewById(R.id.editTextTag);
        buttonSave = findViewById(R.id.buttonSave);
        buttonClear = findViewById(R.id.buttonClear);
        tagContainer = findViewById(R.id.tagContainer);

        if (tagContainer == null) {
            Log.e("MainActivity", "tagContainer is NULL! Check activity_main.xml");
        }

        buttonSave.setOnClickListener(view -> {
            String searchQuery = editTextSearch.getText().toString().trim();
            String tag = editTextTag.getText().toString().trim();

            Log.d("MainActivity", "Save button clicked");

            if (!searchQuery.isEmpty() && !tag.isEmpty()) {
                Log.d("MainActivity", "Adding tag: " + tag + " (" + searchQuery + ")");
                addTag(tag + " (" + searchQuery + ")");
                editTextTag.setText("");
                editTextSearch.setText("");
            } else {
                Log.w("MainActivity", "Search query or tag is empty!");
            }
        });

        buttonClear.setOnClickListener(view -> {
            Log.d("MainActivity", "Clear button clicked");
            tagContainer.removeAllViews();
        });
    }

    private void addTag(String tagText) {

        for (int i = 0; i < tagContainer.getChildCount(); i++) {
            LinearLayout existingLayout = (LinearLayout) tagContainer.getChildAt(i);
            TextView existingTag = (TextView) existingLayout.getChildAt(0);
            if (existingTag.getText().toString().equalsIgnoreCase(tagText)) {
                Log.w("MainActivity", "Tag already exists: " + tagText);
                return;
            }
        }

        LinearLayout tagLayout = new LinearLayout(this);
        tagLayout.setOrientation(LinearLayout.HORIZONTAL);
        tagLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        tagLayout.setPadding(5, 5, 5, 5);

        TextView tagView = new TextView(this);
        tagView.setText(tagText);
        tagView.setTextSize(16);
        tagView.setPadding(20, 20, 20, 20);
        tagView.setBackgroundResource(R.drawable.tag_background);
        tagView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        ));

        Button editButton = new Button(this);
        editButton.setText("Edit");
        editButton.setPadding(10, 10, 10, 10);
        editButton.setBackgroundResource(R.drawable.tag_button_background);
        editButton.setLayoutParams(new LinearLayout.LayoutParams(
                200,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));

        editButton.setOnClickListener(v -> {
            editTextTag.setText(tagText);
            tagContainer.removeView(tagLayout);
        });

        tagLayout.addView(tagView);
        tagLayout.addView(editButton);
        tagContainer.addView(tagLayout);
    }
}
