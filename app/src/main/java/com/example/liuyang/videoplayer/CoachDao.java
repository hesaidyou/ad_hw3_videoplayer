package com.example.liuyang.videoplayer;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public abstract class CoachDao {
    @Query("SELECT * FROM CoachInfo")
    abstract List<CoachInfo> getCoachInfo();

    @Insert
    abstract void insert(CoachInfo... coachInfos);
}
