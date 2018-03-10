package com.when.threemb.when;

import android.content.Context;
import android.content.SharedPreferences;

public class TimetableObject{
    private String mAttendance;
    private String mRoomNo;
    private String mSubject;
    private String mTeacher;
    private String mStart;
    private String mEnd;

    public String getmAttendance(){
        return mAttendance;
    }

    public String getmRoomNo() {
        return mRoomNo;
    }

    public String getmSubject() {
        return mSubject;
    }

    public String getmTeacher() {
        return mTeacher;
    }

    public String getmStart() {
        return mStart;
    }

    public String getmEnd() {
        return mEnd;
    }

    public TimetableObject(String mAttendance, String mSubject, String mTeacher, String mStart, String mEnd,String mRoomNo) {
        this.mAttendance = mAttendance;
        this.mRoomNo = mRoomNo;
        this.mSubject = mSubject;
        this.mTeacher = mTeacher;
        this.mStart = mStart;
        this.mEnd = mEnd;
    }
}
