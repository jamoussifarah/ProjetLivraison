package com.delivery.app.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.delivery.app.data.local.dao.LivraisonDao;
import com.delivery.app.data.local.entity.LivraisonEntity;

@Database(entities = {LivraisonEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "delivery_db";
    private static AppDatabase instance;

    public abstract LivraisonDao livraisonDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME
            ).allowMainThreadQueries().build();
        }
        return instance;
    }
}