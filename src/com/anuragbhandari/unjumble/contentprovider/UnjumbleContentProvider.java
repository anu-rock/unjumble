package com.anuragbhandari.unjumble.contentprovider;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import com.anuragbhandari.unjumble.database.UnjumbleDatabaseHelper;
import com.anuragbhandari.unjumble.database.WordsTable;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class UnjumbleContentProvider extends ContentProvider {
  private UnjumbleDatabaseHelper database;

  // Used for the UriMacher
  private static final int WORDS = 10;

  private static final String AUTHORITY = "com.anuragbhandari.unjumble.contentprovider";

  private static final String BASE_PATH = "words";
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
  + "/" + BASE_PATH);

  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
      + "/words";

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
    sURIMatcher.addURI(AUTHORITY, BASE_PATH, WORDS);
  }

  @Override
  public boolean onCreate() {
    database = new UnjumbleDatabaseHelper(getContext());
    try {
		database.createDataBase();
	} catch (IOException e) {
		throw new Error("Unable to create database");
	}
    database.openDataBase();
    return false;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Using SQLiteQueryBuilder instead of query() method
	SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
	
	// Check if the caller has requested a column which does not exists
	checkColumns(projection);
	
	// Set the table
	queryBuilder.setTables(WordsTable.TABLE_WORDS);
	
	int uriType = sURIMatcher.match(uri);
	switch (uriType) {
		case WORDS:
		  break;
		default:
		  throw new IllegalArgumentException("Unknown URI: " + uri);
	}
	
	SQLiteDatabase db = database.getWritableDatabase();
	Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	// Make sure that potential listeners are getting notified
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  private void checkColumns(String[] projection) {
    String[] available = { WordsTable.COLUMN_WORD, WordsTable.COLUMN_WORDMEANING, WordsTable.COLUMN_WORDHASH, WordsTable.COLUMN_ID };
    if (projection != null) {
      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
      // Check if all columns which are requested are available
      if (!availableColumns.containsAll(requestedColumns)) {
        throw new IllegalArgumentException("Unknown columns in projection");
      }
    }
  }
  
  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public int delete(Uri arg0, String arg1, String[] arg2) {
	throw new UnsupportedOperationException();
  }

  @Override
  public Uri insert(Uri arg0, ContentValues arg1) {
	throw new UnsupportedOperationException();
  }

  @Override
  public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
	throw new UnsupportedOperationException();
  }
}
