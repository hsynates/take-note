package com.hsynates.takenote;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class ViewNoteActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText editTextviewNote;
    String note;
    SQLiteDatabase sqLiteDatabase;
    AlertDialog.Builder alert;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        sqLiteDatabase = this.openOrCreateDatabase("Notes", MODE_PRIVATE, null);
        editTextviewNote = findViewById(R.id.editTextViewNote);
        alert = new AlertDialog.Builder(ViewNoteActivity.this);

        toolbar = findViewById(R.id.ToolbarLayoutViewNote);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("View Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        int intentId = intent.getIntExtra("id", 1);

        try {
            sqLiteDatabase = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes WHERE id = ?", new String[]{String.valueOf(intentId)});

            int notesIx = cursor.getColumnIndex("notes");

            while (cursor.moveToNext()) {
                editTextviewNote.setText((cursor.getString(notesIx)));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_operation_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.update_Note) {
            intent = getIntent();
            int intentId = intent.getIntExtra("id", 1);
            note = editTextviewNote.getText().toString();
            if (note.matches("")) {
                alert.setTitle("WARNING");
                alert.setMessage("");
                alert.setMessage("Note cannot be blank.");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
            } else {
                try {
                    sqLiteDatabase = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
                    sqLiteDatabase.execSQL("UPDATE notes set notes = ? WHERE id = ?", new String[]{String.valueOf(note), String.valueOf((intentId))});
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent = new Intent(ViewNoteActivity.this, FeedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(getApplicationContext(), "Your note has been updated.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        } else if (item.getItemId() == R.id.delete_Note) {
            alert.setTitle("DELETE");
            alert.setMessage("Do you want to delete this note?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    intent = getIntent();
                    int intentId = intent.getIntExtra("id", 1);
                    try {
                        sqLiteDatabase = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
                        sqLiteDatabase.execSQL("DELETE FROM notes WHERE id = ?", new String[]{String.valueOf(intentId)});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    intent = new Intent(ViewNoteActivity.this, FeedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(getApplicationContext(), "Your note has been deleted", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "The deletion has been canceled.", Toast.LENGTH_SHORT).show();
                }
            });
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteNote(View view) {
        alert.setTitle("DELETE");
        alert.setMessage("Do you want to delete this note?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                intent = getIntent();
                int intentId = intent.getIntExtra("id", 1);
                try {
                    sqLiteDatabase = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
                    sqLiteDatabase.execSQL("DELETE FROM notes WHERE id = ?", new String[]{String.valueOf(intentId)});
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent = new Intent(ViewNoteActivity.this, FeedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(getApplicationContext(), "Your note has been deleted", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "The deletion has been canceled.", Toast.LENGTH_SHORT).show();
            }
        });
        alert.show();
    }

    public void updateNote(View view) {
        intent = getIntent();
        int intentId = intent.getIntExtra("id", 1);
        note = editTextviewNote.getText().toString();
        if (note.matches("")) {
            alert.setTitle("WARNING");
            alert.setMessage("Note cannot be blank.");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setNegativeButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alert.show();
        } else {
            try {
                sqLiteDatabase = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
                sqLiteDatabase.execSQL("UPDATE notes set notes = ? WHERE id = ?", new String[]{String.valueOf(note), String.valueOf((intentId))});
            } catch (Exception e) {
                e.printStackTrace();
            }
            intent = new Intent(ViewNoteActivity.this, FeedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Toast.makeText(getApplicationContext(), "Your note has been updated.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }
}