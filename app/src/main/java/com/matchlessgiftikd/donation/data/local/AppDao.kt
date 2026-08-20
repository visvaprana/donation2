package com.matchlessgiftikd.donation.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// ============================================================
// REGULAR DONATION DAO
// ============================================================

@Dao
interface DonationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDonation(donation: DonationEntity): Long

    /**
     * Returns all regular donations that have not yet reached
     * the Laravel server successfully.
     */
    @Query("""
        SELECT * 
        FROM donations 
        WHERE isSynced = 0 
        ORDER BY localId ASC
    """)
    suspend fun getUnsyncedDonations(): List<DonationEntity>

    /**
     * Mark only after Laravel confirms successful insertion.
     */
    @Query("""
        UPDATE donations 
        SET isSynced = 1 
        WHERE localId = :localId
    """)
    suspend fun markAsSynced(localId: Long)

    /**
     * Local list for Android UI.
     */
    @Query("""
        SELECT * 
        FROM donations 
        ORDER BY localId DESC
    """)
    fun getAllDonations(): Flow<List<DonationEntity>>

    /**
     * Number of records still waiting for synchronization.
     */
    @Query("""
        SELECT COUNT(*) 
        FROM donations 
        WHERE isSynced = 0
    """)
    suspend fun getUnsyncedDonationCount(): Int
}


// ============================================================
// PROMISED / COMMITTED DONATION DAO
// ============================================================

@Dao
interface PromisedDonationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromisedDonation(
        promisedDonation: PromisedDonationEntity
    ): Long

    /**
     * Returns all committed donations waiting for Laravel.
     */
    @Query("""
        SELECT * 
        FROM promised_donations 
        WHERE isSynced = 0 
        ORDER BY localId ASC
    """)
    suspend fun getUnsyncedPromisedDonations(): List<PromisedDonationEntity>

    /**
     * Mark only after Laravel confirms successful insertion.
     */
    @Query("""
        UPDATE promised_donations 
        SET isSynced = 1 
        WHERE localId = :localId
    """)
    suspend fun markAsSynced(localId: Long)

    /**
     * Local list for Android UI.
     */
    @Query("""
        SELECT * 
        FROM promised_donations 
        ORDER BY localId DESC
    """)
    fun getAllPromisedDonations(): Flow<List<PromisedDonationEntity>>

    /**
     * Number of promised/committed donations waiting for sync.
     */
    @Query("""
        SELECT COUNT(*) 
        FROM promised_donations 
        WHERE isSynced = 0
    """)
    suspend fun getUnsyncedPromisedDonationCount(): Int
}


// ============================================================
// DISTRICT / THANA META DATA DAO
// ============================================================

@Dao
interface MetaDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistricts(
        districts: List<DistrictEntity>
    )

    @Query("""
        SELECT * 
        FROM districts 
        ORDER BY name ASC
    """)
    suspend fun getAllDistricts(): List<DistrictEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThanas(
        thanas: List<ThanaEntity>
    )

    @Query("""
        SELECT * 
        FROM thanas 
        WHERE districtId = :districtId
        ORDER BY name ASC
    """)
    suspend fun getThanasByDistrict(
        districtId: Int
    ): List<ThanaEntity>
}
