package com.app.appnext.databasehelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.core.app.NavUtils;

import com.app.appnext.modelclasses.ModelView;
import com.app.appnext.modelclasses.PlayerScore;
import com.app.appnext.modelclasses.TeamDetails;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    static String TABLE_SCORES = "SCORES";
    static String TABLE_PLAYERS = "PLAYERS_SCORES";
    static String TABLE_DETAILS = "DETAILS";
    static String ID_SCORES = "_id";
    static String ID_PLAYERS = "_id";
    RestorableSQLiteDatabase restorableSQLiteDatabase;

    public DBHelper(Context context) {
        super(context,"DatabaseName.db",null,1);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //table for storing name of the teams, their types and number of overs
        sqLiteDatabase.execSQL("CREATE TABLE DETAILS(NameA TEXT, NameB TEXT, Type TEXT, Overs INT)");
        //table for storing each team details(include result field and remove type field if found convenient)
        sqLiteDatabase.execSQL("CREATE TABLE SCORES(_id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Toss TEXT, Chose TEXT, Run INT, Wicket INT, Total_Overs INT, Current_Overs DOUBLE)");
        //table for storing individual players data
        sqLiteDatabase.execSQL("CREATE TABLE PLAYERS_SCORES(" + ID_PLAYERS +
                " INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Status TEXT, Run INT, Wicket INT, Fours INT, Sixes INT, Balls INT, Strike_Rate DOUBLE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS DETAILS");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS SCORES");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PLAYERS_SCORES");
        onCreate(sqLiteDatabase);
    }

    public Boolean insertDataIntoTable(String nameA, String nameB, String type, int overs){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NameA", nameA);
        cv.put("NameB", nameB);
        cv.put("Type",type);
        cv.put("Overs", overs);

        long results = db.insert("DETAILS",null,cv);

        if(results == -1)
            return false;
        else
            return true;
    }

    public PlayerScore insertPlayerData( String name, String status, int runs, int wickets,int fours, int sixes,int balls, double strikeRate){

        PlayerScore p = null;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Name", name);
        cv.put("Run",runs);
        cv.put("Wicket", wickets);
        cv.put("Status", status);
        cv.put("Fours", fours);
        cv.put("Sixes",sixes);
        cv.put("Balls", balls);
        cv.put("Strike_Rate", strikeRate);

       // long results = db.insert("PLAYERS_SCORES",null,cv);

        //including restorable

        HashMap<String, String> tableRowId = new HashMap<>();
        tableRowId.put(TABLE_PLAYERS, ID_PLAYERS);
        restorableSQLiteDatabase = new RestorableSQLiteDatabase(db, tableRowId);
        restorableSQLiteDatabase.insert(TABLE_PLAYERS, null, cv, "INSERT PLAYER DATA");


        Cursor c = db.rawQuery("SELECT * FROM PLAYERS_SCORES", null);

        if(c.getCount() > 0)
        {
            while(c.moveToNext())
            {
                p = new PlayerScore(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4),
                        c.getInt(5), c.getInt(6), c.getInt(7), c.getDouble(8));
            }
        }

      /*  if(results == -1)
        {
            return p;
        }

        else*/
            return p;
    }

    public void undoData(String tag){
        SQLiteDatabase db = this.getWritableDatabase();
        HashMap<String, String> tableRowId = new HashMap<>();
        tableRowId.put(TABLE_PLAYERS, ID_PLAYERS);
        restorableSQLiteDatabase = new RestorableSQLiteDatabase(db, tableRowId);
        restorableSQLiteDatabase.restore(tag);
    }

    public TeamDetails insertScore(TeamDetails teamDetails){
    TeamDetails t = null;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
      //  contentValues.put("_id",teamDetails.getId());
        contentValues.put("Name", teamDetails.getName());
        contentValues.put("Run", teamDetails.getRuns());
        contentValues.put("Wicket", teamDetails.getWickets());
        contentValues.put("Toss", teamDetails.getToss());
        contentValues.put("Chose", teamDetails.getChoose());
        contentValues.put("Total_Overs", teamDetails.getTotalOvers());
        contentValues.put("Current_Overs", teamDetails.getCurrentOvers());

        long res = database.insert("SCORES", null, contentValues);
        Cursor c = database.rawQuery("SELECT * FROM SCORES", null);
        if(c.getCount()>0){
            while (c.moveToNext()){
                t = new TeamDetails(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),
                        c.getInt(4), c.getInt(5), c.getInt(6), c.getDouble(7));
            }
        }

        if (res == -1){
            return t;
        }
        else {
            return t;
        }
    }
