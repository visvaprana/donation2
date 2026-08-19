package com.matchlessgiftikd.donation.repository

import android.content.Context
import androidx.work.*
import com.matchlessgiftikd.donation.data.local.*
import com.matchlessgiftikd.donation.data.remote.ApiService
import com.matchlessgiftikd.donation.worker.SyncWorker

class DonationRepository(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val apiService = ApiService.create()

    suspend fun saveDonation(donation: DonationEntity) {
        db.donationDao().insertDonation(donation)
        scheduleSync()
    }

    suspend fun syncMetaData() {
        try {
            val response = apiService.fetchMetaData()
            if (response.isSuccessful && response.body() != null) {
                // Resolved unresolved reference 'it' by using explicit lambda parameter
                val districts = response.body()!!.districts.map { district ->
                    DistrictEntity(district.id, district.name)
                }
                db.metaDataDao().insertDistricts(districts)
            }
        } catch (e: Exception) {
            // Offline scenario: keeps existing cached database
        }
    }

    fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "AutoDonationSync",
            ExistingWorkPolicy.KEEP,
            syncRequest
        )
    }
}
