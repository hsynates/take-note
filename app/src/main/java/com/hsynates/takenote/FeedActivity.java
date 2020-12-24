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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;

public class FeedActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<Integer> arrayListId;
    ArrayList<String> arrayListNotes;
    ArrayAdapter arrayAdapter;
    AlertDialog.Builder alert;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteDatabase = this.openOrCreateDatabase("Notes", MODE_PRIVATE, null);

        toolbar = findViewById(R.id.ToolbarLayoutFeed);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);

        listView = findViewById(R.id.listView);
        arrayListId = new ArrayList<>();
        arrayListNotes = new ArrayList<>();
        alert = new AlertDialog.Builder(FeedActivity.this);

        arrayAdapter = new ArrayAdapter(FeedActivity.this, android.R.layout.simple_list_item_1, arrayListNotes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent = new Intent(FeedActivity.this, ViewNoteActivity.class);
                intent.putExtra("id", arrayListId.get(i));
                startActivity(intent);
            }
        });
        getDataFromSQLite();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_Note) {
            intent = new Intent(FeedActivity.this, AddNoteActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.delete_all) {
            alert.setTitle("WARNING");
            alert.setMessage("Do you want to delete all your notes?");
            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        sqLiteDatabase = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
                        sqLiteDatabase.execSQL("DELETE FROM notes");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "All notes have been deleted.", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(), "Your note has been deleted", Toast.LENGTH_SHORT).show();
                    arrayAdapter.notifyDataSetChanged();
                }
            });
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDataFromSQLite() {
        try {
            sqLiteDatabase = this.openOrCreateDatabase("Notes", MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes", null);

            int idIx = cursor.getColumnIndex("id");
            int notesIx = cursor.getColumnIndex("notes");

            while (cursor.moveToNext()) {
                arrayListId.add(cursor.getInt(idIx));
                arrayListNotes.add(cursor.getString(notesIx));
            }
            cursor.close();
            arrayAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNote(View view) {
        intent = new Intent(FeedActivity.this, AddNoteActivity.class);
        startActivity(intent);
    }

    public void deleteAllNote(View view) {
        alert.setTitle("WARNING");
        alert.setMessage("Do you want to delete all your notes?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    sqLiteDatabase = openOrCreateDatabase("Notes", MODE_PRIVATE, null);
                    sqLiteDatabase.execSQL("DELETE FROM notes");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "All notes have been deleted.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Your note has not been deleted", Toast.LENGTH_SHORT).show();
                arrayAdapter.notifyDataSetChanged();
            }
        });
        alert.show();
    }
}