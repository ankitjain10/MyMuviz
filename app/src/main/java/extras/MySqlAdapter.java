package extras;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import pojo.Movie;

public class MySqlAdapter {
    public static final int BOX_OFFICE = 0;
    public static final int UPCOMING = 1;
    public static final int SEARCH = 2;
    private static final String TAG = MySqlAdapter.class.getSimpleName();
    private MoviesHelper mHelper;
    private SQLiteDatabase mDatabase;

    public MySqlAdapter(Context context) {
        mHelper = new MoviesHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }



    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        //SQLiteDatabase mDatabase = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = mDatabase.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

    public void insertMovies(int table, ArrayList<Movie> listMovies, boolean clearPrevious) {
        if (clearPrevious) {
            deleteMovies(table);
        }
        String TABLE_SELECTED="";
        switch (table){
            case BOX_OFFICE:{
                TABLE_SELECTED=MoviesHelper.TABLE_HIT_MOVIES;
                break;
            }
            case UPCOMING:{
                TABLE_SELECTED=MoviesHelper.TABLE_UPCOMING_MOVIES;
                break;
            }
            case SEARCH:{
                TABLE_SELECTED=MoviesHelper.TABLE_SEARCH_MOVIES;
                break;
            }
        }

        //create a sql prepared statement
        String sql = "INSERT INTO " + TABLE_SELECTED + " VALUES (?,?,?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listMovies.size(); i++) {
            Movie currentMovie = listMovies.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentMovie.getTitle());
            statement.bindLong(3, currentMovie.getReleaseDateTheater() == null ? -1 : currentMovie.getReleaseDateTheater().getTime());
            statement.bindLong(4, currentMovie.getAudienceScore());
            statement.bindString(5, currentMovie.getSynopsis());
            statement.bindString(6, currentMovie.getUrlThumbnail());
            statement.bindString(7, currentMovie.getUrlSelf());
            statement.bindString(8, currentMovie.getUrlCast());
            statement.bindString(9, currentMovie.getUrlReviews());
            statement.bindString(10, currentMovie.getUrlSimilar());
            statement.execute();
        }
        //set the transaction as successful and end the transaction
        //L.m("inserting entries " + listMovies.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        Log.v(TAG, "success");
    }

    public ArrayList<Movie> readMovies(int table) {
        ArrayList<Movie> listMovies = new ArrayList<>();

        String TABLE_SELECTED="";
        switch (table){
            case BOX_OFFICE:{
                TABLE_SELECTED=MoviesHelper.TABLE_HIT_MOVIES;
                break;
            }
            case UPCOMING:{
                TABLE_SELECTED=MoviesHelper.TABLE_UPCOMING_MOVIES;
                break;
            }
            case SEARCH:{
                TABLE_SELECTED=MoviesHelper.TABLE_SEARCH_MOVIES;
                break;
            }
        }

        Cursor cursor = mDatabase.query(TABLE_SELECTED, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {

                Movie movie = new Movie();
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_TITLE)));
                long releaseDateMilliseconds = cursor.getLong(cursor.getColumnIndex(MoviesHelper.COLUMN_RELEASE_DATE));
                movie.setReleaseDateTheater(releaseDateMilliseconds != -1 ? new Date(releaseDateMilliseconds) : null);
                movie.setAudienceScore(cursor.getInt(cursor.getColumnIndex(MoviesHelper.COLUMN_SCORE)));
                movie.setSynopsis(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_SYNOPSIS)));
                movie.setUrlThumbnail(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_THUMBNAIL)));
                movie.setUrlSelf(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_SELF)));
                movie.setUrlCast(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_CAST)));
                movie.setUrlReviews(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_REVIEW)));
                movie.setUrlSimilar(cursor.getString(cursor.getColumnIndex(MoviesHelper.COLUMN_SIMILAR)));
                listMovies.add(movie);
            }
            while (cursor.moveToNext());
        }
        return listMovies;
    }

    public void deleteMovies(int table) {

        String TABLE_SELECTED="";
        switch (table){
            case BOX_OFFICE:{
                TABLE_SELECTED=MoviesHelper.TABLE_HIT_MOVIES;
                break;
            }
            case UPCOMING:{
                TABLE_SELECTED=MoviesHelper.TABLE_UPCOMING_MOVIES;
                break;
            }
            case SEARCH:{
                TABLE_SELECTED=MoviesHelper.TABLE_SEARCH_MOVIES;
                break;
            }
        }
        mDatabase.delete(TABLE_SELECTED, null, null);
    }



    private static class MoviesHelper extends SQLiteOpenHelper {
        Context mContext;
        private static final String DB_NAME = "Hit_Movies_DB";
        private static final String TABLE_HIT_MOVIES = "Hit_Movies_Table";
        private static final String TABLE_UPCOMING_MOVIES = "Upcoming_Movies_Table";
        private static final String TABLE_SEARCH_MOVIES = "Search_Movies_Table";
        private static final int DB_VERSION = 1;
        private static final String COLUMN_ID = "_Id";
        private static final String COLUMN_TITLE = "Title";
        private static final String COLUMN_RELEASE_DATE = "Release_Date";
        private static final String COLUMN_SCORE = "Score";
        private static final String COLUMN_SYNOPSIS = "Synopsis";
        private static final String COLUMN_THUMBNAIL = "Thumbnail";
        private static final String COLUMN_SELF = "Self";
        private static final String COLUMN_CAST = "Cast";
        private static final String COLUMN_REVIEW = "Review";
        private static final String COLUMN_SIMILAR = "Similar";

        private static final String CREATE_TABLE_HIT_MOVIES = "CREATE TABLE " + TABLE_HIT_MOVIES + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT," + COLUMN_RELEASE_DATE + " INTEGER," + COLUMN_SCORE + " INTEGER," +
                COLUMN_SYNOPSIS + " TEXT," + COLUMN_THUMBNAIL + " TEXT," + COLUMN_SELF + " TEXT," + COLUMN_CAST
                + " TEXT," + COLUMN_REVIEW + " TEXT," + COLUMN_SIMILAR + " TEXT" + " ); ";

        private static final String CREATE_TABLE_UPCOMING_MOVIES = "CREATE TABLE " + TABLE_UPCOMING_MOVIES + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT," + COLUMN_RELEASE_DATE + " INTEGER," + COLUMN_SCORE + " INTEGER," +
                COLUMN_SYNOPSIS + " TEXT," + COLUMN_THUMBNAIL + " TEXT," + COLUMN_SELF + " TEXT," + COLUMN_CAST
                + " TEXT," + COLUMN_REVIEW + " TEXT," + COLUMN_SIMILAR + " TEXT" + " ); ";

        private static final String CREATE_TABLE_SEARCH_MOVIES = "CREATE TABLE " + TABLE_SEARCH_MOVIES + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT," + COLUMN_RELEASE_DATE + " INTEGER," + COLUMN_SCORE + " INTEGER," +
                COLUMN_SYNOPSIS + " TEXT," + COLUMN_THUMBNAIL + " TEXT," + COLUMN_SELF + " TEXT," + COLUMN_CAST
                + " TEXT," + COLUMN_REVIEW + " TEXT," + COLUMN_SIMILAR + " TEXT" + " ); ";

        public MoviesHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.print(CREATE_TABLE_HIT_MOVIES);
            System.out.print(CREATE_TABLE_UPCOMING_MOVIES);
            System.out.print(CREATE_TABLE_SEARCH_MOVIES);
            db.execSQL(CREATE_TABLE_HIT_MOVIES);
            db.execSQL(CREATE_TABLE_UPCOMING_MOVIES);
            db.execSQL(CREATE_TABLE_SEARCH_MOVIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE " + TABLE_HIT_MOVIES + "IF EXISTS; ");
            db.execSQL("DROP TABLE " + TABLE_UPCOMING_MOVIES + "IF EXISTS; ");
            db.execSQL("DROP TABLE " + TABLE_SEARCH_MOVIES + "IF EXISTS; ");
        }
    }
}
