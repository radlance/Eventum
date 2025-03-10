package com.radlance.eventum.data.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.radlance.eventum.data.database.local.entity.LocalCategoryEntity
import com.radlance.eventum.data.database.local.entity.LocalNotificationEntity
import com.radlance.eventum.data.database.local.entity.LocalEventEntity
import com.radlance.eventum.data.database.local.entity.LocalEventPriceEntity
import com.radlance.eventum.data.database.local.entity.SearchHistoryQueryEntity

@Database(
    entities = [
        SearchHistoryQueryEntity::class,
        LocalCategoryEntity::class,
        LocalEventEntity::class,
        LocalNotificationEntity::class,
        LocalEventPriceEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class EventumDatabase : RoomDatabase() {
    abstract fun dao(): EventumDao

    companion object {
        @Volatile
        private var INSTANCE: EventumDatabase? = null

        fun getInstance(applicationContext: Context): EventumDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    applicationContext, EventumDatabase::class.java, "eventum_db"
                ).createFromAsset("eventum.db").build()
            }.also {
                INSTANCE = it
            }
        }
    }
}