//method didn't work when used, change working if possible
    public TeamDetails updateTeamTossRuns(String name, int id, int runs){

        TeamDetails t = null;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("Run", runs);

        long results = db.update("SCORES", cv, "_id=? AND Name=?", new String[]{String.valueOf(id), name});

        Cursor c = db.rawQuery("SELECT * FROM SCORES WHERE _id=? AND Name=?", new String[]{String.valueOf(id), name});
        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                t = new TeamDetails(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),
                        c.getInt(4), c.getInt(5), c.getInt(6), c.getDouble(7));
            }
        }
        if (results == -1)
            return t;
        else
            return t;
    }

    public TeamDetails updateTeamTossWickets(String name, int id, int wicket){

        TeamDetails t = null;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("Wicket", wicket);

        long results = db.update("SCORES", cv, "_id=? AND Name=?", new String[]{String.valueOf(id), name});

        Cursor c = db.rawQuery("SELECT * FROM SCORES WHERE _id=? AND Name=?", new String[]{String.valueOf(id), name});
        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                t = new TeamDetails(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),
                        c.getInt(4), c.getInt(5), c.getInt(6), c.getDouble(7));
            }
        }
        if (results == -1)
            return t;
        else
            return t;
    }

    public TeamDetails updateTeamDetails(int id,TeamDetails details){

        TeamDetails t = null;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("Current_Overs", details.getCurrentOvers());

        long results = db.update("SCORES", cv, "_id=?", new String[]{String.valueOf(id)});

        Cursor c = db.rawQuery("SELECT * FROM SCORES WHERE _id=?", new String[]{String.valueOf(id)});
        if(c.getCount() > 0) {
            while (c.moveToNext()) {
                t = new TeamDetails(c.getInt(0), c.getString(1), c.getString(2), c.getString(3),
                        c.getInt(4), c.getInt(5), c.getInt(6), c.getDouble(7));
            }
        }
        if (results == -1)
            return t;
        else
            return t;
    }

    public PlayerScore onUpdatePlayersData(int id, PlayerScore p){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Run", p.getRuns());
        cv.put("Wicket", p.getWickets());
        cv.put("Status", p.getStatus());
        cv.put("Fours", p.getFours());
        cv.put("Sixes", p.getSixes());
        cv.put("Strike_Rate", p.getStrikeRate());
        cv.put("Balls",p.getBalls());

        long results = db.update("PLAYERS_SCORES", cv, "_id=?", new String[]{String.valueOf(id)});

        Cursor cursor = db.rawQuery("SELECT * FROM PLAYERS_SCORES WHERE _id=?", new String[]{String.valueOf(id)});

        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {

                p = new PlayerScore(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                        cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getDouble(8));
            }

        }

            return p;
    }

    public PlayerScore onUpdatePlayersWicket(int wicket, int id){

        PlayerScore p = null;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("Wicket", wicket);
        long results = db.update("PLAYERS_SCORES", cv, "_id=?", new String[]{String.valueOf(id)});
        Cursor cursor = db.rawQuery("SELECT * FROM PLAYERS_SCORES WHERE _id=?", new String[]{String.valueOf(id)});

        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {

                p = new PlayerScore(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                        cursor.getInt(4), cursor.getInt(5), cursor.getInt(6), cursor.getInt(7), cursor.getDouble(8));
            }
        }

        return p;
    }

    public Cursor getData(String type){
        SQLiteDatabase db = this.getWritableDatabase();
        String qry = "";
        Cursor cursor = db.rawQuery("SELECT NameA, NameB, Overs FROM DETAILS WHERE Type=?", new String[]{type});
        return cursor;
    }


    public PlayerScore getPlayersData(String name, int id){
        PlayerScore playerScore = null;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT *  FROM PLAYERS_SCORES WHERE Name=? AND _id=?", new String[]{name, String.valueOf(id)});
        if(c.getCount() > 0)
        {
            while(c.moveToNext())
            {
                playerScore = new PlayerScore(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4),
                        c.getInt(5), c.getInt(6), c.getInt(7), c.getDouble(8));
            }
        }
        database.close();
        return playerScore;
    }

    public PlayerScore getPlayersDataById( int id){
        PlayerScore playerScore = null;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT *  FROM PLAYERS_SCORES WHERE _id=?", new String[]{String.valueOf(id)});
        if(c.getCount() > 0)
        {
            while(c.moveToNext())
            {
                playerScore = new PlayerScore(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getInt(4),
                        c.getInt(5), c.getInt(6), c.getInt(7), c.getDouble(8));
            }
        }
        database.close();
        return playerScore;
    }

    public TeamDetails getTossDetailsData(String name, int id){
        SQLiteDatabase database = this.getReadableDatabase();

        TeamDetails teamDetails = null;
        Cursor cursor = database.rawQuery("SELECT * FROM SCORES WHERE Name=? AND _id=?", new String[]{name, String.valueOf(id)});
        if(cursor.getCount() > 0){
            while (cursor.moveToNext())
            {
                teamDetails = new TeamDetails(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5),
                        cursor.getInt(6), cursor.getDouble(7));

            }
        }



        return teamDetails;
    }

    public Boolean updateStatus(String name, String status){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(status=="striker")
        cv.put("Status", "non-striker");
        else
            cv.put("Status", "striker");

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM PLAYERS_SCORES WHERE Name=?", new String[]{name});

        if(cursor.getCount()>0) {

            long results = sqLiteDatabase.update("PLAYERS_SCORES", cv, "Name=?", new String[]{name});

            if (results == -1)
                return false;
            else
                return true;
        }
        else
            return false;
    }

    public Boolean updatePlayerStatusById(int id, String status){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Status", status);

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM PLAYERS_SCORES WHERE _id=?", new String[]{String.valueOf(id)});

        if(cursor.getCount()>0) {

            long results = sqLiteDatabase.update("PLAYERS_SCORES", cv, "_id=?", new String[]{String.valueOf(id)});

            if (results == -1)
                return false;
            else
                return true;
        }
        else
            return false;
    }

    public Boolean onRetire(String name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Status","retired");

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM PLAYERS_SCORES WHERE Name=?", new String[]{name});

        if(cursor.getCount()>0) {

            long results = sqLiteDatabase.update("PLAYERS_SCORES", cv, "Name=?", new String[]{name});

            if (results == -1)
                return false;
            else
                return true;
        }
        else
            return false;
    }



}
