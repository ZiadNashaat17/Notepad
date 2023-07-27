package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.todo.db.DatabaseHelper;
import com.example.todo.rv.Model;

public class NoteDetailsActivity extends AppCompatActivity {
    private EditText etTitle, etDescription;
    private int receivedId;
    private boolean openAsUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);

        receivedId = getIntent().getIntExtra("id", -1);
        if (receivedId != -1) {
            setTitle(R.string.update_note);
            etTitle.setText(getIntent().getStringExtra("title"));
            etDescription.setText(getIntent().getStringExtra("description"));
            Button btnUpdate = findViewById(R.id.btn_update);
            btnUpdate.setVisibility(View.VISIBLE);
            openAsUpdate = true;
        } else {
            setTitle(R.string.save_note);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (openAsUpdate) {
            return false;
        } else {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.save_note_menu, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_save_note) {
            saveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();

        if (title.isEmpty()) {
            etTitle.setError(getString(R.string.required_field));
        } else {
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("description", description);
            DatabaseHelper helper = new DatabaseHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            long id = db.insert("TABLE_TITLE_BODY", null, values);
            if (id != -1) {
                Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void updateNote(View view) {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();

        if (title.isEmpty()) {
            etTitle.setError(getString(R.string.required_field));
        } else {
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("description", description);
            DatabaseHelper helper = new DatabaseHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();

            String[] whereArgs = {String.valueOf(receivedId)};
            int id = db.update("TABLE_TITLE_BODY", values, "id == ?", whereArgs);
            if (id != 0) {
                Toast.makeText(this, R.string.note_updated, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}