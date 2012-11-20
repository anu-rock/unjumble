package com.anuragbhandari.unjumble.database;

public class WordsTable {
	// Table Structure
	public static final String TABLE_WORDS = "Words";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_WORD = "word";
	public static final String COLUMN_WORDHASH = "word_hash";
	public static final String COLUMN_WORDMEANING = "word_meaning";
	
	// Table creation SQL statement
	//private static final String TABLE_CREATE = "create table " 
	//    + TABLE_WORDS
	//    + "(" 
	//    + COLUMN_ID + " integer primary key autoincrement, " 
	//    + COLUMN_WORD + " varchar(25) not null, " 
	//    + COLUMN_WORDHASH + " varchar(25) not null, "
	//    + COLUMN_WORDMEANING + " text"
	//    + ");";
}
