package roskuski_lamanna.drivingautorespond;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by andrew on 4/7/16.
 */
public class Database {

    /**
     * Database version number
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Name of Database
     */
    private static final String DATABASE_NAME = "datastore";


    private static final String ACTIVITY_DDL = "CREATE TABLE data (" +
            "id text PRIMARY KEY," +
            "value INTEGER NOT NULL);";




    private static final String INSERT_PREF_SQL = "INSERT INTO data (id, value) VALUES ('pref', 1);";


    private static final String INSERT_ACTIVITY_SQL = "INSERT INTO data (id, value) VALUES ('act', 0);";


    /**
     * Singleton Instance
     */
    private static Database dbInstance = null;

    /**
     * Default Constructor
     */
    private Database (){
    }

    /**
     * Get the instance of the class.  If it doesn't exist, it will be created.
     * @return The instance of {@link Database}
     */
    public static Database getInstance(){
        if (dbInstance == null){
            dbInstance = new Database();
        }
        return dbInstance;
    }

    /**
     * Helper class which simplifies database access
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        /**
         * Calls the super class constructor with the proper values
         * @param context calling Context
         */
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Initializes the Database using the hardcoded DDL in the class fields
         * @param db The database to initialize
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println("initializing database");
            db.execSQL(ACTIVITY_DDL);
            db.execSQL(INSERT_ACTIVITY_SQL);
            db.execSQL(INSERT_PREF_SQL);
        }

        /**
         * Not implemented
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public void updateVal(Context ctx, String id, int val){
        ContentValues cv = new ContentValues();
        cv.put("value", val);

        DatabaseHelper dbHelp = new DatabaseHelper(ctx);
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        db.update("data", cv, "id=?", new String[] {id});

        db.close();
        dbHelp.close();

    }

    public int getVal(Context ctx, String id){

        DatabaseHelper dbHelp = new DatabaseHelper(ctx);
        SQLiteDatabase db = dbHelp.getWritableDatabase();

        Cursor res = db.query("data", new String[]{"value"}, "id=?", new String[]{id}, null, null, null);

        res.moveToNext();

        int result = res.getInt(res.getColumnIndex("value"));

        res.close();
        db.close();
        dbHelp.close();
        return  result;
    }
}
