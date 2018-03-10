package com.when.threemb.when;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 8/21/2016.
 */
public class TimetableAdapter extends ArrayAdapter<TimetableObject> {
    TextView attendanceView,subjectView,teacherView,startTimeView,endTimeView,roomNoView;

    public TimetableAdapter(Context context, int resource, List<TimetableObject> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View listItemView=convertView;
        if(listItemView==null)
        {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.timetable_list_item,parent,false);
        }
        TimetableObject currentDay=getItem(position);

        attendanceView=(TextView)listItemView.findViewById(R.id.magnitude);
        attendanceView.setText(currentDay.getmAttendance());

        roomNoView=(TextView)listItemView.findViewById(R.id.Room_No);
        roomNoView.setText(currentDay.getmRoomNo());

        subjectView=(TextView)listItemView.findViewById(R.id.Ppr_Code);
        subjectView.setText(currentDay.getmSubject());

        teacherView=(TextView)listItemView.findViewById(R.id.Fac_Initials);
        teacherView.setText(currentDay.getmTeacher());

        startTimeView=(TextView)listItemView.findViewById(R.id.Start_Time);
        startTimeView.setText(currentDay.getmStart());

        endTimeView=(TextView)listItemView.findViewById(R.id.End_Time);
        endTimeView.setText(currentDay.getmEnd());

        GradientDrawable attendanceCircle = (GradientDrawable) attendanceView.getBackground();

        // Get the appropriate background color based on the current TimetableObject attendance
        int attendanceColor = getAttendanceColor(currentDay.getmAttendance());

        // Set the color on the attendance circle
        attendanceCircle.setColor(attendanceColor);


        return listItemView;
    }

    private int getAttendanceColor(String s) {
        int magnitude=Integer.parseInt(s);
        int magnitudeColorResourceId;
        int magnitudeFloor = (int)(magnitude/10);
        switch (magnitudeFloor) {
            case 10:magnitudeColorResourceId = R.color.magnitude10my;
                break;
            case 9:magnitudeColorResourceId = R.color.magnitude9my;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 1:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 0:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

}
