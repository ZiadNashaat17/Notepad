package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.todo.db.DatabaseHelper;
import com.example.todo.rv.Adapter;
import com.example.todo.rv.Model;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageButton iBtnDelete, iBtnEdit;
    ArrayList<Model> data = new ArrayList<>();
    Adapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.all_notes);
        iBtnEdit = findViewById(R.id.i_btn_edit);



    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void openNoteDetailsActivity(View view) {
        Intent intent = new Intent(this, NoteDetailsActivity.class);
        startActivity(intent);
    }

    private void getData() {
        data.clear();
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TABLE_TITLE_BODY", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            data.add(new Model(title, description, id));
        }
        showData();
    }

    private void showData() {
        View view = findViewById(R.id.no_notes_available);
        if (data.size() == 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
            adapter = new Adapter(this,data);
            recyclerView = findViewById(R.id.homeRecycler);
            recyclerView.setAdapter(adapter);
            swipeToDelete();
        }
    }

    private void swipeToDelete() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView
                    , @NonNull RecyclerView.ViewHolder viewHolder
                    , @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                showDeleteDialog(position);
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    public void deleteFromDB(int position) {
        DatabaseHelper helper = new DatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] args = {String.valueOf(data.get(position).getId())};
        int deletedRows = db.delete("TABLE_TITLE_BODY", "id ==?", args);
        if (deletedRows != 0) {
            Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show();
        }
    }

    public void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_message)
                .setPositiveButton(R.string.delete_dialog_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteFromDB(position);
                        data.remove(position);
                        adapter.notifyDataSetChanged();
                        if (data.size() == 0) {
                            View view = findViewById(R.id.no_notes_available);
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .setNegativeButton(R.string.delete_dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.notifyItemChanged(position);
                    }
                })
                .setCancelable(false)
                .show();
    }
}