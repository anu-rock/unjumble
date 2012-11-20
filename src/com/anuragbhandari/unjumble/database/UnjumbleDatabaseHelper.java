package com.anuragbhandari.unjumble.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

public class UnjumbleDatabaseHelper extends SQLiteOpenHelper {

  private static final String DB_NAME = "wordlist.db";
  private static final int DB_VERSION = 2;
  private static String DB_PATH = Environment.getDataDirectory() + "/data/com.anuragbhandari.unjumble/databases/";
  private SQLiteDatabase myDataBase; 
  private final Context myContext;

  public UnjumbleDatabaseHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
    this.myContext = context;
  }
  
  /**
   * Creates a empty database on the system and rewrites it with your own database.
   */
  public void createDataBase() throws IOException { 
	boolean dbExist = checkDataBase();
	if (dbExist) {
		// Do nothing - database already exists
	}
	else {
		// By calling this method an empty database will be created into the
		// default system path of this app. After that, we'll overwrite that database
		// with our own SQLite database file.
		this.getReadableDatabase();
		try {
			copyDataBase();
		}
		catch (IOException e) {
			throw new Error("Error copying database");
		}
	}
  }
  
  public void openDataBase() throws SQLException {
  	// Open the database
    String myPath = DB_PATH + DB_NAME;
  	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
  }
  
  /**
   * Checks if the database already exists to avoid re-copying the file each time we open the app.
   * @return true if it exists, false if it doesn't
   */
  private boolean checkDataBase() {
  	SQLiteDatabase checkDB = null;
  	try {
  		String myPath = DB_PATH + DB_NAME;
  		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
  	}
  	catch(SQLiteException e) {
  		// database does't exist yet.
  	}
  	if(checkDB != null) {
  		checkDB.close();
  	}
  	return checkDB != null ? true : false;
  }
  
  /**
   * Copies your database from your local assets-folder to the just created empty database in the
   * system folder, from where it can be accessed and handled.
   * This is done by transferring bytestream.
   */
  private void copyDataBase() throws IOException{
  	// Open your local db as the input stream
  	InputStream myInput = myContext.getAssets().open(DB_NAME);
  	// Path to the just created empty db
  	String outFileName = DB_PATH + DB_NAME;
  	// Open the empty db as the output stream
  	OutputStream myOutput = new FileOutputStream(outFileName);
  	// Transfer bytes from the inputfile to the outputfile
  	byte[] buffer = new byte[1024];
  	int length;
  	while ((length = myInput.read(buffer)) > 0) {
  		myOutput.write(buffer, 0, length);
  	}
  	// Close the streams
  	myOutput.flush();
  	myOutput.close();
  	myInput.close();
  }
  
  @Override
  public synchronized void close() {
    if(myDataBase != null)
	    myDataBase.close();

    super.close();
	}

  // This method is called during creation of the database
  @Override
  public void onCreate(SQLiteDatabase database) {
    // Normally one would create their DB here, but as I'm using an existing one
	// so I'll leave this empty
  }

  // Method is called during an upgrade of the database,
  // e.g. if you increase the database version
  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
	  boolean dbExist = checkDataBase();
	  if (dbExist) {
		// Delete the existing DB
		String myPath = DB_PATH + DB_NAME;
		File db = new File(myPath);
		db.delete();
	  }
	  else {
		// Copy the new DB
		this.getReadableDatabase();
		try {
			copyDataBase();
			Toast.makeText(myContext, "New database copied!", Toast.LENGTH_LONG).show();
		}
		catch (IOException e) {
			throw new Error("Error copying database");
		}
	  }
  }
}
