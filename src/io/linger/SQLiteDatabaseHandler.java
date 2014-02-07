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
 * Explanation of implementation:
 * The SQLite table in this class starts empty. When a user logs in, the
 * user's information (name, email, network, etc.) are pushed onto that row.
 * When the application is running, data about the user can be 
 * 
 * @author Emily Pakulski, modified Tamada's code
 * @author Ravi Tamada, original code from tutorial on AndroidHive.info
 */

public class SQLiteDatabaseHandler extends SQLiteOpenHelper
{
    // All Static variables
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
	public static final String USER_ENCRYPTED_PASS = "user_encrypted_pass";
	public static final String USER_PASS = "password";
	public static final String USER_SALT = "user_salt";
	public static final String USER_EMAIL = "user_email";
	public static final String USER_CREATED_AT = "user_created_at";
	public static final String USER_UPDATED_AT = "user_updated_at";
	
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
                + USER_ID + " INTEGER PRIMARY KEY,"
                + USER_NAME + " TEXT,"
                + USER_PHONE + " TEXT UNIQUE,"
                + USER_CREATED_AT + " TEXT" + ");";
        db.execSQL(CREATE_LOGIN_TABLE);
    }
    
    /**
     * Storing user details in database (called when user logs in).
     * */
    public void addUser(String userId, String userName, String userPhone,
    		String userCreatedAt)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // creating row containing user data
        ContentValues values = new ContentValues();

        values.put(USER_ID, userId); 
        values.put(USER_NAME, userName);
        values.put(USER_PHONE, userPhone);
        values.put(USER_CREATED_AT, userCreatedAt);
        
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
            user.put(USER_ID, cursor.getString(1));
            user.put(USER_NAME, cursor.getString(2));
            user.put(USER_PHONE, cursor.getString(3)); 
            user.put(USER_CREATED_AT, cursor.getString(4));
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