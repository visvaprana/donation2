package com.matchlessgift.donation.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        DonationEntity::class, 
        DistrictEntity::class, 
        ThanaEntity::class,
        PromisedDonationEntity::class
    ], 
    version = 1, 
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun donationDao(): DonationDao
    abstract fun metaDataDao(): MetaDataDao
    abstract fun promisedDonationDao(): PromisedDonationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "iskcon_donation_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
