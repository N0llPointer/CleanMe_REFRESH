package com.guglprogers.cleanme;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;
import java.util.Date;

public class CalendarDialog extends BottomSheetDialogFragment {
View main_view;
Date date;
Problem problem;
MainActivity activity;


    public static CalendarDialog newInstance(Problem problem){
        CalendarDialog cal = new CalendarDialog();
        cal.problem = problem;
        return cal;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.calendar_dialog,container,false);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog b = (BottomSheetDialog) dialog;
                View bottomSheetInternal = b.findViewById(android.support.design.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        MaterialCalendarView calendar =  main_view.findViewById(R.id.calendarView);
        date = new Date();
        calendar.setDateSelected(date,true);
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
                date = calendarDay.getDate();
            }
        });
        activity = (MainActivity) getActivity();

        main_view.findViewById(R.id.cardview_calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addEvent(problem,date);
                dismiss();
            }
        });

        return main_view;
    }
}
