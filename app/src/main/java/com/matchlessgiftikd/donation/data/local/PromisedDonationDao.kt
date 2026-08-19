package com.matchlessgiftikd.donation.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PromisedDonationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromisedDonation(donation: PromisedDonationEntity)

    @Query("SELECT * FROM promised_donations WHERE isSynced = 0")
    suspend fun getUnsyncedPromisedDonations(): List<PromisedDonationEntity>

    @Query("UPDATE promised_donations SET isSynced = 1 WHERE localId = :id")
    suspend fun markAsSynced(id: Int)
}
