package com.matchlessgiftikd.donation.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.matchlessgiftikd.donation.data.local.AppDatabase
import com.matchlessgiftikd.donation.data.local.DistrictEntity
import com.matchlessgiftikd.donation.data.local.DonationEntity
import com.matchlessgiftikd.donation.data.remote.ApiService
import com.matchlessgiftikd.donation.data.remote.DistrictDto
import com.matchlessgiftikd.donation.worker.SyncWorker
import kotlinx.coroutines.flow.Flow

class DonationRepository(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val apiService = ApiService.create()

    // 1. Save local donation and trigger background sync
    suspend fun saveDonation(donation: DonationEntity): Long {
        val insertedId = db.donationDao().insertDonation(donation)
        scheduleSync()
        return insertedId
    }

    // 2. Local database queries for UI display
    fun getAllLocalDonations(): Flow<List<DonationEntity>> {
        return db.donationDao().getAllDonationsFlow()
    }

    suspend fun getUnsyncedCount(): Int {
        return db.donationDao().getUnsyncedDonations().size
    }

    // 3. Sync meta-data with explicit success/failure return
    suspend fun syncMetaData(): Boolean {
        return try {
            val response = apiService.fetchMetaData()
            if (response.isSuccessful && response.body() != null) {
                val metaData = response.body()!!
                val districts = metaData.districts.map { district: DistrictDto ->
                    DistrictEntity(id = district.id, name = district.name)
                }
                db.metaDataDao().insertDistricts(districts)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // 4. Queue background sync worker safely
    fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        // ExistingWorkPolicy.KEEP prevents cancelling active workers during rapid offline entries
        WorkManager.getInstance(context).enqueueUniqueWork(
            "AutoDonationSync",
            ExistingWorkPolicy.KEEP,
            syncRequest
        )
    }
}
