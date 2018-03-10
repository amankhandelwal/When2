package com.when.threemb.when;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Freeware Sys on 10/15/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "When";

    // Contacts table name
    private static final String TABLE_NAME = "Timetable";
    private static final String TABLE_ATTEND = "Attendance";


    // Contacts Table Columns names
    private static final String KEY_PPR_CODE = "ppr_code";
    private static final String KEY_FAC_INITIALS = "name";
    private static final String KEY_ROOM_NO = "room";
    private static final String KEY_START_TIME = "starttime";
    private static final String KEY_END_TIME = "endtime";
    private static final String KEY_DAY = "day";
    private static final String KEY_ATTEND = "Attended";
    private static final String KEY_TOTAL = "Total";


    //private static final String KEY_PRIMARY="pk";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*" (ppr_code text primary key,name text,room text,starttime integer,endtime integer,day text)"; */
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME +"("
                + KEY_PPR_CODE + " TEXT," + KEY_FAC_INITIALS + " TEXT,"
                + KEY_ROOM_NO + " TEXT," + KEY_START_TIME +" INTEGER,"+KEY_END_TIME+" INTEGER,"+KEY_DAY+" TEXT"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        //For Attendance Table
        CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_ATTEND +"("
                + KEY_PPR_CODE + " TEXT," +KEY_ATTEND +" INTEGER,"+KEY_TOTAL+" INTEGER"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
    void addPeriod(MyTimetable period) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PPR_CODE, period.getPpr_Code()); // Contact Name
        values.put(KEY_FAC_INITIALS, period.getFac_Initials()); // Contact Phone
        values.put(KEY_ROOM_NO, period.getRoom_No());
        values.put(KEY_START_TIME, period.getStart_Time());
        values.put(KEY_END_TIME, period.getEnd_Time());
        values.put(KEY_DAY, period.getDay());
        // Inserting Row
        long newRowId=db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    void addAttendance(String paper,int attend,int total) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PPR_CODE, paper); // Contact Name
        values.put(KEY_ATTEND, attend); // Contact Phone
        values.put(KEY_TOTAL, total);
        /*values.put(KEY_START_TIME, period.getStart_Time());
        values.put(KEY_END_TIME, period.getEnd_Time());
        values.put(KEY_DAY, period.getDay());*/
        // Inserting Row
        long newRowId=db.insert(TABLE_ATTEND, null, values);
        db.close(); // Closing database connection
    }

    void updateAttendance(String paper,int attend,int total) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PPR_CODE, paper); // Contact Name
        values.put(KEY_ATTEND, attend); // Contact Phone
        values.put(KEY_TOTAL, total);
        /*values.put(KEY_START_TIME, period.getStart_Time());
        values.put(KEY_END_TIME, period.getEnd_Time());
        values.put(KEY_DAY, period.getDay());*/
        // Inserting Row
        long newRowId=db.update(TABLE_ATTEND, values,KEY_PPR_CODE+"=?",new String[]{paper});
        db.close(); // Closing database connection
    }
    /*
    MyTimetable viewPeriod(String ppr_code){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_PPR_CODE,
                        KEY_FAC_INITIALS, KEY_ROOM_NO,KEY_START_TIME,KEY_END_TIME,KEY_DAY }, KEY_PPR_CODE + "=?",
                new String[] { ppr_code }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MyTimetable period = new MyTimetable(cursor.getString(0),
                cursor.getString(1), cursor.getString(2),Integer.parseInt( cursor.getString(3)),Integer.parseInt(cursor.getString(4)), cursor.getString(5));
        // return contact
        return period;
    }
    */


    public ArrayList<TimetableObject> getAllPeriods(String day) {
        ArrayList<TimetableObject> periodList = new ArrayList<TimetableObject>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE "+KEY_DAY+" = \""+day+"\"";;

        String attendQuery;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor csattend;
        int attend,total,percent;

        // looping through all rows and adding to list
        /*cursor.getsrting
        * 0-papercode
        * 1-fac name
        * 2room
        * 3st
        * 4et
        * 5day
        * */
        int count=0;
        if (cursor.moveToFirst()) {
            do {
                attendQuery= "SELECT * FROM " + TABLE_ATTEND + " WHERE "+KEY_PPR_CODE+" = \""+cursor.getString(0)+"\"";
                csattend=db.rawQuery(attendQuery,null);
                if(csattend.moveToFirst()) {
                    attend = csattend.getInt(1);
                    total = csattend.getInt(2);
                    if(total!=0)
                    percent = (attend * 100) / total;
                    else
                    percent=0;

                    TimetableObject to = new TimetableObject(Integer.toString(percent), cursor.getString(2), cursor.getString(0),
                            cursor.getString(1), cursor.getString(3), cursor.getString(4));
                    // Adding contact to list
                    //if(Integer.parseInt(cursor.getString(3))!=900 || count++==0) //JUGAAD
                    periodList.add(to);
                }
            } while (cursor.moveToNext());
        }

        // return contact list
        return periodList;
    }



    public ArrayList<TimetableObject> getAttendanceView() {
        ArrayList<TimetableObject> periodList = new ArrayList<TimetableObject>();
        // Select All Query
        String selectQuery = "SELECT DISTINCT "+KEY_PPR_CODE+" FROM " + TABLE_NAME;

        String attendQuery;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Cursor csattend;
        int attend,total,percent;

        // looping through all rows and adding to list
        /*cursor.getsrting
        * 0-papercode
        * */
        int count=0;
        if (cursor.moveToFirst()) {
            do {
                attendQuery= "SELECT * FROM " + TABLE_ATTEND + " WHERE "+KEY_PPR_CODE+" = \""+cursor.getString(0)+"\"";
                csattend=db.rawQuery(attendQuery,null);
                if(csattend.moveToFirst()) {
                    attend = csattend.getInt(1);
                    total = csattend.getInt(2);
                    if(total!=0)
                        percent = (attend * 100) / total;
                    else
                        percent=0;


                    TimetableObject to = new TimetableObject(Integer.toString(percent), " ", cursor.getString(0),
                            " ", "attended: "+attend, "total: "+total);
                    // Adding contact to list
                    //if(Integer.parseInt(cursor.getString(3))!=900 || count++==0) //JUGAAD
                    periodList.add(to);
                }
            } while (cursor.moveToNext());
        }

        // return contact list
        return periodList;
    }

}
