package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    // Doing SQL query here to hold data from the notes

    Context context;
    private static final String DName = "MadeNotes"; // Database Name
    private static final int DVersion = 1; // Database Version
    private static final String TName = "madenotes"; // Table Name
    private static final String CId = "id"; // Column ID
    private static final String CTitle = "title"; // Column Title
    private static final String CDescript = "description"; // Column Description


    public Database(@Nullable Context context) {
        super(context, DName, null, DVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

            String query = "CREATE TABLE " + TName +
                    " (" + CId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CTitle + " TEXT, " +
                    CDescript + " TEXT); ";

            sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TName);
        onCreate(sqLiteDatabase);
    }

    void addNote(String title, String description) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CTitle, title);
        contentValues.put(CDescript, description);

        long resultValue = sqLiteDatabase.insert(TName, null, contentValues);

        if (resultValue == -1) {
            Toast.makeText(context, "Data Not Added", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(context, "Data Added Successgully", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TName;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = null;
        if (sqLiteDatabase != null) {
            cursor = sqLiteDatabase.rawQuery(query, null);
        }
        return cursor;
    }

    void deleteNotes() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TName;
        sqLiteDatabase.execSQL(query); //execute query
    }

    void updateNotes(String title, String description, String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CTitle, title);
        contentValues.put(CDescript, description);

        long result = sqLiteDatabase.update(TName, contentValues, "id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
        }
    }

    public void slideDeleteNote(String id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(TName, "id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Item Not Deleted", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
