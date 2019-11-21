/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {
    abstract val sleepDatabaseDao : SleepDatabaseDao

    // Companion object allows to access the database, their is no need to instantiate
    companion object {
        // This variable allow us to have an reference of the database, eliminating repeated
        // open and closing
        // connection to the database
        // Volatile means to the value is always up to date and same all execution threads.
        // It will never be cache and all read and writes will be done from the main memory.
        // Changes to one var are available to all thread immediately
        @Volatile
        private var INSTANCE: SleepDatabase? = null

        fun getInstance(context: Context) : SleepDatabase {
            // Important: Prevent multi thread for asking for database instance at the same time.
            // We only one want execution so the database will be initialize once.
            synchronized(this) {
                var instance = INSTANCE

                // If their no instance of the database
                if (instance == null) {
                    instance = Room.databaseBuilder( // Room.databaseBuilder build a database
                            context.applicationContext,
                            SleepDatabase::class.java,            // class name
                            "sleep_history_database"       // database name
                    ).fallbackToDestructiveMigration().build()   // migration in case change one schema to another schema
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}