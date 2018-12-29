package com.example.liuyang.videoplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class CoachInfoActivity extends AppCompatActivity {

    private TextView coachid;
    private TextView coachname;
    private TextView coachcourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_info);
        coachid = (TextView)findViewById(R.id.coachid);
        coachname = (TextView)findViewById(R.id.coachname);
        coachcourse = (TextView)findViewById(R.id.coachcourse);

        //向ROOM数据库插入数据
//        CoachInfo ci = new CoachInfo();
//        ci.setId(123);
//        ci.setName("CRIXES");
//        ci.setCourse("UI");
//        CoachInfoDatabase.getInstance(this).getUserDao().insert(ci);

        // 查询数据
        List<CoachInfo> coaches = CoachInfoDatabase
                .getInstance(CoachInfoActivity.this)
                .getUserDao()
                .getCoachInfo();
        coachid.setText(String.valueOf(coaches.get(0).getId()));
        coachname.setText(String.valueOf(coaches.get(0).getName()));
        coachcourse.setText(String.valueOf(coaches.get(0).getCourse()));

    }
}
