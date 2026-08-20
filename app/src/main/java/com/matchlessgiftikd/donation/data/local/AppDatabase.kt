package com.matchlessgiftikd.donation.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        DonationEntity::class,
        PromisedDonationEntity::class,
        DistrictEntity::class,
        ThanaEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun donationDao(): DonationDao

    abstract fun promisedDonationDao(): PromisedDonationDao

    abstract fun metaDataDao(): MetaDataDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Migration:
         *
         * Database version 1
         *        ↓
         * Database version 2
         *
         * Adds promiseToken to the donations table.
         *
         * Existing records receive promiseToken = 0.
         */
        private val MIGRATION_1_2 =
            object : Migration(1, 2) {

                override fun migrate(
                    database: SupportSQLiteDatabase
                ) {

                    database.execSQL(
                        """
                        ALTER TABLE donations
                        ADD COLUMN promiseToken INTEGER NOT NULL DEFAULT 0
                        """.trimIndent()
                    )
                }
            }

        fun getDatabase(
            context: Context
        ): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "donation_database"
                    )
                        .addMigrations(MIGRATION_1_2)
                        .build()

                INSTANCE = instance

                instance
            }
        }
    }
}
