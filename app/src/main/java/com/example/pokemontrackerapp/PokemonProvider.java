package com.example.pokemontrackerapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.NonNull;

public class PokemonProvider extends ContentProvider {
    public static final String DBNAME = "PokemonDB";
    public static final String TABLENAME = "Pokedex";
    public static final String COL_NATNUM = "NationalNumber";
    public static final String COL_NAME = "Name";
    public static final String COL_SPECIES = "Species";
    public static final String COL_GENDER = "Gender";
    public static final String COL_HEIGHT = "Height";
    public static final String COL_WEIGHT = "Weight";
    public static final String COL_LEVEL = "Level";
    public static final String COL_HP = "HP";
    public static final String COL_ATTACK = "Attack";
    public static final String COL_DEFENSE = "Defense";

    public final static String SQL_CREATE_TABLE = "CREATE TABLE " + TABLENAME + " (" +
            COL_NATNUM + " INTEGER PRIMARY KEY, " +
            COL_NAME + " TEXT, " +
            COL_SPECIES + " TEXT, " +
            COL_GENDER + " TEXT, " +
            COL_HEIGHT + " REAL, " +
            COL_WEIGHT + " REAL, " +
            COL_LEVEL + " INTEGER, " +
            COL_HP + " INTEGER, " +
            COL_ATTACK + " INTEGER, " +
            COL_DEFENSE + " INTEGER" +
            ")";
    public final static Uri CONTENT_URI = Uri.parse("content://com.example.pokemontracker.provider");

    MainDatabaseHelper mHelper;
    protected final class MainDatabaseHelper extends SQLiteOpenHelper {
        public MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            // if you want to add a column or something
        }
    }
    public PokemonProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mHelper.getWritableDatabase().delete(TABLENAME, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        String natNum = values.getAsString(COL_NATNUM);
        String name = values.getAsString(COL_NAME);
        String species = values.getAsString(COL_SPECIES);
        String gender = values.getAsString(COL_GENDER);
        String height = values.getAsString(COL_HEIGHT);
        String weight = values.getAsString(COL_WEIGHT);
        String level = values.getAsString(COL_LEVEL);
        String hp = values.getAsString(COL_HP);
        String attack = values.getAsString(COL_ATTACK);
        String defense = values.getAsString(COL_DEFENSE);
        mHelper.getWritableDatabase().insert(TABLENAME, null, values);
        return uri;
    }

    @Override
    public boolean onCreate() {
        mHelper = new MainDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return mHelper.getReadableDatabase().query(TABLENAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
