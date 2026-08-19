package com.matchlessgiftikd.donation.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// 1. Regular Donation DAO
@Dao
interface DonationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonation(donation: DonationEntity): Long

    @Query("SELECT * FROM donations WHERE isSynced = 0")
    suspend fun getUnsyncedDonations(): List<DonationEntity>

    @Query("UPDATE donations SET isSynced = 1 WHERE localId = :localId")
    suspend fun markAsSynced(localId: Long)

    @Query("SELECT * FROM donations ORDER BY localId DESC")
    fun getAllDonations(): Flow<List<DonationEntity>>
}

// 2. Promised Donation DAO (Added for Promised Donations Syncing)
@Dao
interface PromisedDonationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromisedDonation(promisedDonation: PromisedDonationEntity): Long

    @Query("SELECT * FROM promised_donations WHERE isSynced = 0")
    suspend fun getUnsyncedPromisedDonations(): List<PromisedDonationEntity>

    @Query("UPDATE promised_donations SET isSynced = 1 WHERE localId = :localId")
    suspend fun markAsSynced(localId: Long)

    @Query("SELECT * FROM promised_donations ORDER BY localId DESC")
    fun getAllPromisedDonations(): Flow<List<PromisedDonationEntity>>
}

// 3. MetaData DAO
@Dao
interface MetaDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistricts(districts: List<DistrictEntity>)

    @Query("SELECT * FROM districts ORDER BY name ASC")
    suspend fun getAllDistricts(): List<DistrictEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThanas(thanas: List<ThanaEntity>)

    @Query("SELECT * FROM thanas WHERE districtId = :districtId ORDER BY name ASC")
    suspend fun getThanasByDistrict(districtId: Int): List<ThanaEntity>
}
