package com.when.threemb.when;

/**
 * Created by Freeware Sys on 10/15/2016.
 */

public class MyTimetable {

    private String Ppr_Code, Fac_Initials, Room_No,Day;
    private int Start_Time, End_Time;

    public MyTimetable(String ppr_Code, String fac_Initials, String room_No, int start_Time, int end_Time, String day) {
        Ppr_Code = ppr_Code;
        Fac_Initials = fac_Initials;
        Room_No = room_No;
        Day = day;
        Start_Time = start_Time;
        End_Time = end_Time;
    }

    public String getPpr_Code() {
        return Ppr_Code;
    }

    public String getFac_Initials() {
        return Fac_Initials;
    }

    public String getRoom_No() {
        return Room_No;
    }

    public String getDay() {
        return Day;
    }

    public int getStart_Time() {
        return Start_Time;
    }

    public int getEnd_Time() {
        return End_Time;
    }
}
