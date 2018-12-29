package com.example.liuyang.videoplayer;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = { CoachInfo.class }, version = 1,exportSchema = false)
public abstract class CoachInfoDatabase extends RoomDatabase {
    private static final String DB_NAME = "CoachInfoDatabase.db";
    private static volatile CoachInfoDatabase instance;

    static synchronized CoachInfoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static CoachInfoDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                CoachInfoDatabase.class,
                DB_NAME).allowMainThreadQueries().build();
    }

    public abstract CoachDao getUserDao();

}
