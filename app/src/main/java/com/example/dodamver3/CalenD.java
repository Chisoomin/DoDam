package com.example.dodamver3;

import static com.example.dodamver3.MainActivity.hope;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalenD#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalenD extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalenD() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalenD.
     */
    // TODO: Rename and change types and number of parameters
    public static CalenD newInstance(String param1, String param2) {
        CalenD fragment = new CalenD();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static int SUNDAY = 1;
    public static int MONDAY = 2;
    public static int TUESDAY = 3;
    public static int WEDNSESDAY = 4;
    public static int THURSDAY = 5;
    public static int FRIDAY = 6;
    public static int SATURDAY = 7;

    private TextView mTvCalendarTitle;
    private GridView mGvCalendar;

    private ArrayList<DayInfo> mDayList;
    private CalendarAdapter mCalendarAdapter;

    Calendar mLastMonthCalendar;
    Calendar mThisMonthCalendar;
    Calendar mNextMonthCalendar;

    TextView diary;
    ImageView stamp;

    String date; // ????????? ??????
    String dbDate;
    String month;
    String day;
    static int cnt1=0;

    int happyInt, badInt, sadInt;

    LinearLayout diaryContainer;
    ImageButton ques_cal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calen_d, container,false);

        ImageButton bLastMonth = (ImageButton) view.findViewById(R.id.mleft);
        ImageButton bNextMonth = (ImageButton) view.findViewById(R.id.mright);

        mTvCalendarTitle = (TextView) view.findViewById(R.id.year);
        mGvCalendar = (GridView) view.findViewById(R.id.calendar_grid);

        diary = (TextView) view.findViewById(R.id.diary);
        stamp = (ImageView) view.findViewById(R.id.stamp);

        ques_cal = view.findViewById(R.id.ques_cal);

        ques_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hope == 0){
                    ShowIntro("?????????", "????????? ???????????? ????????? ??? ??? ?????????.", view.getRootView(), R.id.cal, 1);
                }
            }
        });


        bLastMonth.setOnClickListener(this::onClick);
        bNextMonth.setOnClickListener(this::onClick);
        mGvCalendar.setOnItemClickListener(this::onItemClick);

        mDayList = new ArrayList<DayInfo>();


        diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ?????? ????????? ????????? ???
                //??????
                Intent intent = new Intent(view.getContext(), DiaryCalendar.class);
                intent.putExtra("date", date);
                intent.putExtra("dbDate", dbDate);
                startActivity(intent);

            }
        });


        diary.setText("????????? ?????? ????????????. \n???????????? ?????? ??????  ");
        stamp.setVisibility(View.INVISIBLE);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        // ????????? ??? ????????? ??????????????? ????????????.
        mThisMonthCalendar = Calendar.getInstance();
        mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        getCalendar(mThisMonthCalendar);
    }

    private void getCalendar(Calendar calendar) {
        int lastMonthStartDay;
        int dayOfMonth;
        int thisMonthLastDay;

        mDayList.clear();

        // ????????? ???????????? ????????? ?????????. ???????????? ???????????? ?????? ???????????? 1(?????????)?????? 8(????????? ?????????)??? ?????????.)
        dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, -1);
        Log.e("????????? ????????????", calendar.get(Calendar.DAY_OF_MONTH) + "");

        // ???????????? ????????? ????????? ?????????.
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.add(Calendar.MONTH, 1);
        Log.e("????????? ?????????", calendar.get(Calendar.DAY_OF_MONTH) + "");

        if (dayOfMonth == SUNDAY) {
            dayOfMonth += 7;
        }

        lastMonthStartDay -= (dayOfMonth - 1) - 1;


        // ????????? ?????????(?????? ??????)??? ????????????.
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "."
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1));

        DayInfo day;

        Log.e("DayOfMOnth", dayOfMonth + "");


        for (int i = 0; i < dayOfMonth - 1; i++) {
            int date = lastMonthStartDay + i;
            day = new DayInfo();
            day.setDay(Integer.toString(date));
            day.setInMonth(false);

            mDayList.add(day);
        }
        for (int i = 1; i <= thisMonthLastDay; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(true);

            mDayList.add(day);
        }
        for (int i = 1; i < 42 - (thisMonthLastDay + dayOfMonth - 1) + 1; i++) {
            day = new DayInfo();
            day.setDay(Integer.toString(i));
            day.setInMonth(false);
            mDayList.add(day);

        }

        initCalendarAdapter();
    }

    private Calendar getLastMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, -1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + ". "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1));
        return calendar;
    }

    private Calendar getNextMonth(Calendar calendar) {
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        calendar.add(Calendar.MONTH, +1);
        mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + ". "
                + (mThisMonthCalendar.get(Calendar.MONTH) + 1));
        return calendar;
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {

        for (int i = 0; i < parent.getChildCount(); i++) {
            parent.getChildAt(i).findViewById(R.id.flag).setVisibility(View.INVISIBLE);

        }

        v.findViewById(R.id.flag).setVisibility(View.VISIBLE);

        if ((mThisMonthCalendar.get(Calendar.MONTH) + 1) <= 9 && (mThisMonthCalendar.get(Calendar.MONTH) + 1) >= 1) {
            month = "0" + (mThisMonthCalendar.get(Calendar.MONTH) + 1);
        } else {//if ((mThisMonthCalendar.get(Calendar.MONTH) + 1) >= 10 && (mThisMonthCalendar.get(Calendar.MONTH) + 1) <= 12) {
            month = "" + (mThisMonthCalendar.get(Calendar.MONTH) + 1);
        }


        if ((Integer.parseInt(String.valueOf(mDayList.get(position).getDay()))) <= 9 && (Integer.parseInt(String.valueOf(mDayList.get(position).getDay())) >= 1)) {
            day = "0" + mDayList.get(position).getDay();
        } else {//if ((Integer.parseInt(String.valueOf(mDayList.get(position).getDay()))) >= 10 && (Integer.parseInt(String.valueOf(mDayList.get(position).getDay())) <= 31)) {
            day = "" + (Integer.parseInt(String.valueOf(mDayList.get(position).getDay())));
        }

        date = mThisMonthCalendar.get(Calendar.YEAR) + ". " +
                month + ". " +
                day;

        dbDate = mThisMonthCalendar.get(Calendar.YEAR) + "-" +
                month + "-" +
                day;

        //?????? ????????????

        //DB ??????
        DiaryDBHelper dbHelper = new DiaryDBHelper(getView().getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select date, content, happy, bad, sad from DiaryData;", null);


        //?????? ????????? ?????? ???


        diary.setText("????????? ?????? ????????????. \n???????????? ?????? ??????  ");
        stamp.setVisibility(View.INVISIBLE);

        while (cursor.moveToNext()) {

            if (cursor.getString(0).equals(dbDate)) {
                diary.setText(cursor.getString(1));
                happyInt = cursor.getInt(2);
                badInt = cursor.getInt(3);
                sadInt = cursor.getInt(4);

                //????????? ?????? ??????
                if (happyInt == badInt && badInt == sadInt && happyInt != 0) // ?????? ??????(?????? ???)
                    stamp.setImageResource(R.drawable.stamp_full_feel);
                if (happyInt > badInt && happyInt > sadInt) // ??????
                    stamp.setImageResource(R.drawable.stamp_happy);
                if (badInt > happyInt && badInt > sadInt) // ??????
                    stamp.setImageResource(R.drawable.stamp_angry);
                if (sadInt > badInt && sadInt > happyInt) //??????
                    stamp.setImageResource(R.drawable.stamp_sad);
                if (happyInt == badInt && happyInt > sadInt) // ??????+??????
                    stamp.setImageResource(R.drawable.stamp_happy_angry);
                if (happyInt == sadInt && happyInt > badInt) // ??????+??????
                    stamp.setImageResource(R.drawable.stamp_sad_happy);
                if (badInt == sadInt && badInt > happyInt) // ??????+??????
                    stamp.setImageResource(R.drawable.stamp_sad_angry);
                if (happyInt == badInt && badInt == 0 && sadInt == 0) // ?????? 000
                    stamp.setImageResource(R.drawable.stamp_no_feel);
                //????????? ???
                stamp.setVisibility(View.VISIBLE);
            }


        }


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mleft:
                mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
            case R.id.mright:
                mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
                getCalendar(mThisMonthCalendar);
                break;
        }
    }

    private void initCalendarAdapter() {
        mCalendarAdapter = new CalendarAdapter(getContext(), R.layout.item_view_calendar, mDayList);
        mGvCalendar.setAdapter(mCalendarAdapter);
    }

    private void ShowIntro(String title, String text, View view, int id, final int type) {

        new GuideView.Builder(getContext())
                .setTitle(title)
                .setContentText(text)
                .setTargetView(view.findViewById(id))
                .setContentTextSize(12)//optional
                .setTitleTextSize(14)//optional
                .setDismissType(DismissType.anywhere) //optional - default dismissible by TargetView
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        if (type == 1) {
                            ShowIntro("?????? ?????? ??? ??????", "????????? ????????? ????????? ?????????, ???????????? ???????????? ????????? ??? ??? ?????????.", getView(), R.id.diaryContainer, 2);
                        } else if (type == 2) {

                        }
                    }
                })
                .build()
                .show();
    }
}