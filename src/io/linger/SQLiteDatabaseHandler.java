package io.linger;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
/**
 * Database handler that deals with the temporary login table, which allows
 * the app to keep track of whether a user is logged in, and who that user is.
 * 
 */

public class SQLiteDatabaseHandler extends SQLiteOpenHelper
{
    // All static variables
    private static final int DATABASE_VERSION = 1;
 
    private static final String DATABASE_NAME = "linger_db";

    /* Login table specific constants */
    private static final String TABLE_LOGIN = "login";
    
    // php tags
    public static final String TAG_POST = "post_data";
    public static final String TAG_GET = "get_data";
    
    // Login table column names
	public static final String USER_ID = "user_id";
	public static final String USER_NAME = "user_name";
	public static final String USER_PHONE = "user_phone";
	public static final String USER_HASH = "hash";
	public static final String USER_UNENCRYPTED_PASS = "user_unencrypted_pass";
	public static final String USER_SALT = "user_salt";
	public static final String USER_EMAIL = "user_email";
	public static final String USER_ACCESS_TOKEN = "secret_key";
	
    /** Constructor. */
    public SQLiteDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    /**
     * Creating tables containing login data and logged in user's network's
     * data.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + USER_PHONE + " TEXT UNIQUE,"
                + USER_ACCESS_TOKEN + "TEXT" + ");";
        db.execSQL(CREATE_LOGIN_TABLE);
    }
    
    /**
     * Storing user details in database (called when user logs in).
     * */
    public void addUser(String userPhone, String userAccessToken)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // creating row containing user data
        ContentValues values = new ContentValues();

        values.put(USER_PHONE, userPhone);
        values.put(USER_ACCESS_TOKEN, userAccessToken);
        
        db.insert(TABLE_LOGIN, null, values); // insert row
        db.close(); // Closing database connection
    }
     
    /**
     * Getting the logged in user's data from database.
     * @return HashMap with user's name, email, network, uid & created_at
     * */
    public HashMap<String, String> getUserDetails()
    {
    	HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst(); // Move to first row (only one row in table)
        // fill hashmap "user"
        if(cursor.getCount() > 0)
        {
            user.put(USER_PHONE, cursor.getString(1));
            user.put(USER_ACCESS_TOKEN, cursor.getString(2));
        }
        cursor.close();
        db.close();
        return user;
    }
 
    /**
     * Used for getting user login status; user is logged in when there are
     * rows in the table.
     * */
    public int getLoginRowCount()
    {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }
     
    /**
     * Reset table by deleting the contents.
     * */
    public void resetTables()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOGIN, null, null); // Delete all rows
        db.close();
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        // Create tables again
        onCreate(db);
    }
}