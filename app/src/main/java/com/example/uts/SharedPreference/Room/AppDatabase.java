package com.example.uts.SharedPreference.Room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.uts.entity.User;

//@Database(entities = {User.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}