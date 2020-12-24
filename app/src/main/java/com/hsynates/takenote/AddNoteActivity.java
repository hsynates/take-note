package com.hsynates.takenote;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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

public class AddNoteActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText editTextNote;
    String note;
    AlertDialog.Builder alert;
    Intent intent;
    SQLiteDatabase sqLiteDatabase;
    SQLiteStatement sqLiteStatement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        sqLiteDatabase = this.openOrCreateDatabase("Notes", MODE_PRIVATE, null);

        toolbar = findViewById(R.id.ToolbarLayoutAddNote);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextNote = findViewById(R.id.editTextNote);
        alert = new AlertDialog.Builder(AddNoteActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_Note) {
            note = editTextNote.getText().toString();
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
                    sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY, notes VARCHAR)");
                    //sqLiteDatabase.execSQL("INSERT INTO notes(notes) VALUES(?)");
                    String stringSQL = "INSERT INTO notes(notes) VALUES(?)";
                    sqLiteStatement = sqLiteDatabase.compileStatement(stringSQL);
                    sqLiteStatement.bindString(1, note);
                    sqLiteStatement.execute();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent = new Intent(AddNoteActivity.this, FeedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(getApplicationContext(), "Your note has been saved.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveNote(View view) {
        note = editTextNote.getText().toString();
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
                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS notes(id INTEGER PRIMARY KEY, notes VARCHAR)");
                //sqLiteDatabase.execSQL("INSERT INTO notes(notes) VALUES(?)");
                String stringSQL = "INSERT INTO notes(notes) VALUES(?)";
                sqLiteStatement = sqLiteDatabase.compileStatement(stringSQL);
                sqLiteStatement.bindString(1, note);
                sqLiteStatement.execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
            intent = new Intent(AddNoteActivity.this, FeedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Toast.makeText(getApplicationContext(), "Your note has been saved.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }
}